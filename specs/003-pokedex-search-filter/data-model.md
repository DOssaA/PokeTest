# Data Model: Pokedex Search & Filters

**Date**: January 25, 2026

## Entities

### PokemonSummary

Represents a Pokemon entry shown in the Pokedex list.

- **Fields**: id (Int), name (String), imageUrl (String), types (List<String>), isFavorite (Boolean)
- **Source**: Cached Pokemon entity + favorites set
- **Notes**: types must be populated from detail data to enable type filtering.

### PokemonTypeOption

Represents a selectable type in the filter modal.

- **Fields**: name (String), isSelected (Boolean)
- **Validation**: name is lowercase; must be present in AvailableTypes.

### PokedexFilters

Represents the active search and filter constraints.

- **Fields**: query (String), favoritesOnly (Boolean), selectedTypes (Set<String>)
- **Validation**:
  - query is trimmed; empty query means no text filtering
  - selectedTypes must be a subset of AvailableTypes

### AvailableTypes

Represents the authoritative list of Pokemon types for filtering.

- **Fields**: types (List<String>)
- **Source**: PokeAPI Types endpoint

### PokedexPagingState

Tracks list pagination and lazy-loading state.

- **Fields**: limit (Int), offset (Int), hasMore (Boolean), isLoadingInitial (Boolean), isLoadingMore (Boolean), loadMoreError (String?)
- **Validation**:
  - limit > 0
  - offset >= 0

## Relationships

- **PokedexFilters** applies to **PokemonSummary** list to produce a filtered list.
- **PokemonTypeOption** is derived from **AvailableTypes**, and selection updates **PokedexFilters.selectedTypes**.
- **PokedexPagingState** governs which Pokemon pages are fetched and appended to the list.

## State Transitions

- When query or filters change, the list is recomputed from the cached list and current favorites.
- When the lazy-load trigger fires and hasMore is true, offset increases by limit and isLoadingMore toggles true until load completes.
- On load-more failure, loadMoreError is set and isLoadingMore resets to false; retry clears the error and re-attempts.
