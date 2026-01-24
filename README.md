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
- Data layer uses the repository pattern with remote (PokeAPI) and local
  (Room + encrypted DataStore) sources.
- UI uses MVI with `BaseMVIViewModel`, single-activity, and Jetpack Compose.
- Dependency injection (Koin) provides module wiring and testability.

## Data Flow

PokeAPI → Retrofit/OkHttp/Moshi → Repository → Use Cases → MVI ViewModels → Compose UI

## Tech Stack

- **Language**: Kotlin
- **Jetpack**: Compose, Navigation, ViewModel, Room, DataStore
- **Networking**: Retrofit, OkHttp, Moshi
- **Dependency Injection**: Koin
- **Images**: Coil
- **Async**: Kotlin Coroutines + Flow
- **Tests**: Kotlin tests, JUnit, Mockk, Turbine, Compose UI tests
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
- **Jobs**: runs `./gradlew build`, `./gradlew test` (unit tests) and `./gradlew connectedCheck`
- (ui tests) on Ubuntu
- **Code Review**: Automatic Codex code reviews run on pull request creation

## Security

- HTTPS-only network traffic.
- OAuth for authentication.
- Local persistence encrypts user data.
- Static analysis is required; credentials are never stored in the repo.

## Data & Caching

- PokeAPI responses are cached locally to respect fair use and improve performance.
- Favorite Pokemon are stored locally with encryption and persist across relaunch.

## Design Reference

- Use `style_reference.png` for colors, typography, spacing, and layout cues.

## Getting Started

- **Requirements**: Android Studio + SDK, Android 8.0+ device/emulator (minSdk 26)
- **Run**: Open in Android Studio and run the `app` configuration

## Testing

- **Unit tests**: `./gradlew test`
- **Instrumented tests**: `./gradlew connectedAndroidTest`

## For Contributors

If you are adding features, start with the spec-kit workflow and align your
work with the constitution and the guidelines in `agents.md`.
