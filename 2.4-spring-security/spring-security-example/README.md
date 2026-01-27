# Spring Security Example — Seguridad avanzada

Proyecto **mínimo** de demostración para **"Seguridad avanzada en Spring Security"** (guía *DESARROLLO EN JAVA - SPRING BOOT*).

## Solo dos endpoints

| Método | Ruta          | Acceso     | Descripción        |
|--------|---------------|------------|--------------------|
| `GET`  | `/api/users`  | Autenticado| Lista de usuarios  |
| `POST` | `/api/users`  | Público    | Agregar usuario    |

No hay otras rutas. Autenticación vía **HTTP Basic** (usuario y contraseña en la petición).

## Qué incluye

- **SecurityFilterChain**: POST público, GET protegido.
- **UserDetailsService**: usuarios desde BD (`CustomUserDetailsService`).
- **PasswordEncoder**: BCrypt.
- **Usuario**: `username`, `email`, `password` (encriptada), `role`.

## Tecnologías

- Java 17 · Spring Boot 3.2 · Spring Security 6 · JPA · PostgreSQL · Lombok  
- Docker Compose (app + PostgreSQL).

## Estructura

```
src/main/java/com/example/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   └── UserController.java       # GET y POST /api/users
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   ├── CustomUserDetailsService.java
│   └── UserService.java
└── exception/
    ├── BusinessRuleException.java
    └── GlobalExceptionHandler.java
```

## Cómo arrancar

```bash
cp .env.sample .env
docker compose up -d --build
```

API en `http://localhost:8080`. El script `sql/01-initial.sql` crea la tabla e inserta **admin** / **admin**.

## Cómo probar

**1. Agregar usuario (público)**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"ana","email":"ana@test.com","password":"secret123"}'
```

**2. Listar usuarios (HTTP Basic)**

```bash
curl -u admin:admin http://localhost:8080/api/users
```

O con el usuario recién creado:

```bash
curl -u ana:secret123 http://localhost:8080/api/users
```
