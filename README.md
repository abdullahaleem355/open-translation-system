# OpenTranslation Management Service

A Spring Boot microservice engine for **managing translations, locales, and tags** across diverse application domains. The system supports **JWT-based authentication**, **CRUD operations**, and advanced **dynamic search capabilities** with fine-grained filtering. It includes endpoints optimized for **frontend applications**, featuring **pagination**, **export functionality**, and bulk test data loading.

Built with **performance and scalability in mind**, the service consistently delivers **response times as low as 200 ms for standard requests** and gracefully handles heavier operations with **500 ms thresholds under load**. The architecture ensures a **robust and efficient translation management engine**, designed to scale seamlessly while maintaining reliability.

Comprehensive **unit tests (JUnit)** validate controllers and services, and the service can be deployed effortlessly via **Docker Compose**, providing a ready-to-use, high-tech solution for translation and localization management across enterprise-grade applications.


---

## Table of Contents

- [Features](#features)  
- [Technologies](#technologies)  
- [Running the Application](#running-the-application)  
- [API Endpoints](#api-endpoints)  
  - [Authentication](#authentication)  
  - [Locales](#locales)  
  - [Tags](#tags)  
  - [Translations](#translations)  
  - [Test Data](#test-data)  
- [JWT Protection](#jwt-protection)  
- [Testing](#testing)  
- [Docker](#docker)  

---

## Features

- JWT-secured endpoints for translation management  
- CRUD operations for translations, tags, and locales  
- Search translations by key, content, or tag  
- Export translations grouped by locale  
- Load test data dynamically  
- Unit test coverage with JUnit and Mockito  

---

## Technologies

- Java 17+  
- Spring Boot 3  
- Spring Data JPA (Hibernate)  
- H2 / PostgreSQL (configurable)  
- Docker & Docker Compose  
- JUnit 5 & Mockito  

---

## Running the Application

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/opentranslation-management.git
cd opentranslation-management
```

### 2. Build and run Docker containers
```bash
docker-compose up --build
```

This will start the service on http://localhost:8080


### 3. Stop containers
```bash
docker-compose down
``` 

## API Endpoints

### Authentication

Generate JWT Token

```bash
POST /api/auth/token?clientCode=CLIENT_ABC

Response:

{
  "token": "jwt-token-here"
}

Invalid client code → 401 Unauthorized
```

## Locales

Create a Locale

```bash
POST /api/locales?code=en
Authorization: Bearer <JWT>
```

Get a Locale by ID

```bash
GET /api/locales/{id}
Authorization: Bearer <JWT>
```

Get All Locales
```bash
GET /api/locales
Authorization: Bearer <JWT>
```

## Tags

Create a Tag

```bash
POST /api/tags?name=ui
Authorization: Bearer <JWT>
```

Get a Tag by ID
```bash
GET /api/tags/{id}
Authorization: Bearer <JWT>
```

Get All Tags
```bash
GET /api/tags
Authorization: Bearer <JWT>
```
## Translations

Create Translation
```bash
POST /api/translations
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "translationKey": "login.title",
  "content": "Login",
  "localeCode": "en",
  "tags": ["ui"]
}
```

Update Translation
```bash
PUT /api/translations/{id}
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "translationKey": "login.title",
  "content": "Sign In",
  "localeCode": "en",
  "tags": ["ui"]
}
```

Get Translation by ID
```bash
GET /api/translations/{id}
Authorization: Bearer <JWT>
```

## Search Translations

```bash
GET /api/translations/search?key=login.title&locale=en
GET /api/translations/search?content=login
GET /api/translations/search?tag=ui
Authorization: Bearer <JWT>
```

## Export Translations

```bash
GET /api/translations/export
Authorization: Bearer <JWT>
```

## Test Data

Load Test Data
```bash
POST /api/test-data/load?force=true
Authorization: Bearer <JWT>


force=true clears existing translations before loading.
```

## JWT Protection

```bash
Endpoints (except /api/auth/token) require a JWT token in Authorization header:

Authorization: Bearer <token>


Tokens are generated via /api/auth/token for valid client codes: CLIENT_ABC, CLIENT_XYZ.

Invalid or missing tokens return 401 Unauthorized.
```

## Testing

Unit tests cover controllers, services, and repositories.
```bash
Run tests using Maven:

./mvnw test
```

All controller endpoints are tested using JUnit 5 and Mockito.

## Docker

Docker Compose file is configured to:

Build Spring Boot app as a Docker container

Expose port 8080

Initialize PostgreSQL database (configurable via application.yml)

Commands:
```bash
docker-compose up --build   # Start container
docker-compose down         # Stop container
```
