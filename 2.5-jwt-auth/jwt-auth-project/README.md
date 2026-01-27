# Spring Security — Autenticación con JWT

Proyecto de demostración para el módulo **"Autenticación con JWT"** (guía *DESARROLLO EN JAVA - SPRING BOOT*).

Este proyecto implementa una arquitectura de seguridad **Stateless** utilizando **JSON Web Tokens (JWT)**.

## 🎯 Endpoints

| Método | Ruta              | Acceso     | Descripción                        |
|--------|-------------------|------------|------------------------------------|
| `POST` | `/api/users`      | Público    | Registrar nuevo usuario            |
| `POST` | `/api/auth/login` | Público    | Autenticar y obtener Token JWT     |
| `GET`  | `/api/users`      | Protegido  | Lista de usuarios (Requiere Token) |

> **Nota:** Para acceder a las rutas protegidas, se debe enviar el header `Authorization: Bearer <token>`.

## 📦 Qué incluye

- **SecurityConfig**: Configuración Stateless con JWT, sin sesiones HTTP
- **JwtAuthenticationFilter**: Filtro personalizado que intercepta peticiones para validar tokens JWT
- **JwtService**: Servicio para generar y validar tokens JWT (firma HMAC SHA256)
- **AuthController**: Endpoint de login para obtener tokens JWT
- **UserService**: Lógica de negocio para gestión de usuarios
- **CustomUserDetailsService**: Carga de usuarios desde PostgreSQL para Spring Security
- **DTOs**: Separación de capas usando Data Transfer Objects
- **BCrypt**: Encriptación segura de contraseñas
- **Validación**: Validación de datos de entrada con Bean Validation

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2** · **Spring Security 6**
- **JJWT 0.12.3** (Librería para manejo de Tokens JWT)
- **JPA** · **PostgreSQL** · **Lombok**
- **Docker Compose**

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   ├── SecurityConfig.java              # Configuración de seguridad
│   ├── JwtAuthenticationFilter.java      # Filtro JWT personalizado
│   └── DataInitializer.java              # Inicialización de datos
├── controller/
│   ├── AuthController.java               # POST /api/auth/login
│   ├── UserController.java               # GET y POST /api/users
│   └── HomeController.java               # GET /
├── dto/
│   ├── UserRequestDTO.java               # DTO para crear usuarios
│   ├── UserResponseDTO.java              # DTO para respuestas de usuarios
│   ├── LoginRequestDTO.java               # DTO para login
│   └── LoginResponseDTO.java             # DTO para respuesta de login
├── model/
│   └── User.java                         # Entidad Usuario
├── repository/
│   └── UserRepository.java                # Repositorio JPA
├── service/
│   ├── JwtService.java                   # Servicio JWT
│   ├── CustomUserDetailsService.java     # UserDetailsService personalizado
│   └── UserService.java                  # Lógica de negocio de usuarios
└── exception/
    ├── BusinessRuleException.java        # Excepción de negocio
    └── GlobalExceptionHandler.java       # Manejador global de excepciones
```

## 🚀 Cómo arrancar

```bash
# 1. Copiar archivo de configuración
cp .env.sample .env

# 2. Iniciar servicios con Docker Compose
docker compose up -d --build

# 3. La API estará disponible en http://localhost:8080
```

El script `sql/01-initial.sql` crea la tabla de usuarios. El usuario **admin** / **admin** se crea automáticamente al iniciar la aplicación.

## 🧪 Cómo probar

### 1. Registrar un nuevo usuario (público)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ana",
    "email": "ana@test.com",
    "password": "secret123"
  }'
```

**Respuesta esperada:**
```json
{
  "id": 2,
  "username": "ana",
  "email": "ana@test.com",
  "role": "ROLE_USER",
  "enabled": true
}
```

### 2. Login y obtener token JWT (público)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

> **Importante:** Copia el valor del campo `token` para usarlo en el siguiente paso.

### 3. Acceder a endpoint protegido con JWT

```bash
# Reemplaza <TOKEN> con el token obtenido en el paso anterior
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <TOKEN>"
```

**Ejemplo completo:**
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzODk2NzIwMCwiZXhwIjoxNjM5MDUzNjAwfQ..."
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "role": "ROLE_ADMIN",
    "enabled": true
  },
  {
    "id": 2,
    "username": "ana",
    "email": "ana@test.com",
    "role": "ROLE_USER",
    "enabled": true
  }
]
```

## 🔐 Conceptos Clave

### JWT (JSON Web Token)
- **Stateless**: No requiere sesiones en el servidor
- **Portable**: El token contiene toda la información necesaria
- **Firmado**: Garantiza la integridad del token

### Flujo de Autenticación
1. Usuario se registra con `POST /api/users`
2. Usuario hace login con `POST /api/auth/login` y recibe un token JWT
3. Usuario incluye el token en el header `Authorization: Bearer <token>` en peticiones subsiguientes
4. El `JwtAuthenticationFilter` valida el token en cada petición
5. Si el token es válido, Spring Security establece la autenticación en el contexto

### Seguridad
- Las contraseñas se encriptan con **BCrypt** antes de guardarse
- Los tokens JWT tienen **expiración** (24 horas por defecto)
- Las rutas protegidas requieren un token válido
- Los DTOs aseguran que **nunca se exponga la contraseña** en las respuestas
