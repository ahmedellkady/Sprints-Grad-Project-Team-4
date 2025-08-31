# Hospital Management System API

##  Overview

The Hospital Management System is a secure, modular, and testable RESTful API built with Spring Boot. It is designed to manage patient-doctor interactions, appointments, medicine inventory, and administrative tasks within a hospital environment. The system supports multi-role user authentication (Admin, Doctor, Patient, Pharmacy) with role-based access control using JSON Web Tokens (JWT).

---

## Code Structure

The project follows a standard layered architecture to ensure a clear separation of concerns, making the codebase clean, scalable, and easy to maintain.

- **`config`**: Contains application-wide configuration beans, such as `ApplicationConfig` for security providers and `MapperConfig` for ModelMapper.

- **`controller`**: The API layer. These classes handle incoming HTTP requests, validate request bodies (DTOs), and delegate business logic to the service layer.

- **`dto`**: Data Transfer Objects are used as the API's public contract for requests and responses, separating the API from the internal domain model.

- **`exception`**: Home to custom exception classes (e.g., `ResourceNotFoundException`) and the `GlobalExceptionHandler`, which uses `@ControllerAdvice` to provide consistent JSON error responses.

- **`mapper`**: Contains classes responsible for mapping between JPA entities and DTOs, typically using a library like ModelMapper.

- **`model`**: The domain layer, containing all JPA entities (e.g., `User`, `Appointment`, `Medicine`) and enums that define the database schema.

- **`repository`**: The data access layer. These interfaces extend Spring Data JPA's `JpaRepository` to handle all database operations.

- **`security`**: Contains all Spring Security and JWT-related infrastructure, including the `SecurityConfig` (for HTTP rules), the `JwtAuthFilter`, and the `JwtService` implementation.

- **`service`**: The business logic layer. Interfaces define the application's core functionalities, and their implementations contain the business rules and orchestrate data flow.

