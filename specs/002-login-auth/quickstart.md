# Quickstart: Login Entry Authentication

## Overview
This feature adds a login-first experience with three simulated authentication methods: username/password, biometric, and Google OAuth-style flow. After success, credentials are stored securely and the user is navigated to the home screen with a Compose animation.

## Preconditions
- App installs and launches on Android 26+
- No existing authenticated user session
- Style reference image is available for UI alignment

## Manual Verification

### 1) Username + Password
1. Launch the app (login screen should appear first).
2. Enter non-empty username and password.
3. Tap submit.
4. Expect animated transition to home and secure credential storage.
5. Try empty or whitespace-only input; expect clear error message and no navigation.

### 2) Biometric
1. On login screen, select biometric login.
2. If biometrics are available, confirm the prompt.
3. Expect animated transition to home and credential storage.
4. If biometrics are unavailable or canceled, expect a clear message and remain on login.

### 3) Google OAuth-style Flow (Simulated)
1. Select Google login.
2. Complete the simulated flow in the OAuth-style screen:
   - Continue: shows a loading state, then returns fake token + profile and navigates to home
   - Cancel: returns to login with a message
   - Simulate error: shows an error message and returns to login

## Test Coverage Expectations
- Unit tests for reducers, use cases, repository, and data sources
- Integration tests for simulated providers and secure storage behavior
- UI tests for login screen inputs, buttons, and navigation transitions
