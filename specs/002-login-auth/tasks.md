---

description: "Task list for login entry authentication implementation"
---

# Tasks: Login Entry Authentication

**Input**: Design documents from `/specs/002-login-auth/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are REQUIRED for every user story (TDD).

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and dependency setup

- [ ] T001 [P] Verify/add AndroidX Biometric dependency in `app/build.gradle.kts`
- [ ] T002 [P] Add biometric permission in `app/src/main/AndroidManifest.xml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure required before any user story work

- [ ] T003 [P] Define auth domain models in `app/src/main/java/com/darioossa/poketest/domain/model/AuthModels.kt`
- [ ] T004 [P] Add auth repository interface in `app/src/main/java/com/darioossa/poketest/domain/repository/AuthRepository.kt`
- [ ] T005 [P] Add auth local data source interface in `app/src/main/java/com/darioossa/poketest/data/auth/local/AuthLocalDataSource.kt`
- [ ] T006 [P] Add auth remote data source interface in `app/src/main/java/com/darioossa/poketest/data/auth/remote/AuthRemoteDataSource.kt`
- [ ] T007 [P] Implement auth encryption helper in `app/src/main/java/com/darioossa/poketest/data/auth/crypto/AuthCryptoManager.kt`
- [ ] T008 [P] Implement encrypted credential storage in `app/src/main/java/com/darioossa/poketest/data/auth/local/AuthDataStore.kt`
- [ ] T009 Implement auth repository in `app/src/main/java/com/darioossa/poketest/data/auth/AuthRepositoryImpl.kt`
- [ ] T010 [P] Wire auth dependencies in `app/src/main/java/com/darioossa/poketest/di/Modules.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Login with Username and Password (Priority: P1) 🎯 MVP

**Goal**: Let users log in with non-empty username/password, store credentials securely, and navigate to home with animation.

**Independent Test**: Launch app, submit valid credentials, see animated transition to home; invalid input stays with error.

### Tests for User Story 1 (REQUIRED) ⚠️

- [ ] T011 [P] [US1] Reducer unit tests for login validation in `app/src/test/java/com/darioossa/poketest/ui/auth/LoginReducerTest.kt`
- [ ] T012 [P] [US1] Use case tests for password login in `app/src/test/java/com/darioossa/poketest/domain/usecase/LoginWithPasswordUseCaseTest.kt`
- [ ] T013 [P] [US1] Repository tests for password auth + secure storage in `app/src/test/java/com/darioossa/poketest/data/auth/AuthRepositoryPasswordTest.kt`
- [ ] T014 [P] [US1] UI tests for login validation and navigation in `app/src/androidTest/java/com/darioossa/poketest/ui/auth/LoginScreenPasswordTest.kt`

### Implementation for User Story 1

- [ ] T015 [P] [US1] Implement login state/events/effects/reducer in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginReducer.kt`
- [ ] T016 [US1] Implement LoginViewModel for password flow in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginViewModel.kt`
- [ ] T017 [P] [US1] Implement password login use case in `app/src/main/java/com/darioossa/poketest/domain/usecase/LoginWithPasswordUseCase.kt`
- [ ] T018 [US1] Implement password auth path in `app/src/main/java/com/darioossa/poketest/data/auth/AuthRepositoryImpl.kt`
- [ ] T019 [US1] Build login UI (username/password, submit, errors) in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginScreen.kt`
- [ ] T020 [US1] Apply style reference tokens in `app/src/main/java/com/darioossa/poketest/ui/theme/Color.kt`
- [ ] T021 [US1] Add login route and start destination logic in `app/src/main/java/com/darioossa/poketest/ui/PokedexNavGraph.kt`
- [ ] T022 [US1] Implement login-to-home animation in `app/src/main/java/com/darioossa/poketest/ui/PokedexNavGraph.kt`

**Checkpoint**: User Story 1 fully functional and testable independently

---

## Phase 4: User Story 2 - Login with Biometric (Priority: P2)

**Goal**: Enable biometric login with native BiometricPrompt and clear outcomes.

**Independent Test**: Trigger biometric login; confirm success navigates to home, cancel/error stays on login with message.

### Tests for User Story 2 (REQUIRED) ⚠️

- [ ] T023 [P] [US2] Reducer tests for biometric states in `app/src/test/java/com/darioossa/poketest/ui/auth/LoginBiometricReducerTest.kt`
- [ ] T024 [P] [US2] Use case tests for biometric login in `app/src/test/java/com/darioossa/poketest/domain/usecase/LoginWithBiometricUseCaseTest.kt`
- [ ] T025 [P] [US2] Repository tests for biometric outcomes in `app/src/test/java/com/darioossa/poketest/data/auth/AuthRepositoryBiometricTest.kt`
- [ ] T026 [P] [US2] UI tests for biometric flow in `app/src/androidTest/java/com/darioossa/poketest/ui/auth/LoginScreenBiometricTest.kt`

### Implementation for User Story 2

