# AWS Academy Foundations API

REST API for managing students (Alumnos) and teachers (Profesores) using in-memory storage.

## Requirements

- Java 17
- Maven 3.x

## Project Structure

```
aws-project/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── uady/
│                   └── awsproject/
│                       ├── AwsProjectApplication.java
│                       ├── alumno/
│                       │   ├── model/
│                       │   │   └── Alumno.java
│                       │   ├── controller/
│                       │   │   └── AlumnoController.java
│                       │   ├── service/
│                       │   │   ├── AlumnoService.java
│                       │   │   └── AlumnoServiceImpl.java
│                       │   └── repository/
│                       │       ├── AlumnoRepository.java
│                       │       └── InMemoryAlumnoRepository.java
│                       ├── profesor/
│                       │   ├── model/
│                       │   │   └── Profesor.java
│                       │   ├── controller/
│                       │   │   └── ProfesorController.java
│                       │   ├── service/
│                       │   │   ├── ProfesorService.java
│                       │   │   └── ProfesorServiceImpl.java
│                       │   └── repository/
│                       │       ├── ProfesorRepository.java
│                       │       └── InMemoryProfesorRepository.java
│                       └── common/
│                           ├── exception/
│                           │   ├── NotFoundException.java
│                           │   ├── ApiError.java
│                           │   └── GlobalExceptionHandler.java
│                           └── util/
│                               └── IdGenerator.java
```

## Build and Run

### Using Maven Wrapper (Recommended)

```bash
# Windows
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Alumnos (Students)

- `GET /alumnos` - Get all students
- `GET /alumnos/{id}` - Get student by ID
- `POST /alumnos` - Create new student
- `PUT /alumnos/{id}` - Update student
- `DELETE /alumnos/{id}` - Delete student

### Profesores (Teachers)

- `GET /profesores` - Get all teachers
- `GET /profesores/{id}` - Get teacher by ID
- `POST /profesores` - Create new teacher
- `PUT /profesores/{id}` - Update teacher
- `DELETE /profesores/{id}` - Delete teacher

## Models

### Alumno (Student)

```json
{
  "id": 1,
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "matricula": "A001234",
  "promedio": 8.5
}
```

**Validations:**
- `nombres`: required, not blank, max 100 characters
- `apellidos`: required, not blank, max 100 characters
- `matricula`: required, not blank, unique, max 50 characters
- `promedio`: required, not null, range 0.0 - 10.0

### Profesor (Teacher)

```json
{
  "id": 1,
  "numeroEmpleado": "E001234",
  "nombres": "María",
  "apellidos": "López Sánchez",
  "horasClase": 20
}
```

**Validations:**
- `numeroEmpleado`: required, not blank, unique, max 50 characters
- `nombres`: required, not blank, max 100 characters
- `apellidos`: required, not blank, max 100 characters
- `horasClase`: required, not null, range 0 - 168 hours/week

## HTTP Status Codes

- `200 OK` - Successful GET, PUT, DELETE
- `201 Created` - Successful POST
- `400 Bad Request` - Validation errors or business rule violations
- `404 Not Found` - Entity not found
- `500 Internal Server Error` - Unexpected errors

## Error Response Format

```json
{
  "status": 404,
  "message": "Alumno with id 1 not found",
  "path": "/alumnos/1",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## Testing with cURL

### Create Alumno
```bash
curl -X POST http://localhost:8080/alumnos \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Juan",
    "apellidos": "Pérez García",
    "matricula": "A001234",
    "promedio": 8.5
  }'
```

### Get All Alumnos
```bash
curl http://localhost:8080/alumnos
```

### Get Alumno by ID
```bash
curl http://localhost:8080/alumnos/1
```

### Update Alumno
```bash
curl -X PUT http://localhost:8080/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Juan Carlos",
    "apellidos": "Pérez García",
    "matricula": "A001234",
    "promedio": 9.0
  }'
```

### Delete Alumno
```bash
curl -X DELETE http://localhost:8080/alumnos/1
```

### Create Profesor
```bash
curl -X POST http://localhost:8080/profesores \
  -H "Content-Type: application/json" \
  -d '{
    "numeroEmpleado": "E001234",
    "nombres": "María",
    "apellidos": "López Sánchez",
    "horasClase": 20
  }'
```

## Notes

- All data is stored in-memory using `ConcurrentHashMap`
- Data will be lost when the application stops
- IDs are auto-generated sequentially starting from 1
- All responses are in JSON format
- The application uses Spring Boot 3.x with Java 17

