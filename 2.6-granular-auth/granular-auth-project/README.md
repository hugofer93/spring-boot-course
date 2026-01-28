# Autorización Granular y Roles Jerárquicos

Proyecto simple para entender **roles jerárquicos** y **autorización granular** en Spring Security.

## 🎯 Objetivo

Este proyecto demuestra cómo implementar diferentes niveles de acceso basados en roles, desde acceso público hasta acceso exclusivo de administradores.

## 📋 Roles del Sistema

| Rol | Descripción | Acceso |
|-----|-------------|--------|
| **ROLE_USER** | Usuario básico | Endpoints públicos y de usuario |
| **ROLE_MODERATOR** | Moderador | Todo lo de USER + gestión de contenido |
| **ROLE_ADMIN** | Administrador | Acceso completo al sistema |

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2** · **Spring Security 6**
- **JJWT 0.12.3** (Librería para manejo de Tokens JWT)
- **JPA** · **PostgreSQL** · **Lombok**
- **Autenticación JWT** (stateless, simple para este ejemplo)

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   ├── SecurityConfig.java          # Configuración de seguridad y roles
│   └── JwtAuthenticationFilter.java # Filtro JWT personalizado
├── controller/
│   ├── HomeController.java          # Endpoint raíz (público)
│   ├── AuthController.java          # POST /api/auth/login
│   ├── PublicController.java        # Endpoints públicos
│   ├── UserController.java          # Endpoints para usuarios autenticados
│   ├── ModeratorController.java     # Endpoints para moderadores
│   └── AdminController.java         # Endpoints para administradores
├── dto/
│   ├── LoginRequest.java            # DTO para login
│   └── LoginResponse.java           # DTO para respuesta de login
└── service/
    └── JwtService.java               # Servicio para generar y validar tokens JWT
```

## 🚀 Cómo arrancar

### Opción 1: Con Docker Compose

```bash
# 1. Copiar archivo de configuración
cp .env.sample .env

# 2. Iniciar servicios
docker compose up -d --build

# 3. La API estará disponible en http://localhost:8080
```

### Opción 2: Sin Docker

1. Configurar PostgreSQL localmente
2. Actualizar `application.yml` con credenciales de BD
3. Ejecutar: `mvn spring-boot:run`

## 👥 Usuarios de Prueba

El proyecto tiene estos usuarios configurados en memoria:

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `user` | `password` | ROLE_USER |
| `moderator` | `password` | ROLE_MODERATOR |
| `admin` | `password` | ROLE_ADMIN |

## 🧪 Cómo probar

### 1. Endpoint Público (sin autenticación)

```bash
curl http://localhost:8080/api/public/info
```

**Respuesta esperada:**
```json
{
  "message": "Esta es información pública",
  "access": "No requiere autenticación"
}
```

### 2. Login y obtener token JWT

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "password"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

> **Importante:** Copia el valor del campo `token` para usarlo en los siguientes pasos.

### 3. Endpoint para Usuario Autenticado

```bash
# Reemplaza <TOKEN> con el token obtenido en el paso anterior
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer <TOKEN>"
```

**Ejemplo completo:**
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Respuesta esperada:**
```json
{
  "message": "Perfil del usuario",
  "username": "user",
  "authorities": [{"authority": "ROLE_USER"}],
  "access": "Requiere autenticación (cualquier rol)"
}
```

### 4. Endpoint para Moderador

```bash
# 1. Login como moderador
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"moderator","password":"password"}'

# 2. Usar el token para acceder al endpoint
curl -X GET http://localhost:8080/api/moderator/content \
  -H "Authorization: Bearer <TOKEN>"
```

**Respuesta esperada:**
```json
{
  "message": "Contenido que solo moderadores pueden ver",
  "username": "moderator",
  "access": "Requiere rol MODERATOR o ADMIN"
}
```

**Intenta con token de usuario básico (debe fallar):**
```bash
# Login como user y usar ese token
curl -X GET http://localhost:8080/api/moderator/content \
  -H "Authorization: Bearer <TOKEN_DE_USER>"
