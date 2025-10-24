# Backend Dev Verification

## CORS
- Allowed origins: via env CORS_ALLOWED_ORIGINS (comma-separated). Default: http://localhost:3000
- Exposed headers: Content-Disposition (for downloads)
- Allowed headers: *
- Allowed methods: GET, POST, PUT, PATCH, DELETE, OPTIONS

## Env Variables and Defaults
- SERVER_PORT: default 3001
- DB_HOST: default localhost
- DB_PORT: default 5001
- DB_NAME: default gmail
- DB_USER: default postgres
- DB_PASSWORD: default postgres
- DB_URL: override full JDBC URL
- JWT_SECRET: default dev-local-jwt-secret-change-me-and-make-it-longer
- JWT_EXP: default 86400
- FILE_STORAGE_DIR: default ./attachments (if empty it will fallback to a local attachments folder)
- CORS_ALLOWED_ORIGINS: default http://localhost:3000

## Endpoints quick check (after starting DB and backend)
- GET /health
- POST /api/v1/auth/register
- POST /api/v1/auth/login
- GET /api/v1/emails/inbox
- POST /api/v1/emails/compose
- PATCH /api/v1/emails/{id}/flags
- Attachments:
  - POST /api/v1/attachments/upload
  - GET /api/v1/attachments/{id}/download

Ensure Content-Disposition is present when downloading attachments and accessible from the browser (exposed via CORS).