```
src/
 ├── main/
 │   ├── java/
 │   │   └── com/team4/hospital_system/
 │   │       ├── HospitalSystemApplication.java
 │   │       ├── config/
 │   │       │   ├── ApplicationConfig.java
 │   │       │   ├── ModelMapperConfig.java
 │   │       │   └── MyControllerAdvice.java
 │   │       ├── controller/
 │   │       │   ├── AppointmentController.java
 │   │       │   ├── AuthController.java
 │   │       │   ├── CartController.java
 │   │       │   ├── DoctorController.java
 │   │       │   ├── FollowUpController.java
 │   │       │   ├── MedicineController.java
 │   │       │   ├── MessagingController.java
 │   │       │   ├── PrescriptionController.java
 │   │       │   └── ScheduleController.java
 │   │       ├── dto/
 │   │       │   ├── request/
 │   │       │   │   ├── AcceptAppointmentDto.java
 │   │       │   │   ├── AddPrescriptionItemDto.java
 │   │       │   │   ├── AppointmentRequestDto.java
 │   │       │   │   ├── CartItemRequest.java
 │   │       │   │   ├── CheckoutDto.java
 │   │       │   │   ├── CreateMedicineDto.java
 │   │       │   │   ├── CreatePrescriptionDto.java
 │   │       │   │   ├── FollowUpRequestDto.java
 │   │       │   │   ├── LoginDto.java
 │   │       │   │   ├── MedicineSearchDto.java
 │   │       │   │   ├── RefreshTokenDto.java
 │   │       │   │   ├── RegisterDoctorDto.java
 │   │       │   │   ├── RegisterPatientDto.java
 │   │       │   │   ├── RegisterPharmacyDto.java
 │   │       │   │   ├── RescheduleCancelDto.java
 │   │       │   │   ├── SendMessageDto.java
 │   │       │   │   └── UpdateMedicineDto.java
 │   │       │   └── response/
 │   │       │       ├── AppointmentDto.java
 │   │       │       ├── AppointmentResponseDto.java
 │   │       │       ├── AuthResponseDto.java
 │   │       │       ├── CartDto.java
 │   │       │       ├── CartItemDto.java
 │   │       │       ├── ConversationSummaryDto.java
 │   │       │       ├── DoctorScheduleDto.java
 │   │       │       ├── ErrorResponseDto.java
 │   │       │       ├── FollowUpResponseDto.java
 │   │       │       ├── MedicineDto.java
 │   │       │       ├── MessageDto.java
 │   │       │       ├── OrderDto.java
 │   │       │       ├── OrderItemDto.java
 │   │       │       ├── PatientHistoryDto.java
 │   │       │       ├── PrescriptionDto.java
 │   │       │       └── UserDto.java

 │   │       ├── exception/
 │   │       │   ├── BadRequestException.java
 │   │       │   ├── DuplicateResourceException.java
 │   │       │   ├── GlobalExceptionHandler.java
 │   │       │   └── ResourceNotFoundException.java
 │   │       ├── mapper/
 │   │       │   └── mapper.java
 │   │       ├── model/
 │   │       │   ├── Appointment.java
 │   │       │   ├── Doctor.java
 │   │       │   ├── Medicine.java
 │   │       │   ├── Message.java
 │   │       │   ├── Order.java
 │   │       │   ├── OrderItem.java
 │   │       │   ├── Patient.java
 │   │       │   ├── Pharmacy.java
 │   │       │   ├── Prescription.java
 │   │       │   ├── PrescriptionItem.java
 │   │       │   ├── User.java
 │   │       │   ├── UserPrinciple.java
 │   │       │   └── enums/
 │   │       ├── repository/
 │   │       │   ├── AppointmentRepository.java
 │   │       │   ├── DoctorRepository.java
 │   │       │   ├── MedicineRepository.java
 │   │       │   ├── MessageRepository.java
 │   │       │   ├── OrderRepository.java
 │   │       │   ├── PatientRepository.java
 │   │       │   ├── PharmacyRepository.java
 │   │       │   ├── PrescriptionRepository.java
 │   │       │   └── UserRepository.java
 │   │       ├── security/
 │   │       │   ├── JwtAuthFilter.java
 │   │       │   └── JwtServiceImpl.java
 │   │       └── service/
 │   │           ├── impl/
 │   │           │   ├── AppointmentServiceImpl.java
 │   │           │   ├── AuthServiceImpl.java
 │   │           │   ├── CartServiceImpl.java
 │   │           │   ├── DoctorServiceImpl.java
 │   │           │   ├── FollowUpServiceImpl.java
 │   │           │   ├── MedicineServiceImpl.java
 │   │           │   ├── MessagingServiceImpl.java
 │   │           │   ├── MyUserDetailsService.java
 │   │           │   ├── OrderServiceImpl.java
 │   │           │   ├── PrescriptionServiceImpl.java
 │   │           │   └── ScheduleServiceImpl.java
 │   │           ├── AdminService.java
 │   │           ├── AppointmentService.java
 │   │           ├── AuthService.java
 │   │           ├── CartService.java
 │   │           ├── DoctorService.java
 │   │           ├── FollowUpService.java
 │   │           ├── JwtService.java
 │   │           ├── MedicineService.java
 │   │           ├── MessagingService.java
 │   │           ├── OrderService.java
 │   │           ├── PatientHistoryService.java
 │   │           ├── PrescriptionService.java
 │   │           └── ScheduleService.java

 │   └── resources/
 │       └── application.properties
 └── test/
     └── java/com/team4/hospital_system/
         ├── controller/
         │   ├── AppointmentAndScheduleControllerTest.java
         │   ├── FollowUpControllerTest.java
         │   └── MessagingControllerTest.java
         ├── repository/
         │   └── MessageRepositoryIntegrationTest.java
         └── service/
             ├── impl/
             │   ├── MessagingServiceImplTest.java
             │   └── MessagingServiceIntegrationTest.java
             ├── AppointmentServiceTest.java
             ├── DoctorServiceTest.java
             ├── FollowUpServiceTest.java
             ├── HospitalSystemApplicationTests.java
             └── OrderServiceTest.java


```


---

##  Tech Stack
- **Spring Boot** (REST API framework)
- **Spring Security + JWT** (authentication & authorization)
- **Spring Data JPA (Hibernate)** (database access)
- **ModelMapper** (DTO ↔ Entity mapping)
- **JUnit 5 + Mockito** (testing)
- **MySQL** (primary database)
- **H2** (in-memory test database)
---

##  Configuration

The application's core behavior, including security, dependency injection, and error handling, is managed through dedicated configuration classes.

