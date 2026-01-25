package com.darioossa.poketest.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.darioossa.poketest.ui.auth.AuthEntryScreen
import com.darioossa.poketest.ui.pokeDetail.PokeDetailContainerScreen
import com.darioossa.poketest.ui.pokedex.PokedexContainerScreen
import com.darioossa.poketest.util.biometric.BiometricPromptManager

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
    biometricPromptManager: BiometricPromptManager
) {
    NavHost(navController = navController, startDestination = PokedexRoutes.Login) {
        composable(PokedexRoutes.Login) {
            AuthEntryScreen(navController, biometricPromptManager)
        }

        composable(PokedexRoutes.Pokedex) {
            PokedexContainerScreen(navController, edgeToEdgePadding)
        }

        composable(
            PokedexRoutes.Detail,
            arguments = listOf(navArgument(PokemonIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            PokeDetailContainerScreen(edgeToEdgePadding, backStackEntry)
        }
    }
}
