package com.example.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.ShopInfo

@Composable
fun ShopSettingsScreen(
    viewModel: DukaanViewModel,
    shopInfo: ShopInfo
) {
    val isUrdu = shopInfo.isUrdu
    var shopName by remember(shopInfo) { mutableStateOf(shopInfo.shopName) }
    var shopOwner by remember(shopInfo) { mutableStateOf(shopInfo.shopOwner) }
    var shopPhone by remember(shopInfo) { mutableStateOf(shopInfo.shopPhone) }
    var shopAddress by remember(shopInfo) { mutableStateOf(shopInfo.shopAddress) }
    var shopLogoUri by remember(shopInfo) { mutableStateOf(shopInfo.shopLogoUri) }
    var savedFeedback by remember { mutableStateOf(false) }

    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            shopLogoUri = uri.toString()
            viewModel.updateShopDetails(shopName, shopOwner, shopPhone, shopAddress, uri.toString())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("shop_settings_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Language & Dark Mode Toggles Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isUrdu) "ایپ ترتیبات (Language & Theme)" else "App Settings & Preferences",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Language Toggle (English / اردو)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = Strings.languageToggleLabel(isUrdu),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isUrdu) "اردو (Urdu) منتخب ہے" else "English selected",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isUrdu,
                            onCheckedChange = { viewModel.toggleLanguage(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("language_toggle_switch")
                        )
                    }

                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (shopInfo.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = "Dark Mode",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = Strings.darkModeToggleLabel(isUrdu),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (shopInfo.isDarkMode) "Dark theme enabled" else "Light theme enabled",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = shopInfo.isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode(it) },
                            modifier = Modifier.testTag("dark_mode_toggle_switch")
                        )
                    }
                }
            }
        }

        // Shop Profile & Bill Information
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Shop Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Strings.settingsTitle(isUrdu),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Shop Logo Upload
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                .clickable {
                                    logoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (shopLogoUri != null) {
                                AsyncImage(
                                    model = shopLogoUri,
                                    contentDescription = "Shop Logo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Upload",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = Strings.shopLogoLabel(isUrdu),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (isUrdu) "بل اور رسیدوں پر دکان کا لوگو آئے گا" else "Will appear on invoices and bills",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedButton(
                                onClick = {
                                    logoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.padding(top = 4.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(Strings.uploadImage(isUrdu), fontSize = 12.sp)
                            }
                        }
                    }

                    // Shop Name
                    OutlinedTextField(
                        value = shopName,
                        onValueChange = {
                            shopName = it
                            savedFeedback = false
                        },
                        label = { Text(Strings.shopNameLabel(isUrdu)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_shop_name_input")
                    )

                    // Owner Name
                    OutlinedTextField(
                        value = shopOwner,
                        onValueChange = {
                            shopOwner = it
                            savedFeedback = false
                        },
                        label = { Text(Strings.ownerNameLabel(isUrdu)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_owner_name_input")
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = shopPhone,
                        onValueChange = {
                            shopPhone = it
                            savedFeedback = false
                        },
                        label = { Text(Strings.shopPhoneLabel(isUrdu)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_shop_phone_input")
                    )

                    // Address
                    OutlinedTextField(
                        value = shopAddress,
                        onValueChange = {
                            shopAddress = it
                            savedFeedback = false
                        },
                        label = { Text(Strings.shopAddressLabel(isUrdu)) },
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_shop_address_input")
                    )

                    // Save Button
                    Button(
                        onClick = {
                            viewModel.updateShopDetails(shopName, shopOwner, shopPhone, shopAddress, shopLogoUri)
                            savedFeedback = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_shop_settings_button")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (savedFeedback) (if (isUrdu) "محفوظ کر لیا گیا! ✓" else "Saved Successfully! ✓") else Strings.save(isUrdu),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Demo Data Reset Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isUrdu) "ڈیمو فیبرک کیٹلاگ" else "Demo Fabric Catalog",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isUrdu) "پاکستانی فیبرکس (واش اینڈ ویئر، کاٹن، کرتہ، لیڈیز سوٹ) کا مستند کیٹلاگ دوبارہ لوڈ کریں۔" else "Reload authentic Pakistani fabrics catalog (Wash & Wear, Cotton, Kurta, Ladies suit) with photos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    OutlinedButton(
                        onClick = { viewModel.reloadDemoData() },
                        modifier = Modifier.testTag("reload_demo_catalog_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reload")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Strings.resetDemoData(isUrdu))
                    }
                }
            }
        }
    }
}
