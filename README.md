# MaestroCoach (Backend)

A Spring Boot REST API for managing teachers, students, learning items and assignments in a music teaching environment.

This project demonstrates:
- clean layered architecture (controller → service → repository)
- consistent error handling and validation
- RESTful API design
- testable backend structure with MockMvc and unit tests

> Built as part of a backend portfolio to demonstrate real-world Java + Spring development practices.
 
> **Current state:** The project is currently in a transition from in-memory storage to a real database.
>
> - **Student** and **Assignment** modules already use PostgreSQL via Spring Data JPA
> - **Teacher** and **LearningItem** modules still use in-memory storage
> - Database migrations are managed with Flyway (see `V1–V3` scripts)
>
> This incremental approach reflects an iterative development process where persistence is introduced step by step.

---

## Features

- **Teachers**
    - Create teachers
    - List all teachers
    - Get teacher by id
    - List students of a teacher

- **Students**
    - Create students
    - Assign a teacher to a student
    - List assignments of a student (optional status filter)

- **Learning items**
    - Create learning items (with category)
    - List all learning items
    - Get learning item by id
    - Delete learning item

- **Assignments**
    - Create assignment (student ↔ learning item)
    - Mark assignment as completed

- **Validation & errors**
    - Request validation via Jakarta Validation (`@Valid`)
    - Consistent API error payloads (`ApiErrorResponse`)

---

## Tech stack

- Java 17
- Spring Boot 4.0.x (currently 4.0.3)
- Maven (wrapper included: `./mvnw`)

### Backend
- Spring Web
- Spring Data JPA (partial usage)
- Jakarta Validation

### Database
- PostgreSQL (in progress)
- Flyway (database migrations)

### Testing
- JUnit 5
- Mockito
- Spring Boot Test (MockMvc)

---

## Database (in progress)

The project uses PostgreSQL with Flyway for schema management.

Current migrations include:
- `V1` – students and assignments tables
- `V2` – teachers and student-teacher relationship
- `V3` – learning items and assignment relationships

The database layer is being introduced incrementally across services.

---

## Testing

The project includes both unit and controller tests:

- **Service tests** – business logic tested with Mockito
- **Controller tests** – endpoint behavior tested with MockMvc
- Validation and error responses are also covered

Run tests:

```bash
./mvnw test
```

---

## Quick start

### Requirements
- **Java 17** installed
- (Optional) Maven is **not required** because the project includes the Maven Wrapper

### Run locally

```bash
# from the repository root
./mvnw spring-boot:run
```

The API will be available at:

- `http://localhost:8080`
- Health check: `GET http://localhost:8080/api/health`
- Actuator: `GET http://localhost:8080/actuator`

### Run tests

```bash
./mvnw test
```

### Build a jar

```bash
./mvnw clean package
# then run the produced jar:
java -jar target/*.jar
```

---

## API

Base path: **`/api`**

### Health

- `GET /api/health`

Response example:

```json
{
  "status": "UP",
  "appName": "MaestroCoach"
}
```

---

### Teachers

- `POST /api/teachers`
- `GET /api/teachers`
- `GET /api/teachers/{teacherId}`
- `GET /api/teachers/{teacherId}/students`

Create teacher (request):

```json
{
  "fullName": "Ada Lovelace",
  "email": "ada@example.com"
}
```

Create teacher (response):

```json
{
  "id": "3b2f3c46-8c4a-4f1e-a6c2-9c6f8f5a20b1",
  "fullName": "Ada Lovelace",
  "email": "ada@example.com"
}
```

---

### Students

- `POST /api/students`
- `POST /api/students/{studentId}/assign-teacher/{teacherId}`
- `GET /api/students/{studentId}/assignments`
    - Optional query param: `?status=ASSIGNED|COMPLETED`

Create student (request):

```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "instrument": "piano"
}
```

Create student (response):

```json
{
  "id": "c6d05d7e-9f83-4b01-8b35-bba2d7d0b0f2",
  "fullName": "John Doe",
  "email": "john@example.com",
  "instrument": "piano",
  "teacherId": null
}
```

Assign teacher:

```bash
curl -X POST "http://localhost:8080/api/students/<studentId>/assign-teacher/<teacherId>"
```

List assignments of a student:

```bash
curl "http://localhost:8080/api/students/<studentId>/assignments"
curl "http://localhost:8080/api/students/<studentId>/assignments?status=COMPLETED"
```

---

### Learning items

- `POST /api/learning-items`
- `GET /api/learning-items`
- `GET /api/learning-items/{learningItemId}`
- `DELETE /api/learning-items/{learningItemId}`

Learning categories:

- `SOLFEGE`
- `MUSIC_THEORY`
- `MUSIC_HISTORY`
- `INSTRUMENT_PRACTICE`

Create learning item (request):

```json
{
  "title": "Major scales",
  "category": "INSTRUMENT_PRACTICE",
  "description": "Two octaves, hands together, 60 bpm."
}
```

---

### Assignments

- `POST /api/assignments`
- `POST /api/assignments/{assignmentId}/complete`

Assignment statuses:

- `ASSIGNED`
- `COMPLETED`

Create assignment (request):

```json
{
  "studentId": "c6d05d7e-9f83-4b01-8b35-bba2d7d0b0f2",
  "learningItemId": "b6e2c0cf-4dd1-4016-9c7e-8d0a4c2d3a11"
}
```

Complete an assignment:

```bash
curl -X POST "http://localhost:8080/api/assignments/<assignmentId>/complete"
```

---

## Example workflow

1. Create a teacher
2. Create a student
3. Assign the teacher to the student
4. Create a learning item
5. Assign the learning item to the student
6. Mark the assignment as completed

This flow demonstrates how the API supports a realistic teaching scenario.

---

## Error responses

The API uses a consistent error payload:

```json
{
  "message": "Student not found",
  "status": 404,
  "path": "/api/students/<studentId>/assignments"
}
```

Validation failures return `400` with `message: "Validation failed"`.

```md
Error handling is centralized using `@RestControllerAdvice`, ensuring:
- consistent HTTP status codes
- unified error response structure
- separation of concerns between layers
```

---

## Project structure

```
src/main/java/com/maestrocoach
├── api            # REST controllers + DTOs + error handling
├── domain         # core domain model (entities + enums)
├── repository     # Spring Data JPA repositories (PostgreSQL)
├── persistence    # in-memory stores (used by some modules)
└── service        # business logic
```

---

## Architecture

The application follows a layered architecture:

- **Controller layer** – REST endpoints, request validation, response mapping
- **Service layer** – business logic and orchestration
- **Repository layer** – data access abstraction
- **Domain layer** – core entities and enums

Key design decisions:
- Service layer handles all business rules and existence checks
- Controllers are kept thin (no business logic)
- Consistent exception handling via `@RestControllerAdvice`
- Validation is handled using Jakarta Validation (`@Valid`)

---

## Roadmap

- Add a real database (PostgreSQL)
- Add API documentation
- Add basic authentication
- Improve testing coverage
- Add pagination for list endpoints

---

## Why this project?

This project was built to practice and demonstrate:
- clean backend architecture
- REST API design principles
- structured error handling and validation
- test-driven thinking

It reflects how I approach backend development in a real-world scenario.

---

## License

This repository is currently intended for portfolio and educational purposes.
