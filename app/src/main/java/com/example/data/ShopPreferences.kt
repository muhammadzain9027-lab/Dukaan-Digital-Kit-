package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ShopInfo(
    val shopName: String = "Al-Madina Fabrics & Cloth House",
    val shopOwner: String = "Haji Muhammad Tariq",
    val shopPhone: String = "03001234567",
    val shopAddress: String = "Shop #24, Main Cloth Market, Faisalabad",
    val shopLogoUri: String? = null,
    val isUrdu: Boolean = false,
    val isDarkMode: Boolean = false,
    val appTheme: String = "emerald"
)

class ShopPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dukaan_kit_prefs", Context.MODE_PRIVATE)

    private val _shopInfoFlow = MutableStateFlow(loadShopInfo())
    val shopInfoFlow: StateFlow<ShopInfo> = _shopInfoFlow.asStateFlow()

    fun getShopInfo(): ShopInfo = _shopInfoFlow.value

    private fun loadShopInfo(): ShopInfo {
        return ShopInfo(
            shopName = prefs.getString("shop_name", "Al-Madina Fabrics & Cloth House") ?: "Al-Madina Fabrics & Cloth House",
            shopOwner = prefs.getString("shop_owner", "Haji Muhammad Tariq") ?: "Haji Muhammad Tariq",
            shopPhone = prefs.getString("shop_phone", "03001234567") ?: "03001234567",
            shopAddress = prefs.getString("shop_address", "Shop #24, Main Cloth Market, Faisalabad") ?: "Shop #24, Main Cloth Market, Faisalabad",
            shopLogoUri = prefs.getString("shop_logo_uri", null),
            isUrdu = prefs.getBoolean("is_urdu", false),
            isDarkMode = prefs.getBoolean("is_dark_mode", false),
            appTheme = prefs.getString("app_theme", "emerald") ?: "emerald"
        )
    }

    fun updateShopDetails(
        name: String,
        owner: String,
        phone: String,
        address: String,
        logoUri: String?
    ) {
        prefs.edit()
            .putString("shop_name", name)
            .putString("shop_owner", owner)
            .putString("shop_phone", phone)
            .putString("shop_address", address)
            .putString("shop_logo_uri", logoUri)
            .apply()
        _shopInfoFlow.value = _shopInfoFlow.value.copy(
            shopName = name,
            shopOwner = owner,
            shopPhone = phone,
            shopAddress = address,
            shopLogoUri = logoUri
        )
    }

    fun toggleLanguage(isUrdu: Boolean) {
        prefs.edit().putBoolean("is_urdu", isUrdu).apply()
        _shopInfoFlow.value = _shopInfoFlow.value.copy(isUrdu = isUrdu)
    }

    fun toggleDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_mode", isDark).apply()
        _shopInfoFlow.value = _shopInfoFlow.value.copy(isDarkMode = isDark)
    }

    fun setAppTheme(theme: String) {
        prefs.edit().putString("app_theme", theme).apply()
        _shopInfoFlow.value = _shopInfoFlow.value.copy(appTheme = theme)
    }
}
