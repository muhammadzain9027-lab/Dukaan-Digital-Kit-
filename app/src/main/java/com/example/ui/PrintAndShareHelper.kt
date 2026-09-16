package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.data.BillEntity
import com.example.data.BillItem
import com.example.data.ShopInfo
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrintAndShareHelper {

    fun openWhatsAppOrder(
        context: Context,
        productName: String,
        pricePkr: Int,
        shopPhone: String,
        isUrdu: Boolean
    ) {
        val rawMessage = Strings.getWhatsAppMessage(productName, pricePkr, isUrdu)
        val encodedMessage = try {
            URLEncoder.encode(rawMessage, "UTF-8")
        } catch (e: Exception) {
            rawMessage.replace(" ", "%20")
        }

        // Format phone: if starts with 03, convert to 923...
        val cleanPhone = shopPhone.replace(Regex("[^0-9]"), "")
        val intlPhone = when {
            cleanPhone.startsWith("03") -> "92" + cleanPhone.substring(1)
            cleanPhone.startsWith("92") -> cleanPhone
            cleanPhone.isNotBlank() -> cleanPhone
            else -> ""
        }

        val url = if (intlPhone.isNotBlank()) {
            "https://wa.me/$intlPhone?text=$encodedMessage"
        } else {
            "https://wa.me/?text=$encodedMessage"
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareBillText(
        context: Context,
        shopInfo: ShopInfo,
        customerName: String,
        customerPhone: String,
        items: List<BillItem>,
        discount: Int,
        total: Int,
        billNumber: String,
        isUrdu: Boolean
    ) {
        val dateStr = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(Date())
        val sb = StringBuilder()

        if (isUrdu) {
            sb.append("🛍️ *${shopInfo.shopName}*\n")
            sb.append("📍 ${shopInfo.shopAddress}\n")
            sb.append("📞 رابطہ: ${shopInfo.shopPhone}\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            sb.append("بل نمبر: $billNumber\n")
            sb.append("تاریخ: $dateStr\n")
            sb.append("گاہک: $customerName\n")
            if (customerPhone.isNotBlank()) sb.append("موبائل: $customerPhone\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            items.forEach { item ->
                sb.append("• ${item.productName}\n")
                sb.append("  ${item.quantity} x Rs. ${item.unitPricePkr} = Rs. ${item.subtotal}\n")
            }
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            if (discount > 0) sb.append("رعایت (ڈسکاؤنٹ): Rs. $discount\n")
            sb.append("*حتمی ٹوٹل: Rs. $total PKR*\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            sb.append("شکریہ! دوبارہ تشریف لائیں۔\n")
        } else {
            sb.append("🛍️ *${shopInfo.shopName}*\n")
            sb.append("📍 ${shopInfo.shopAddress}\n")
            sb.append("📞 Contact: ${shopInfo.shopPhone}\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            sb.append("Bill #: $billNumber\n")
            sb.append("Date: $dateStr\n")
            sb.append("Customer: $customerName\n")
            if (customerPhone.isNotBlank()) sb.append("Phone: $customerPhone\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            items.forEach { item ->
                sb.append("• ${item.productName}\n")
                sb.append("  ${item.quantity} x Rs. ${item.unitPricePkr} = Rs. ${item.subtotal}\n")
            }
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            if (discount > 0) sb.append("Discount: Rs. $discount\n")
            sb.append("*Grand Total: Rs. $total PKR*\n")
            sb.append("━━━━━━━━━━━━━━━━━━━━\n")
            sb.append("Thank you for shopping with us!\n")
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, sb.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Bill Receipt")
        context.startActivity(shareIntent)
    }

    fun printBillDocument(
        context: Context,
        shopInfo: ShopInfo,
        customerName: String,
        customerPhone: String,
        items: List<BillItem>,
        discount: Int,
        total: Int,
        billNumber: String,
        isUrdu: Boolean
    ) {
        val dateStr = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(Date())
        val subtotal = items.sumOf { it.subtotal }

        val rowsHtml = items.joinToString("\n") { item ->
            """
            <tr>
                <td style="text-align: left; padding: 6px; border-bottom: 1px solid #ddd;">${item.productName} (${item.category})</td>
                <td style="text-align: center; padding: 6px; border-bottom: 1px solid #ddd;">${item.quantity}</td>
                <td style="text-align: right; padding: 6px; border-bottom: 1px solid #ddd;">Rs. ${item.unitPricePkr}</td>
                <td style="text-align: right; padding: 6px; border-bottom: 1px solid #ddd; font-weight: bold;">Rs. ${item.subtotal}</td>
            </tr>
            """.trimIndent()
        }

        val htmlContent = """
        <!DOCTYPE html>
        <html dir="${if (isUrdu) "rtl" else "ltr"}">
        <head>
            <meta charset="utf-8">
            <style>
                body { font-family: sans-serif, 'Noto Nastaliq Urdu'; margin: 20px; color: #111; font-size: 13px; }
                .header { text-align: center; border-bottom: 2px solid #0F5132; padding-bottom: 10px; margin-bottom: 15px; }
                .shop-title { font-size: 20px; font-weight: bold; color: #0F5132; margin: 0; }
                .shop-meta { font-size: 12px; color: #555; margin-top: 4px; }
                .bill-meta { display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 12px; border-bottom: 1px dashed #aaa; padding-bottom: 8px; }
                table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }
                th { background-color: #F1F8F4; color: #0F5132; padding: 8px; font-size: 12px; border-bottom: 2px solid #0F5132; }
                .total-box { float: right; width: 220px; margin-top: 10px; }
                .total-line { display: flex; justify-content: space-between; padding: 4px 0; }
                .grand-total { border-top: 2px solid #0F5132; font-size: 16px; font-weight: bold; color: #0F5132; padding-top: 6px; }
                .footer { clear: both; text-align: center; margin-top: 30px; font-size: 12px; color: #666; border-top: 1px solid #eee; padding-top: 10px; }
            </style>
        </head>
        <body>
            <div class="header">
                <h1 class="shop-title">${shopInfo.shopName}</h1>
                <div class="shop-meta">${shopInfo.shopOwner} | 📞 ${shopInfo.shopPhone}</div>
                <div class="shop-meta">📍 ${shopInfo.shopAddress}</div>
            </div>

            <table style="width: 100%; font-size: 12px; margin-bottom: 10px;">
                <tr>
                    <td><strong>${if (isUrdu) "بل نمبر:" else "Bill #:"}</strong> $billNumber</td>
                    <td style="text-align: right;"><strong>${if (isUrdu) "تاریخ:" else "Date:"}</strong> $dateStr</td>
                </tr>
                <tr>
                    <td><strong>${if (isUrdu) "گاہک:" else "Customer:"}</strong> $customerName</td>
                    <td style="text-align: right;"><strong>${if (isUrdu) "فون:" else "Phone:"}</strong> ${customerPhone.ifBlank { "N/A" }}</td>
                </tr>
            </table>

            <table>
                <thead>
                    <tr>
                        <th style="text-align: left;">${if (isUrdu) "کپڑا / ورائٹی" else "Fabric / Item"}</th>
                        <th style="text-align: center;">${if (isUrdu) "تعداد" else "Qty"}</th>
                        <th style="text-align: right;">${if (isUrdu) "قیمت" else "Rate"}</th>
                        <th style="text-align: right;">${if (isUrdu) "ٹوٹل" else "Total"}</th>
                    </tr>
                </thead>
                <tbody>
                    $rowsHtml
                </tbody>
            </table>

            <div class="total-box">
                <table style="width: 100%;">
                    <tr>
                        <td>${if (isUrdu) "کل رقم:" else "Subtotal:"}</td>
                        <td style="text-align: right;">Rs. $subtotal</td>
                    </tr>
                    ${if (discount > 0) "<tr><td>" + (if (isUrdu) "رعایت:" else "Discount:") + "</td><td style=\"text-align: right; color: #DC2626;\">- Rs. $discount</td></tr>" else ""}
                    <tr style="font-size: 15px; font-weight: bold; border-top: 2px solid #0F5132;">
                        <td style="padding-top: 6px; color: #0F5132;">${if (isUrdu) "حتمی رقم:" else "Grand Total:"}</td>
                        <td style="padding-top: 6px; text-align: right; color: #0F5132;">Rs. $total</td>
                    </tr>
                </table>
            </div>

            <div class="footer">
                <p><strong>${if (isUrdu) "دکان ڈیجیٹل کٹ | کپڑے کی رسید" else "Dukaan Digital Kit | Fabric Store Invoice"}</strong></p>
                <p>${if (isUrdu) "تشریف لانے کا شکریہ! فروخت شدہ مال تبدیل ہو سکتا ہے واپسی نہیں۔" else "Thank you for your business! Goods once sold can be exchanged within 7 days."}</p>
            </div>
        </body>
        </html>
        """.trimIndent()

        // Create an offscreen WebView to execute print job
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = false

            override fun onPageFinished(view: WebView, url: String) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter = webView.createPrintDocumentAdapter("Bill_$billNumber")
                    val printJob = printManager.print(
                        "Dukaan Bill $billNumber",
                        printAdapter,
                        PrintAttributes.Builder().build()
                    )
                } else {
                    Toast.makeText(context, "Print service not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)
    }
}
