package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LowStockRed

enum class DukaanTab {
    PRODUCTS,
    BILLING,
    DASHBOARD,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: DukaanViewModel
) {
    val shopInfo by viewModel.shopInfo.collectAsState()
    val isUrdu = shopInfo.isUrdu
    val billItems by viewModel.billItems.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()

    var currentTab by remember { mutableStateOf(DukaanTab.PRODUCTS) }

    val layoutDirection = if (isUrdu) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when (currentTab) {
                                DukaanTab.PRODUCTS -> Strings.tabProducts(isUrdu)
                                DukaanTab.BILLING -> Strings.tabBilling(isUrdu)
                                DukaanTab.DASHBOARD -> Strings.tabDashboard(isUrdu)
                                DukaanTab.SETTINGS -> Strings.tabSettings(isUrdu)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        // Quick theme switcher
                        IconButton(
                            onClick = {
                                val themes = listOf("emerald", "royal", "maroon", "obsidian")
                                val currentIndex = themes.indexOf(shopInfo.appTheme.lowercase()).let { if (it < 0) 0 else it }
                                val nextTheme = themes[(currentIndex + 1) % themes.size]
                                viewModel.setAppTheme(nextTheme)
                            },
                            modifier = Modifier.testTag("top_bar_theme_toggle")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Switch Theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Quick language switch in top bar
                        IconButton(
                            onClick = { viewModel.toggleLanguage(!isUrdu) },
                            modifier = Modifier.testTag("top_bar_lang_toggle")
                        ) {
                            Text(
                                text = if (isUrdu) "EN" else "اردو",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.testTag("top_app_bar")
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("bottom_navigation_bar")
                ) {
                    // Products Tab
                    NavigationBarItem(
                        selected = currentTab == DukaanTab.PRODUCTS,
                        onClick = { currentTab = DukaanTab.PRODUCTS },
                        icon = {
                            Icon(imageVector = Icons.Default.Inventory, contentDescription = "Products")
                        },
                        label = { Text(Strings.tabProducts(isUrdu), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_products")
                    )

                    // Billing Tab
                    NavigationBarItem(
                        selected = currentTab == DukaanTab.BILLING,
                        onClick = { currentTab = DukaanTab.BILLING },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (billItems.isNotEmpty()) {
                                        Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                            Text("${billItems.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Receipt, contentDescription = "Billing")
                            }
                        },
                        label = { Text(Strings.tabBilling(isUrdu), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_billing")
                    )

                    // Dashboard Tab
                    NavigationBarItem(
                        selected = currentTab == DukaanTab.DASHBOARD,
                        onClick = { currentTab = DukaanTab.DASHBOARD },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (lowStockProducts.isNotEmpty()) {
                                        Badge(containerColor = LowStockRed) {
                                            Text("${lowStockProducts.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard")
                            }
                        },
                        label = { Text(Strings.tabDashboard(isUrdu), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_dashboard")
                    )

                    // Settings Tab
                    NavigationBarItem(
                        selected = currentTab == DukaanTab.SETTINGS,
                        onClick = { currentTab = DukaanTab.SETTINGS },
                        icon = {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                        },
                        label = { Text(Strings.tabSettings(isUrdu), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_settings")
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .imePadding()
            ) {
                when (currentTab) {
                    DukaanTab.PRODUCTS -> ProductsScreen(
                        viewModel = viewModel,
                        shopInfo = shopInfo,
                        onNavigateToBilling = { currentTab = DukaanTab.BILLING }
                    )
                    DukaanTab.BILLING -> BillGeneratorScreen(
                        viewModel = viewModel,
                        shopInfo = shopInfo
                    )
                    DukaanTab.DASHBOARD -> DashboardScreen(
                        viewModel = viewModel,
                        shopInfo = shopInfo
                    )
                    DukaanTab.SETTINGS -> ShopSettingsScreen(
                        viewModel = viewModel,
                        shopInfo = shopInfo
                    )
                }
            }
        }
    }
}
