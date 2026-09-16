package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ProductEntity::class, BillEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun billDao(): BillDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dukaan_digital_kit_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial Pakistani fabric inventory
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    populateInitialFabrics(database.productDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialFabrics(dao: ProductDao) {
            val initial = listOf(
                ProductEntity(
                    name = "Grace Fabrics Royal Wash & Wear (Navy)",
                    category = "Wash & Wear",
                    pricePkr = 3800,
                    stockQuantity = 18,
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_wash_wear_1789534167101}",
                    notes = "Wrinkle-free premium fallback fabric, perfect for summer/autumn"
                ),
                ProductEntity(
                    name = "Pasha Egyptian Latha 100% Cotton (White)",
                    category = "Cotton",
                    pricePkr = 4500,
                    stockQuantity = 14,
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_cotton_1789534179606}",
                    notes = "Crisp starch finish, genuine cotton thread with golden selvage"
                ),
                ProductEntity(
                    name = "Kohinoor Jacquard Textured Kurta (Olive)",
                    category = "Kurta",
                    pricePkr = 3200,
                    stockQuantity = 8,
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_kurta_1789534192004}",
                    notes = "Self-embossed luxury weave for Eid & festive occasions"
                ),
                ProductEntity(
                    name = "Bareeze Luxury Embroidered Lawn 3-Piece",
                    category = "Ladies",
                    pricePkr = 6500,
                    stockQuantity = 12,
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_ladies_1789534204216}",
                    notes = "Embroidered front with digital printed pure chiffon dupatta"
                ),
                ProductEntity(
                    name = "Dynasty Executive Wash & Wear (Slate Grey)",
                    category = "Wash & Wear",
                    pricePkr = 3600,
                    stockQuantity = 4, // Low stock (<5)
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_wash_wear_1789534167101}",
                    notes = "Soft fall, breathable micro-filament yarn"
                ),
                ProductEntity(
                    name = "Gul Ahmed Boski Finish Soft Cotton (Cream)",
                    category = "Cotton",
                    pricePkr = 4200,
                    stockQuantity = 3, // Low stock (<5)
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_cotton_1789534179606}",
                    notes = "Ultra soft boski touch, premium shine and durability"
                ),
                ProductEntity(
                    name = "J. Junaid Jamshed Festive Kurta Fabric",
                    category = "Kurta",
                    pricePkr = 3400,
                    stockQuantity = 2, // Low stock (<5)
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_kurta_1789534192004}",
                    notes = "Rich traditional dye with soft handfeel"
                ),
                ProductEntity(
                    name = "Al-Karam Chiffon Dupatta Lawn Suit (Pastel)",
                    category = "Ladies",
                    pricePkr = 5200,
                    stockQuantity = 16,
                    imageUri = "android.resource://com.aistudio.dukaankit.pkfab/${R.drawable.fabric_ladies_1789534204216}",
                    notes = "Lightweight summer lawn with organza embroidered neckline border"
                )
            )
            dao.insertAll(initial)
        }
    }
}
