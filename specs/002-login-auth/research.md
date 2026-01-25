# Research: Login Entry Authentication

**Date**: January 24, 2026

## Sources (official docs)

- AndroidX Biometric release notes (latest stable 1.1.0, December 17, 2025): https://developer.android.com/jetpack/androidx/releases/biometric
- BiometricPrompt API reference: https://developer.android.com/reference/androidx/biometric/BiometricPrompt
- BiometricManager API reference (canAuthenticate): https://developer.android.com/reference/androidx/biometric/BiometricManager
- Android Keystore system: https://developer.android.com/privacy-and-security/keystore
- AndroidX Security Crypto release notes (1.1.0 deprecated all APIs, July 30, 2025): https://developer.android.com/jetpack/androidx/releases/security
- DataStore release notes (latest stable 1.2.0, November 19, 2025; page last updated 2026-01-14): https://developer.android.com/jetpack/androidx/releases/datastore
- Animations in Compose (AnimatedContent, AnimatedVisibility, animate*AsState): https://developer.android.com/develop/ui/compose/animation/introduction

## Decisions

### 1) Biometric authentication
**Decision**: Use AndroidX BiometricPrompt with BiometricManager.canAuthenticate(authenticators) to gate availability and handle outcomes (success, cancel, error).
**Rationale**: BiometricPrompt is the system-provided UI for biometrics and is supported across Android versions through AndroidX Biometric. BiometricManager provides a consistent availability check for the requested authenticators.
**Alternatives considered**:
- Platform-only android.hardware.biometrics.BiometricPrompt (less compatible with older devices)
- Custom biometric UI (not allowed; platform requires the system prompt)

### 2) Secure credential storage with DataStore
**Decision**: Use Preferences DataStore for storage and encrypt credentials before persistence using a key stored in Android Keystore. Avoid AndroidX Security Crypto since its APIs are deprecated in 1.1.0.
**Rationale**: DataStore provides reliable asynchronous storage, but it does not encrypt values by default. Android Keystore protects key material and supports non-exportable keys. The AndroidX Security Crypto library is deprecated in favor of platform APIs, so Keystore-backed encryption is preferred.
**Alternatives considered**:
- AndroidX Security Crypto (deprecated as of 1.1.0)
- SharedPreferences with custom encryption (superseded by DataStore)

### 3) Compose animations for screen transitions
**Decision**: Use Compose animation APIs (AnimatedContent, AnimatedVisibility, animate*AsState) to implement the login-to-home transition and in-screen motion.
**Rationale**: Compose animation APIs are official, stable, and align with the single-activity Compose architecture used in the app.
**Alternatives considered**:
- Accompanist navigation animations (third-party)
- XML-based or Activity transition animations (not aligned with Compose-only UI)

### 4) Simulated Google OAuth flow
**Decision**: Implement a local provider that returns Success (fake token + profile), Cancel, Error, or Loading states to drive the OAuth-like UI.
**Rationale**: The feature explicitly requires local-only simulation while matching real OAuth UX and supporting loading/error testing.
**Alternatives considered**:
- Real OAuth integration (out of scope)
- Simplified single-step button without flow UI (insufficient UX fidelity)
