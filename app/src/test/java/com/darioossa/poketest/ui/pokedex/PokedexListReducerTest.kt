package com.darioossa.poketest.ui.pokedex

import com.darioossa.poketest.testdata.PokedexTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PokedexListReducerTest {
    private val reducer = PokedexListReducer()

    @Test
    fun `search query filters items by name`() {
        val items = listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
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

        val (newState, effect) = reducer.reduce(state, PokedexListEvent.SearchQueryChanged("pika"))

        assertEquals("pika", newState.query)
        assertEquals(1, newState.visibleItems.size)
        assertEquals("pikachu", newState.visibleItems.first().name)
        assertNull(effect)
    }

    @Test
    fun `empty query restores full list`() {
        val items = listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = listOf(PokedexTestData.pikachuSummary),
            query = "pika",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = false,
            loadMoreError = null,
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(state, PokedexListEvent.SearchQueryChanged(""))

        assertEquals("", newState.query)
        assertEquals(2, newState.visibleItems.size)
        assertNull(effect)
    }

    @Test
    fun `favorites filter limits visible items`() {
        val items = listOf(
            PokedexTestData.pikachuSummary.copy(isFavorite = false),
            PokedexTestData.bulbasaurSummary.copy(isFavorite = true)
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
            loadMoreError = null,
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(
            state,
            PokedexListEvent.FiltersChanged(favoritesOnly = true, selectedTypes = emptySet())
        )

        assertEquals(true, newState.favoritesOnly)
        assertEquals(1, newState.visibleItems.size)
        assertEquals("bulbasaur", newState.visibleItems.first().name)
        assertNull(effect)
    }

    @Test
    fun `type filter limits visible items`() {
        val items = listOf(PokedexTestData.pikachuSummary, PokedexTestData.bulbasaurSummary)
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = listOf("electric", "grass"),
            isLoadingMore = false,
            loadMoreError = null,
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(
            state,
            PokedexListEvent.FiltersChanged(favoritesOnly = false, selectedTypes = setOf("grass"))
        )

        assertEquals(setOf("grass"), newState.selectedTypes)
        assertEquals(1, newState.visibleItems.size)
        assertEquals("bulbasaur", newState.visibleItems.first().name)
        assertNull(effect)
    }

    @Test
    fun `load more started updates state`() {
        val items = listOf(PokedexTestData.pikachuSummary)
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = false,
            loadMoreError = "Previous error",
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(state, PokedexListEvent.LoadMoreStarted)

        assertEquals(true, newState.isLoadingMore)
        assertEquals(null, newState.loadMoreError)
        assertNull(effect)
    }

    @Test
    fun `load more failed sets error`() {
        val items = listOf(PokedexTestData.pikachuSummary)
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = true,
            loadMoreError = null,
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(
            state,
            PokedexListEvent.LoadMoreFailed("Network error")
        )

        assertEquals(false, newState.isLoadingMore)
        assertEquals("Network error", newState.loadMoreError)
        assertNull(effect)
    }

    @Test
    fun `more data loaded appends items`() {
        val items = listOf(PokedexTestData.pikachuSummary)
        val moreItems = listOf(PokedexTestData.bulbasaurSummary)
        val state = PokedexListState(
            isLoading = false,
            items = items,
            visibleItems = items,
            query = "",
            favoritesOnly = false,
            selectedTypes = emptySet(),
            availableTypes = emptyList(),
            isLoadingMore = true,
            loadMoreError = null,
            errorMessage = null
        )

        val (newState, effect) = reducer.reduce(
            state,
            PokedexListEvent.MoreDataLoaded(moreItems)
        )

        assertEquals(2, newState.items.size)
        assertEquals(2, newState.visibleItems.size)
        assertEquals(false, newState.isLoadingMore)
        assertNull(effect)
    }
}
