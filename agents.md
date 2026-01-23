# PokeTest Agent Context

This file provides AI agents with the authoritative project context and rules.
Follow these instructions before making changes.

## Architecture

- Follow Google's recommended Android architecture with three layers: UI, Domain,
  Data. Dependencies are unidirectional toward the Data layer.
- UI layer uses MVI with Jetpack ViewModel.
- Single-activity architecture with Jetpack Compose for UI.
- Scalability is supported by dependency injection, enabling Test Driven
  Development (TDD) to reduce defects.

## Security

- All network traffic MUST use HTTPS.
- OAuth is required for authentication.
- User data must be encrypted in local persistence.
- Use static analysis tools to prevent vulnerabilities.
- Never store credentials inside the project repository.

## Tech Stack

- Language: Kotlin
- Libraries: Jetpack (Compose, Navigation, ViewModel, Room, DataStore), Retrofit
  (HTTP client), Koin (dependency injection), Coil (efficient image loading),
  JUnit (unit tests), Mockk (tests mocks), Espresso (ui tests), Turbine (flows tests)
- AI acceleration: using Spec Driven Development (GitHub spec-kit)

## Constitution (Authoritative Definitions)

### Core Principles

1) **Code Quality via SOLID, DRY, and Patterns**  
   Code MUST follow SOLID and DRY. Apply design patterns only when they reduce
   coupling, clarify intent, or improve testability.

2) **Test-Driven Development (Non-Negotiable)**  
   All features start with tests: red → green → refactor. Unit, integration, and
   UI tests cover logic, boundaries, and critical flows. Tests MUST run in CI.

3) **User Experience Consistency**  
   Reuse navigation, components, and copy patterns. New components must be
   reusable and documented. Accessibility and localization readiness are required.

4) **Performance Requirements and Budgets**  
   Each feature defines measurable performance targets and budgets. Regressions
   require explicit approval and mitigation.

5) **Official Documentation as Source of Truth**  
   Use the latest official documentation for frameworks/APIs. Record sources and
   versions/dates in specs/plans. Deviations must be justified.

6) **Documentation Clarity & Kotlin Conventions**  
   Non-intuitive features MUST be documented in the appropriate location
   (KDoc, README, feature docs, or in-app copy). Code and documentation MUST
   follow Kotlin coding conventions and KDoc style.

### Engineering Standards

- Enforce SOLID/DRY in design reviews; refactor when duplication or coupling appears.
- Prefer composition over inheritance; keep public APIs minimal and explicit.
- Use consistent error handling and logging patterns across modules.
- Apply static analysis, linting, and formatting; correctness warnings must be fixed
  before merge.
- Kotlin coding conventions and KDoc style are required for code and documentation.

### Development Workflow & Quality Gates

- **TDD gate**: tests written and failing before implementation.
- **Design gate**: use the simplest pattern that solves the problem.
- **UX gate**: align with design system; document new components.
- **Performance gate**: define budgets and verify no regressions.
- **Documentation gate**: record official sources with versions/dates; document
  non-intuitive features; follow language conventions and documentation style.
- **Review gate**: every PR includes a Constitution Check with evidence.

### Governance

- The constitution supersedes team conventions and feature-specific preferences.
- Amendments require a documented proposal, maintainer approval, and updates to
  dependent templates/guidance.
- Semantic versioning applies: MAJOR (breaking), MINOR (new/expanded), PATCH (clarify).
- Non-compliant changes must be corrected or explicitly exempted with rationale.

**Constitution version**: (see version in `.specify/memory/constitution.md`).
