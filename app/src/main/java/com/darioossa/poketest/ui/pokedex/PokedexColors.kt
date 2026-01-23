package com.darioossa.poketest.ui.pokedex

import androidx.compose.ui.graphics.Color

fun pokemonAccentColor(id: Int): Color {
    return when (id % 6) {
        0 -> Color(0xFF5B8DEF)
        1 -> Color(0xFF8E68D5)
        2 -> Color(0xFF61B15A)
        3 -> Color(0xFFF2B84B)
        4 -> Color(0xFFF28B4B)
        else -> Color(0xFFE75B5B)
    }
}
