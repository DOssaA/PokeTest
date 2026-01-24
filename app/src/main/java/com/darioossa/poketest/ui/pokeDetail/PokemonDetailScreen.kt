package com.darioossa.poketest.ui.pokeDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.darioossa.poketest.domain.model.PokemonDetail
import com.darioossa.poketest.ui.pokedex.pokemonAccentColor

object PokemonDetailScreenTags {
    const val Content = "pokemon_detail_content"
    const val Loading = "pokemon_detail_loading"
    const val Error = "pokemon_detail_error"
}

@Composable
fun PokemonDetailScreen(
    state: PokemonDetailState,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(PokemonDetailScreenTags.Loading)
                    )
                }
            }
            state.errorMessage != null -> {
                ErrorDetailState(state.errorMessage, onRetry)
            }
            state.detail != null -> {
                DetailContent(state.detail)
            }
        }
    }
}

@Composable
private fun DetailContent(detail: PokemonDetail) {
    val headerColor = pokemonAccentColor(detail.id)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag(PokemonDetailScreenTags.Content)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column {
                Text(
                    text = detail.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = detail.types.joinToString(" / ") { it.replaceFirstChar { c -> c.uppercase() } },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            AsyncImage(
                model = detail.imageUrl,
                contentDescription = detail.name,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionHeader(title = "About")
                Text(
                    text = detail.description ?: "Not available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                SectionHeader(title = "Attributes")
                AttributeRow("Height", detail.height?.toString() ?: "Not available")
                AttributeRow("Weight", detail.weight?.toString() ?: "Not available")
                AttributeRow("Category", detail.category ?: "Not available")

                SectionHeader(title = "Abilities")
                if (detail.abilities.isEmpty()) {
                    Text(text = "Not available", color = MaterialTheme.colorScheme.onSurface)
                } else {
                    detail.abilities.forEach { ability ->
                        Text(
                            text = ability.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                SectionHeader(title = "Base Stats")
                if (detail.stats.isEmpty()) {
                    Text(text = "Not available", color = MaterialTheme.colorScheme.onSurface)
                } else {
                    detail.stats.forEach { stat ->
                        StatRow(stat.name, stat.value, headerColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun AttributeRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun StatRow(label: String, value: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label.replaceFirstChar { it.uppercase() })
            Text(text = value.toString(), fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        val normalized = (value.coerceIn(0, 150) / 150f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(color.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(normalized)
                    .height(6.dp)
                    .background(color)
            )
        }
    }
}

@Composable
private fun ErrorDetailState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(PokemonDetailScreenTags.Error),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        androidx.compose.material3.Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}
