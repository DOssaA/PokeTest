# Implementation Plan: Pokedex Search & Filters

**Branch**: `003-pokedex-search-filter` | **Date**: January 25, 2026 | **Spec**: `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/spec.md`
**Input**: Feature specification from `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/spec.md`

**Note**: This plan follows the project constitution and the existing UI/Domain/Data layer architecture.

## Summary

Add a search field below the Pokedex subtitle with a small rounded filter button that opens a modal for favorites and type filters, then implement lazy loading so additional Pokemon load before the list ends. Types will be sourced from PokeAPI detail data so type filters are accurate, and a type list will be shown in the modal. The UI will follow the visual direction in `/Users/darioossa/Repos/PokeTest/style_reference.png`, all UI elements will be composables, and state updates will be performed on the Main dispatcher where UI is affected. Tests and docs for the Pokedex screen will be expanded per TDD.

## Technical Context

**Language/Version**: Kotlin 2.3.0 (JVM 11)  
**Primary Dependencies**: Jetpack Compose BOM 2026.01.00 (Material3, Navigation), Coroutines 1.10.2, Retrofit 3.0.0 + OkHttp 5.3.2 + Moshi 1.15.2, Room 2.8.4, DataStore 1.2.0, Koin 4.1.1, Coil 2.7.0  
**Storage**: Room (Pokemon cache) + encrypted DataStore Preferences (favorites/auth)  
**Testing**: JUnit4, MockK, Turbine, kotlinx-coroutines-test, Compose UI tests  
**Target Platform**: Android (minSdk 26, targetSdk 36)  
**Project Type**: mobile (single-activity Compose app)  
**Performance Goals**: search/filter results visible within 1s; lazy-load append within 2s of trigger; scrolling stays responsive (no jank)  
**Constraints**: HTTPS-only, OAuth required for auth, local data encrypted, TDD gate, MVI pattern, UI components are composables, UI state updates run on Main dispatcher, style matches `/Users/darioossa/Repos/PokeTest/style_reference.png`  
**Scale/Scope**: Pokedex list screen; pages of 20; support continuous browsing of 200+ items per session

### Official Documentation Sources (versions/dates recorded)

- Jetpack Compose Search Bar docs (experimental API noted), accessed 2026-01-25: `https://developer.android.com/develop/ui/compose/components/search-bar`
- Jetpack Compose Bottom Sheets docs (ModalBottomSheet usage), accessed 2026-01-25: `https://developer.android.com/develop/ui/compose/components/bottom-sheets`
- LazyGridState API reference (snapshotFlow guidance), accessed 2026-01-25: `https://developer.android.com/reference/kotlin/androidx/compose/foundation/lazy/grid/LazyGridState`
- Kotlin Coroutines dispatchers and Main dispatcher docs, accessed 2026-01-25: `https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html` and `https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-main.html`
- PokeAPI v2 documentation (pagination, Pokemon, Types endpoints), accessed 2026-01-25: `https://pokeapi.co/docs/v2`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] SOLID/DRY and intentional patterns are planned
- [x] TDD: tests will be written first and fail before implementation
- [x] UX consistency: reuse navigation/components/copy; accessibility/localization planned
- [x] Performance budgets defined with regression checks
- [x] Official docs sources recorded with versions/dates
- [x] Documentation planned for non-intuitive behavior; Kotlin conventions enforced
- [x] Security constraints covered (HTTPS, OAuth, local encryption, no credentials)

## Project Structure

### Documentation (this feature)

```text
/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/
- plan.md
- research.md
- data-model.md
- quickstart.md
- contracts/
- tasks.md
```

### Source Code (repository root)

```text
/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/
- data/
  - local/
    - PokeLocalDataSource.kt
    - PokemonDao.kt
    - PokemonEntity.kt
  - mapper/
    - PokemonMappers.kt
  - remote/
    - PokeApiService.kt
    - dto/
  - repository/
    - PokemonRepository.kt
    - PokemonRepositoryImpl.kt
- domain/
  - model/
    - PokemonModels.kt
  - usecase/
    - GetPokemonListUseCase.kt
    - ObserveFavoritesUseCase.kt
    - ToggleFavoriteUseCase.kt
- ui/
  - base/
    - BaseMVIViewModel.kt
  - pokedex/
    - PokedexContainerScreen.kt
    - PokedexListScreen.kt
    - PokedexListReducer.kt
    - PokedexListViewModel.kt
    - PokedexColors.kt
- di/
  - Modules.kt

/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/
- ui/pokedex/
  - PokedexListViewModelTest.kt

/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/
- ui/pokedex/
  - PokedexListScreenTest.kt
  - PokedexNavigationFlowTest.kt
  - FavoritesPersistenceTest.kt
```

**Structure Decision**: Single Android app module with layered packages (ui, domain, data) under `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest`.

## Complexity Tracking

No constitution violations.

## Constitution Re-check (post-design)

- [x] SOLID/DRY and intentional patterns are still aligned with the design artifacts
- [x] TDD flow preserved (tests planned before implementation)
- [x] UX consistency and style reference accounted for
- [x] Performance budgets and lazy-load behavior remain defined
- [x] Official docs sources still recorded and applicable
- [x] Documentation updates included for non-intuitive behavior
- [x] Security constraints unchanged and respected