* **`ApplicationConfig.java`**: This is the primary configuration class for Spring Security.
    * **`SecurityFilterChain`**: Defines the HTTP security rules for all API endpoints. It specifies which paths are public (e.g., `/auth/**`) and which require authentication.
    * **`AuthenticationProvider`**: Configures the main authentication mechanism (`DaoAuthenticationProvider`) by linking the `UserDetailsService` (to find users) with the `PasswordEncoder` (to verify passwords).
    * **`AuthenticationManager`**: The core Spring bean that processes authentication requests, used by the `AuthService` during login.
    * **`PasswordEncoder`**: Provides a `BCryptPasswordEncoder` bean for securely hashing and verifying user passwords.

* **`ModelMapperConfig.java`**: Provides a singleton `ModelMapper` bean to the Spring application context. This allows the `ModelMapper` instance to be injected into any component (like the `mapper` class) to handle the conversion between entity and DTO objects.

* **`MyControllerAdvice.java`**: A global exception handler that intercepts exceptions thrown from any controller.
    * It ensures that all API errors are returned as a consistent, structured JSON object (`ErrorResponseDto`).
    * It handles specific exceptions like `MethodArgumentNotValidException` (for DTO validation failures, returning a `400 Bad Request`) and `DuplicateResourceException` (returning a `409 Conflict`).

---

## Business Logic (Service Layer)

The service layer contains the core business logic of the application. It orchestrates data flow between the controllers and repositories and enforces all business rules.

### Authentication & Users

* **`AuthService`**: Handles all user registration and authentication logic.
    * **Rules**: Ensures new users have a unique email. Hashes passwords using BCrypt before saving. Authenticates credentials against the database and, on success, generates JWTs by calling the `JwtService`.
* **`AdminService`**: Manages administrative tasks related to users.
    * **Rules**: Provides functionality to list users by role and delete any user account from the system. Access is restricted to `ADMIN` users.
* **`JwtService`**: A specialized service for handling all JWT operations.
    * **Rules**: Generates short-lived access tokens and long-lived refresh tokens. It is also responsible for validating tokens and extracting user details (email) from them.

### Appointments & Scheduling

* **`AppointmentService`**: Manages the lifecycle of appointments.
    * **Rules**: Handles the creation of new appointments, ensuring there are no scheduling conflicts with a doctor's existing appointments.
* **`ScheduleService`**: Provides read-only access to doctor schedules.
    * **Rules**: Fetches a doctor's upcoming schedule, including their working days and already booked appointment slots.
* **`FollowUpService`**: Manages follow-up consultations.
    * **Rules**: Allows a doctor to create follow-up records linked to a patient and previous appointments.

### Medical Records

* **`DoctorService`**: Governs actions specific to doctors.
    * **Rules**: Primarily responsible for fetching the consolidated medical history of a specific patient for review.
* **`PrescriptionService`**: Manages the creation and retrieval of medical prescriptions.
    * **Rules**: Allows a doctor to create a prescription for a patient and add multiple medicines to it. Patients can view their own prescriptions.

### Pharmacy & Orders

* **`MedicineService`**: Handles the inventory management for pharmacies.
    * **Rules**: Provides full CRUD (Create, Read, Update, Delete) operations for a pharmacy to manage its own medicine stock. Also supports public searching and listing of medicines.
* **`CartService`**: Manages the state of a patient's shopping cart.
    * **Rules**: Allows a patient to add, remove, and view items in their cart for a specific pharmacy.
* **`OrderService`**: Handles the final checkout process.
    * **Rules**: Converts a patient's cart into a formal `Order`, calculates the total price, and updates medicine stock levels accordingly.

### Communication

* **`MessagingService`**: Manages direct, non-real-time communication between patients and doctors.
    * **Rules**: Allows users to send messages, view conversation histories, and mark messages as read. Ensures a user can only see their own conversations.

## API Endpoints

The API is divided into logical sections based on resources. All protected endpoints require a valid JWT `accessToken` to be sent in the `Authorization` header as a Bearer Token.

### Authentication (`/auth`)

These endpoints are public and handle user registration and login.

