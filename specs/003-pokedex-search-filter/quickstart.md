# Quickstart: Pokedex Search & Filters

**Date**: January 25, 2026

## Scope

This feature adds Pokedex search, filters (favorites + types), and lazy loading. UI styling should follow `/Users/darioossa/Repos/PokeTest/style_reference.png`.

## TDD Workflow

1. Write failing unit tests for new reducer/viewmodel logic and filter behavior.
2. Write failing UI tests for search, filter modal, and lazy-load behavior.
3. Implement the feature until tests pass, then refactor.

## Run Tests

Unit tests:

```
./gradlew testDebugUnitTest
```

Android UI tests (device/emulator required):

```
./gradlew connectedDebugAndroidTest
```

## Useful Paths

- Feature spec: `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/spec.md`
- Plan: `/Users/darioossa/Repos/PokeTest/specs/003-pokedex-search-filter/plan.md`
- Style reference: `/Users/darioossa/Repos/PokeTest/style_reference.png`
