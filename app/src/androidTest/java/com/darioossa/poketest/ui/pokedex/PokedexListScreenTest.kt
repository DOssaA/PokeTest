package com.darioossa.poketest.ui.pokedex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.darioossa.poketest.domain.model.PokemonSummary
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class PokedexListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun listShowsPokemonItems() {
        val items = listOf(
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
            PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), true)
        )
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = listOf("grass", "fire"),
            isLoadingMore = false,
            loadMoreError = null,
            errorMessage = null
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {},
                onSearchQueryChanged = {},
                onOpenFilters = {},
                onApplyFilters = { _,_ -> },
                onLoadMore = {}
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List).assertIsDisplayed()
        composeRule.onNodeWithText("bulbasaur", substring = true, ignoreCase = true)
            .assertIsDisplayed()
        composeRule.onNodeWithText("charmander", substring = true, ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun searchFiltersVisibleItems() {
        val items = listOf(
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
            PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), true)
        )
        var state by mutableStateOf(
            PokedexListState(
                isLoading = false,
                items = items,
                visibleItems = items,
                query = "",
                favoritesOnly = false,
                selectedTypes = emptySet(),
                availableTypes = listOf("grass", "fire"),
                isLoadingMore = false,
                loadMoreError = null,
                errorMessage = null
            )
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {},
                onSearchQueryChanged = { query ->
                    state = state.copy(
                        query = query,
                        visibleItems = items.filter { it.name.contains(query, ignoreCase = true) }
                    )
                },
                onOpenFilters = {},
                onApplyFilters = { _,_ -> },
                onLoadMore = {}
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.SearchField)
            .performTextInput("bulb")

        composeRule.onNodeWithText("bulbasaur", substring = true, ignoreCase = true)
            .assertIsDisplayed()
        composeRule.onNodeWithText("charmander", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun filterButtonOpensFilterSheet() {
        val items = listOf(
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
            PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), true)
        )
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = listOf("grass", "fire"),
            isLoadingMore = false,
            loadMoreError = null,
            errorMessage = null
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {},
                onSearchQueryChanged = {},
                onOpenFilters = {},
                onApplyFilters = { _, _ -> },
                onLoadMore = {}
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.FilterButton)
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithTag(PokedexListScreenTags.FilterSheet)
            .assertIsDisplayed()
    }

    @Test
    fun scrollNearEndTriggersLoadMore() {
        val items = (1..40).map { id ->
            PokemonSummary(id, "pokemon$id", "https://example.com/$id.png", emptyList(), false)
        }
        var loadMoreCalls = 0
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = false,
            loadMoreError = null,
            errorMessage = null
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {},
                onSearchQueryChanged = {},
                onOpenFilters = {},
                onApplyFilters = { _, _ -> },
                onLoadMore = { loadMoreCalls += 1 }
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List)
            .performScrollToIndex(items.lastIndex)

        composeRule.runOnIdle {
            assertEquals(1, loadMoreCalls)
        }
    }

    @Test
    fun loadMoreErrorShowsRetry() {
        val items = listOf(
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false)
        )
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = false,
            loadMoreError = "Network error",
            errorMessage = null
        )

        composeRule.setContent {
            PokedexListScreen(
                state = state,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = {},
                onSearchQueryChanged = {},
                onOpenFilters = {},
                onApplyFilters = { _, _ -> },
                onLoadMore = {}
            )
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.LoadMoreRetry)
            .assertIsDisplayed()
    }
}
