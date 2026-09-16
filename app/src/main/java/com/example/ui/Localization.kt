package com.example.ui

object Strings {
    fun appName(isUrdu: Boolean) = if (isUrdu) "دکان ڈیجیٹل کٹ" else "Dukaan Digital Kit"
    fun appTagline(isUrdu: Boolean) = if (isUrdu) "پاکستانی کپڑے کی دکان کا ڈیجیٹل نظام" else "Fabric Shop Digital Kit"

    // Navigation Tabs
    fun tabProducts(isUrdu: Boolean) = if (isUrdu) "پراڈکٹس" else "Products"
    fun tabBilling(isUrdu: Boolean) = if (isUrdu) "بل جنریٹر" else "Billing"
    fun tabDashboard(isUrdu: Boolean) = if (isUrdu) "ڈیش بورڈ" else "Dashboard"
    fun tabSettings(isUrdu: Boolean) = if (isUrdu) "سیٹنگز" else "Settings"

    // Categories
    fun categoryAll(isUrdu: Boolean) = if (isUrdu) "تمام" else "All"
    fun categoryWashWear(isUrdu: Boolean) = if (isUrdu) "واش اینڈ ویئر" else "Wash & Wear"
    fun categoryCotton(isUrdu: Boolean) = if (isUrdu) "کاٹن" else "Cotton"
    fun categoryKurta(isUrdu: Boolean) = if (isUrdu) "کرتہ" else "Kurta"
    fun categoryLadies(isUrdu: Boolean) = if (isUrdu) "لیڈیز" else "Ladies"

    fun translateCategory(cat: String, isUrdu: Boolean): String {
        if (!isUrdu) return cat
        return when (cat) {
            "Wash & Wear" -> "واش اینڈ ویئر"
            "Cotton" -> "کاٹن"
            "Kurta" -> "کرتہ"
            "Ladies" -> "لیڈیز"
            else -> cat
        }
    }

    // WhatsApp Message
    fun getWhatsAppMessage(productName: String, pricePkr: Int, isUrdu: Boolean): String {
        return if (isUrdu) {
            "سلام، مجھے $productName چاہیے قیمت $pricePkr روپے۔ کیا یہ اسٹاک میں دستیاب ہے؟"
        } else {
            "Salam, I want $productName Price $pricePkr PKR"
        }
    }

    // Dashboard
    fun totalItems(isUrdu: Boolean) = if (isUrdu) "کل ورائٹی" else "Total Items"
    fun totalStock(isUrdu: Boolean) = if (isUrdu) "کل تھان / سوٹ" else "Total Stock"
    fun totalValue(isUrdu: Boolean) = if (isUrdu) "اسٹاک کی مالیت" else "Inventory Value"
    fun lowStockAlert(isUrdu: Boolean) = if (isUrdu) "کم اسٹاک الرٹ (<5)" else "Low Stock Alert (<5)"
    fun lowStockWarning(count: Int, isUrdu: Boolean) =
        if (isUrdu) "$count آئٹمز کا اسٹاک ۵ سے کم ہے!" else "$count items have stock below 5!"
    fun restock(isUrdu: Boolean) = if (isUrdu) "+5 اسٹاک ڈالیں" else "+5 Restock"

    // Search and Filters
    fun searchHint(isUrdu: Boolean) = if (isUrdu) "کپڑا، برانڈ یا کیٹیگری تلاش کریں..." else "Search fabric, brand, category..."
    fun addProduct(isUrdu: Boolean) = if (isUrdu) "نیا پراڈکٹ شامل کریں" else "Add Product"
    fun editProduct(isUrdu: Boolean) = if (isUrdu) "تبدیل کریں" else "Edit"
    fun deleteProduct(isUrdu: Boolean) = if (isUrdu) "ڈیلیٹ کریں" else "Delete"
    fun confirmDelete(isUrdu: Boolean) = if (isUrdu) "کیا آپ واقعی یہ پراڈکٹ ڈیلیٹ کرنا چاہتے ہیں؟" else "Are you sure you want to delete this product?"

