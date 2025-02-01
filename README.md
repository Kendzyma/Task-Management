# Task Management Service

## Overview
The Task Management Service is responsible for handling the creation, updating, and management of tasks within the system. It provides API endpoints to create, update, delete, and query tasks.

## Features

- **Task Creation:** Allows users to create tasks with attributes such as title, description, due date, status, and associated user.
- **Task Updates:** Supports task updates, including status changes (e.g., from "TODO" to "IN_PROGRESS" or "DONE").
- **Task Search:** Provides functionality to search for tasks based on attributes like name, status, and due date.
- **Task Deletion:** Allows users to delete tasks, with checks to ensure tasks are not deleted if associated with completed work or critical processes.

## Design Decisions and Trade-offs

### Key Design Choices

- **Validation Logic:** Inline validation ensures that tasks adhere to business rules, such as prohibiting invalid status transitions or requiring a valid due date.
- **Efficient Status Updates:** Task status transitions are managed using centralized logic to prevent inconsistent state changes and minimize errors.
- **Logging with AOP:** Aspect-Oriented Programming (AOP) is used for logging key operations, providing fine-grained control over which operations are logged and their format.
- **Transactional Management:** Operations that modify task data are wrapped in `@Transactional` annotations to ensure atomicity and prevent partial updates.
- **Task Scheduling & Reminders:** Background jobs are utilized for tasks such as overdue reminders and notifications. **Spring Scheduler** or **Quartz** is used to handle these time-sensitive tasks.
- **Domain-Driven Design:** Task business logic (e.g., calculating priority, determining overdue status) is encapsulated within the task entity to promote maintainability and clarity.
- **Error Handling:** Specific exceptions such as `BadRequestException` and `ForbiddenException` provide meaningful error messages and context for the consumer.
- **DTO Mapping:** **ModelMapper** or **MapStruct** is used to convert entities into DTOs, ensuring that the API layer remains decoupled from the database layer.
- **Database Migrations with Flyway:** **Flyway** is employed for database migrations to ensure schema consistency across environments and version control of changes.

## Run Locally

```bash
mvn clean package -DskipTests
docker compose up -d