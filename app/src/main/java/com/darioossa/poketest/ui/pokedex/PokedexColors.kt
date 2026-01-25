package com.darioossa.poketest.ui.pokedex

import androidx.compose.ui.graphics.Color

fun pokemonAccentColor(id: Int): Color {
    return when (id % 6) {
        0 -> Color(0xFF5C8DFF) // vivid blue
        1 -> Color(0xFF7A5CE6) // purple
        2 -> Color(0xFF62C36F) // green
        3 -> Color(0xFFFFC54D) // yellow
        4 -> Color(0xFFFF9A4D) // orange
        else -> Color(0xFFFF6B6B) // red
    }
}
