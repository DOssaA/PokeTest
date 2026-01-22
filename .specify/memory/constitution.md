<!--
Sync Impact Report
- Version: template -> 1.0.0
- Modified principles: N/A (new constitution)
- Added sections: Core Principles (5), Engineering Standards, Development Workflow & Quality Gates, Governance
- Removed sections: None
- Templates requiring updates:
  - ✅ .specify/templates/plan-template.md
  - ✅ .specify/templates/spec-template.md
  - ✅ .specify/templates/tasks-template.md
  - ✅ .specify/templates/checklist-template.md
  - ⚠ .specify/templates/commands/*.md (directory not present)
- Follow-up TODOs:
  - TODO(RATIFICATION_DATE): Original adoption date not found in repository history.
-->
# PokeTest Constitution

## Core Principles

### I. Code Quality via SOLID, DRY, and Patterns
Code MUST follow SOLID and DRY principles: each class or module has a single
responsibility, dependencies are inverted where appropriate, and duplication is
eliminated through shared abstractions. Apply proven design patterns (e.g.,
Repository, Strategy, MVVM) only when they reduce coupling, clarify intent, or
support testability; avoid pattern-for-pattern's-sake. Rationale: disciplined
structure keeps the codebase adaptable and lowers long-term maintenance cost.

### II. Test-Driven Development (Non-Negotiable)
All features start with tests. For every user story, write failing tests first
(red), implement the minimal change to pass (green), then refactor. Unit tests
cover business logic, integration tests cover component boundaries, and UI tests
cover critical user flows. Tests MUST be executed in CI for every change, and
critical paths MUST not merge without automated coverage. Rationale: TDD enforces
clear requirements and reduces regressions.

### III. User Experience Consistency
User flows MUST reuse existing navigation patterns, visual components, and copy
style. New UI components require a reusable implementation and documentation in
the UI guidelines before use. Accessibility (contrast, text scaling, touch
targets) and localization readiness are required for user-facing screens.
Rationale: consistent UX reduces user confusion and support overhead.

### IV. Performance Requirements and Budgets
Every feature MUST define performance targets and budgets (e.g., render time,
startup time, network latency, memory) in its specification. Changes MUST not
regress agreed budgets without explicit approval and mitigation steps.
Performance testing and profiling are required when introducing new data flows,
animations, or background work. Rationale: measurable performance prevents
quality drift and preserves user trust.

### V. Official Documentation as Source of Truth
Use the latest official documentation for frameworks, libraries, and platform
APIs as the primary reference. Feature specs and plans MUST record the sources
and versions/dates consulted. Architecture decisions and deviations from
official guidance MUST be documented with rationale. Rationale: authoritative
sources reduce integration risk and rework.

## Engineering Standards

- Follow SOLID/DRY in design reviews; refactor when duplication or tight coupling
  appears.
- Prefer composition over inheritance; keep public APIs minimal and explicit.
- Use consistent error handling and logging patterns across modules.
- Apply static analysis, linting, and formatting tools configured for the
  codebase; warnings that impact correctness MUST be resolved before merge.

## Development Workflow & Quality Gates

- TDD gate: tests are written and failing before implementation begins.
- Design gate: choose the simplest design pattern that solves the problem; avoid
  unnecessary abstractions.
- UX gate: align with existing design system, navigation, and copy standards;
  document any new component.
- Performance gate: define and validate performance budgets; capture baseline
  measurements and verify no regressions.
- Documentation gate: include official documentation sources and versions/dates
  in research or spec artifacts.
- Review gate: every PR includes a Constitution Check and evidence for the above.

## Governance

- The constitution supersedes team conventions and feature-specific preferences.
- Amendments require a documented proposal, approval by project maintainers, and
  updates to all dependent templates and guidance files.
- Versioning follows semantic versioning: MAJOR for breaking governance changes,
  MINOR for new or materially expanded principles, PATCH for clarifications.
- Compliance is verified during review; non-compliant changes MUST be corrected
  or explicitly exempted with rationale before merge.

**Version**: 1.0.0 | **Ratified**: TODO(RATIFICATION_DATE): Original adoption date not found in repository history. | **Last Amended**: 2026-01-22
