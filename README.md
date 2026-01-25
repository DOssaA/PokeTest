# PokeTest

PokeTest es una app nativa de Android que usa PokeAPI para ayudar a los usuarios
a explorar Pokémon, ver detalles y elegir favoritos. El proyecto enfatiza una
arquitectura limpia, desarrollo guiado por pruebas (TDD) y una UI consistente y
agradable.

## Destacado

- Explora y descubre Pokémon con pantallas rápidas y responsivas.
- Consulta detalles completos y administra favoritos.
- Busca por nombre, filtra por tipo/favoritos y sigue navegando con carga incremental.
- Construida con una arquitectura escalable por capas y disciplina TDD.

Ingreso por biometría y búsqueda:
https://github.com/user-attachments/assets/83558a27-18f7-44d7-a4a3-4b8bbdaadc64

## Instalación

- **Requisitos**: Android Studio (estable), Android SDK (API 26+), JDK 11 (incluido en Android Studio), Git.
- **Clonar**: clona el repositorio y abre el proyecto en Android Studio.
- **Sincronizar**: permite que Gradle sincronice; si se solicita, usa JDK 11 para Gradle.
- **SDK**: asegúrate de que `local.properties` tenga `sdk.dir` (Android Studio lo genera).
- **Dispositivo**: usa un emulador o dispositivo Android 8.0+ con acceso a Internet.

## Operación

- Inicia la app con la configuración `app` desde Android Studio.
- En el login local, usa cualquier usuario/contraseña no vacíos.
- En Google sign-in, selecciona el resultado simulado para probar estados.
- Explora la Pokédex, abre detalles, y marca favoritos desde la pantalla de detalle.
- La primera carga requiere red; luego se reutiliza el caché local cuando está disponible.

## Arquitectura

- Tres capas: UI, Domain, Data (recomendado por Google), con dependencias
  unidireccionales hacia la capa Data.
- La capa Data usa el patrón repositorio con fuentes remota (PokeAPI) y local
  (Room + DataStore cifrado).
- La UI usa MVI con `BaseMVIViewModel`, single-activity y Jetpack Compose.
- La inyección de dependencias (Koin) permite el cableado de módulos y la testabilidad.

## Flujo de datos

PokeAPI → Retrofit/OkHttp/Moshi → Repository → Use Cases → MVI ViewModels → Compose UI

## Tech Stack

- **Lenguaje**: Kotlin
- **Jetpack**: Compose, Navigation, ViewModel, Room, DataStore
- **Networking**: Retrofit, OkHttp, Moshi
- **Inyección Dependencias**: Koin
- **Imágenes**: Coil
- **Asincronía**: Kotlin Coroutines + Flow
- **Tests**: Kotlin tests, JUnit, Mockk, Turbine, Compose UI tests
- **aceleración IA con Spec-Driven Development**: flujos spec-kit (de Github)

## Estándares de Calidad

Este repo sigue la constitución del proyecto en `.specify/memory/constitution.md`:

- **SOLID + DRY** con patrones pragmáticos.
- **Test-Driven Development** (red → green → refactor) como flujo por defecto.
- **Consistencia UX** en navegación, componentes y copy.
- **Presupuestos de performance** con objetivos explícitos y checks de regresión.
- **Claridad documental + convenciones Kotlin** para features no intuitivas.
- **Docs oficiales** como fuente de verdad para plataforma y librerías.

## CI (GitHub Actions)

- **Workflow**: Android CI (`.github/workflows/android.yml`)
- **Triggers**: push y pull requests a `main`
- **Jobs**: ejecuta `./gradlew build`, `./gradlew test` (unit tests) y
  `./gradlew connectedCheck` (UI tests) en Ubuntu
- **Code Review**: reviews automáticos de Codex al crear un PR

## Security

- Tráfico de red solo por HTTPS.
- OAuth para autenticación.
- Persistencia local cifra los datos del usuario.
- Se requiere análisis estático; las credenciales nunca se almacenan en el repo.

