package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Wash & Wear", "Cotton", "Kurta", "Ladies"
    val pricePkr: Int,
    val stockQuantity: Int,
    val imageUri: String? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class BillItem(
    val productId: Long,
    val productName: String,
    val category: String,
    val unitPricePkr: Int,
    val quantity: Int
) {
    val subtotal: Int get() = unitPricePkr * quantity
}
