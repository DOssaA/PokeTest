---

description: "Task list for Pokedex Search & Filters"
---

# Tasks: Pokedex Search & Filters

**Input**: Design documents from `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/
**Tests**: TDD required (spec + user request). Tests MUST be written and failing before implementation, and code changes must make them pass.
**Organization**: Tasks grouped by user story to enable independent implementation and testing.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Minimal shared setup needed by all stories

- [x] T001 Update Pokedex style tokens to align with `/Users/darioossa/Repos/PokeTest/style_reference.png` in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexColors.kt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that must exist before any user story work

### Tests for Foundational Work (REQUIRED) ⚠️

- [x] T002 [P] Add remote data source tests for type list endpoint in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/data/remote/PokeRemoteDataSourceTest.kt`
- [x] T003 [P] Add repository tests for type list + hydrated summaries in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/data/repository/PokemonRepositoryImplTest.kt`
- [x] T004 [P] Add use case tests for type list in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/domain/usecase/GetPokemonTypesUseCaseTest.kt`

### Implementation for Foundational Work

- [x] T005 Add PokeAPI type list endpoint support in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeApiService.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeRemoteDataSource.kt`
- [x] T006 Add repository contract and implementation for Pokemon type list and type hydration in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepository.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`
- [x] T007 Wire new type use case and DI bindings in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/GetPokemonTypesUseCase.kt` and `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/di/Modules.kt`
- [x] T008 Make foundational tests pass by refining `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeApiService.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/PokeRemoteDataSource.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepository.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/domain/usecase/GetPokemonTypesUseCase.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/di/Modules.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Search Pokemon by name (Priority: P1) 🎯 MVP

**Goal**: Provide an inline search field below the subtitle that filters the list by name.

**Independent Test**: Enter a query and verify list results update and restore when cleared.

### Tests for User Story 1 (REQUIRED) ⚠️

- [x] T009 [P] [US1] Add reducer tests for search query filtering in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt`
- [x] T010 [P] [US1] Add ViewModel tests for search query + empty state in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt`
- [x] T011 [P] [US1] Add Compose UI test for search field filtering in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt`

### Implementation for User Story 1

- [x] T012 [US1] Extend state/events for search query + filtered items in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`
- [x] T013 [US1] Update ViewModel to apply search filtering on Main dispatcher in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`
- [x] T014 [US1] Add search bar UI below subtitle and bind query in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`
- [x] T015 [US1] Add search strings and empty-state copy in `/Users/darioossa/Repos/PokeTest/app/src/main/res/values/strings.xml`
- [x] T016 [US1] Make US1 tests pass by refining `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/res/values/strings.xml`

**Checkpoint**: User Story 1 works independently and all US1 tests pass

---

## Phase 4: User Story 2 - Filter by type and favorites (Priority: P2)

**Goal**: Provide a filter button that opens a modal with favorites toggle and selectable types.

**Independent Test**: Apply favorites/types filters and verify the list shows only matching Pokemon.

### Tests for User Story 2 (REQUIRED) ⚠️

- [x] T017 [P] [US2] Add reducer tests for favorites/type filters in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt`
- [x] T018 [P] [US2] Add ViewModel tests for favorites/types filters combined with search in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt`
- [x] T019 [P] [US2] Add Compose UI test for filter modal interactions in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt`

### Implementation for User Story 2

- [x] T020 [US2] Add API DTO reuse for type list response in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/remote/dto/PokemonListDto.kt`
- [x] T021 [US2] Fetch and cache Pokemon types for summaries during list load in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`
- [x] T022 [US2] Add filter state/events for favorites + types in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`
- [x] T023 [US2] Load available types and apply filters on Main dispatcher in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`
- [x] T024 [US2] Build filter button + modal UI with selectable types in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`
- [x] T025 [US2] Add filter strings/content descriptions in `/Users/darioossa/Repos/PokeTest/app/src/main/res/values/strings.xml`
- [x] T026 [US2] Make US2 tests pass by refining `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/data/repository/PokemonRepositoryImpl.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/res/values/strings.xml`

**Checkpoint**: User Story 2 works independently and all US2 tests pass

---

## Phase 5: User Story 3 - Continue browsing with automatic loading (Priority: P3)

**Goal**: Automatically load the next page before the end of the list.

**Independent Test**: Scroll near end and verify new items append; simulate failure and verify retry.

### Tests for User Story 3 (REQUIRED) ⚠️

- [x] T027 [P] [US3] Add reducer tests for load-more state in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt`
- [x] T028 [P] [US3] Add ViewModel paging tests for load-more + error handling in `/Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt`
- [x] T029 [P] [US3] Add Compose UI test for load-more trigger and retry UI in `/Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt`

### Implementation for User Story 3

- [x] T030 [US3] Add paging state/events and load-more error in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`
- [x] T031 [US3] Implement load-more flow and append behavior in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`
- [x] T032 [US3] Add LazyGrid scroll trigger + load-more UI states in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`
- [x] T033 [US3] Make US3 tests pass by refining `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListReducer.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModel.kt`, `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`

**Checkpoint**: User Story 3 works independently and all US3 tests pass

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Documentation and accessibility updates

- [x] T034 [P] Update Pokedex feature documentation in `/Users/darioossa/Repos/PokeTest/README.md`
- [x] T035 [P] Verify accessibility and localization readiness (content descriptions, strings) in `/Users/darioossa/Repos/PokeTest/app/src/main/java/com/darioossa/poketest/ui/pokedex/PokedexListScreen.kt`
- [ ] T036 Run quickstart validation steps and record outcomes in `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies
- **Foundational (Phase 2)**: Depends on Setup completion
- **User Stories (Phase 3-5)**: Depend on Foundational completion
- **Polish (Phase 6)**: After all desired user stories complete

### User Story Dependencies

- **US1 (P1)**: Can start after Foundational - independent
- **US2 (P2)**: Can start after Foundational - independent, but benefits from US1 search state
- **US3 (P3)**: Can start after Foundational - independent

### Parallel Opportunities

- Test tasks within a story can run in parallel
- UI and data layer tasks in different files can run in parallel within the same story
- Different user stories can proceed in parallel after Foundational is done

---

## Parallel Example: User Story 1

```bash
Task: "Add reducer tests for search query filtering in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt"
Task: "Add ViewModel tests for search query + empty state in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt"
Task: "Add Compose UI test for search field filtering in /Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt"
```

---

## Parallel Example: User Story 2

```bash
Task: "Add reducer tests for favorites/type filters in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt"
Task: "Add ViewModel tests for favorites/types filters combined with search in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt"
Task: "Add Compose UI test for filter modal interactions in /Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt"
```

---

## Parallel Example: User Story 3

```bash
Task: "Add reducer tests for load-more state in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListReducerTest.kt"
Task: "Add ViewModel paging tests for load-more + error handling in /Users/darioossa/Repos/PokeTest/app/src/test/java/com/darioossa/poketest/ui/pokedex/PokedexListViewModelTest.kt"
Task: "Add Compose UI test for load-more trigger and retry UI in /Users/darioossa/Repos/PokeTest/app/src/androidTest/java/com/darioossa/poketest/ui/pokedex/PokedexListScreenTest.kt"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate US1 tests pass and behavior matches spec

### Incremental Delivery

1. Setup + Foundational
2. US1 (search)
3. US2 (filters)
4. US3 (lazy loading)
5. Polish & documentation
