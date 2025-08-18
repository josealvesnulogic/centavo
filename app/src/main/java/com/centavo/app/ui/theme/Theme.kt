package com.centavo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Azul marinho como base
val Navy = Color(0xFF0B2545)
val NavyDark = Color(0xFF081A30)
val Teal = Color(0xFF3AB4F2)
val Sky = Color(0xFF79C2FF)
val Success = Color(0xFFB6F2C2)
val Danger = Color(0xFFFFC2C2)

private val LightColors = lightColorScheme(
    primary = Navy,
    onPrimary = Color.White,
    secondary = Teal,
    onSecondary = Color.White,
    background = Color(0xFFF7F9FC),
    onBackground = Color(0xFF0A1420),
    surface = Color.White,
    onSurface = Color(0xFF0A1420),
)

private val DarkColors = darkColorScheme(
    primary = Sky,
    onPrimary = Color(0xFF081018),
    secondary = Teal,
    onSecondary = Color(0xFF081018),
    background = Color(0xFF0A1420),
    onBackground = Color(0xFFE6ECF5),
    surface = Color(0xFF101A28),
    onSurface = Color(0xFFE6ECF5),
)

@Composable
fun CentavoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}