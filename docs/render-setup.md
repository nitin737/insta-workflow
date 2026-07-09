# Render Deployment Setup

This document outlines the high-level changes made to integrate Render for deploying the `resume-api` using GitHub Actions.

## High-Level Changes
A new GitHub Actions workflow was created to automate the deployment process. The pipeline handles building the Java application, containerizing it with Docker, pushing it to the GitHub Container Registry (GHCR), and then triggering Render to pull and deploy the updated image.

- **Workflow File**: `.github/workflows/deploy-render.yml`

This approach (building on GitHub Actions) was chosen over Render's native auto-deploy because compiling the Spring Boot application and downloading LaTeX dependencies within Render's free tier can lead to out-of-memory errors or build timeouts.

---

## Architecture
1. **GitHub Actions**: 
   - Compiles the application (`./gradlew build`).
   - Builds the Docker image.
   - Pushes the image to `ghcr.io`.
2. **Render**: 
   - Hosted as a **Web Service**.
   - Pulls the pre-built image from `ghcr.io` upon receiving a webhook trigger from the GitHub Actions pipeline.

---

## Required Manual Setup

If setting this up from scratch, the following manual steps are required:

### 1. Create a Render Web Service
1. Navigate to the [Render Dashboard](https://dashboard.render.com).
2. Create a new **Web Service**.
3. Select **"Deploy an existing image from a registry"**.
4. Set the **Image URL** to: `ghcr.io/<github-username>/resume-api:latest`.
5. Once the service is created, go to **Settings**.
6. Copy the **Deploy Hook URL**.

> **Note on Private Repositories:** If the GitHub repository is private, Render requires registry credentials to pull the image from GHCR. You must provide a GitHub Personal Access Token (PAT) with `read:packages` permissions as the password.

### 2. Configure GitHub Secrets
1. Go to the GitHub repository -> **Settings** -> **Secrets and variables** -> **Actions**.
2. Add a new repository secret:
   - **Name**: `RENDER_DEPLOY_HOOK_URL`
   - **Secret**: Paste the Deploy Hook URL copied from Render.

### 3. Trigger Deployment
Pushing to the `main` or `master` branch will trigger the workflow.
```bash
git push origin main
```
The GitHub Actions workflow will build the image, push it to GHCR, and call the `RENDER_DEPLOY_HOOK_URL` to initiate the deployment on Render.
