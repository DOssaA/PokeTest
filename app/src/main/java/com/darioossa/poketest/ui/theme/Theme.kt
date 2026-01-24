package com.darioossa.poketest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PokeRed,
    secondary = PokeBlue,
    tertiary = PokeYellow,
    background = Color(0xFF121212),
    surface = Color(0xFF1C1C1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color(0xFFF0F0F0),
    onSurface = Color(0xFFF0F0F0)
)

private val LightColorScheme = lightColorScheme(
    primary = PokeRed,
    secondary = PokeBlue,
    tertiary = PokeYellow,
    background = PokeBackground,
    surface = PokeSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = PokeOnBackground,
    onSurface = PokeOnSurface
)

@Composable
fun PokeTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
