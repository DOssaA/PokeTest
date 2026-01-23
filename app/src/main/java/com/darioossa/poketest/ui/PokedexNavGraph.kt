package com.darioossa.poketest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.darioossa.poketest.ui.pokedex.PokedexListEffect
import com.darioossa.poketest.ui.pokedex.PokedexListScreen
import com.darioossa.poketest.ui.pokedex.PokedexListViewModel
import com.darioossa.poketest.ui.pokedex.PokemonDetailScreen
import com.darioossa.poketest.ui.pokedex.PokemonDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

private const val ROUTE_POKEDEX = "pokedex"
private const val ROUTE_DETAIL = "pokemon/{id}"

fun pokemonDetailRoute(id: Int) = "pokemon/$id"

@Composable
fun PokedexNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ROUTE_POKEDEX) {
        composable(ROUTE_POKEDEX) {
            val viewModel: PokedexListViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.effect.collectLatest { effect ->
                    if (effect is PokedexListEffect.NavigateToDetail) {
                        navController.navigate(pokemonDetailRoute(effect.pokemonId))
                    }
                }
            }

            PokedexListScreen(
                state = state,
                onPokemonClick = viewModel::openDetail,
                onToggleFavorite = viewModel::toggleFavorite,
                onRetry = viewModel::retry
            )
        }

        composable(
            ROUTE_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("id") ?: return@composable
            val viewModel: PokemonDetailViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(pokemonId) {
                viewModel.loadPokemonDetail(pokemonId)
            }

            PokemonDetailScreen(
                state = state,
                onRetry = { viewModel.loadPokemonDetail(pokemonId, forceRefresh = true) }
            )
        }
    }
}