- [ ] T027 [P] [US2] Implement BiometricPrompt wrapper in `app/src/main/java/com/darioossa/poketest/util/biometric/BiometricPromptManager.kt`
- [ ] T028 [P] [US2] Implement biometric login use case in `app/src/main/java/com/darioossa/poketest/domain/usecase/LoginWithBiometricUseCase.kt`
- [ ] T029 [US2] Implement biometric handling in `app/src/main/java/com/darioossa/poketest/data/auth/remote/AuthRemoteDataSourceImpl.kt`
- [ ] T030 [US2] Extend LoginViewModel for biometric flow in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginViewModel.kt`
- [ ] T031 [US2] Add biometric button and messaging to UI in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginScreen.kt`

**Checkpoint**: User Stories 1 and 2 are independently functional

---

## Phase 5: User Story 3 - Login with Simulated Google Account (Priority: P3)

**Goal**: Provide OAuth-style Google login UI with local provider outcomes (Success, Cancel, Error, Loading).

**Independent Test**: Complete each simulated outcome and verify UI state + navigation behavior.

### Tests for User Story 3 (REQUIRED) ⚠️

- [ ] T032 [P] [US3] Reducer tests for Google OAuth flow states in `app/src/test/java/com/darioossa/poketest/ui/auth/LoginGoogleReducerTest.kt`
- [ ] T033 [P] [US3] Use case tests for Google login in `app/src/test/java/com/darioossa/poketest/domain/usecase/LoginWithGoogleUseCaseTest.kt`
- [ ] T034 [P] [US3] Provider tests for OAuth outcomes in `app/src/test/java/com/darioossa/poketest/data/auth/GoogleAuthProviderTest.kt`
- [ ] T035 [P] [US3] UI tests for Google OAuth flow in `app/src/androidTest/java/com/darioossa/poketest/ui/auth/LoginScreenGoogleTest.kt`

### Implementation for User Story 3

- [ ] T036 [P] [US3] Implement simulated Google provider in `app/src/main/java/com/darioossa/poketest/data/auth/remote/GoogleAuthProvider.kt`
- [ ] T037 [P] [US3] Implement Google login use case in `app/src/main/java/com/darioossa/poketest/domain/usecase/LoginWithGoogleUseCase.kt`
- [ ] T038 [US3] Wire Google flow in `app/src/main/java/com/darioossa/poketest/data/auth/remote/AuthRemoteDataSourceImpl.kt`
- [ ] T039 [US3] Extend LoginViewModel for Google OAuth flow in `app/src/main/java/com/darioossa/poketest/ui/auth/LoginViewModel.kt`
- [ ] T040 [US3] Build OAuth-style UI screen in `app/src/main/java/com/darioossa/poketest/ui/auth/GoogleOAuthScreen.kt`
- [ ] T041 [US3] Integrate OAuth screen in navigation in `app/src/main/java/com/darioossa/poketest/ui/PokedexNavGraph.kt`

**Checkpoint**: All user stories are independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements affecting multiple stories and documentation

- [ ] T042 [P] Add accessibility labels and localizable strings in `app/src/main/res/values/strings.xml`
- [ ] T043 [P] Document auth simulation and storage behavior in `README.md`
- [ ] T044 [P] Add KDoc for auth storage/encryption behaviors in `app/src/main/java/com/darioossa/poketest/data/auth/local/AuthDataStore.kt`
- [ ] T045 [P] Validate quickstart steps and update notes in `specs/002-login-auth/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies
- **Foundational (Phase 2)**: Depends on Setup completion
- **User Stories (Phase 3+)**: Depend on Foundational completion
- **Polish (Final Phase)**: Depends on all desired user stories

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational - integrates with shared auth flow
- **User Story 3 (P3)**: Can start after Foundational - integrates with shared auth flow

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- Foundational tasks marked [P] can run in parallel
- Tests within each story marked [P] can run in parallel
- UI and data-layer tasks across different stories can run in parallel after Foundational

---

## Parallel Example: User Story 1

```bash
# Run User Story 1 tests in parallel
Task: "Reducer unit tests for login validation in app/src/test/java/com/darioossa/poketest/ui/auth/LoginReducerTest.kt"
Task: "Use case tests for password login in app/src/test/java/com/darioossa/poketest/domain/usecase/LoginWithPasswordUseCaseTest.kt"
Task: "Repository tests for password auth + secure storage in app/src/test/java/com/darioossa/poketest/data/auth/AuthRepositoryPasswordTest.kt"
Task: "UI tests for login validation and navigation in app/src/androidTest/java/com/darioossa/poketest/ui/auth/LoginScreenPasswordTest.kt"

# Build UI and domain pieces in parallel (different files)
Task: "Implement login state/events/effects/reducer in app/src/main/java/com/darioossa/poketest/ui/auth/LoginReducer.kt"
Task: "Implement password login use case in app/src/main/java/com/darioossa/poketest/domain/usecase/LoginWithPasswordUseCase.kt"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate User Story 1 independently with its test suite

### Incremental Delivery

1. Setup + Foundational
2. User Story 1 → validate
3. User Story 2 → validate
4. User Story 3 → validate
5. Polish & cross-cutting tasks
