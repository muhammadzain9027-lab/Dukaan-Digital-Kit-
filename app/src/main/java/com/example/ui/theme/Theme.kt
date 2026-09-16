package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
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

private val LightColorScheme =
    lightColorScheme(
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

@Composable
fun DukaanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
