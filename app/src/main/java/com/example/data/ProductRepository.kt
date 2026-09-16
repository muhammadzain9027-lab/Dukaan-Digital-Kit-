package com.example.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val billDao: BillDao
) {
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val lowStockProducts: Flow<List<ProductEntity>> = productDao.getLowStockProducts()
    val allBills: Flow<List<BillEntity>> = billDao.getAllBills()

    suspend fun insertProduct(product: ProductEntity): Long = productDao.insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) = productDao.deleteProduct(product)

    suspend fun addStock(id: Long, amount: Int) = productDao.addStock(id, amount)

    suspend fun deductStock(id: Long, amount: Int) = productDao.deductStock(id, amount)

    suspend fun insertBill(bill: BillEntity): Long = billDao.insertBill(bill)

    suspend fun deleteBill(bill: BillEntity) = billDao.deleteBill(bill)

    suspend fun ensureInitialDataLoaded() {
        if (productDao.getCount() == 0) {
            AppDatabase.populateInitialFabrics(productDao)
        }
    }
}
