package com.darioossa.poketest.ui.pokeDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.darioossa.poketest.ui.PokemonIdArg
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokeDetailContainerScreen(
    edgeToEdgePadding: PaddingValues,
    backStackEntry: NavBackStackEntry,
    detailViewModel: PokemonDetailViewModel = koinViewModel()
) {
    Box(modifier = Modifier.padding(paddingValues = edgeToEdgePadding)) {
        val pokemonId = backStackEntry.arguments?.getInt(PokemonIdArg) ?: return
        val state by detailViewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(pokemonId) {
            detailViewModel.loadPokemonDetail(pokemonId)
        }

        PokemonDetailScreen(
            state = state,
            onRetry = { detailViewModel.loadPokemonDetail(pokemonId, forceRefresh = true) }
        )
    }
}