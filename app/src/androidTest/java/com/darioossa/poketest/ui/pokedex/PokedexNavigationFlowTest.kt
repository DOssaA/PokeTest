package com.darioossa.poketest.ui.pokedex

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonSummary
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailScreen
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailScreenTags
import com.darioossa.poketest.ui.pokeDetail.PokemonDetailState
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class PokedexNavigationFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun listNavigatesToTwoDetailsAndBack() {
        composeRule.setContent {
            TestPokedexNavHost()
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List).assertIsDisplayed()

        composeRule.onNodeWithTag("pokemon_card_25").performClick()
        composeRule.onNodeWithTag(PokemonDetailScreenTags.Content).assertIsDisplayed()
        composeRule.onNodeWithText("Pikachu").assertIsDisplayed()
        composeRule.activity.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List).assertIsDisplayed()

        composeRule.onNodeWithTag("pokemon_card_1").performClick()
        composeRule.onNodeWithTag(PokemonDetailScreenTags.Content).assertIsDisplayed()
        composeRule.onNodeWithText("Bulbasaur").assertIsDisplayed()
        composeRule.activity.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        composeRule.onNodeWithTag(PokedexListScreenTags.List).assertIsDisplayed()
    }

    @Test
    fun favoritesToggleForMultiplePokemon() {
        val items = listOf(
            PokemonSummary(25, "pikachu", "https://example.com/25.png", listOf("electric"), false),
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
            PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), false),
            PokemonSummary(7, "squirtle", "https://example.com/7.png", listOf("water"), false),
            PokemonSummary(10, "caterpie", "https://example.com/10.png", listOf("bug"), false)
        )
        val random = Random(0)
        val favoriteIds = items.shuffled(random).take(3).map { it.id }
        val uncheckIds = favoriteIds.take(2)

        composeRule.setContent {
            val listState = remember {
                mutableStateOf(
                    PokedexListState(
                        isLoading = false,
                        items = items,
                        errorMessage = null
                    )
                )
            }

            PokedexListScreen(
                state = listState.value,
                onPokemonClick = {},
                onRetry = {},
                onToggleFavorite = { id ->
                    listState.value = listState.value.copy(
                        items = listState.value.items.map { item ->
                            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
                        }
                    )
                }
            )
        }

        favoriteIds.forEach { id ->
            composeRule.onNodeWithTag("favorite_toggle_$id").performClick()
            composeRule.onNodeWithTag("favorite_toggle_$id").assertContentDescriptionEquals("Favorite On")
        }

        uncheckIds.forEach { id ->
            composeRule.onNodeWithTag("favorite_toggle_$id").performClick()
            composeRule.onNodeWithTag("favorite_toggle_$id").assertContentDescriptionEquals("Favorite Off")
        }

        val stillFavorite = favoriteIds.last()
        composeRule.onNodeWithTag("favorite_toggle_$stillFavorite").assertContentDescriptionEquals("Favorite On")
    }
}

@Composable
private fun TestPokedexNavHost() {
    val navController = rememberNavController()
    val listItems = remember {
        listOf(
            PokemonSummary(25, "pikachu", "https://example.com/25.png", listOf("electric"), false),
            PokemonSummary(1, "bulbasaur", "https://example.com/1.png", listOf("grass"), false),
            PokemonSummary(4, "charmander", "https://example.com/4.png", listOf("fire"), false)
        )
    }
    val detailMap = remember {
        mapOf(
            25 to sampleDetail(
                id = 25,
                name = "pikachu",
                description = "Electric mouse."
            ),
            1 to sampleDetail(
                id = 1,
                name = "bulbasaur",
                description = "Seed pokemon."
            ),
            4 to sampleDetail(
                id = 4,
                name = "charmander",
                description = "Lizard pokemon."
            )
        )
    }

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            PokedexListScreen(
                state = PokedexListState(
                    isLoading = false,
                    items = listItems,
                    errorMessage = null
                ),
                onPokemonClick = { id -> navController.navigate("detail/$id") },
                onToggleFavorite = {},
                onRetry = {}
            )
        }
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val pokemonId = entry.arguments?.getInt("id") ?: return@composable
            val detail = detailMap[pokemonId]
            PokemonDetailScreen(
                state = PokemonDetailState(
                    isLoading = false,
                    detail = detail,
                    errorMessage = null
                ),
                onRetry = {}
            )
        }
    }
}

private fun sampleDetail(id: Int, name: String, description: String) = PokemonDetail(
    id = id,
    name = name,
    imageUrl = "https://example.com/$id.png",
    description = description,
    stats = emptyList(),
    abilities = emptyList(),
    types = listOf("grass"),
    height = 1,
    weight = 10,
    category = "Seed"
)
