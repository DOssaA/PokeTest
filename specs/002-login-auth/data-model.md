# Data Model: Login Entry Authentication

## Entities

### UserCredential
Represents the authenticated identity stored on-device after a successful login.

**Fields**
- **credentialId**: Unique identifier for the stored credential
- **authMethod**: PASSWORD, BIOMETRIC, or GOOGLE
- **username**: User-provided identifier (required for PASSWORD; optional for other methods)
- **secret**: User secret or simulated token (required for PASSWORD; required for GOOGLE token; not applicable for BIOMETRIC)
- **profile**: Optional profile data for Google login (displayName, email, avatarUrl)
- **lastAuthenticatedAt**: Timestamp of the most recent successful login
- **storageState**: STORED or FAILED (tracks post-auth storage outcome)

**Validation Rules**
- username must be non-empty and not whitespace for PASSWORD
- secret must be non-empty for PASSWORD and GOOGLE
- profile is present only for GOOGLE

### AuthenticationEvent
Records a login attempt and its outcome.

**Fields**
- **eventId**: Unique identifier for the event
- **authMethod**: PASSWORD, BIOMETRIC, or GOOGLE
- **outcome**: SUCCESS, CANCELED, ERROR
- **occurredAt**: Timestamp of the attempt
- **errorReason**: Optional code/message when outcome is ERROR

**Validation Rules**
- outcome must be one of SUCCESS, CANCELED, ERROR
- errorReason is required when outcome is ERROR

## State Transitions

- **Login Attempt** -> **AuthenticationEvent(outcome)**
  - SUCCESS: creates/updates UserCredential
  - CANCELED: no credential change
  - ERROR: no credential change

- **Credential Persistence**
  - SUCCESS: UserCredential.storageState = STORED
  - FAILURE: UserCredential.storageState = FAILED and login is not completed
