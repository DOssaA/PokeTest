package com.darioossa.poketest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.darioossa.poketest.ui.pokedex.PokedexListEffect
import com.darioossa.poketest.ui.pokedex.PokedexListScreen
import com.darioossa.poketest.ui.pokedex.PokedexListViewModel
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailScreen
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

private const val ROUTE_POKEDEX = "pokedex"
private const val ROUTE_DETAIL = "pokemon/{id}"
private const val POKEMON_ID_ARG = "id"

fun pokemonDetailRoute(id: Int) = "pokemon/$id"

@Composable
fun PokedexNavGraph(
    navController: NavHostController,
    listViewModel: PokedexListViewModel = koinViewModel(),
    detailViewModel: PokemonDetailViewModel = koinViewModel()
) {
    NavHost(navController = navController, startDestination = ROUTE_POKEDEX) {
        composable(ROUTE_POKEDEX) {
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
                onRetry = listViewModel::retry
            )
        }

        composable(
            ROUTE_DETAIL,
            arguments = listOf(navArgument(POKEMON_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt(POKEMON_ID_ARG) ?: return@composable
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
}
