package com.notaday.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = SoftBlue,
    secondary = SoftPurple,
    tertiary = SoftTeal,
    background = WarmWhite,
    surface = WarmWhite
)

private val DarkScheme = darkColorScheme(
    primary = SoftBlueDark,
    secondary = SoftPurpleDark,
    tertiary = SoftTealDark
)

@Composable
fun NotadayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = NotadayTypography,
        content = content
    )
}
