# Research: Pokedex List and Details

## Decision 1: Layered Architecture and Repository Pattern

**Decision**: Use UI -> Domain -> Data layers with repository pattern and
use-case classes in the domain layer.

**Rationale**: The Android architecture guide recommends UI and data layers and
allows a domain layer for reusable or complex logic. Repositories centralize
and abstract data sources, and use cases encapsulate single responsibilities.

**Alternatives considered**:
- UI layer calling data sources directly (rejected: violates separation and
  repository guidance)
- No domain layer (rejected: use cases are required for this feature set)

## Decision 2: UI State Management with ViewModel and Compose

**Decision**: Use Jetpack Compose UI with ViewModel state holders, implemented
via the existing `BaseMVIViewModel` for MVI state management.

**Rationale**: Android guidance describes UI state holders like ViewModel and
modern UI built with Compose. This aligns with the project architecture and
existing base classes.

**Alternatives considered**:
- Activity-only state management (rejected: not aligned with architecture guide)
- LiveData-only state holders (rejected: existing MVI/Flow setup)

## Decision 3: Networking with Retrofit and PokeAPI Fair Use

**Decision**: Use Retrofit for HTTP access to PokeAPI `GET` endpoints. No auth
required; apply local caching per fair use policy.

**Rationale**: PokeAPI documentation states the API is GET-only, requires no
authentication, and requires local caching to comply with fair use. Retrofit is
a type-safe HTTP client for Android/JVM.

**Alternatives considered**:
- OkHttp-only client (rejected: Retrofit provides typed APIs and converters)
- GraphQL API (rejected: feature requirements are covered by REST endpoints)

## Decision 4: Local Persistence for Cache and Favorites (Encrypted)

**Decision**: Cache Pokemon list/detail in Room. Store user favorites in
DataStore with app-level encryption using Android Keystore-managed keys.

**Rationale**: Room is recommended for local structured data and offline cache.
DataStore is recommended for small datasets and typed objects. Android Keystore
provides app-scoped key storage; encrypted shared preferences APIs are
deprecated, so a Keystore-backed encryption approach avoids deprecated APIs.

**Alternatives considered**:
- SharedPreferences (rejected: DataStore recommended for key-value storage)
- EncryptedSharedPreferences (rejected: deprecated in androidx.security:security-crypto)
- Room-only for favorites (rejected: user data encryption requirement is clearer
  with explicit encryption layer)

## Decision 5: Dependency Injection

**Decision**: Use Koin modules and `startKoin` to wire dependencies for data,
domain, and UI layers.

**Rationale**: Koin documentation describes module definitions and `startKoin`
as the container entry point, matching the project tech stack.

**Alternatives considered**:
- Manual service locator (rejected: inconsistent with DI standard)
- Dagger/Hilt (rejected: project uses Koin)

## Sources and Access Dates

- Android Guide to app architecture (UI/Data/Domain layers, repositories):
  https://developer.android.com/topic/architecture (accessed 2026-01-23)
- Android Domain layer guidance (use cases):
  https://developer.android.com/topic/architecture/domain-layer
  (accessed 2026-01-23)
- Jetpack Compose documentation overview:
  https://developer.android.com/develop/ui/compose/documentation
  (accessed 2026-01-23)
- Room persistence library guide:
  https://developer.android.com/training/data-storage/room (accessed 2026-01-23)
- DataStore guide:
  https://developer.android.com/topic/libraries/architecture/datastore
  (accessed 2026-01-23)
- DataStore 1.2.0 release notes (version reference):
  https://developer.android.com/jetpack/androidx/releases/datastore
  (accessed 2026-01-23)
- Android Keystore system:
  https://developer.android.com/privacy-and-security/keystore
  (accessed 2026-01-23)
- EncryptedSharedPreferences deprecation note:
  https://developer.android.com/reference/kotlin/androidx/security/crypto/EncryptedSharedPreferences
  (accessed 2026-01-23)
- Koin startKoin reference:
  https://insert-koin.io/docs/reference/koin-core/start-koin/ (accessed 2026-01-23)
- Retrofit (type-safe HTTP client):
  https://github.com/square/retrofit (accessed 2026-01-23)
- PokeAPI v2 documentation (GET-only, no auth, fair use caching):
  https://staging.pokeapi.co/docs/v2 (accessed 2026-01-23)
