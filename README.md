# PokeTest

PokeTest is a native Android app that uses the PokeAPI to help users explore
Pokemon, view details, and pick favorites. The project emphasizes clean
architecture, test-first development, and a consistent, delightful UI.

## Highlights

- Browse and discover Pokemon with fast, responsive screens.
- View rich details and manage favorites.
- Built with a scalable, layered architecture and TDD discipline.

## Architecture

- Three layers: UI, Domain, Data (Google-recommended), with unidirectional
  dependencies toward the Data layer.
- UI uses MVI with Jetpack ViewModel, single-activity, and Jetpack Compose.
- Dependency injection (Koin) supports modularity and testability.

## Tech Stack

- **Language**: Kotlin
- **Jetpack**: Compose, Navigation, ViewModel, Room, DataStore
- **Networking**: Retrofit
- **Dependency Injection**: Koin
- **Images**: Coil
- **Tests**: Kotlin tests, JUnit, Mockk, Espresso
- **Spec-Driven Development**: spec-kit workflows

## Quality Standards

This repo follows the project constitution in `.specify/memory/constitution.md`:

- **SOLID + DRY** with pragmatic design patterns.
- **Test-Driven Development** (red → green → refactor) as the default flow.
- **UX consistency** across navigation, components, and copy.
- **Performance budgets** with explicit targets and regression checks.
- **Documentation clarity + Kotlin conventions** for non-intuitive features.
- **Official docs** as the source of truth for platform and library usage.

## CI (GitHub Actions)

- **Workflow**: Android CI (`.github/workflows/android.yml`)
- **Triggers**: push and pull requests to `main`
- **Jobs**: runs `./gradlew build` and `./gradlew test` (unit tests) on Ubuntu

## Security

- HTTPS-only network traffic.
- OAuth for authentication.
- Local persistence encrypts user data.
- Static analysis is required; credentials are never stored in the repo.

## For Contributors

If you are adding features, start with the spec-kit workflow and align your
work with the constitution and the guidelines in `agents.md`.
