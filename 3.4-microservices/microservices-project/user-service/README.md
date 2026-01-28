# User Service

Microservicio para la gestión de usuarios del sistema.

## 🎯 Propósito

**User Service** es un microservicio independiente que maneja todas las operaciones relacionadas con usuarios. Se registra en Eureka para que otros servicios puedan descubrirlo y comunicarse con él.

## 📋 Funcionalidades

- ✅ **CRUD de usuarios**: Crear, leer, actualizar y eliminar usuarios
- ✅ **Service Discovery**: Se registra automáticamente en Eureka
- ✅ **API REST**: Endpoints RESTful para gestión de usuarios
- ✅ **Datos en memoria**: Almacenamiento temporal en memoria (para ejemplo educativo)

## 🛠️ Tecnologías

- **Spring Boot 3.2**
- **Spring Cloud Netflix Eureka Client**
- **Spring Web** (REST)
- **Lombok**
- **Java 17**

## ⚙️ Configuración

### application.yml

```yaml
spring:
  application:
    name: user-service  # Nombre con el que se registra en Eureka

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
- **Nombre del servicio**: `user-service` (se registra como `USER-SERVICE` en Eureka)
- **Eureka**: Se conecta a Eureka en `http://localhost:8761/eureka/`

## 🚀 Ejecución

### Prerequisitos

1. **Eureka Server** debe estar corriendo en el puerto 8761

### Opción 1: Maven

```bash
mvn spring-boot:run
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

## 📡 Endpoints

### Listar todos los usuarios

```http
GET /users
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan@example.com"
  },
  {
    "id": 2,
    "name": "María García",
    "email": "maria@example.com"
  }
]
```

### Obtener usuario por ID

```http
GET /users/{id}
```

**Ejemplo:**
```bash
curl http://localhost:8081/users/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@example.com"
}
```

### Crear nuevo usuario

```http
POST /users
Content-Type: application/json
```

**Ejemplo:**
```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos López",
    "email": "carlos@example.com"
  }'
```

**Respuesta:**
```json
{
  "id": 4,
  "name": "Carlos López",
  "email": "carlos@example.com"
}
```

## 🏗️ Estructura del Proyecto

```
user-service/
├── src/main/java/com/example/userservice/
│   ├── UserServiceApplication.java    # Clase principal
│   ├── controller/
│   │   └── UserController.java        # Controlador REST
│   ├── model/
│   │   └── User.java                  # Modelo de datos
│   └── service/
│       └── UserService.java           # Lógica de negocio
└── src/main/resources/
    ├── application.yml                # Configuración local
    └── application-docker.yml         # Configuración Docker
```

## 📦 Modelo de Datos

### User

```java
public class User {
    private Long id;
    private String name;
    private String email;
}
```

## 🔄 Integración con Eureka

Este servicio se registra automáticamente en Eureka con el nombre `USER-SERVICE`. Otros servicios pueden descubrirlo usando:

- **Nombre del servicio**: `USER-SERVICE`
- **URL directa**: `http://localhost:8081`
- **Vía Gateway**: `http://localhost:8080/api/users`

## 🔍 Verificación

### Verificar que el servicio está corriendo

```bash
curl http://localhost:8081/users
```

### Verificar registro en Eureka

1. Abre http://localhost:8761
2. Busca `USER-SERVICE` en la lista de aplicaciones registradas

### Health Check

```bash
curl http://localhost:8081/actuator/health
```

## 📝 Notas Importantes

1. **Datos en memoria**: Los datos se pierden al reiniciar el servicio (es solo para ejemplo educativo)
2. **Eureka**: Debe estar corriendo antes de iniciar este servicio
3. **Puerto**: El puerto 8081 debe estar disponible

## 🐛 Troubleshooting

### El servicio no se registra en Eureka

- Verifica que Eureka esté corriendo en el puerto 8761
- Revisa la configuración de `eureka.client.service-url.defaultZone`
- Revisa los logs para ver errores de conexión

### Error "Connection refused" al acceder a endpoints

- Verifica que el servicio esté corriendo
- Verifica que el puerto 8081 esté disponible
- Revisa los logs del servicio

### El Gateway no puede encontrar el servicio

- Verifica que el servicio esté registrado en Eureka
- Espera unos segundos después de iniciar (necesita tiempo para registrarse)
- Verifica que el nombre del servicio sea `USER-SERVICE` (en mayúsculas)

## 📚 Recursos

- [Spring Cloud Eureka Client Documentation](https://spring.io/projects/spring-cloud-netflix)
- [Spring Boot REST Documentation](https://spring.io/guides/gs/rest-service/)

---

**Nota**: Este es un servicio de ejemplo educativo. En producción, considera usar una base de datos persistente.
