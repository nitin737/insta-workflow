# High-Level Design (HLD) and Low-Level Design (LLD) for Instagram Workflow Automation

## 1. Current Project Analysis (`insta-carousel-maker`)

Your project is an excellent MVP. Here’s what you’ve already solved:

| Component | What it does | Automation Readiness |
| :--- | :--- | :--- |
| `DESIGN.md` | Defines the Go-AI Tech Neon design tokens (colors, fonts, spacing). | **High** – Fully programmatic. |
| `GEMINI_PROMPT.md` | A structured prompt that forces Gemini to output a strict 7-slide JSON schema. | **Critical** – This is your core API contract. |
| `carousel-builder.html` | A visual rendering engine that takes the JSON and draws 1080x1080 slides in the browser. | **Core Asset** – Just needs a headless driver. |

**The Gap**: You manually copy JSON from Gemini, paste it into the HTML, and take screenshots.  
**The Goal**: Automate `JSON Generation → Rendering → Publishing → Analytics`.

---

## 2. High-Level Design (HLD) – The End-to-End System

The system is divided into **4 Core Services** and **1 Orchestrator**.

```text
┌──────────────────────────────────────────────────────────────────────────────────┐
│                        ORCHESTRATOR (Scheduler + State Machine)                │
│                (Triggers at 9:00 AM Mon/Wed/Fri | Manages Retries & Alerts)    │
└───────────────┬──────────────────┬──────────────────┬──────────────────────────┘
                │                  │                  │
                ▼                  ▼                  ▼
┌───────────────────────┐ ┌─────────────────┐ ┌─────────────────────────────────┐
│   1. Content Gen      │ │  2. Rendering   │ │  3. Publishing & Analytics      │
│  (Gemini + Rules)    │──▶ (Headless      │──▶ (Instagram Graph API +        │
│                       │    Browser or     │    PostgreSQL for Feedback Loop) │
│  Input: Topic/Pillar │    Node-Canvas)   │                                 │
│  Output: JSON Slides │  Output: 7 PNGs   │  Output: Live Post + Insights    │
└───────────────────────┘ └─────────────────┘ └─────────────────────────────────┘
```

### The Orchestrator (State Machine)
Each content piece moves through 5 states:
1. **Draft** – Topic selected.
2. **Generated** – JSON validated against your schema.
3. **Rendered** – PNGs generated and uploaded to a CDN (e.g., Cloudinary).
4. **Published** – Live on Instagram (or scheduled).
5. **Analyzed** – Engagement data pulled back to influence future topic selection.

---

## 3. Low-Level Design (LLD) – Deep Dive into Modules

### A. Content Generation Module (Java/Spring Boot recommended)

Since your `GEMINI_PROMPT.md` is already perfect, we just wrap it in an API.

- **Endpoint**: `POST /api/v1/generate`
- **Request Body**:
  ```json
  {
    "topic": "Why context.Context matters in Go",
    "pillar": "Go Bites",
    "tone": "educational",
    "target_date": "2026-07-01T09:00:00Z"
  }
  ```
- **Internal Logic**:
  1. Fetch the system prompt from `GEMINI_PROMPT.md`.
  2. Inject the user's `topic` and `pillar`.
  3. Call `gemini-1.5-pro-latest` or `gemini-2.0-flash` with JSON response enabled.
  4. Validate the response against a **Java Record/DTO with Bean Validation (Jackson)** (ensuring a 5-slide schema matching `gemini-carousel-prompt-1.md`).
  5. **Enrichment**: Use a Java syntax highlighter (e.g., Jygments or custom parsing) to add ANSI or HTML color tokens to the code blocks (your HTML renderer can consume these).
  6. Generate a **Caption** and **Hashtag set** dynamically (e.g., 5 niche tags + 3 broad tags based on the pillar).
- **Output**: Save the enriched JSON to a database with status `generated`.

---

### B. Carousel Rendering Module (The bridge to your HTML)

**Current Approach: Server-Side HTML Templating (Recommended)**
- The HTML template (e.g., `carousel-template.html`) is stored in the Spring Boot `src/main/resources` directory.
- The Java insta-api takes the Gemini JSON output and injects it directly into the HTML template (using a template engine like Thymeleaf, Mustache, or simple string replacement).
- The insta-api passes the pre-populated HTML to a headless browser (Puppeteer/Playwright or a Java wrapper like Playwright-Java).
- The headless browser renders the page, loops through the 7 slides, uses `page.screenshot()`, and generates `slide_1.png` to `slide_7.png`.

**Previous Approaches (Archived for Record):**

*Path 1: Headless Browser with page.evaluate()*
- You write a Node.js or Python script that launches a headless Chromium instance.
- It opens your `carousel-builder.html` locally.
- It uses `page.evaluate()` to inject the JSON directly into the HTML's JavaScript context (mocking the "Load JSON" button).
- It uses `page.screenshot()` in a loop, sliding through each of the 7 slides.
- It saves the screenshots as `slide_1.png` to `slide_7.png`.