| Method | Path                           | Role Required | Description                                       |
| :----- | :----------------------------- | :------------ | :------------------------------------------------ |
| `POST` | `/auth/register/patient`       | Public        | Registers a new patient account.                  |
| `POST` | `/auth/register/doctor`        | Public        | Registers a new doctor account.                   |
| `POST` | `/auth/register/pharmacy`      | Public        | Registers a new pharmacy account.                 |
| `POST` | `/auth/login`                  | Public        | Authenticates a user and returns JWTs.            |

### Appointments (`/api`)

| Method | Path                                   | Role Required | Description                                       |
| :----- | :------------------------------------- | :------------ | :------------------------------------------------ |
| `POST` | `/api/patients/{patientId}/appointments` | `PATIENT`     | Books a new appointment with a doctor.            |
| `GET`  | `/api/patients/{patientId}/appointments` | `PATIENT`     | Lists all appointments for a specific patient.    |
| `GET`  | `/api/doctors/{doctorId}/appointments`   | `DOCTOR`      | Lists all appointments for a specific doctor.     |

### Doctor Actions (`/api/doctors`)

| Method | Path                                          | Role Required | Description                                       |
| :----- | :-------------------------------------------- | :------------ | :------------------------------------------------ |
| `GET`  | `/api/doctors/{doctorId}/patients/{patientId}/history` | `DOCTOR`      | Retrieves the medical history for a patient.      |

### Medicine Inventory (`/api/medicines`)

| Method | Path                                          | Role Required | Description                                       |
| :----- | :-------------------------------------------- | :------------ | :------------------------------------------------ |
| `POST` | `/api/medicines/pharmacies/{pharmacyId}`      | `PHARMACY`    | Adds a new medicine to the pharmacy's inventory.  |
| `PUT`  | `/api/medicines/pharmacies/{pharmacyId}/{medicineId}` | `PHARMACY`    | Updates an existing medicine's details.           |
| `DELETE`| `/api/medicines/pharmacies/{pharmacyId}/{medicineId}`| `PHARMACY`    | Deletes a medicine from the inventory.            |
| `GET`  | `/api/medicines/{id}`                         | Authenticated | Retrieves a single medicine by its ID.            |
| `GET`  | `/api/medicines`                              | Authenticated | Lists all medicines with pagination.              |
| `POST` | `/api/medicines/search`                       | Authenticated | Searches for medicines based on criteria.         |

### Prescriptions (`/api/prescriptions`)

| Method | Path                                                  | Role Required | Description                                       |
| :----- | :---------------------------------------------------- | :------------ | :------------------------------------------------ |
| `POST` | `/api/prescriptions/doctors/{doctorId}/patients/{patientId}` | `DOCTOR`      | Creates a new prescription for a patient.         |
| `POST` | `/api/prescriptions/doctors/{doctorId}/{prescriptionId}/items` | `DOCTOR`      | Adds a medicine to an existing prescription.      |
| `GET`  | `/api/prescriptions/patients/{patientId}`             | `PATIENT`     | Lists all prescriptions for a specific patient.   |
| `GET`  | `/api/prescriptions/{id}`                             | Authenticated | Retrieves a single prescription by its ID.        |

### Messaging (`/api/messages`)

| Method | Path                                 | Role Required       | Description                                       |
| :----- | :----------------------------------- | :------------------ | :------------------------------------------------ |
| `POST` | `/api/messages/send`                 | `DOCTOR` or `PATIENT` | Sends a message to another user.                  |
| `GET`  | `/api/messages/conversations`        | `DOCTOR` or `PATIENT` | Lists all conversation summaries for the user.    |
| `GET`  | `/api/messages/conversations/{partnerId}` | `DOCTOR` or `PATIENT` | Retrieves the full message history with another user. |
| `PATCH`| `/api/messages/{messageId}/read`     | `DOCTOR` or `PATIENT` | Marks a specific message as read.                 |

### Cart & Orders (`/api/cart`)

| Method | Path                           | Role Required | Description                                       |
| :----- | :----------------------------- | :------------ | :------------------------------------------------ |
| `GET`  | `/api/cart/{patientId}/{pharmacyId}`| `PATIENT`     | Gets the current state of a patient's cart.       |
| `POST` | `/api/cart/{patientId}/add`    | `PATIENT`     | Adds a medicine to the patient's cart.            |
| `DELETE`| `/api/cart/{patientId}/remove` | `PATIENT`     | Removes a medicine from the patient's cart.       |
| `DELETE`| `/api/cart/{patientId}/clear`  | `PATIENT`     | Clears all items from the patient's cart.         |
| `POST` | `/api/cart/{patientId}/checkout`| `PATIENT`     | Converts the cart into a formal order.            |
| `GET`  | `/api/cart/{patientId}/orders` | `PATIENT`     | Lists all past orders for a patient.              |
| `GET`  | `/api/cart/orders/{orderId}`   | Authenticated | Retrieves a single order by its ID.               |

