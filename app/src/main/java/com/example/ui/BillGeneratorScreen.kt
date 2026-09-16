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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.BillItem
import com.example.data.ShopInfo
import com.example.ui.theme.LowStockRed
import com.example.ui.theme.WhatsAppGreen

@Composable
fun BillGeneratorScreen(
    viewModel: DukaanViewModel,
    shopInfo: ShopInfo
) {
    val context = LocalContext.current
    val isUrdu = shopInfo.isUrdu
    val products by viewModel.allProducts.collectAsState()
    val billItems by viewModel.billItems.collectAsState()
    val customerName by viewModel.billCustomerName.collectAsState()
    val customerPhone by viewModel.billCustomerPhone.collectAsState()
    val discount by viewModel.billDiscount.collectAsState()
    val lastGeneratedBill by viewModel.lastGeneratedBill.collectAsState()
    val showBillReceiptDialog by viewModel.showBillReceiptDialog.collectAsState()

    val subtotal = billItems.sumOf { it.subtotal }
    val finalTotal = (subtotal - discount).coerceAtLeast(0)

    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateShopDetails(
                shopInfo.shopName,
                shopInfo.shopOwner,
                shopInfo.shopPhone,
                shopInfo.shopAddress,
                uri.toString()
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("bill_generator_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Shop Branding Header Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shop Logo upload / display
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable {
                                logoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("shop_logo_picker_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (shopInfo.shopLogoUri != null) {
                            AsyncImage(
                                model = shopInfo.shopLogoUri,
                                contentDescription = "Shop Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Upload Logo",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = if (isUrdu) "لوگو" else "Logo",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = shopInfo.shopName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "📞 ${shopInfo.shopPhone} • ${shopInfo.shopOwner}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            text = shopInfo.shopAddress,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Customer Details Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isUrdu) "گاہک کی معلومات" else "Customer Information",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { viewModel.billCustomerName.value = it },
                            label = { Text(Strings.customerNameLabel(isUrdu)) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("bill_customer_name")
                        )

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { viewModel.billCustomerPhone.value = it },
                            label = { Text(Strings.customerPhoneLabel(isUrdu)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("bill_customer_phone")
                        )
                    }
                }
            }
        }

        // Quick Product Selector Row
        item {
            Text(
                text = Strings.selectProducts(isUrdu),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyRow(
                contentPadding = PaddingValues(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .width(150.dp)
                            .clickable { viewModel.addProductToBill(product) }
                            .testTag("quick_add_product_${product.id}")
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Text(
                                text = "Rs. ${product.pricePkr}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = Strings.translateCategory(product.category, isUrdu),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = Color.White,
                                        modifier = Modifier.padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bill Items Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${Strings.itemsInBill(isUrdu)} (${billItems.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (billItems.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { viewModel.clearBill() },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(Strings.clearBill(isUrdu), fontSize = 12.sp)
                    }
                }
            }
        }

        // Bill Items List
        if (billItems.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = Strings.emptyBillNotice(isUrdu),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(billItems, key = { it.productId }) { item ->
                BillItemRow(
                    item = item,
                    isUrdu = isUrdu,
                    onIncrement = { viewModel.incrementBillItem(item.productId) },
                    onDecrement = { viewModel.decrementBillItem(item.productId) },
                    onRemove = { viewModel.removeBillItem(item.productId) }
                )
            }

            // Calculation & Totals Card
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Subtotal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(Strings.subtotal(isUrdu), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Rs. $subtotal", fontWeight = FontWeight.SemiBold)
                        }

                        // Discount Input
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(Strings.discount(isUrdu), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = if (discount == 0) "" else discount.toString(),
                                onValueChange = {
                                    val num = it.filter { ch -> ch.isDigit() }.toIntOrNull() ?: 0
                                    viewModel.billDiscount.value = num
                                },
                                placeholder = { Text("0") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .width(110.dp)
                                    .testTag("bill_discount_input")
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Grand Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = Strings.grandTotal(isUrdu),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Rs. $finalTotal PKR",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Action Buttons: Print Bill & WhatsApp Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Print Button
                            Button(
                                onClick = {
                                    val savedBill = viewModel.finalizeAndSaveBill(deductInventory = true)
                                    val bNum = savedBill?.billNumber ?: "DDK-${System.currentTimeMillis() % 10000}"
                                    PrintAndShareHelper.printBillDocument(
                                        context = context,
                                        shopInfo = shopInfo,
                                        customerName = customerName.ifBlank { "Walk-in Customer" },
                                        customerPhone = customerPhone,
                                        items = billItems,
                                        discount = discount,
                                        total = finalTotal,
                                        billNumber = bNum,
                                        isUrdu = isUrdu
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("print_bill_button")
                            ) {
                                Icon(imageVector = Icons.Default.Print, contentDescription = "Print")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(Strings.printBill(isUrdu), fontWeight = FontWeight.Bold)
                            }

                            // WhatsApp Share Button
                            Button(
                                onClick = {
                                    val savedBill = viewModel.finalizeAndSaveBill(deductInventory = false)
                                    val bNum = savedBill?.billNumber ?: "DDK-${System.currentTimeMillis() % 10000}"
                                    PrintAndShareHelper.shareBillText(
                                        context = context,
                                        shopInfo = shopInfo,
                                        customerName = customerName.ifBlank { "Valued Customer" },
                                        customerPhone = customerPhone,
                                        items = billItems,
                                        discount = discount,
                                        total = finalTotal,
                                        billNumber = bNum,
                                        isUrdu = isUrdu
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("whatsapp_share_bill_button")
                            ) {
                                Icon(imageVector = Icons.Outlined.Chat, contentDescription = "Share WhatsApp", tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(Strings.shareWhatsApp(isUrdu), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Success Bill Modal
    if (showBillReceiptDialog && lastGeneratedBill != null) {
        val bill = lastGeneratedBill!!
        AlertDialog(
            onDismissRequest = { viewModel.showBillReceiptDialog.value = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = if (isUrdu) "بل کامیابی سے بن گیا!" else "Bill Generated Successfully!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("${if (isUrdu) "بل نمبر:" else "Bill #:"} ${bill.billNumber}")
                    Text("${if (isUrdu) "گاہک:" else "Customer:"} ${bill.customerName}")
                    Text("${if (isUrdu) "کل رقم:" else "Total Amount:"} Rs. ${bill.totalPkr} PKR", fontWeight = FontWeight.Bold)
                    Text(
                        text = if (isUrdu) "اسٹاک خودکار طور پر اپ ڈیٹ کر دیا گیا ہے۔" else "Stock automatically deducted from inventory.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.showBillReceiptDialog.value = false
                        viewModel.clearBill()
                    }
                ) {
                    Text(if (isUrdu) "ٹھیک ہے" else "Done")
                }
            }
        )
    }
}

@Composable
fun BillItemRow(
    item: BillItem,
    isUrdu: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bill_item_row_${item.productId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Rs. ${item.unitPricePkr} x ${item.quantity} = Rs. ${item.subtotal}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Quantity adjusters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(28.dp)
                ) {
                    IconButton(onClick = onDecrement, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(28.dp)
                ) {
                    IconButton(onClick = onIncrement, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = LowStockRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
