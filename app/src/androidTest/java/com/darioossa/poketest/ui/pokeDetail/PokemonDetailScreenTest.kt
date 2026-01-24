package com.darioossa.poketest.ui.pokeDetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.darioossa.poketest.domain.model.PokemonAbility
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.domain.model.PokemonStat
import org.junit.Rule
import org.junit.Test

class PokemonDetailScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun detailShowsSections() {
        val state = PokemonDetailState(
            isLoading = false,
            detail = PokemonDetail(
                id = 25,
                name = "pikachu",
                imageUrl = "https://example.com/pikachu.png",
                description = "A yellow electric mouse.",
                stats = listOf(PokemonStat("hp", 35), PokemonStat("attack", 55)),
                abilities = listOf(PokemonAbility("static", false)),
                types = listOf("electric"),
                height = 4,
                weight = 60,
                category = "Mouse Pokemon"
            ),
            errorMessage = null
        )

        composeRule.setContent {
            PokemonDetailScreen(state = state, onRetry = {})
        }

        composeRule.onNodeWithTag(PokemonDetailScreenTags.Content).assertIsDisplayed()
        composeRule.onNodeWithText("About").assertIsDisplayed()
        composeRule.onNodeWithText("Base Stats").assertIsDisplayed()
    }
}