    // Product Fields
    fun productNameLabel(isUrdu: Boolean) = if (isUrdu) "کپڑے کا نام / ورائٹی" else "Fabric Name / Variety"
    fun categoryLabel(isUrdu: Boolean) = if (isUrdu) "کیٹیگری منتخب کریں" else "Select Category"
    fun priceLabel(isUrdu: Boolean) = if (isUrdu) "قیمت (روپے PKR)" else "Price (PKR)"
    fun stockLabel(isUrdu: Boolean) = if (isUrdu) "اسٹاک کی مقدار (تھان / سوٹ)" else "Stock Quantity (Suits/Than)"
    fun notesLabel(isUrdu: Boolean) = if (isUrdu) "تفصیل / کوالٹی نوٹس" else "Details / Quality Notes"
    fun uploadImage(isUrdu: Boolean) = if (isUrdu) "تصویر لگائیں" else "Upload Image"
    fun save(isUrdu: Boolean) = if (isUrdu) "محفوظ کریں" else "Save"
    fun cancel(isUrdu: Boolean) = if (isUrdu) "منسوخ" else "Cancel"

    // Billing
    fun billGeneratorTitle(isUrdu: Boolean) = if (isUrdu) "نئی رسید / بل" else "Bill / Receipt Generator"
    fun customerNameLabel(isUrdu: Boolean) = if (isUrdu) "گاہک کا نام" else "Customer Name"
    fun customerPhoneLabel(isUrdu: Boolean) = if (isUrdu) "گاہک کا موبائل نمبر" else "Customer Phone"
    fun selectProducts(isUrdu: Boolean) = if (isUrdu) "پراڈکٹ منتخب کریں" else "Select Products"
    fun itemsInBill(isUrdu: Boolean) = if (isUrdu) "بل میں شامل آئٹمز" else "Items in Bill"
    fun subtotal(isUrdu: Boolean) = if (isUrdu) "کل رقم" else "Subtotal"
    fun discount(isUrdu: Boolean) = if (isUrdu) "رعایت (ڈسکاؤنٹ)" else "Discount (PKR)"
    fun grandTotal(isUrdu: Boolean) = if (isUrdu) "حتمی رقم (ٹوٹل)" else "Grand Total (PKR)"
    fun printBill(isUrdu: Boolean) = if (isUrdu) "بل پرنٹ کریں" else "Print Bill"
    fun shareWhatsApp(isUrdu: Boolean) = if (isUrdu) "واٹس ایپ رسید بھیجیں" else "Share WhatsApp Bill"
    fun clearBill(isUrdu: Boolean) = if (isUrdu) "نیا بل بنائیں" else "Clear Bill"
    fun emptyBillNotice(isUrdu: Boolean) = if (isUrdu) "بل بنانے کے لیے نیچے سے کپڑے منتخب کریں" else "Select fabrics below to add to bill"

    // Settings
    fun settingsTitle(isUrdu: Boolean) = if (isUrdu) "دکان کی معلومات اور ترتیبات" else "Shop Info & Settings"
    fun shopNameLabel(isUrdu: Boolean) = if (isUrdu) "دکان کا نام" else "Shop Name"
    fun ownerNameLabel(isUrdu: Boolean) = if (isUrdu) "مالک کا نام" else "Owner Name"
    fun shopPhoneLabel(isUrdu: Boolean) = if (isUrdu) "دکان کا فون نمبر" else "Shop Phone / WhatsApp"
    fun shopAddressLabel(isUrdu: Boolean) = if (isUrdu) "دکان کا پتہ / مارکیٹ" else "Shop Address / Market"
    fun shopLogoLabel(isUrdu: Boolean) = if (isUrdu) "دکان کا لوگو" else "Shop Logo"
    fun languageToggleLabel(isUrdu: Boolean) = if (isUrdu) "زبان منتخب کریں" else "Language / زبان"
    fun darkModeToggleLabel(isUrdu: Boolean) = if (isUrdu) "ڈارک موڈ" else "Dark Mode"
    fun resetDemoData(isUrdu: Boolean) = if (isUrdu) "ڈیمو کپڑے دوبارہ لوڈ کریں" else "Reload Demo Fabrics"
}
