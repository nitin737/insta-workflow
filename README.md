<div align="center">
  <h3 align="center">Insta-Workflow</h3>

  <p align="center">
    Complete Automation of Instagram Workflow
    <br />
    <br />
    <a href="#about-the-project">View Demo</a>
    ·
    <a href="#usage">Report Bug</a>
    ·
    <a href="#usage">Request Feature</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#project-structure">Project Structure</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>

## About The Project

**Insta-Workflow** is an end-to-end automated pipeline for generating engaging, high-quality Instagram carousel posts. It seamlessly bridges AI-driven content generation with beautiful, template-driven design.

The system utilizes Gemini AI (via a Spring Boot insta-api) to generate post content and structures, while a standalone React and Tailwind CSS renderer converts design templates into static, portable HTML files. The Java insta-api then injects dynamic data into these templates and creates polished images ready for publishing.

### Built With

* [![Java][Java-badge]][Java-url]
* [![Spring Boot][SpringBoot-badge]][SpringBoot-url]
* [![React][React-badge]][React-url]
* [![TailwindCSS][Tailwind-badge]][Tailwind-url]
* [![PostgreSQL][Postgres-badge]][Postgres-url]
* [![Docker][Docker-badge]][Docker-url]

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

* [Node.js](https://nodejs.org/) (v18 or higher)
* [Docker](https://www.docker.com/) and Docker Compose
* Java 21+ (if running the insta-api locally without Docker)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/your-username/insta-workflow.git
   ```
2. Setup environment variables by copying `.env.example` to `.env` (make sure to set your `GEMINI_API_KEY`)
   ```sh
   cp .env.example .env
   ```
3. Start the insta-api and Database services via Docker
   ```sh
   docker-compose up -d --build
   ```
4. Install Renderer dependencies
   ```sh
   cd renderer
   npm install
   ```

## Project Structure

The repository is logically divided into two primary workspaces:

* **`insta-api/`**: A Spring Boot application providing the core logic. It integrates with the Gemini API to orchestrate content creation, handles database interactions via PostgreSQL, and injects data into rendering templates.
* **`renderer/`**: A React-based templating engine. It builds self-contained HTML files populated with Tailwind CSS that serve as the visual layout for Instagram slides.

## Usage

### Rendering Templates

The `renderer` project provides standard HTML previews and template generation for the Java insta-api. To test or build templates, navigate to the `renderer/` directory:

```bash
# Standard mode (visual preview with sample.json)
npm run generate

# Template mode (Mustache template generation for Java injection)
# For Windows PowerShell:
$env:TEMPLATE_MODE="true"; npm run generate
# For Linux/Mac:
TEMPLATE_MODE=true npm run generate
```

## License

Distributed under the MIT License. See `LICENSE` for more information.

[Java-badge]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://java.com/
[SpringBoot-badge]: https://img.shields.io/badge/spring_boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot
[React-badge]: https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB
[React-url]: https://reactjs.org/
[Tailwind-badge]: https://img.shields.io/badge/tailwindcss-%2338B2AC.svg?style=for-the-badge&logo=tailwind-css&logoColor=white
[Tailwind-url]: https://tailwindcss.com/
[Postgres-badge]: https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Docker-badge]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
