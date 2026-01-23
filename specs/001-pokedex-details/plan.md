# Implementation Plan: Pokedex List and Details

**Branch**: `001-pokedex-details` | **Date**: 2026-01-23 | **Spec**: /Users/darioossa/Repos/PokeTest/specs/001-pokedex-details/spec.md
**Input**: Feature specification from `/specs/001-pokedex-details/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. Align the plan with the project constitution.

## Summary

Build a Pokedex-style two screens: first a screen that lists Pokemon with image and name, lets
users favorite from the list, and opens a rich detail screen, the second screen to build.
The design follows the reference: `style_reference.png`. Data comes from PokeAPI 
over HTTPS with local caching, favorites persist locally with encryption, 
and UI follows MVI using `BaseMVIViewModel`.

## Technical Context

**Language/Version**: Kotlin 2.0.21  
**Primary Dependencies**: Jetpack Compose (BOM 2024.09.00), ViewModel,
Navigation, Room, DataStore, Retrofit, Koin, Coil  
**Storage**: Room cache for Pokemon data; encrypted DataStore for user favorites
(see research)  
**Testing**: JUnit4, MockK, Turbine, kotlinx-coroutines-test, Compose UI tests  
**Target Platform**: Android (minSdk 26, targetSdk 36, compileSdk 36)  
**Project Type**: mobile (single Android app)  
**Performance Goals**: List first render <= 3s; detail render <= 2s after tap; 60
fps scroll for list  
**Constraints**: HTTPS-only; local user data encrypted; PokeAPI fair use caching;
accessibility-ready; Kotlin conventions  
**Scale/Scope**: 2 screens (list + detail), favorites persistence, cached list
and details

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
specs/001-pokedex-details/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
app/src/main/java/com/darioossa/poketest/
├── data/
│   ├── local/           # Room db + DAOs + cache
│   ├── remote/          # Retrofit services + DTOs
│   ├── repository/      # PokemonRepository impls
│   └── mapper/          # DTO/entity/domain mappers
├── domain/
│   ├── model/           # Domain models
│   └── usecase/         # Use cases for list/detail/favorites
├── ui/
│   ├── pokedex/         # list + detail screens + state/reducer
│   ├── base/            # BaseMVIViewModel
│   └── theme/
└── di/                  # Koin modules

app/src/test/java/com/darioossa/poketest/
app/src/androidTest/java/com/darioossa/poketest/
```

**Structure Decision**: Use existing `data`, `domain`, `ui`, and `di` packages
with repository pattern in data, use cases in domain, and MVI state in UI.

## Complexity Tracking

No constitution gate violations.

## Phase 0: Outline & Research

Research completed in `research.md` and all unknowns resolved.

## Phase 1: Design & Contracts

Design artifacts completed in `data-model.md`, `contracts/`, and `quickstart.md`.
Agent context updated.

## Post-Design Constitution Recheck

- [x] SOLID/DRY and intentional patterns remain valid
- [x] TDD still required and test scope defined
- [x] UX consistency and accessibility planned
- [x] Performance budgets preserved
- [x] Official docs recorded with versions/dates
- [x] Documentation for non-intuitive behavior planned
- [x] Security constraints covered (HTTPS, OAuth policy noted, encryption planned)
