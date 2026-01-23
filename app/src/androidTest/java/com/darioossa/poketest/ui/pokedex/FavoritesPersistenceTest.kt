package com.darioossa.poketest.ui.pokedex

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.darioossa.poketest.domain.model.PokemonSummary
import org.junit.Rule
import org.junit.Test

class FavoritesPersistenceTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun favoriteToggleUpdatesState() {
        val state = mutableStateOf(
            PokedexListState(
                isLoading = false,
                items = listOf(
                    PokemonSummary(25, "pikachu", "https://example.com/25.png", emptyList(), false)
                ),
                errorMessage = null
            )
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state.value,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {
                    val updated = state.value.items.map {
                        if (it.id == 25) it.copy(isFavorite = !it.isFavorite) else it
                    }
                    state.value = state.value.copy(items = updated)
                }
            )
        }

        composeRule.onNodeWithContentDescription("Favorite Off").assertIsDisplayed()
        state.value = state.value.copy(items = listOf(state.value.items.first().copy(isFavorite = true)))
        composeRule.onNodeWithContentDescription("Favorite On").assertIsDisplayed()
    }
}