### Schedules & Follow-ups

*Note: There seems to be some overlap between these controllers. This documentation reflects the current code.*

| Method | Path                                                  | Role Required | Description                                       |
| :----- | :---------------------------------------------------- | :------------ | :------------------------------------------------ |
| `GET`  | `/api/v1/schedules/doctors/{doctorId}`                | Authenticated | Views a doctor's schedule and booked slots.       |
| `POST` | `/api/follow-ups/doctors/{doctorId}`                  | `DOCTOR`      | Creates a new follow-up record for a patient.     |
| `GET`  | `/api/follow-ups/patients/{patientId}`                | `PATIENT`     | Retrieves all follow-ups for a patient.           |

---

##  Security

The application is secured using Spring Security with a stateless, token-based authentication strategy using JSON Web Tokens (JWT).

* **`JwtServiceImpl.java`**: This class is the core component for handling JWTs.
    * It is responsible for generating a secure, signed `accessToken` and `refreshToken` upon successful user login.
    * It also contains the logic to validate incoming tokens by checking their signature and expiration date, and to extract user information (the email) from the token's claims.
    * **Note**: For security, the `secretKey` is generated dynamically at startup and is not hardcoded.

* **`JwtAuthFilter.java`**: This is a custom filter that runs once for every HTTP request.
    * Its primary role is to intercept incoming requests, check for an `Authorization: Bearer <token>` header, and validate the token using the `JwtService`.
    * If the token is valid, it fetches the user's details, creates an authentication principal, and sets it in the `SecurityContextHolder`, effectively authenticating the user for the duration of the request.
    * The filter is designed to bypass this logic if no token is present, allowing public endpoints (like `/auth/**`) to be accessed.

* **`ApplicationConfig.java` / `SecurityConfig.java`**: These configuration classes tie the security components together.
    * They define the `SecurityFilterChain`, which establishes the security rules for all API endpoints (e.g., which paths are public and which require authentication).
    * They register the `JwtAuthFilter` into the Spring Security filter chain, ensuring it runs before the main authorization checks.

---

##  Testing Strategy

The project maintains a high level of code quality and reliability through a comprehensive testing strategy that includes unit, integration, and controller tests.

### Unit Tests (`@ExtendWith(MockitoExtension.class)`)

Unit tests are used to verify the business logic within individual service classes in isolation, without involving the database or other external components.

-   **Frameworks**: JUnit 5 & Mockito.
-   **Location**: `src/test/java/...`
-   **Methodology**:
    -   Dependencies such as repositories are mocked using the `@Mock` annotation.
    -   The service class being tested is instantiated with these mocks using `@InjectMocks`.
    -   The `when(...).thenReturn(...)` syntax is used to define the behavior of mocked dependencies.
    -   Each test method focuses on a single business rule, covering both success scenarios and failure cases. For example, `MessagingServiceImplTest` verifies that `sendMessage()` succeeds with valid users and correctly throws a `ResourceNotFoundException` if the sender or receiver does not exist.

### Integration Tests (`@SpringBootTest`)

Integration tests are designed to verify the service layer's interaction with the database and other components within a fully loaded application context.

-   **Frameworks**: Spring Boot Test, JUnit 5.
-   **Methodology**:
    -   The full Spring application context is loaded using `@SpringBootTest`.
    -   Tests are run against a dedicated test database, configured via `@ActiveProfiles("test")`.
    -   The `@Transactional` annotation is used on the test class to ensure that each test runs in its own transaction, which is automatically rolled back at the end. This prevents tests from interfering with each other and keeps the database clean.
    -   The `MessagingServiceIntegrationTest` demonstrates this by saving real `User` and `Message` entities to the database and then asserting that the service logic (e.g., retrieving a conversation) correctly fetches and processes the persisted data.

### Controller / Web Layer Tests (`@WebMvcTest`)

