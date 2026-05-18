package com.krossale.antiscam.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    secondary = BlueLight,
    onSecondary = White,
    background = LightGray,
    onBackground = Navy,
    surface = White,
    onSurface = Navy,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = NavyLight
)

@Composable
fun AntiScamTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
