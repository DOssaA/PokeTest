package com.darioossa.poketest.ui.pokedex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.darioossa.poketest.ui.pokemonDetailRoute
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokedexContainerScreen(
    navController: NavHostController,
    edgeToEdgePadding: PaddingValues,
    listViewModel: PokedexListViewModel = koinViewModel(),
) {
    Box(modifier = Modifier.padding(paddingValues = edgeToEdgePadding)) {
        val state by listViewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            listViewModel.effect.collectLatest { effect ->
                if (effect is PokedexListEffect.NavigateToDetail) {
                    navController.navigate(pokemonDetailRoute(effect.pokemonId))
                }
            }
        }

        PokedexListScreen(
            state = state,
            onPokemonClick = listViewModel::openDetail,
            onToggleFavorite = listViewModel::toggleFavorite,
            onRetry = listViewModel::retry,
            onSearchQueryChanged = listViewModel::updateSearchQuery,
            onOpenFilters = {},
            onApplyFilters = listViewModel::applyFilters,
            onLoadMore = listViewModel::loadMore
        )
    }
}
