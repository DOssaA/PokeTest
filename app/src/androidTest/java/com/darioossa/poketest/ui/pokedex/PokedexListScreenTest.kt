package com.darioossa.poketest.ui.pokedex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.darioossa.poketest.domain.model.PokemonSummary
import org.junit.Rule
import org.junit.Test

class PokedexListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun listShowsPokemonItems() {
        val state = PokedexListState(
            isLoading = false,
            items = listOf(
                PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
                PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), true)
            ),
            errorMessage = null
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {}
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List).assertIsDisplayed()
        composeRule.onNodeWithText("bulbasaur", substring = true, ignoreCase = true)
            .assertIsDisplayed()
        composeRule.onNodeWithText("charmander", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }
}
