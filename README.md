# Spring Boot 3 JWT Authentication Project

This is a production-grade Spring Boot application that demonstrates a secure REST API with JWT-based authentication and role-based access control.

## Features

*   **User Registration:** New users can register with their details.
*   **JWT Authentication:** Secure login endpoint that returns a JSON Web Token (JWT) upon successful authentication.
*   **Role-Based Access Control (RBAC):** Endpoints are secured based on user roles (e.g., `USER`, `ADMIN`).
*   **Password Encryption:** User passwords are securely hashed using Argon2.
*   **Custom Exception Handling:** Graceful handling of exceptions with meaningful error responses.
*   **Input Validation:** Validation of request bodies to ensure data integrity.
*   **Unit and Integration Tests:** Comprehensive test suite for controllers and security configuration.

## Technologies Used

*   **Java 21**
*   **Spring Boot 3.5.0**
    *   Spring Web
    *   Spring Security
    *   Spring Data JPA
    *   Spring Boot Starter Validation
*   **Lombok:** To reduce boilerplate code.
*   **JJWT (Java JWT):** For creating and parsing JSON Web Tokens.
*   **Oracle Database:** As the primary data store.
*   **Gradle:** For dependency management and building the project.
*   **JUnit 5 & Mockito:** For unit and integration testing.

## Getting Started

### Prerequisites

*   Java 21 or later
*   Gradle
*   An Oracle Database instance

### Configuration

1.  Open `src/main/resources/application.properties`.
2.  Update the `spring.datasource.*` properties to point to your Oracle Database instance.

### Building the Project

```bash
./gradlew build
```

### Running the Project

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`.

## API Endpoints

| Method | Endpoint      | Description                                         | Access Control      |
| :----- | :------------ | :-------------------------------------------------- | :------------------ |
| `POST` | `/login`      | Authenticate a user and receive a JWT.              | Public              |
| `POST` | `/reg`        | Register a new user.                                | Public              |
| `GET`  | `/home`       | A sample endpoint for authenticated users.          | Authenticated Users |
| `GET`  | `/settings`   | A sample endpoint accessible only to administrators.| `ADMIN` Role        |

## Security Implementation

This project uses Spring Security to protect its endpoints. Here's a breakdown of the security flow:

1.  **Public Endpoints:** `/login` and `/reg` are public and can be accessed without authentication.
2.  **Authentication:** When a user sends a `POST` request to `/login` with valid credentials, the `PersonAuthenticationService` validates them.
3.  **JWT Generation:** Upon successful authentication, the `JWTUtility` service generates a JWT containing the user's email and roles.
4.  **Authorization:** For subsequent requests to protected endpoints, the client must include the JWT in the `Authorization` header as a Bearer token.
5.  **JWT Filter:** The `JwtAuthenticationFilter` intercepts each request, validates the JWT, and sets the user's authentication details in the `SecurityContextHolder`.
6.  **Access Control:** Spring Security's authorization mechanism then checks if the authenticated user has the required role to access the requested endpoint.

## Testing

The project includes a suite of tests to ensure correctness and security.

*   **Controller Tests:** `LoginControllerTest` and `RegisterControllerTest` verify the behavior of the API endpoints.
*   **Security Tests:** `SecurityTest` ensures that the security configuration is working as expected, testing token validation, and endpoint access control.

To run the tests:

```bash
./gradlew test
```