# Respuesta: 403 Forbidden
```

### 5. Endpoint para Administrador

```bash
# 1. Login como administrador
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# 2. Usar el token para acceder al endpoint
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <TOKEN>"
```

**Respuesta esperada:**
```json
{
  "message": "Lista de usuarios (solo administradores)",
  "username": "admin",
  "access": "Requiere rol ADMIN exclusivamente"
}
```

**Intenta con token de moderador (debe fallar):**
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <TOKEN_DE_MODERATOR>"
# Respuesta: 403 Forbidden
```

## 🔐 Conceptos Clave

### 1. Autenticación JWT

- **Stateless**: No requiere sesiones en el servidor
- **Token**: Se envía en cada petición en el header `Authorization: Bearer <token>`
- **Expiración**: Los tokens tienen tiempo de vida (24 horas por defecto)

### 2. Autorización en SecurityConfig

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/public/**").permitAll()
    .requestMatchers("/api/auth/login").permitAll()
    .requestMatchers("/api/user/**").authenticated()
    .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "ADMIN")
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
)
```

### 3. Autorización Granular con @PreAuthorize

```java
@PreAuthorize("hasRole('USER')")
@PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
```

### 4. Jerarquía de Roles

- **USER**: Acceso básico
- **MODERATOR**: Acceso a USER + gestión de contenido
- **ADMIN**: Acceso completo

## 📚 Endpoints Disponibles

| Método | Ruta | Acceso | Rol Requerido |
|--------|------|--------|---------------|
| `GET` | `/` | Público | Ninguno |
| `GET` | `/api/public/info` | Público | Ninguno |
| `POST` | `/api/auth/login` | Público | Ninguno (obtener token) |
| `GET` | `/api/user/profile` | Autenticado | Cualquier rol (requiere JWT) |
| `GET` | `/api/user/dashboard` | Autenticado | USER o superior (requiere JWT) |
| `GET` | `/api/moderator/content` | Moderador | MODERATOR o ADMIN (requiere JWT) |
| `POST` | `/api/moderator/content` | Moderador | MODERATOR o ADMIN (requiere JWT) |
| `PUT` | `/api/moderator/content/{id}` | Moderador | MODERATOR o ADMIN (requiere JWT) |
| `GET` | `/api/admin/users` | Administrador | Solo ADMIN (requiere JWT) |
| `DELETE` | `/api/admin/users/{id}` | Administrador | Solo ADMIN (requiere JWT) |
| `GET` | `/api/admin/settings` | Administrador | Solo ADMIN (requiere JWT) |

> **Nota:** Para acceder a las rutas protegidas, se debe enviar el header `Authorization: Bearer <token>`.

## 🎓 Aprendizaje

Este proyecto demuestra:

1. **Autenticación JWT**: Tokens stateless para autenticación
2. **Roles jerárquicos**: USER < MODERATOR < ADMIN
3. **Autorización granular**: Control de acceso por endpoint
4. **@PreAuthorize**: Anotaciones para control fino
5. **hasRole() vs hasAuthority()**: Diferentes formas de verificar roles
6. **Seguridad por método HTTP**: GET, POST, PUT, DELETE

## 🔄 Flujo de Autenticación

1. Usuario hace login con `POST /api/auth/login` y recibe un token JWT
2. Usuario incluye el token en el header `Authorization: Bearer <token>` en peticiones subsiguientes
3. El `JwtAuthenticationFilter` valida el token en cada petición
4. Si el token es válido, Spring Security establece la autenticación en el contexto
5. Las reglas de autorización verifican los roles del usuario

## 🔄 Próximos Pasos

- Implementar permisos más granulares (no solo roles)
- Implementar control de acceso basado en recursos (RBAC)
- Estudiar expresiones SpEL más complejas
- Aprender sobre refresh tokens