These tests focus exclusively on the API layer, ensuring that controllers handle HTTP requests and responses correctly without loading the entire application context.

-   **Frameworks**: Spring Boot Test, MockMvc, JUnit 5.
-   **Methodology**:
    -   Only the web layer is loaded using `@WebMvcTest(YourController.class)`, which is much faster than `@SpringBootTest`.
    -   Service layer dependencies are mocked using the `@MockBean` annotation.
    -   `MockMvc` is used to perform fake HTTP requests to the controller endpoints.
    -   Assertions are made on the HTTP response, such as the status code (`.andExpect(status().isOk())`) and the JSON response body (`.andExpect(jsonPath("$.id").value(...))`).
    -   The `MessagingControllerTest` shows how to test protected endpoints by mocking an authenticated user with `@WithMockUser` and the `SecurityMockMvcRequestPostProcessors`.

---



# Docker Quick Start

This  explains how to run **any Spring Boot project** with **MySQL** using the provided **Dockerfile** and **docker-compose.yml**. It’s optimized for speed (multi‑stage build), small image size, and minimal setup.

## What’s included
- **Dockerfile** — Builds your app JAR with Maven, then runs it on Temurin JRE 17.
- **docker-compose.yml** — Starts **MySQL 8.0** and your app; waits for DB health before starting the app.

## Requirements
- Docker Desktop (Win/macOS) or Docker Engine (Linux)
- (Windows) WSL2 enabled

> Put `Dockerfile` and `docker-compose.yml` in the **project root** (same folder as `pom.xml`).

---

## 1‑Minute Quick Start

```bash
# From your project root
docker compose up --build

# Follow app logs
docker compose logs -f app
```

Open your app at **http://localhost:8080**

**Stop**: `docker compose down`  
**Restart after code changes**: `docker compose up -d --build`

---

## How it works

### Dockerfile (multi-stage)
1. **Build stage** uses Maven to compile and package the JAR (cache-enabled to speed up rebuilds).
2. **Runtime stage** copies the JAR into a small JRE image and runs it on port **8080**.

### docker-compose.yml
- **db** (MySQL 8.0) publishes **3307 → 3306**, creates the DB and user, and exposes a named volume `mysql_data`.
- **app** builds your project image and sets Spring env vars:
  - `SPRING_PROFILES_ACTIVE=docker`
  - `SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/app_db`
  - `SPRING_DATASOURCE_USERNAME=app_user`
  - `SPRING_DATASOURCE_PASSWORD=app_pass`
  - `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- `depends_on` waits until DB is **healthy** before starting the app.

> **Note:** These environment variables override values in `application.properties`/`application.yml` at runtime.

---

## Database Details

**Host (from your machine):**
- Host: `127.0.0.1`
- Port: `3307`
- DB: `app_db`
- User/Pass: `app_user` / `app_pass`

**Inside the Docker network (from the app container):**
- Host: `db`
- Port: `3306`

**Test connection with MySQL CLI (host):**
```bash
mysql -h 127.0.0.1 -P 3307 -u app_user -p
# then: SHOW DATABASES; USE app_db;
```

---

## Optional: Run App Locally (DB in Docker)

1. Start only the database:
   ```bash
   docker compose up -d db
   ```
2. Point Spring to `127.0.0.1:3307` in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://127.0.0.1:3307/app_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=app_user
   spring.datasource.password=app_pass
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Run your app from IDE/CLI.

---

## Common Issues & Fixes

- **Docker daemon not running**  
  Start Docker Desktop (Windows/macOS) or system service (Linux).

- **Port already in use (8080 or 3307)**  
  Change the host ports in `docker-compose.yml` accordingly.

- **`Access denied` / wrong JDBC host**  
  Inside Docker, use `jdbc:mysql://**db**:3306/...` *not* `localhost`.

- **DB not ready**  
  Check: `docker compose ps` and `docker logs mysql-db`. The app starts only after MySQL healthcheck passes.

- **Schema issues**  
  Adjust `SPRING_JPA_HIBERNATE_DDL_AUTO` (e.g., `validate`, `update`, `create`, `create-drop`) as appropriate.

- **Reset the database (dev only!)**
  ```bash
  docker compose down -v   # removes containers and volumes, erasing DB data
  docker compose up -d --build
  ```

---

##  Database Schema
![ERD](<erd.jpg>)