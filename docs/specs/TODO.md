# Insta-Workflow Automation TODO List

Based on the roadmap defined in `docs/specs/automation_spec.md`, here is the current progress of the project.

## Phase 1: The "Glue" Layer
**Goal**: Go from manual to semi-automated (Generate + Screenshot).

- [x] Create a `/insta-api` folder and initialize a Spring Boot project.
- [x] Write a `GenerationService.java` that handles the Gemini API call logic (currently simulated with a placeholder).
- [x] Implement the renderer. *(Note: Using Playwright + Thymeleaf directly in Java instead of a separate Node.js Puppeteer script)*.
- [x] *Success Metric*: Able to trigger the generation and get 7 PNGs saved to disk locally.

## Phase 2: The API & Database
**Goal**: Go from local to a web service.

- [x] Wrap the Java logic in a **Spring Boot** REST Controller (`GenerationController.java`).
- [x] Implement the actual Gemini API call inside `GenerationService.java` using Spring AI or standard REST client.
- [x] Replace placeholder `carousel-template.html` with the real MVP design and CSS.
- [x] Add a database (PostgreSQL via Docker Compose is already configured) with Spring Data JPA to track the `state` of each job.
- [x] Upload generated PNGs to **Cloudinary** instead of saving them locally.
- [x] *Success Metric*: `POST /api/v1/carousels` returns a JSON with `{"status": "rendered", "image_urls": [...]}` containing real Cloudinary URLs.

## Phase 3: The Publisher
**Goal**: Full automation.

- [x] Implement the Instagram Graph API steps in Java (`publishCarousel` method).
- [x] Set up a simple scheduler (using Spring `@Scheduled`) to check for pending posts.
- [x] Add an alert/retry system: if the API returns a rate-limit error, automatically back off.
- [ ] *Success Metric*: A post goes live on Instagram completely unattended at the scheduled time.

## Phase 4: The Brain
**Goal**: Self-improving system.

- [ ] Hook up the Instagram Analytics endpoint.
- [ ] Build a simple dashboard that visualizes which pillars/topics are winning.
- [ ] Feed the winning topics back into the "Topic Enrichment" step of the Content Generation.
