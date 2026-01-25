@file:OptIn(ExperimentalMaterial3Api::class)

package com.darioossa.poketest.ui.pokedex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import coil.compose.AsyncImage
import com.darioossa.poketest.R
import com.darioossa.poketest.domain.model.PokemonSummary
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

object PokedexListScreenTags {
    const val List = "pokedex_list"
    const val Loading = "pokedex_loading"
    const val Error = "pokedex_error"
    const val SearchField = "pokedex_search_field"
    const val EmptyState = "pokedex_empty_state"
    const val FilterButton = "pokedex_filter_button"
    const val FilterSheet = "pokedex_filter_sheet"
    const val LoadMore = "pokedex_load_more"
    const val LoadMoreRetry = "pokedex_load_more_retry"
}

@Composable
fun PokedexListScreen(
    state: PokedexListState,
    onPokemonClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onRetry: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onOpenFilters: () -> Unit,
    onApplyFilters: (Boolean, Set<String>) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var localFavoritesOnly by remember(state.favoritesOnly) {
        mutableStateOf(state.favoritesOnly)
    }
    var localSelectedTypes by remember(state.selectedTypes) {
        mutableStateOf(state.selectedTypes)
    }
    val loadMoreThreshold = 4
    val loadMoreTriggerIndex = (state.visibleItems.size - 1 - loadMoreThreshold).coerceAtLeast(0)

    androidx.compose.runtime.LaunchedEffect(
        listState,
        state.visibleItems.size,
        state.isLoadingMore,
        state.loadMoreError
    ) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .filter { lastIndex ->
                val hasUserScrolled = listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
                state.visibleItems.isNotEmpty() &&
                    hasUserScrolled &&
                    lastIndex >= loadMoreTriggerIndex &&
                    !state.isLoadingMore &&
                    state.loadMoreError == null
            }
            .collect { onLoadMore() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.pokedex_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.pokedex_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(PokedexListScreenTags.SearchField),
                    placeholder = {
                        Text(text = stringResource(R.string.pokedex_search_placeholder))
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )
                Spacer(modifier = Modifier.size(10.dp))
                IconButton(
                    onClick = {
                        showFilters = true
                        onOpenFilters()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag(PokedexListScreenTags.FilterButton)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = stringResource(R.string.pokedex_filter_button_cd),
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag(PokedexListScreenTags.Loading)
                        )
                    }
                }
                state.errorMessage != null -> {
                    ErrorState(
                        message = state.errorMessage,
                        onRetry = onRetry,
                        modifier = Modifier.weight(1f)
                    )
                }
                state.visibleItems.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag(PokedexListScreenTags.EmptyState),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.pokedex_empty_state),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    PokemonGrid(
                        items = state.visibleItems,
                        listState = listState,
                        isLoadingMore = state.isLoadingMore,
                        loadMoreError = state.loadMoreError,
                        onLoadMore = onLoadMore,
                        onPokemonClick = onPokemonClick,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }

    if (showFilters) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = sheetState,
            modifier = Modifier.testTag(PokedexListScreenTags.FilterSheet)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.pokedex_filter_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = localFavoritesOnly,
                        onCheckedChange = { checked ->
                            localFavoritesOnly = checked
                            onApplyFilters(localFavoritesOnly, localSelectedTypes)
                        }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(R.string.pokedex_filter_favorites))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.pokedex_filter_types),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                state.availableTypes.forEach { type ->
                    val isSelected = localSelectedTypes.contains(type)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                localSelectedTypes = if (checked) {
                                    localSelectedTypes + type
                                } else {
                                    localSelectedTypes - type
                                }
                                onApplyFilters(localFavoritesOnly, localSelectedTypes)
                            }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = type.replaceFirstChar { it.uppercase() })
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun PokemonGrid(
    items: List<PokemonSummary>,
    listState: LazyGridState,
    isLoadingMore: Boolean,
    loadMoreError: String?,
    onLoadMore: () -> Unit,
    onPokemonClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(PokedexListScreenTags.List)
    ) {
        items(items, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) },
                onToggleFavorite = { onToggleFavorite(pokemon.id) }
            )
        }
        if (isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag(PokedexListScreenTags.LoadMore),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        if (loadMoreError != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag(PokedexListScreenTags.LoadMoreRetry),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = loadMoreError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onLoadMore) {
                        Text(text = stringResource(R.string.pokedex_load_more_retry))
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: PokemonSummary,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val headerColor = pokemonAccentColor(pokemon.id)
    Card(
        onClick = onClick,
        modifier = Modifier.testTag("pokemon_card_${pokemon.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(12.dp)
            ) {
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .testTag("favorite_toggle_${pokemon.id}")
                        .semantics {
                            contentDescription = if (pokemon.isFavorite) "Favorite On" else "Favorite Off"
                        }
                ) {
                    Icon(
                        imageVector = if (pokemon.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .align(Alignment.Center)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (pokemon.types.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        pokemon.types.take(2).forEach { type ->
                            TypeChip(label = type)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeChip(label: String) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(PokedexListScreenTags.Error),
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
