package com.example.virtualcardexample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB2BE),
    secondary = Color(0xFFE7BDBE),
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF1C1B1B),
    surface = Color(0xFF1C1B1B),
    onPrimary = Color(0xFF670020),
    onSecondary = Color(0xFF44292A),
    onTertiary = Color(0xFF492532),
    onBackground = Color(0xFFE6E1E1),
    onSurface = Color(0xFFE6E1E1),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFBF0031),
    secondary = Color(0xFF775657),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFFFBFF),
    surface = Color(0xFFFFFBFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF201A1B),
    onSurface = Color(0xFF201A1B),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = true, // Default to dark theme as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
