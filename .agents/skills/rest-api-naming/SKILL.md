---
name: rest-api-naming
description: Validates and suggests REST API resource naming conventions based on industry best practices. Use this skill when designing, creating, or reviewing REST API endpoints.
---

# REST API Naming Conventions

When designing or reviewing REST API endpoints, enforce the following best practices:

1. **Use Nouns, Not Verbs**: Represent resources with nouns (e.g., `/users`, not `/getUsers`). Use HTTP methods (GET, POST, PUT, DELETE) to define actions.
2. **Plural Nouns for Collections**: Use plural nouns to denote collection resources (e.g., `/device-management/managed-devices`, `/users/{id}/accounts`). Store resources should also be plural (e.g., `/users/{id}/playlists`).
3. **Hierarchy with Forward Slash**: Use the forward-slash (`/`) to indicate a hierarchical relationship between resources (e.g., `/device-management/managed-devices/{id}`).
4. **No Trailing Slash**: Do not use a trailing forward slash (`/`) at the end of URIs. It adds no semantic value and can cause confusion.
5. **Use Hyphens**: Use hyphens (`-`) to separate words and improve readability for long path segments (e.g., `/managed-devices`, not `/managedDevices`).
6. **No Underscores**: Avoid using underscores (`_`) as they can be obscured by underlines in some browsers or screens.
7. **Lowercase Letters**: Always prefer lowercase letters in URI paths consistently.
8. **No File Extensions**: Do not use file extensions (e.g., `.xml`, `.json`) in URIs. Rely on the `Content-Type` header for media type negotiation.
9. **Filter with Query Parameters**: Enable sorting, filtering, and pagination by passing input parameters as query components, rather than creating new API endpoints (e.g., `/managed-devices?region=USA&brand=XYZ&sort=installation-date`).
10. **Custom Actions**: If an action doesn't naturally apply to the definition of resources, use a POST request with the action as a query parameter (or body), or create a custom resource that represents the action state. Avoid using verbs in the URI (e.g., use `/scripts/{id}/status` instead of `/scripts/{id}/execute`).

By applying these principles, REST URIs remain consistent, highly readable, and strictly adhere to REST architectural constraints.
