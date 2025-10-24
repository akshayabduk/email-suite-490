# Project Repository

This is the initial README file for the project.

Backend now supports user labels with CRUD and email assignment:
- CRUD under /api/v1/labels
- Assign/unassign labels to emails
- List emails by label
See gmail_backend/email-suite-490/README.md for details.

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

## Email APIs

All endpoints require Authorization: Bearer <token>.

- GET /api/v1/emails/inbox?page=0&size=20  
- GET /api/v1/emails/sent?page=0&size=20  
- GET /api/v1/emails/drafts?page=0&size=20  
- GET /api/v1/emails/trash?page=0&size=20  
- GET /api/v1/emails/archived?page=0&size=20  
- GET /api/v1/emails/{id}  
- GET /api/v1/emails/search?q=hello&page=0&size=20  
- POST /api/v1/emails/compose  
  {
    "to": "recipient@example.com",
    "cc": "",
    "bcc": "",
    "subject": "Hello",
    "bodyHtml": "<p>Hi</p>",
    "bodyText": "Hi",
    "threadId": "optional-thread-id"
  }
- POST /api/v1/emails/draft  
  body like compose; saves draft
- PATCH /api/v1/emails/{id}/flags  
  {
    "read": true,
    "starred": true,
    "archived": false,
    "deleted": false
  }

## Label APIs

- GET /api/v1/labels?page=0&size=50 — List labels for current user
- POST /api/v1/labels — Create label { "name": "Work", "color": "#2563EB" }
- PATCH /api/v1/labels/{id} — Update label { "name": "Personal", "color": "#F59E0B" }
- DELETE /api/v1/labels/{id} — Delete label

Assignments:
- POST /api/v1/labels/emails/{emailId}/assign — { "labelIds": [1,2] }
- POST /api/v1/labels/emails/{emailId}/unassign — { "labelIds": [1] }

List emails by label:
- GET /api/v1/labels/{labelId}/emails?page=0&size=20

Swagger UI: /swagger-ui.html  
OpenAPI JSON: /api-docs

### Environment
- Configure DB via environment variables (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)
- Configure JWT via JWT_SECRET (32+ chars recommended) and optional JWT_EXP (seconds)
See gmail_backend/.env.example for a sample.