## Authentication (Simulated)

- El flujo de login es local y está pensado para pruebas de UI/UX.
- Usuario/contraseña acepta cualquier valor no vacío.
- El login biométrico usa el prompt del sistema y devuelve éxito/cancelación/error local.
- El inicio con Google muestra una UI estilo OAuth respaldada por un proveedor local
  que puede simular éxito, cancelación, error o una respuesta demorada.
- Los logins exitosos guardan credenciales de forma segura usando DataStore cifrado.

## Data & Caching

- Las respuestas de PokeAPI se cachean localmente para respetar el uso justo y mejorar el rendimiento.
- Los favoritos se almacenan localmente con cifrado y persisten tras reiniciar la app.
- La lista de la Pokédex soporta búsqueda, filtros por tipo/favoritos y carga incremental.

## Design Reference

- Usa como referencia https://www.figma.com/design/pFG8ymYDeuRKDVFkzgPF7v/Pok%C3%A9dex--Community-?node-id=0-1&p=f

## Testing

- **Unit tests**: `./gradlew test`
- **Instrumented tests**: `./gradlew connectedAndroidTest`

## For Contributors

Si agregas features, comienza con el flujo spec-kit y alinea tu trabajo con la
constitución y las guías en `agents.md`.

----------------------

# PokeTest

PokeTest is a native Android app that uses the PokeAPI to help users explore
Pokemon, view details, and pick favorites. The project emphasizes clean
architecture, test-first development, and a consistent, delightful UI.

## Highlights

- Browse and discover Pokemon with fast, responsive screens.
- View rich details and manage favorites.
- Search by name, filter by type/favorites, and keep browsing with lazy loading.
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
- **Jobs**: runs `./gradlew build`, `./gradlew test` (unit tests) and
  `./gradlew connectedCheck` (ui tests) on Ubuntu
- **Code Review**: Automatic Codex code reviews run on pull request creation

## Security

- HTTPS-only network traffic.
- OAuth for authentication.
- Local persistence encrypts user data.
- Static analysis is required; credentials are never stored in the repo.

## Authentication (Simulated)

- The login flow is local-only and intended for UI/UX testing.
- Username/password accepts any non-empty values.
- Biometric login uses the system prompt and returns a local success/cancel/error outcome.
- Google sign-in shows an OAuth-style UI backed by a local provider that can simulate
  success, cancel, error, or a delayed response to exercise loading states.
- Successful logins store credentials securely using encrypted DataStore.

## Data & Caching

- PokeAPI responses are cached locally to respect fair use and improve performance.
- Favorite Pokemon are stored locally with encryption and persist across relaunch.
- The Pokedex list supports search, type/favorite filters, and incremental loading.

## Design Reference

- Use as reference https://www.figma.com/design/pFG8ymYDeuRKDVFkzgPF7v/Pok%C3%A9dex--Community-?node-id=0-1&p=f

## Installation

- **Requirements**: Android Studio (stable), Android SDK (API 26+), JDK 11 (bundled with Android Studio), Git.
- **Clone**: clone the repo and open the project in Android Studio.
- **Sync**: allow Gradle to sync; if prompted, use JDK 11 for Gradle.
- **SDK**: ensure `local.properties` contains `sdk.dir` (Android Studio generates it).
- **Device**: use an Android 8.0+ emulator or device with Internet access.

## Operation

- Launch the app using the `app` run configuration in Android Studio.
- For local login, use any non-empty username/password.
- For Google sign-in, select a simulated outcome to exercise states.
- Browse the Pokedex, open details, and toggle favorites from the detail screen.
- The first load requires network access; afterward local cache is used when available.

## Testing

- **Unit tests**: `./gradlew test`
- **Instrumented tests**: `./gradlew connectedAndroidTest`

## For Contributors

If you are adding features, start with the spec-kit workflow and align your
work with the constitution and the guidelines in `agents.md`.
