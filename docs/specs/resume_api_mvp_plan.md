# Resume API MVP Implementation Plan

This document details the High-Level Design (HLD) and Low-Level Design (LLD) for the five core features needed to bring the `resume-api` to a production-ready Minimum Viable Product (MVP) state.

## Proposed Changes

### 1. API-Key Authentication

**HLD**: Secure the API using API keys for developers/tenants. Every request must include an `X-API-KEY` header. Rate limiting will ensure abuse prevention per tenant.
**LLD**:
- **Entity**: `Tenant` or `ApiKey` entity (ID, Key Hash, Tenant Name, Tier).
- **Security**: Implement a Spring Security `OncePerRequestFilter` (`ApiKeyAuthenticationFilter`) that extracts the `X-API-KEY` header, hashes it (if storing securely), and validates it against the database.
- **Rate Limiting**: Integrate `Bucket4j` (backed by Redis or in-memory Caffeine) tied to the `ApiKey` to enforce the limits (e.g., free tier = 10 requests/hour).

### 2. List Templates with Thumbnails

**HLD**: Provide an endpoint for frontend applications to display available templates with visual previews so users can pick their preferred style.
**LLD**:
- **Storage**: Store a pre-generated `thumbnail.png` inside each template's directory (e.g., `src/main/resources/templates/corporate/thumbnail.png`). Alternatively, store thumbnails on Cloudinary/S3. *(Note: Exact hosting strategy to be decided later).*
- **Endpoint**: `GET /api/v1/templates`
- **Response Shape**:
  ```json
  {
    "templates": [
      {
        "id": "corporate",
        "name": "Corporate Style",
        "thumbnail_url": "/api/v1/templates/corporate/thumbnail.png"
      }
    ]
  }
  ```

### 3. Save Resume and Re-generate

**HLD**: Allow users to store their resume JSON on the server. This prevents the need to constantly re-transmit large JSON payloads and enables a continuous "save and resume" editing experience.
**LLD**:
- **Database Entity**: `ResumeData` (ID `UUID`, `Tenant_ID`, `JSON_Payload` as `JSONB`, `Created_At`, `Updated_At`). Use PostgreSQL's `JSONB` for efficient storage.
- **Endpoints**:
  - `POST /api/v1/resumes` -> Saves JSON, returns `resume_id`.
  - `GET /api/v1/resumes/{id}` -> Retrieves the saved JSON.
  - `PUT /api/v1/resumes/{id}` -> Updates the JSON.
  - `POST /api/v1/resumes/{id}/generate` -> Triggers the PDF generation using the stored JSON data.

### 4. Custom Theme Overrides

**HLD**: Empower users to tweak templates by passing dynamic design properties (colors, fonts) inside their JSON payload, making the templates flexible without maintaining multiple `.tex` variants.
**LLD**:
- **Schema Update**: Add a `theme` object to the resume JSON schema (e.g., `primaryColor: "2A3B4C"`).
- **Thymeleaf & LaTeX Integration**:
  Update `template.html` (Thymeleaf template for LaTeX) to inject these variables.
  Example in the template:
  ```latex
  \definecolor{primary}{HTML}{[(${theme.primaryColor != null ? theme.primaryColor : '000000'})]}
  ```
- **Java DTO**: Update the Java Request DTOs to map the `theme` fields.

### 5. Generation Webhook (Asynchronous Processing)

**HLD**: PDF compilation via LaTeX can be slow. For integrations, support async processing where the API responds immediately with an `Accepted (202)` status and later POSTs the result to a specified webhook URL.
**LLD**:
- **Endpoint**: `POST /api/v1/resumes/generate-async`
- **Payload**: Accept a `webhook_url` along with the standard resume JSON (or `resume_id`).
- **Processing**:
  - The controller uses Spring's `@Async` to offload the LaTeX compilation to a background worker thread. *(Note: Using Spring's internal thread pool for the initial MVP).*
  - Returns `HTTP 202 Accepted` immediately.
  - Once compilation finishes, a `RestTemplate` or `WebClient` makes an HTTP POST request to the provided `webhook_url` containing the PDF data (or a public download link) and status (success/failure).

## Verification Plan
- Unit tests for the `ApiKeyAuthenticationFilter`.
- Integration tests simulating the complete lifecycle: Save JSON -> Trigger Webhook Async Generation -> Verify Webhook invocation using WireMock.
