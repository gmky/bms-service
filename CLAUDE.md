# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BMS Service is a Spring Boot 4.x microservice for Business Management System. It provides access control client APIs secured with OAuth2/JWT and backed by PostgreSQL with Redis caching.

- **Package**: `io.kalenz.bms`
- **Java**: 21
- **Build**: Maven with wrapper (`./mvnw`)

## Development Commands

```bash
./mvnw clean compile              # Compile the project
./mvnw test                       # Run all tests
./mvnw test -Dtest=ClassName      # Run specific test class
./mvnw test -Dtest=ClassName#methodName  # Run specific test method
./mvnw spring-boot:run            # Run application locally
./mvnw package                   # Build JAR
```

## Stack

Spring Boot 4.0.8 + Spring Security (OAuth2 Resource Server) + Spring Data JPA + Redis Cache + Liquibase + MapStruct + Micrometer Tracing + **Jackson 3.x** (`tools.jackson.databind`, not `com.fasterxml.jackson`).

## Architecture

**Project Structure** — single Maven module with bounded-context subdirectories:

```
src/main/java/io/kalenz/bms/
├── config/           # JacksonConfig (primary JsonMapper), CacheConfig, SecurityConfig, PropertiesConfig
├── mapper/           # EntityMapper<D, E> — MapStruct base interface
├── properties/       # AppProperties (prefix: app)
├── security/         # KeycloakAuthenticationConverter
├── util/
│   └── masking/     # @Masking annotation, JsonUtil, strategies, registry
├── access/control/  # access-control bounded context (empty scaffold)
└── approval/        # approval bounded context (empty scaffold)
```

**JacksonConfig** — produces the `@Primary` `JsonMapper` bean registered with the masking module. Do not create a separate `ObjectMapper` bean without the masking module, or `@Masking` fields will not be masked.

**Security Flow** — All endpoints require JWT authentication via Keycloak. Public paths are configurable via `app.security.public-paths` in `application.yaml` (default: `/error`, `/v3/api-docs/**`, `/swagger-ui/**`). JWT validation extracts the `sub` claim as the authentication principal.

**Generated Code** — The `openapi-generator-maven-plugin` generates API interfaces from OpenAPI specs at `src/main/resources/specs/`. Generated code uses package `io.kalenz.approval.generated.api.*`. Run `./mvnw generate-sources` to regenerate.

**Database Migrations** — Managed via Liquibase. Master changelog: `src/main/resources/db/changelog/db.changelog-master.xml`. Add migration XML files to this directory following Liquibase naming conventions.

**Configuration** — Application properties are bound via `@ConfigurationProperties` with prefix `app`. Custom properties live in `io.kalenz.bms.properties.AppProperties`. The `PropertiesConfig` class enables both `@EnableConfigurationProperties` and `@ConfigurationPropertiesScan`.

**Entity Mapping** — Use `EntityMapper<D, E>` interface (MapStruct) for DTO ↔ Entity conversions. Implementations are auto-generated at compile time.

## Masking Module — `io.kalenz.bms.util.masking`

Field-level data masking during JSON serialization. Powered by a Jackson 3.x `ValueSerializerModifier`.

### Usage

```java
public class UserDto {
    private String name;

    @Masking(strategy = MaskingStrategyType.EMAIL)
    private String email;

    @Masking(strategy = MaskingStrategyType.USERNAME)
    private String username;

    @Masking(strategy = MaskingStrategyType.PHONE)
    private String phone;

    @Masking(strategy = MaskingStrategyType.CUSTOM, keepFirst = 2, keepLast = 2)
    private String ssn;
}

// Serialization — uses the primary JsonMapper (with masking module registered)
String json = JsonUtil.toString(user);
```

### Built-in Strategies

| Strategy  | Input                  | Output                |
|----------|------------------------|-----------------------|
| `EMAIL`  | `john.doe@gmail.com`   | `j***@gmail.com`      |
| `USERNAME`| `johndoe123`           | `j********3`          |
| `PHONE`  | `+1 (555) 123-4567`   | `*******4567`         |
| `CUSTOM` | `johndoe` (keepFirst=2)| `jo***e`              |

### Custom Strategy Registration

Implement `MaskingStrategy` and register via `MaskingStrategyRegistry`:

```java
registry.register(MaskingStrategyType.CUSTOM, value -> "CUSTOM_" + value);
// Then use with @Masking(strategy = CUSTOM, keepFirst=11, keepLast=0)
```

### Null Handling

Returns empty string `""` when the input value is null or blank.

## External Dependencies

| Service    | Host            | Port |
|-----------|-----------------|------|
| PostgreSQL | localhost       | 5432 |
| Redis     | localhost       | 6379 |
| Keycloak  | localhost:8089  | —    |

Keycloak realm: `kalenz`, issuer: `http://localhost:8089/realms/kalenz`
