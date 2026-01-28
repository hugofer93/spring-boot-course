# User Service

Microservicio para gestión de usuarios. Este servicio expone endpoints REST que son consumidos por otros servicios usando Feign Client.

## 🎯 Propósito

**User Service** es el servicio proveedor que gestiona la información de usuarios. Expone una API REST que será consumida por otros microservicios (como Order Service) usando Spring Cloud OpenFeign.

## 📋 Funcionalidades

- ✅ **Gestión de usuarios**: CRUD completo de usuarios
- ✅ **Service Discovery**: Se registra en Eureka para ser descubierto por otros servicios
- ✅ **API REST**: Endpoints RESTful para operaciones con usuarios
- ✅ **Datos de ejemplo**: Incluye usuarios de ejemplo al iniciar

## 🛠️ Tecnologías

- **Spring Boot 3.2**
- **Spring Cloud Netflix Eureka Client**
- **Lombok**
- **Java 17**

## ⚙️ Configuración

### application.yml

```yaml
spring:
  application:
    name: user-service

server:
  port: 8081

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### Configuración Clave

- **Puerto**: 8081
- **spring.application.name**: `user-service` (debe coincidir con el nombre en `@FeignClient`)
- **Eureka**: Se registra automáticamente en Eureka para descubrimiento de servicios

## 🚀 Ejecución

### Prerequisitos

1. **Eureka Server** debe estar corriendo en el puerto 8761

### Opción 1: Maven

```bash
mvn clean package
java -jar target/user-service-1.0.0.jar
```

### Opción 2: Docker

```bash
docker build -t user-service .
docker run -p 8081:8081 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ \
  user-service
```

### Opción 3: Docker Compose

```bash
# Desde el directorio raíz del proyecto
docker-compose up user-service
```

## 📡 Endpoints API

### Base URL

```
http://localhost:8081
```

### 0. Información del servicio

Obtiene información sobre el servicio y lista todos los endpoints disponibles.

**Endpoint:** `GET /`

**Respuesta Exitosa (200 OK):**
```json
{
  "service": "User Service",
  "message": "Microservicio para gestión de usuarios",
  "version": "1.0.0",
  "status": "running",
  "port": 8081,
  "endpoints": {
    "GET /": "Información del servicio (este endpoint)",
    "GET /users": "Obtiene todos los usuarios",
    "GET /users/{id}": "Obtiene un usuario por ID",
    "POST /users": "Crea un nuevo usuario"
  },
  "endpointDescriptions": {
    "GET /users": "Retorna una lista de todos los usuarios registrados",
    "GET /users/{id}": "Retorna la información de un usuario específico por su ID",
    "POST /users": "Crea un nuevo usuario. Body: {name, email, address}"
  },
  "feignClient": {
    "note": "Este servicio puede ser consumido por otros servicios usando Feign Client",
    "serviceName": "user-service",
    "example": "@FeignClient(name = \"user-service\")"
  }
}
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8081/
```

---

### 1. Obtener todos los usuarios

Obtiene la lista completa de usuarios.

**Endpoint:** `GET /users`

**Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan.perez@example.com",
    "address": "Calle Principal 123"
  },
  {
    "id": 2,
    "name": "María García",
    "email": "maria.garcia@example.com",
    "address": "Avenida Central 456"
  },
  {
    "id": 3,
    "name": "Carlos López",
    "email": "carlos.lopez@example.com",
    "address": "Plaza Mayor 789"
  }
]
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8081/users
```

**Ejemplo con Feign Client:**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users")
    List<User> getAllUsers();
}
```

---

---

### 2. Obtener usuario por ID

Obtiene un usuario específico por su ID.

**Endpoint:** `GET /users/{id}`

**Parámetros:**
- `id` (path): ID del usuario (Long)

**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "address": "Calle Principal 123"
}
```

**Respuesta de Error (404 Not Found):**
```json
{
  "timestamp": "2026-01-28T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Usuario no encontrado"
}
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8081/users/1
```

