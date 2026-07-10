---
goal: Resume API MVP Implementation Plan
version: 1.0
date_created: 2026-07-10
last_updated: 2026-07-10
owner: Antigravity
status: 'Planned'
tags: feature, mvp, architecture
---

# Introduction

![Status: Planned](https://img.shields.io/badge/status-Planned-blue)

This plan outlines the implementation of four core MVP features for the Resume API: API-Key Authentication, Save & Re-generate capabilities, Custom Theme Overrides, and Async Generation Webhooks, while conforming to the requested architectural constraints (Supabase, ThreadPoolTaskExecutor) and deferring the thumbnail generation.

## 1. Requirements & Constraints

- **REQ-001**: API must secure endpoints using an `X-API-KEY` header and validate it against a tenant database.
- **REQ-002**: System must enforce rate limiting per tenant.
- **REQ-003**: System must store JSON payloads to a PostgreSQL database using `JSONB` for a "Save and Resume" editing experience.
- **REQ-004**: System must allow dynamic styling overrides via a `theme` property in the JSON payload.
- **REQ-005**: System must support async PDF generation triggered via a webhook endpoint, responding with HTTP 202 immediately.
- **CON-001**: Application must use a Supabase PostgreSQL database connection configured via environment variables.
- **CON-002**: Asynchronous processing must utilize a `ThreadPoolTaskExecutor` to bound thread creation and prevent memory exhaustion.
- **CON-003**: Thumbnail generation endpoint is deferred for the MVP and must not be implemented.

## 2. Implementation Steps

### Implementation Phase 1: Database & Security Foundation

- GOAL-001: Configure Supabase integration and implement tenant-based API key authentication.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-001 | Add `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, and `org.postgresql:postgresql` to `resume-api/build.gradle` | | |
| TASK-002 | Configure `resume-api/src/main/resources/application.properties` with Supabase JDBC URL using environment variables | | |
| TASK-003 | Create `Tenant` JPA Entity with fields (`ID`, `Key Hash`, `Tenant Name`, `Tier`) | | |
| TASK-004 | Create `TenantRepository` Spring Data JPA interface | | |
| TASK-005 | Implement `ApiKeyAuthenticationFilter` to validate `X-API-KEY` header | | |
| TASK-006 | Create `SecurityConfig` to enforce authentication and Bucket4j rate limiting | | |

### Implementation Phase 2: Save and Re-generate

- GOAL-002: Implement endpoints and persistence for storing resume JSON data.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-007 | Create `ResumeData` JPA Entity with `JSONB` column mapping | | |
| TASK-008 | Create `ResumeDataRepository` Spring Data JPA interface | | |
| TASK-009 | Add POST, GET, PUT endpoints in `ResumeController` for `/api/v1/resumes/{id}` | | |
| TASK-010 | Implement CRUD and generation triggering logic in `ResumeService` | | |

### Implementation Phase 3: Custom Theme Overrides

- GOAL-003: Allow users to override template themes dynamically via API requests.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-011 | Add `Map<String, String> theme` to `GenerateRequest` DTO | | |
| TASK-012 | Update `ResumeRendererStrategy` interface and implementations to pass `theme` to Thymeleaf context | | |
| TASK-013 | Modify `template.tex` (Corporate style) to use Thymeleaf expressions for colors (e.g., `\definecolor{primary}{HTML}{[(${theme.primaryColor})]}`) | | |

### Implementation Phase 4: Async Webhook Generation

- GOAL-004: Implement non-blocking PDF generation with webhook callbacks.

| Task | Description | Completed | Date |
|------|-------------|-----------|------|
| TASK-014 | Add `spring-boot-starter-webflux` to `resume-api/build.gradle` | | |
| TASK-015 | Create `AsyncGenerateRequest` DTO containing `webhookUrl` | | |
| TASK-016 | Add `@PostMapping("/generate-async")` in `ResumeController` returning HTTP 202 | | |
| TASK-017 | Configure `AsyncConfig` with a `ThreadPoolTaskExecutor` bean | | |
| TASK-018 | Create `AsyncGenerationService` to generate PDF and POST to `webhookUrl` | | |

## 3. Alternatives

- **ALT-001**: Using `SimpleAsyncTaskExecutor` instead of `ThreadPoolTaskExecutor`. Rejected because it creates a new thread for each task, risking OOM errors under heavy PDF generation load.
- **ALT-002**: Generating thumbnails dynamically on request. Rejected/Deferred to simplify the MVP scope based on user constraints.
- **ALT-003**: Local PostgreSQL via Docker Compose. Rejected in favor of managed Supabase instance for consistency with deployment strategies.

## 4. Dependencies

- **DEP-001**: Supabase PostgreSQL database instance.
- **DEP-002**: Bucket4j for rate limiting (already present in the dependencies).
- **DEP-003**: Spring Boot WebFlux for `WebClient`.

## 5. Files

- **FILE-001**: `resume-api/build.gradle`
- **FILE-002**: `resume-api/src/main/resources/application.properties`
- **FILE-003**: `resume-api/src/main/java/com/example/resumeapi/model/Tenant.java`
- **FILE-004**: `resume-api/src/main/java/com/example/resumeapi/repository/TenantRepository.java`
- **FILE-005**: `resume-api/src/main/java/com/example/resumeapi/security/ApiKeyAuthenticationFilter.java`
- **FILE-006**: `resume-api/src/main/java/com/example/resumeapi/config/SecurityConfig.java`
- **FILE-007**: `resume-api/src/main/java/com/example/resumeapi/model/ResumeData.java`
- **FILE-008**: `resume-api/src/main/java/com/example/resumeapi/repository/ResumeDataRepository.java`
- **FILE-009**: `resume-api/src/main/java/com/example/resumeapi/controller/ResumeController.java`
- **FILE-010**: `resume-api/src/main/java/com/example/resumeapi/service/ResumeService.java`
- **FILE-011**: `resume-api/src/main/java/com/example/resumeapi/model/GenerateRequest.java`
- **FILE-012**: `resume-api/src/main/java/com/example/resumeapi/strategy/ResumeRendererStrategy.java`
- **FILE-013**: `resume-api/src/main/resources/templates/corporate/template.tex`
- **FILE-014**: `resume-api/src/main/java/com/example/resumeapi/model/AsyncGenerateRequest.java`
- **FILE-015**: `resume-api/src/main/java/com/example/resumeapi/config/AsyncConfig.java`
- **FILE-016**: `resume-api/src/main/java/com/example/resumeapi/service/AsyncGenerationService.java`

## 6. Testing

- **TEST-001**: Unit tests for `ApiKeyAuthenticationFilter` validating valid, invalid, and missing keys.
- **TEST-002**: Integration tests for Async Generation verifying the Webhook callback using `MockRestServiceServer` or WireMock.
- **TEST-003**: Repository tests for `ResumeDataRepository` ensuring proper `JSONB` serialization/deserialization.

## 7. Risks & Assumptions

- **RISK-001**: Storing large volumes of generated PDFs could exceed storage limits. Mitigation: PDF files themselves are not stored persistently in the database, only the source JSON configurations.
- **ASSUMPTION-001**: The Supabase connection string and credentials will be provided correctly in the target deployment environments.

## 8. Related Specifications / Further Reading

- [resume_api_mvp_plan.md](file:///c:/Users/nitin/Desktop/dev/insta-workflow/docs/specs/resume_api_mvp_plan.md)
