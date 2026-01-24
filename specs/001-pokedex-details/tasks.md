---

description: "Task list for Pokedex List and Details"
---

# Tasks: Pokedex List and Details

**Input**: Design documents from `/Users/darioossa/Repos/PokeTest/specs/001-pokedex-details/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/

**Tests**: REQUIRED by constitution (TDD). Write tests first and ensure they fail before implementation.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Mobile**: `app/src/main/java/`, `app/src/test/java/`, `app/src/androidTest/java/`
- Paths below use absolute paths as required

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and dependency setup

- [x] T001 Add Retrofit/OkHttp/Room/DataStore/Koin/Coil/Navigation versions in `/Users/darioossa/Repos/PokeTest/gradle/libs.versions.toml`
- [x] T002 Wire dependencies and plugins (Room compiler/KSP if needed) in `/Users/darioossa/Repos/PokeTest/app/build.gradle.kts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T003 Define domain models in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/model/PokemonModels.kt`
- [x] T004 Define PokeAPI DTOs in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/dto/PokemonListDto.kt`
- [x] T005 Define PokeAPI detail/species DTOs in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/dto/PokemonDetailDto.kt`
- [x] T006 Create Room entities and DAO in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/PokemonEntity.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/PokemonDao.kt`
- [x] T007 Create Room database in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/PokemonDatabase.kt`
- [x] T008 Implement encrypted favorites storage in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/FavoritesDataStore.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/CryptoManager.kt`
- [x] T009 Define Retrofit service in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeApiService.kt`
- [x] T010 Add remote data source wrapper in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeRemoteDataSource.kt`
- [x] T011 Add local data source wrapper in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/local/PokeLocalDataSource.kt`
- [x] T012 Implement mappers in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/mapper/PokemonMappers.kt`
- [x] T013 Define repository interface in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepository.kt`
- [x] T014 Implement repository in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`
- [x] T015 Configure Koin modules in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/di/Modules.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Browse Pokedex List (Priority: P1) 🎯 MVP

**Goal**: Show a Pokedex list as the first screen and open a basic detail view.

**Independent Test**: Launch app, see list first, open detail showing name + image, return with scroll position preserved.

### Tests for User Story 1 (REQUIRED) ⚠️

- [x] T016 [P] [US1] Create list test fixtures in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/testdata/PokedexTestData.kt`
- [x] T017 [P] [US1] Test list use case in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/domain/usecase/GetPokemonListUseCaseTest.kt`
- [x] T018 [P] [US1] Test list view model in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt`
- [x] T019 [P] [US1] Compose UI test for list screen in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt`

### Implementation for User Story 1

- [x] T020 [US1] Implement list use case in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/GetPokemonListUseCase.kt`
- [x] T021 [US1] Implement list reducer/state/event/effect in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`
- [x] T022 [US1] Implement list ViewModel (BaseMVIViewModel) in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`
- [x] T023 [US1] Implement list UI and item composable in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`
- [x] T024 [US1] Implement base detail screen (name + image) in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokemonDetailScreen.kt`
- [x] T025 [US1] Add navigation graph and routes in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexNavGraph.kt` and update `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/MainActivity.kt`
- [x] T026 [US1] Apply style_reference theme tokens in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/theme/Color.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/theme/Theme.kt`

**Checkpoint**: User Story 1 is fully functional and testable independently

---

## Phase 4: User Story 2 - Favorite a Pokemon (Priority: P2)

**Goal**: Allow users to favorite/unfavorite Pokemon from the list with persistence.

**Independent Test**: Toggle favorite on list item, relaunch app, verify persisted state.

### Tests for User Story 2 (REQUIRED) ⚠️

- [x] T027 [P] [US2] Test toggle favorite use case in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/domain/usecase/ToggleFavoriteUseCaseTest.kt`
- [x] T028 [P] [US2] Test list ViewModel favorite behavior in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt`
- [x] T029 [P] [US2] UI test for favorites persistence in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/FavoritesPersistenceTest.kt`

### Implementation for User Story 2

- [x] T030 [US2] Implement favorite use cases in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/ToggleFavoriteUseCase.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/ObserveFavoritesUseCase.kt`
- [x] T031 [US2] Wire favorites persistence into repository in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`
- [x] T032 [US2] Handle favorite events in list reducer/view model in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`
- [x] T033 [US2] Update list UI with favorite toggle in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`

**Checkpoint**: User Story 2 is functional and persists favorites across relaunch

---

## Phase 5: User Story 3 - View Rich Pokemon Details (Priority: P3)

**Goal**: Show rich Pokemon detail data (stats, description, abilities, attributes).

**Independent Test**: Open detail screen and verify all required sections and missing-field messaging.

### Tests for User Story 3 (REQUIRED) ⚠️

- [x] T034 [P] [US3] Test detail use case in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/domain/usecase/GetPokemonDetailUseCaseTest.kt`
- [x] T035 [P] [US3] Test detail ViewModel in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokemonDetailViewModelTest.kt`
- [x] T036 [P] [US3] UI test for detail sections in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokemonDetailScreenTest.kt`

### Implementation for User Story 3

- [x] T037 [US3] Implement detail use case in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/GetPokemonDetailUseCase.kt`
- [x] T038 [US3] Add detail/species mapping in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/mapper/PokemonMappers.kt`
- [x] T039 [US3] Implement detail ViewModel (BaseMVIViewModel) in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokemonDetailViewModel.kt`
- [x] T040 [US3] Expand detail UI sections in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokemonDetailScreen.kt`
- [x] T041 [US3] Add loading/error UI states for detail in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokemonDetailScreen.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokemonDetailReducer.kt`
- [x] T042 [US3] Update detail navigation args in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexNavGraph.kt`

**Checkpoint**: User Story 3 is complete and all detail fields render correctly

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T043 [P] Document caching and encrypted favorites behavior in `/Users/darioossa/Repos/PokeTest/README.md`
- [x] T044 [P] Add KDoc on repository behaviors in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepository.kt`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependencies on other stories
- **User Story 2 (P2)**: Depends on User Story 1 list UI and data flow
- **User Story 3 (P3)**: Depends on User Story 1 navigation and base detail screen

### Within Each User Story

- Tests MUST be written and FAIL before implementation
- Models before use cases
- Use cases before ViewModels
- ViewModels before UI integration

### Parallel Opportunities

- All tests marked [P] can be developed in parallel
- Data mappers and DTOs can be implemented in parallel with local data layer
- UI components can be built in parallel once ViewModel contracts are defined

---

## Parallel Example: User Story 1

```text
- [ ] T017 [P] [US1] Test list use case in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/domain/usecase/GetPokemonListUseCaseTest.kt
- [ ] T018 [P] [US1] Test list view model in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt
- [ ] T019 [P] [US1] Compose UI test for list screen in /Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate against quickstart in `/Users/darioossa/Repos/PokeTest/specs/001-pokedex-details/quickstart.md`

### Incremental Delivery

1. Build and validate User Story 1
2. Add favorites (User Story 2)
3. Add rich details (User Story 3)
4. Apply polish tasks
