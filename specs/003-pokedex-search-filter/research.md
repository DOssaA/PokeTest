# Research: Pokedex Search & Filters

**Date**: January 25, 2026
**Goal**: Resolve design and integration choices for search, filtering, and lazy loading using official references.

## Decision 1: Search input component

**Decision**: Use a Material3 TextField/OutlinedTextField for the inline search field rather than the experimental SearchBar composable.

**Rationale**: The SearchBar composable is marked experimental in the official docs, and the feature only needs a compact inline search field below the subtitle (no suggestion panel). A standard TextField aligns with the reference layout while avoiding experimental APIs.

**Alternatives considered**:
- Use Material3 SearchBar (rejected: experimental API; additional UI chrome not required for this screen).

**Sources** (accessed 2026-01-25):
- https://developer.android.com/develop/ui/compose/components/search-bar

## Decision 2: Filter modal presentation

**Decision**: Use a Material3 ModalBottomSheet for the filter modal.

**Rationale**: The design calls for a modal overlay with controls for favorites and type checkboxes. Official guidance recommends ModalBottomSheet for bottom-anchored modal content with explicit show/hide control.

**Alternatives considered**:
- Dialog (rejected: less aligned with the reference style and bottom-anchored interaction).

**Sources** (accessed 2026-01-25):
- https://developer.android.com/develop/ui/compose/quick-guides/content/create-bottom-sheet

## Decision 3: Lazy-load trigger for grid

**Decision**: Observe LazyGridState layout info via snapshotFlow and trigger load-more when the last visible item index is within a threshold of the total item count.

**Rationale**: The LazyGridState API reference recommends snapshotFlow for side effects based on scrolling state to avoid recomposition loops. This fits the requirement to start loading before the end of the list.

**Alternatives considered**:
- Paging library integration (rejected for now: adds new dependency and changes architecture; manual paging already exists in the repository).

**Sources** (accessed 2026-01-25):
- https://developer.android.com/reference/kotlin/androidx/compose/foundation/lazy/grid/LazyGridState

## Decision 4: Type data sources for filtering

**Decision**: Use PokeAPI list pagination for initial list data, then fetch Pokemon detail for each visible page to populate types, and use the PokeAPI types endpoint to populate the type checklist in the filter modal.

**Rationale**: The list endpoint only provides name/URL resources, while the detail endpoint exposes the Pokemon types needed for accurate filtering. The types endpoint provides the authoritative list of types for the modal selection.

**Alternatives considered**:
- Derive types only from loaded Pokemon (rejected: could omit types not present in the loaded subset and create inconsistent filter options).

**Sources** (accessed 2026-01-25):
- https://pokeapi.co/docs/v2 (Resource Lists/Pagination, Pokemon endpoint, Types endpoint)

## Decision 5: Main-thread safety for UI state

**Decision**: Ensure that state updates that drive UI (MVI state and effects) are dispatched on Dispatchers.Main, while network and database work stays on IO.

**Rationale**: Dispatchers.Main is defined as the UI-thread dispatcher; UI-driven state updates should be confined to Main, while IO work remains off the UI thread.

**Alternatives considered**:
- Use only IO dispatcher and rely on StateFlow thread-safety (rejected: violates explicit requirement to run UI-affecting data on Main).

**Sources** (accessed 2026-01-25):
- https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-main.html
- https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html
