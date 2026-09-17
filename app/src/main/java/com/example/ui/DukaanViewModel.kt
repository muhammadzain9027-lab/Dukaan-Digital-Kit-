package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.BillEntity
import com.example.data.BillItem
import com.example.data.ProductEntity
import com.example.data.ProductRepository
import com.example.data.ShopInfo
import com.example.data.ShopPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DukaanViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = ProductRepository(database.productDao(), database.billDao())
    private val shopPreferences = ShopPreferences(application)

    val shopInfo: StateFlow<ShopInfo> = shopPreferences.shopInfoFlow

    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.lowStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBills: StateFlow<List<BillEntity>> = repository.allBills
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and filter state
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val showLowStockOnly = MutableStateFlow(false)

    // Filtered products flow
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        searchQuery,
        selectedCategory,
        showLowStockOnly
    ) { products, query, category, lowStockOnly ->
        products.filter { product ->
            val matchesQuery = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true) ||
                product.notes.contains(query, ignoreCase = true) ||
                product.pricePkr.toString().contains(query)

            val matchesCategory = category == "All" || product.category.equals(category, ignoreCase = true)
            val matchesLowStock = !lowStockOnly || product.stockQuantity < 5

            matchesQuery && matchesCategory && matchesLowStock
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Bill Generator State
    val billCustomerName = MutableStateFlow("")
    val billCustomerPhone = MutableStateFlow("")
    val billDiscount = MutableStateFlow(0)
    val billItems = MutableStateFlow<List<BillItem>>(emptyList())

    // Dialog & UI State
    val editingProduct = MutableStateFlow<ProductEntity?>(null)
    val isAddEditOpen = MutableStateFlow(false)
    val deletingProduct = MutableStateFlow<ProductEntity?>(null)
    val lastGeneratedBill = MutableStateFlow<BillEntity?>(null)
    val showBillReceiptDialog = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.ensureInitialDataLoaded()
        }
    }

    // Product actions
    fun openAddProduct() {
        editingProduct.value = null
        isAddEditOpen.value = true
    }

    fun openEditProduct(product: ProductEntity) {
        editingProduct.value = product
        isAddEditOpen.value = true
    }

    fun dismissAddEdit() {
        editingProduct.value = null
        isAddEditOpen.value = false
    }

    fun saveProduct(
        name: String,
        category: String,
        pricePkr: Int,
        stockQuantity: Int,
        imageUri: String?,
        notes: String
    ) {
        viewModelScope.launch {
            val existing = editingProduct.value
            if (existing != null) {
                repository.updateProduct(
                    existing.copy(
                        name = name,
                        category = category,
                        pricePkr = pricePkr,
                        stockQuantity = stockQuantity,
                        imageUri = imageUri ?: existing.imageUri,
                        notes = notes
                    )
                )
            } else {
                repository.insertProduct(
                    ProductEntity(
                        name = name,
                        category = category,
                        pricePkr = pricePkr,
                        stockQuantity = stockQuantity,
                        imageUri = imageUri,
                        notes = notes
                    )
                )
            }
            dismissAddEdit()
        }
    }

    fun confirmDelete(product: ProductEntity) {
        deletingProduct.value = product
    }

    fun dismissDelete() {
        deletingProduct.value = null
    }

    fun executeDelete() {
        val product = deletingProduct.value ?: return
        viewModelScope.launch {
            repository.deleteProduct(product)
            deletingProduct.value = null
        }
    }

    fun quickRestock(productId: Long, amount: Int = 5) {
        viewModelScope.launch {
            repository.addStock(productId, amount)
        }
    }

    // Billing actions
    fun addProductToBill(product: ProductEntity) {
        val currentList = billItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.productId == product.id }
        if (index >= 0) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            currentList.add(
                BillItem(
                    productId = product.id,
                    productName = product.name,
                    category = product.category,
                    unitPricePkr = product.pricePkr,
                    quantity = 1
                )
            )
        }
        billItems.value = currentList
    }

    fun incrementBillItem(productId: Long) {
        val currentList = billItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            val item = currentList[index]
            currentList[index] = item.copy(quantity = item.quantity + 1)
            billItems.value = currentList
        }
    }

    fun decrementBillItem(productId: Long) {
        val currentList = billItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.productId == productId }
        if (index >= 0) {
            val item = currentList[index]
            if (item.quantity > 1) {
                currentList[index] = item.copy(quantity = item.quantity - 1)
            } else {
                currentList.removeAt(index)
            }
            billItems.value = currentList
        }
    }

    fun removeBillItem(productId: Long) {
        billItems.value = billItems.value.filter { it.productId != productId }
    }

    fun clearBill() {
        billItems.value = emptyList()
        billCustomerName.value = ""
        billCustomerPhone.value = ""
        billDiscount.value = 0
    }

    fun finalizeAndSaveBill(deductInventory: Boolean = true): BillEntity? {
        val items = billItems.value
        if (items.isEmpty()) return null

        val subtotal = items.sumOf { it.subtotal }
        val discount = billDiscount.value.coerceAtMost(subtotal)
        val total = (subtotal - discount).coerceAtLeast(0)

        val billNumber = "DDK-${System.currentTimeMillis() % 100000}"
        val summary = items.joinToString(", ") { "${it.quantity}x ${it.productName}" }

        val bill = BillEntity(
            billNumber = billNumber,
            customerName = billCustomerName.value.ifBlank { "Walk-in Customer" },
            customerPhone = billCustomerPhone.value,
            itemsSummary = summary,
            subtotalPkr = subtotal,
            discountPkr = discount,
            totalPkr = total,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertBill(bill)
            if (deductInventory) {
                items.forEach { item ->
                    repository.deductStock(item.productId, item.quantity)
                }
            }
        }

        lastGeneratedBill.value = bill
        showBillReceiptDialog.value = true
        return bill
    }

    // Shop settings
    fun updateShopDetails(
        name: String,
        owner: String,
        phone: String,
        address: String,
        logoUri: String?
    ) {
        shopPreferences.updateShopDetails(name, owner, phone, address, logoUri)
    }

    fun toggleLanguage(isUrdu: Boolean) {
        shopPreferences.toggleLanguage(isUrdu)
    }

    fun toggleDarkMode(isDark: Boolean) {
        shopPreferences.toggleDarkMode(isDark)
    }

    fun setAppTheme(theme: String) {
        shopPreferences.setAppTheme(theme)
    }

    fun reloadDemoData() {
        viewModelScope.launch {
            AppDatabase.populateInitialFabrics(database.productDao())
        }
    }
}