**Ejemplo con Feign Client:**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
```

---

### 3. Crear nuevo usuario

Crea un nuevo usuario en el sistema.

**Endpoint:** `POST /users`

**Content-Type:** `application/json`

**Body Request:**
```json
{
  "name": "Ana Martínez",
  "email": "ana.martinez@example.com",
  "address": "Calle Nueva 321"
}
```

**Nota:** El campo `id` es opcional y será generado automáticamente si no se proporciona.

**Respuesta Exitosa (201 Created):**
```json
{
  "id": 4,
  "name": "Ana Martínez",
  "email": "ana.martinez@example.com",
  "address": "Calle Nueva 321"
}
```

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Martínez",
    "email": "ana.martinez@example.com",
    "address": "Calle Nueva 321"
  }'
```

**Ejemplo con Feign Client:**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/users")
    User createUser(@RequestBody User user);
}
```

---

## 📊 Modelo de Datos

### User

```java
public class User {
    private Long id;           // ID único del usuario
    private String name;        // Nombre completo
    private String email;       // Email del usuario
    private String address;     // Dirección del usuario
}
```

**Ejemplo JSON:**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "address": "Calle Principal 123"
}
```

---

## 🔗 Integración con Feign Client

Este servicio está diseñado para ser consumido por otros servicios usando **Spring Cloud OpenFeign**.

### Ejemplo de Cliente Feign

En otro servicio (por ejemplo, `order-service`):

```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users")
    List<User> getAllUsers();
    
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
    
    @PostMapping("/users")
    User createUser(@RequestBody User user);
}
```

### Configuración Requerida

1. El servicio consumidor debe tener `@EnableFeignClients` en su clase principal
2. El `name` en `@FeignClient` debe coincidir con `spring.application.name` de este servicio (`user-service`)
3. Eureka debe estar corriendo para el descubrimiento de servicios

---

## 🧪 Ejemplos de Uso

### 1. Obtener todos los usuarios

```bash
curl http://localhost:8081/users
```

### 2. Obtener usuario específico

```bash
curl http://localhost:8081/users/1
```

### 3. Crear nuevo usuario

```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pedro Sánchez",
    "email": "pedro.sanchez@example.com",
    "address": "Avenida Libertad 555"
  }'
```

### 4. Verificar que el servicio está registrado en Eureka

```bash
curl http://localhost:8761/eureka/apps/USER-SERVICE
```

---

## 📝 Datos de Ejemplo

Al iniciar, el servicio incluye los siguientes usuarios de ejemplo:

| ID | Nombre | Email | Dirección |
|----|--------|-------|-----------|
| 1 | Juan Pérez | juan.perez@example.com | Calle Principal 123 |
| 2 | María García | maria.garcia@example.com | Avenida Central 456 |
| 3 | Carlos López | carlos.lopez@example.com | Plaza Mayor 789 |

---

## 🔍 Verificación

### Verificar que el servicio está corriendo

```bash
curl http://localhost:8081/users
```

### Verificar registro en Eureka

Abre tu navegador en: http://localhost:8761

Deberías ver `USER-SERVICE` en la lista de servicios registrados.

### Ver logs del servicio

```bash
# Si estás usando Docker
docker logs user-service

# Si estás ejecutando localmente
# Los logs aparecerán en la consola
```

---

## 🐛 Troubleshooting

### El servicio no se registra en Eureka

- Verifica que Eureka Server esté corriendo
- Revisa la URL de Eureka en `application.yml`
- Verifica que el puerto 8081 esté disponible

### Error 404 al acceder a endpoints

- Verifica que el servicio esté corriendo
- Confirma que estás usando la URL correcta: `http://localhost:8081/users`
- Revisa los logs del servicio para errores

### Feign Client no puede encontrar el servicio

- Verifica que el nombre en `@FeignClient` sea exactamente `user-service`
- Asegúrate de que Eureka esté corriendo y el servicio esté registrado
- Verifica que `@EnableFeignClients` esté presente en el servicio consumidor

---

## 📚 Recursos Relacionados

- [Spring Cloud OpenFeign Documentation](https://spring.io/projects/spring-cloud-openfeign)
- [Eureka Service Discovery](https://spring.io/projects/spring-cloud-netflix)
- [Spring Boot REST Documentation](https://spring.io/guides/gs/rest-service/)

---

**Nota**: Este servicio es parte del proyecto de demostración de comunicación entre servicios con Feign Client.
