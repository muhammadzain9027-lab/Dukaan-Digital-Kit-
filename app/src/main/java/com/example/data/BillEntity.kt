package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val billNumber: String,
    val customerName: String,
    val customerPhone: String = "",
    val itemsSummary: String, // e.g. "2x Grace Wash & Wear, 1x Pasha Cotton"
    val subtotalPkr: Int,
    val discountPkr: Int = 0,
    val totalPkr: Int,
    val timestamp: Long = System.currentTimeMillis()
)
