# Java Spring Playground

Учебный проект для практики Java и Spring Boot на реальных небольших задачах.

## Current exercise

Spring Boot application that:

- sends a `GET` request to the CloudBooking TEST API;
- receives a JSON response;
- maps the response to a Java object;
- reads the `status` field;
- prints the result to standard output.

Example output:

```text
CloudBooking health status: UP

Tech stack
- Java 25
- Spring Boot 4.1.1
- Gradle
- Spring RestClient

Project structure
src/main/java
├── dev/timur/playground/JavaSpringPlaygroundApplication.java
└── dev/timur/playground/health/
    ├── HealthStatusResponse.java
    └── HealthStatusService.java

Learning notes
Detailed Java and Spring learning notes are stored in:
docs/java-spring-learning-notes.md

Run
./gradlew bootRun