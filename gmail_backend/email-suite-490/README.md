# Project Repository

This is the initial README file for the project.

## Backend Auth (gmail_backend)

Base URL: http://localhost:3001

- Register: POST /api/v1/auth/register
  Body:
  {
    "email": "user@example.com",
    "password": "StrongP@ssw0rd"
  }

- Login: POST /api/v1/auth/login
  Body:
  {
    "email": "user@example.com",
    "password": "StrongP@ssw0rd"
  }

- Current user: GET /api/v1/auth/me
  Headers:
  Authorization: Bearer <token>

Swagger UI: /swagger-ui.html  
OpenAPI JSON: /api-docs

### Environment
- Configure DB via environment variables (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)
- Configure JWT via JWT_SECRET (32+ chars recommended) and optional JWT_EXP (seconds)
See gmail_backend/.env.example for a sample.
