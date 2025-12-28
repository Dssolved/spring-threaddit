# Threaddit

Threaddit is a simplified Reddit-like web application built with Spring Boot. It allows users to create communities,
share posts, comment on them, and vote.

## Features

- **User Management**: Registration and authentication (Basic Auth).
- **Communities**: Create and manage communities.
- **Posts**: Create posts within communities.
- **Comments**: Comment on posts.
- **Voting**: Upvote or downvote posts and comments.
- **Roles**: Support for USER, MODERATOR, and ADMIN roles.

## Tech Stack

- **Java 21**
- **Spring Boot 4.x** (Web, Data JPA, Security)
- **PostgreSQL** (Database)
- **Flyway** (Database Migrations)
- **MapStruct** (Object Mapping)
- **Lombok** (Boilerplate reduction)
- **SpringDoc OpenAPI (Swagger)** (API Documentation)
- **Docker & Docker Compose** (Containerization)

## Getting Started

### Prerequisites

- Docker and Docker Compose
- JDK 21 (for local development)
- Gradle (optional, uses wrapper)

### Launching the Application

#### Using Docker (Recommended)

To start the entire stack (App + Database) using Docker Compose:

```bash
./gradlew bootJar
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

#### Running Locally

1. Start the PostgreSQL database:
   ```bash
   docker-compose up -d postgres
   ```

2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Database Migrations

This project uses **Flyway** for database schema management. Migrations are located in
`src/main/resources/db/migration`.

Current migrations:

1. `V1__create_users_and_roles.sql`: Initial schema for users and roles.
2. `V2__create_communities.sql`: Schema for communities.
3. `V3__create_posts.sql`: Schema for posts.
4. `V4__create_comments.sql`: Schema for comments.
5. `V5__create_votes.sql`: Schema for voting system.
6. `V6__insert_base_roles.sql`: Seeds the database with default roles (USER, ADMIN, MODERATOR).

Migrations run automatically on application startup.

## API Documentation & Postman

### Swagger UI

The interactive API documentation is available at:
`http://localhost:8080/swagger-ui.html`

### Postman Collection

To use Postman:

1. Start the application.
2. Open Postman.
3. Select **Import**.
4. Use the Link: `http://localhost:8080/v3/api-docs`.
5. This will generate a Postman collection based on the current OpenAPI specification.

*Note: All endpoints except `/api/auth/**` require Basic Authentication.*

## Architecture Description

The project follows a standard layered architecture:

- **Controller Layer**: Handles HTTP requests and responses (`com.example.threaddit.controller`).
- **Service Layer**: Contains business logic and orchestrates data flow (`com.example.threaddit.service`).
- **Repository Layer**: Manages data persistence using Spring Data JPA (`com.example.threaddit.repository`).
- **Entity Layer**: Represents database tables as JPA entities (`com.example.threaddit.entity`).
- **DTO Layer**: Data Transfer Objects for API requests and responses (`com.example.threaddit.dto`).
- **Mapper Layer**: Uses MapStruct to convert between Entities and DTOs (`com.example.threaddit.mapper`).

### Security

- Secured with **Spring Security**.
- Uses **Basic Authentication**.
- Password hashing using **BCrypt**.

### Data Mapping

- **MapStruct** is used for efficient, type-safe mapping between domain entities and DTOs, keeping the layers decoupled.
