# Data Model: Pokedex List and Details

## Entities

### PokemonSummary
Represents a list item in the Pokedex screen.

- **id**: Int (positive)
- **name**: String (non-empty)
- **imageUrl**: String (valid URL, https)
- **types**: List<String> (optional)
- **isFavorite**: Boolean

### PokemonDetail
Represents rich detail content for a Pokemon.

- **id**: Int (positive)
- **name**: String (non-empty)
- **imageUrl**: String (valid URL, https)
- **description**: String (optional)
- **stats**: List<PokemonStat> (optional)
- **abilities**: List<PokemonAbility> (optional)
- **types**: List<String> (optional)
- **height**: Int (optional, non-negative)
- **weight**: Int (optional, non-negative)
- **category**: String (optional)

### PokemonStat
- **name**: String (non-empty)
- **value**: Int (non-negative)

### PokemonAbility
- **name**: String (non-empty)
- **isHidden**: Boolean (optional)

### Favorite
User preference stored locally (encrypted).

- **pokemonId**: Int (positive)
- **createdAt**: Instant

## Relationships

- PokemonSummary and PokemonDetail share the same `id` and `name`.
- PokemonDetail aggregates many PokemonStat and PokemonAbility entries.
- Favorite references PokemonSummary/PokemonDetail via `pokemonId`.

## Validation Rules

- IDs must be positive integers.
- Names must be non-empty and trimmed.
- Image URLs must be HTTPS.
- Stats and attributes may be absent; missing values should be handled as
  "not available" in UI.
- Favorites are unique by `pokemonId`.