*Path 2: Native Node-Canvas (Long-term scaling)*
- Rewrite the layout logic of your HTML into a Node.js library using `node-canvas` or `sharp`.
- Translate your CSS flexbox/grid rules into imperative drawing commands.
- This removes browser overhead, making it 10x faster and Docker-friendly.

**The Rendering Queue**:
To avoid blocking the API, push render jobs to a Redis Queue (BullMQ). A worker picks it up, renders the PNGs, and uploads them to **Cloudinary S3**. The final output is an array of public image URLs.

---

### C. Publishing Pipeline Module (Instagram Graph API)

Instagram does not allow direct binary uploads; it requires **public URLs**.

- **Step 1: Create Containers** (For each slide):
  ```http
  POST /<ig-user-id>/media
  {
    "image_url": "https://cdn.yourdomain.com/slide_1.png",
    "is_carousel_item": true
  }
  ```
  *Response*: `container_id_1`, `container_id_2`... `container_id_7`.

- **Step 2: Create Carousel Container**:
  ```http
  POST /<ig-user-id>/media
  {
    "media_type": "CAROUSEL",
    "children": ["id_1", "id_2", ..., "id_7"],
    "caption": "Your AI-generated caption \n\n #golang #insta-api ..."
  }
  ```
  *Response*: `carousel_container_id`.

- **Step 3: Publish**:
  ```http
  POST /<ig-user-id>/media_publish
  {
    "creation_id": "carousel_container_id"
  }
  ```
- **Scheduling**: The Graph API doesn't support native scheduling for Creators. You must implement a **Cron Scheduler** (e.g., Spring `@Scheduled` or `Quartz`) that holds the job until the `target_date` and then executes Steps 1-3 exactly at that time.

---

### D. Analytics & Feedback Loop Module

- **Data Collection**: Every 24 hours, hit the Graph API `/{media_id}/insights` to get `impressions`, `reach`, `engagement`, and `saved`.
- **Store**: Keep a PostgreSQL table:
  ```sql
  CREATE TABLE posts (
    id UUID PRIMARY KEY,
    topic TEXT,
    pillar TEXT, -- "Go Bites", "Project Spotlight", etc.
    post_time TIMESTAMP,
    views INT,
    saves INT,
    shares INT
  );
  ```
- **The Loop**: Run a weekly aggregation. If `pillar = "Go Bites"` has a 40% higher save rate than `pillar = "Ecosystem"`, the Orchestrator will prioritize generating "Go Bites" content for the next week. This closes the feedback loop.

---

## 4. Roadmap for Your Repository (`insta-carousel-maker`)

### Phase 1: The "Glue" Layer (Week 1)
**Goal**: Go from manual to semi-automated (Generate + Screenshot).

1. Create a `/insta-api` folder and initialize a Spring Boot project.
2. Write a `GenerationService.java` that calls the Gemini API exactly as your prompt instructs.
3. Write a `render.js` using Puppeteer that launches your HTML and screenshots all 7 slides.
4. *Success Metric*: You run `./mvnw spring-boot:run -Dspring-boot.run.arguments="--topic=Channels"` and get 7 PNGs on your desktop automatically.

### Phase 2: The API & Database (Week 2)
**Goal**: Go from local to a web service.

1. Wrap the Java logic in a **Spring Boot** REST Controller.
2. Add a lightweight SQLite/H2 database (with Spring Data JPA) to track the `state` of each job.
3. Instead of saving PNGs locally, upload them to **Cloudinary** (their upload API gives you the public URL instantly).
4. *Success Metric*: You hit `POST /generate` via Postman, and it returns a JSON with `{"status": "rendered", "image_urls": [...]}`.

### Phase 3: The Publisher (Week 3)
**Goal**: Full automation.

1. Implement the Instagram Graph API steps in Java (`publishCarousel` method).
2. Set up a simple scheduler (using Spring `@Scheduled`) to check for pending posts every 5 minutes.
3. Add an alert system: if the API returns a rate-limit error, automatically back off for 15 minutes.
4. *Success Metric*: A post goes live on Instagram completely unattended at 9:00 AM.

### Phase 4: The Brain (Week 4+)
**Goal**: Self-improving system.

1. Hook up the Analytics endpoint.
2. Build a simple dashboard (or use Streamlit) that visualizes which pillars are winning.
3. Feed the winning topics back into the "Topic Enrichment" step of the Content Generation.

---

## 5. Recommended Tech Stack Summary

| Layer | Technology | Why |
| :--- | :--- | :--- |
| **insta-api** | Java + Spring Boot | Enterprise-grade, robust ecosystem with Spring Web and strong type safety. |
| **Queue** | Redis + RQ (or BullMQ) | Handles concurrent renders without dropping requests. |
| **Rendering** | Puppeteer (Node) | Reuses your HTML/CSS perfectly. |
| **Image Hosting** | Cloudinary | Converts PNGs to WebP automatically, provides instant CDN links required by Instagram. |
| **Database** | Supabase (PostgreSQL) | Built-in REST API, easy for future dashboarding. |
| **Deployment** | Docker + Fly.io | Simple, supports Puppeteer dependencies well. |
