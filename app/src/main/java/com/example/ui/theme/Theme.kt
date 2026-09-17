package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Emerald Bazaar
private val EmeraldDarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = Color(0xFF022C1A),
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = GoldAccentDark,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = GoldContainerDark,
    onSecondaryContainer = Color(0xFFFDE68A),
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    error = LowStockRed
)

private val EmeraldLightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = Color(0xFF042F1C),
    secondary = GoldAccent,
    onSecondary = Color.White,
    secondaryContainer = GoldContainerLight,
    onSecondaryContainer = Color(0xFF78350F),
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    error = LowStockRed
)

// 2. Royal Sapphire
private val RoyalDarkColorScheme = darkColorScheme(
    primary = RoyalPrimaryDark,
    onPrimary = Color(0xFF0B1938),
    primaryContainer = RoyalContainerDark,
    onPrimaryContainer = Color(0xFFBFDBFE),
    secondary = RoyalAccentDark,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = RoyalAccentContainerDark,
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF0A101D),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF0F172A),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = LowStockRed
)

private val RoyalLightColorScheme = lightColorScheme(
    primary = RoyalPrimary,
    onPrimary = Color.White,
    primaryContainer = RoyalContainerLight,
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = RoyalAccent,
    onSecondary = Color.White,
    secondaryContainer = RoyalAccentContainerLight,
    onSecondaryContainer = Color(0xFF78350F),
    background = Color(0xFFF8FAFC),
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = TextSecondaryLight,
    error = LowStockRed
)

// 3. Imperial Maroon
private val MaroonDarkColorScheme = darkColorScheme(
    primary = MaroonPrimaryDark,
    onPrimary = Color(0xFF380718),
    primaryContainer = MaroonContainerDark,
    onPrimaryContainer = Color(0xFFFECDD3),
    secondary = MaroonAccentDark,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = MaroonAccentContainerDark,
    onSecondaryContainer = Color(0xFFFDE68A),
    background = Color(0xFF180A10),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF24101A),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF3B1C2A),
    onSurfaceVariant = Color(0xFFFDA4AF),
    error = LowStockRed
)

private val MaroonLightColorScheme = lightColorScheme(
    primary = MaroonPrimary,
    onPrimary = Color.White,
    primaryContainer = MaroonContainerLight,
    onPrimaryContainer = Color(0xFF881337),
    secondary = MaroonAccent,
    onSecondary = Color.White,
    secondaryContainer = MaroonAccentContainerLight,
    onSecondaryContainer = Color(0xFF78350F),
    background = Color(0xFFFFF7F8),
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFFDE8ED),
    onSurfaceVariant = TextSecondaryLight,
    error = LowStockRed
)

// 4. Obsidian Titanium
private val ObsidianDarkColorScheme = darkColorScheme(
    primary = ObsidianPrimaryDark,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = ObsidianContainerDark,
    onPrimaryContainer = Color(0xFFCBD5E1),
    secondary = ObsidianAccentDark,
    onSecondary = Color(0xFF022C1A),
    secondaryContainer = ObsidianAccentContainerDark,
    onSecondaryContainer = Color(0xFFA7F3D0),
    background = Color(0xFF090D14),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF111827),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = TextSecondaryDark,
    error = LowStockRed
)

private val ObsidianLightColorScheme = lightColorScheme(
    primary = ObsidianPrimary,
    onPrimary = Color.White,
    primaryContainer = ObsidianContainerLight,
    onPrimaryContainer = Color(0xFF0F172A),
    secondary = ObsidianAccent,
    onSecondary = Color.White,
    secondaryContainer = ObsidianAccentContainerLight,
    onSecondaryContainer = Color(0xFF064E3B),
    background = Color(0xFFF8FAFC),
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,
    error = LowStockRed
)

@Composable
fun DukaanTheme(
    themeKey: String = "emerald",
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme: ColorScheme = when (themeKey.lowercase()) {
        "royal" -> if (darkTheme) RoyalDarkColorScheme else RoyalLightColorScheme
        "maroon" -> if (darkTheme) MaroonDarkColorScheme else MaroonLightColorScheme
        "obsidian" -> if (darkTheme) ObsidianDarkColorScheme else ObsidianLightColorScheme
        else -> if (darkTheme) EmeraldDarkColorScheme else EmeraldLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
