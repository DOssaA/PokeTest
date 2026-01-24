package com.darioossa.poketest.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.darioossa.poketest.ui.auth.AuthEntryScreen
import com.darioossa.poketest.ui.pokedex.PokedexListEffect
import com.darioossa.poketest.ui.pokedex.PokedexListScreen
import com.darioossa.poketest.ui.pokedex.PokedexListViewModel
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailScreen
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailViewModel
import com.darioossa.poketest.util.biometric.BiometricPromptManager
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

object PokedexRoutes {
    const val Login = "login"
    const val Pokedex = "pokedex"
    const val Detail = "pokemon/{id}"
}

const val PokemonIdArg = "id"

fun pokemonDetailRoute(id: Int) = "pokemon/$id"

@Composable
fun PokedexNavGraph(
    navController: NavHostController,
    edgeToEdgePadding: PaddingValues,
    biometricPromptManager: BiometricPromptManager,
    listViewModel: PokedexListViewModel = koinViewModel(),
    detailViewModel: PokemonDetailViewModel = koinViewModel()
) {
    NavHost(navController = navController, startDestination = PokedexRoutes.Login) {
        composable(PokedexRoutes.Login) {
            AuthEntryScreen(navController, biometricPromptManager)
        }

        composable(PokedexRoutes.Pokedex) {
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
                    onRetry = listViewModel::retry
                )
            }
        }

        composable(
            PokedexRoutes.Detail,
            arguments = listOf(navArgument(PokemonIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            Box(modifier = Modifier.padding(paddingValues = edgeToEdgePadding)) {
                val pokemonId = backStackEntry.arguments?.getInt(PokemonIdArg) ?: return@composable
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
}
