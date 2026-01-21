package com.example.template

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryPink = Color(0xFFE91E63)
private val SecondaryPink = Color(0xFFFCE4EC)
private val DarkPink = Color(0xFF880E4F)
private val LightPinkBackground = Color(0xFFFFF0F5)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPink,
    onPrimary = Color.White,
    primaryContainer = DarkPink,
    onPrimaryContainer = SecondaryPink,
    secondary = SecondaryPink,
    onSecondary = DarkPink,
    secondaryContainer = Color(0xFF3D1A2B),
    onSecondaryContainer = SecondaryPink,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFE0E0E0),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPink,
    onPrimary = Color.White,
    primaryContainer = SecondaryPink,
    onPrimaryContainer = DarkPink,
    secondary = SecondaryPink,
    onSecondary = DarkPink,
    secondaryContainer = SecondaryPink,
    onSecondaryContainer = DarkPink,
    background = LightPinkBackground,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = SecondaryPink,
    onSurfaceVariant = DarkPink,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun ValentineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
