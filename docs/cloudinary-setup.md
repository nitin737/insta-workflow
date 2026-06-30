# Cloudinary Integration Setup

This document details the high-level changes made to integrate Cloudinary for hosting generated Instagram carousel images.

## Overview
As part of Phase 2 of our automation specification, the system has been updated to upload the AI-generated carousel images to Cloudinary instead of saving them locally. This enables the backend to supply public CDN URLs to the Instagram Graph API during the publishing phase.

## Configuration Updates
- **Dependency Added**: Added `com.cloudinary:cloudinary-http45` to `build.gradle`.
- **Properties File Updates**: Added Cloudinary environment properties to `application.properties`:
  - `cloudinary.cloud-name`
  - `cloudinary.api-key`
  - `cloudinary.api-secret`
- **Environment Variables**: To run the backend, you must configure the following properties in your local `.env` or system environment:
  - `CLOUDINARY_CLOUD_NAME`
  - `CLOUDINARY_API_KEY`
  - `CLOUDINARY_API_SECRET`

## Code Changes
- **`CloudinaryConfig.java`**: A Spring `@Configuration` class that instantiates a `Cloudinary` bean using the properties defined above.
- **`RenderingService.java`**: 
  - Instead of saving screenshots locally via `Paths.get(fileName)`, the Playwright page captures screenshots into memory (`byte[]`).
  - These bytes are then uploaded to Cloudinary using `cloudinary.uploader().upload()`.
  - The service now returns a list of public Cloudinary secure URLs instead of local file paths.
