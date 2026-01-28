# Order Service

Microservicio para gestión de pedidos que consume el User Service usando **Spring Cloud OpenFeign**. Este servicio demuestra cómo implementar comunicación entre microservicios usando Feign Client.

## 🎯 Propósito

**Order Service** es el servicio consumidor que gestiona pedidos y utiliza **Feign Client** para obtener información de usuarios desde el User Service. Este servicio demuestra:

- ✅ Uso de `@FeignClient` para comunicación entre servicios
- ✅ Integración con Eureka para descubrimiento de servicios
- ✅ Enriquecimiento de datos con información de otros servicios
- ✅ Validación de datos usando servicios remotos

## 📋 Funcionalidades

- ✅ **Gestión de pedidos**: CRUD completo de pedidos
- ✅ **Comunicación con Feign**: Consume User Service usando Feign Client
- ✅ **Enriquecimiento de datos**: Agrega información de usuario a los pedidos
- ✅ **Validación**: Valida usuarios antes de crear pedidos
- ✅ **Service Discovery**: Se registra en Eureka y descubre otros servicios

## 🛠️ Tecnologías

- **Spring Boot 3.2**
- **Spring Cloud OpenFeign**
- **Spring Cloud Netflix Eureka Client**
- **Lombok**
- **Java 17**

## ⚙️ Configuración

### application.yml

```yaml
spring:
  application:
    name: order-service

server:
  port: 8082

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 10000
      user-service:
        connectTimeout: 5000
        readTimeout: 10000
  compression:
    request:
      enabled: true
    response:
      enabled: true
  logging:
    level: BASIC
```

### Configuración Clave

- **Puerto**: 8082
- **Feign Client**: Configurado para comunicarse con `user-service`
- **Timeouts**: 5 segundos conexión, 10 segundos lectura
- **Compresión**: Habilitada para requests y responses

## 🚀 Ejecución

### Prerequisitos

1. **Eureka Server** debe estar corriendo
2. **User Service** debe estar corriendo y registrado en Eureka

### Opción 1: Maven

```bash
mvn clean package
java -jar target/order-service-1.0.0.jar
```

### Opción 2: Docker

```bash
docker build -t order-service .
docker run -p 8082:8082 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ \
  order-service
```

### Opción 3: Docker Compose

```bash
# Desde el directorio raíz del proyecto
docker-compose up order-service
```

## 📡 Endpoints API

### Base URL

```
http://localhost:8082
```

### 0. Información del servicio

Obtiene información sobre el servicio y lista todos los endpoints disponibles.

**Endpoint:** `GET /`

**Respuesta Exitosa (200 OK):**
```json
{
  "service": "Order Service",
  "message": "Microservicio para gestión de pedidos - Usa Feign Client para comunicarse con User Service",
  "version": "1.0.0",
  "status": "running",
  "port": 8082,
  "endpoints": {
    "GET /": "Información del servicio (este endpoint)",
    "GET /orders": "Obtiene todos los pedidos (con información de usuario vía Feign)",
    "GET /orders/{id}": "Obtiene un pedido por ID (con información de usuario vía Feign)",
    "POST /orders": "Crea un nuevo pedido (valida usuario vía Feign)",
    "GET /orders/users": "Obtiene usuarios usando Feign Client directamente"
  },
  "endpointDescriptions": {
    "GET /orders": "Retorna todos los pedidos enriquecidos con información del usuario obtenida del User Service usando Feign Client",
    "GET /orders/{id}": "Retorna un pedido específico con información del usuario obtenida vía Feign Client",
    "POST /orders": "Crea un nuevo pedido. Valida que el usuario exista usando Feign Client. Body: {userId, productName, amount}",
    "GET /orders/users": "Ejemplo directo de uso de Feign Client - Obtiene usuarios del User Service"
  },
  "feignClient": {
    "note": "Este servicio consume el User Service usando Spring Cloud OpenFeign",
    "consumedService": "user-service",
    "implementation": "Ver UserClient.java para la interfaz @FeignClient",
    "configuration": "Ver FeignConfig.java para configuración personalizada"
  },
  "examples": {
    "Get all orders": "curl http://localhost:8082/orders",
    "Get order by ID": "curl http://localhost:8082/orders/1",
    "Create order": "curl -X POST http://localhost:8082/orders -H 'Content-Type: application/json' -d '{\"userId\":1,\"productName\":\"Laptop\",\"amount\":999.99}'",
    "Get users via Feign": "curl http://localhost:8082/orders/users"
  }
}
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8082/
```

---

### 1. Obtener todos los pedidos

Obtiene la lista completa de pedidos con información del usuario obtenida mediante Feign Client.

**Endpoint:** `GET /orders`

**Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 1,
    "productName": "Laptop Dell XPS",
    "amount": 1299.99,
    "orderDate": "2026-01-26T10:30:00",
    "status": "DELIVERED",
    "userName": "Juan Pérez",
    "userEmail": "juan.perez@example.com"
  },
  {
    "id": 2,
    "userId": 2,
    "productName": "iPhone 15 Pro",
    "amount": 999.99,
    "orderDate": "2026-01-27T14:20:00",
    "status": "SHIPPED",
    "userName": "María García",
    "userEmail": "maria.garcia@example.com"
  },
  {
    "id": 3,
    "userId": 1,
    "productName": "Monitor LG 27 pulgadas",
    "amount": 299.99,
    "orderDate": "2026-01-28T09:15:00",
    "status": "PENDING",
    "userName": "Juan Pérez",
    "userEmail": "juan.perez@example.com"
  }
]
```

**Nota:** Los campos `userName` y `userEmail` se obtienen del User Service usando Feign Client.

**Ejemplo con cURL:**
```bash
curl http://localhost:8082/orders
```

---

---

### 2. Obtener pedido por ID

Obtiene un pedido específico por su ID con información del usuario.

**Endpoint:** `GET /orders/{id}`

**Parámetros:**
- `id` (path): ID del pedido (Long)

**Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "userId": 1,
  "productName": "Laptop Dell XPS",
  "amount": 1299.99,
  "orderDate": "2026-01-26T10:30:00",
  "status": "DELIVERED",
  "userName": "Juan Pérez",
  "userEmail": "juan.perez@example.com"
}
```

**Respuesta de Error (404 Not Found):**
```json
{
  "timestamp": "2026-01-28T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pedido no encontrado"
}
```

**Ejemplo con cURL:**
```bash
curl http://localhost:8082/orders/1
```

---

### 3. Crear nuevo pedido

Crea un nuevo pedido. **Valida que el usuario exista** usando Feign Client antes de crear el pedido.

**Endpoint:** `POST /orders`

**Content-Type:** `application/json`

**Body Request:**
```json
{
  "userId": 1,
  "productName": "Teclado Mecánico",
  "amount": 149.99
}
```

**Nota:** 
- El campo `id` es opcional y será generado automáticamente
- El campo `orderDate` se establece automáticamente
- El campo `status` se establece automáticamente como "PENDING"
- Los campos `userName` y `userEmail` se obtienen del User Service usando Feign Client

**Respuesta Exitosa (201 Created):**
```json
{
  "id": 4,
  "userId": 1,
  "productName": "Teclado Mecánico",
  "amount": 149.99,
  "orderDate": "2026-01-28T12:30:00",
  "status": "PENDING",
  "userName": "Juan Pérez",
  "userEmail": "juan.perez@example.com"
}
```

**Respuesta de Error (400 Bad Request):**
Si el usuario no existe o hay un error al validar:
```json
{
  "timestamp": "2026-01-28T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Usuario no encontrado"
}
```

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Teclado Mecánico",
    "amount": 149.99
  }'
```

---

### 4. Obtener usuarios usando Feign Client

Endpoint de ejemplo que demuestra el uso directo de Feign Client para obtener usuarios del User Service.

**Endpoint:** `GET /orders/users`

**Descripción:** Este endpoint llama directamente al User Service usando Feign Client y retorna la lista de usuarios.

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
curl http://localhost:8082/orders/users
```

**Nota:** Este endpoint demuestra cómo Feign Client puede ser usado directamente desde un controlador para llamar a otros servicios.

---

## 📊 Modelo de Datos

### Order

```java
public class Order {
    private Long id;                    // ID único del pedido
    private Long userId;                // ID del usuario (referencia al User Service)
    private String productName;         // Nombre del producto
    private Double amount;              // Monto del pedido
    private LocalDateTime orderDate;    // Fecha del pedido
    private String status;              // Estado: PENDING, SHIPPED, DELIVERED
    
    // Campos enriquecidos desde User Service (vía Feign Client)
    private String userName;            // Nombre del usuario
    private String userEmail;           // Email del usuario
}
```

**Ejemplo JSON:**
```json
{
  "id": 1,
  "userId": 1,
  "productName": "Laptop Dell XPS",
  "amount": 1299.99,
  "orderDate": "2026-01-26T10:30:00",
  "status": "PENDING",
  "userName": "Juan Pérez",
  "userEmail": "juan.perez@example.com"
}
```

---

## 🔗 Implementación de Feign Client

### UserClient Interface

```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
```

### Habilitación de Feign

```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

### Configuración Personalizada

Ver `FeignConfig.java` para:
- Interceptors personalizados
- Configuración de logging
- Headers personalizados

---

## 🧪 Ejemplos de Uso

### 1. Obtener todos los pedidos (con información de usuario)

```bash
curl http://localhost:8082/orders
```

### 2. Obtener pedido específico

```bash
curl http://localhost:8082/orders/1
```

### 3. Crear nuevo pedido (valida usuario vía Feign)

```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "productName": "Mouse Inalámbrico",
    "amount": 49.99
  }'
```

### 4. Obtener usuarios usando Feign Client directamente

```bash
curl http://localhost:8082/orders/users
```

### 5. Flujo completo: Crear pedido y verificar

```bash
# 1. Ver usuarios disponibles
curl http://localhost:8082/orders/users

# 2. Crear pedido para usuario ID 1
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Auriculares Bluetooth",
    "amount": 79.99
  }'

# 3. Ver el pedido creado (con información del usuario)
curl http://localhost:8082/orders/4
```

---

## 📝 Datos de Ejemplo

Al iniciar, el servicio incluye los siguientes pedidos de ejemplo:

| ID | Usuario ID | Producto | Monto | Estado | Fecha |
|----|------------|----------|-------|--------|-------|
| 1 | 1 | Laptop Dell XPS | $1,299.99 | DELIVERED | 2 días atrás |
| 2 | 2 | iPhone 15 Pro | $999.99 | SHIPPED | 1 día atrás |
| 3 | 1 | Monitor LG 27 pulgadas | $299.99 | PENDING | Hoy |

**Nota:** La información del usuario (`userName`, `userEmail`) se obtiene dinámicamente del User Service usando Feign Client.

---

## 🔍 Verificación

### Verificar que el servicio está corriendo

```bash
curl http://localhost:8082/orders
```

### Verificar registro en Eureka

Abre tu navegador en: http://localhost:8761

Deberías ver `ORDER-SERVICE` en la lista de servicios registrados.

### Verificar comunicación con User Service

```bash
# Este endpoint llama al User Service usando Feign Client
curl http://localhost:8082/orders/users
```

### Ver logs del servicio

```bash
# Si estás usando Docker
docker logs order-service

# Busca logs de Feign Client para ver las llamadas HTTP
```

---

## 🐛 Troubleshooting

### Error: Feign no encuentra el servicio

**Síntoma:** `java.net.UnknownHostException: user-service`

**Soluciones:**
- Verifica que User Service esté registrado en Eureka
- Verifica que el nombre en `@FeignClient` sea exactamente `user-service`
- Asegúrate de que `@EnableFeignClients` esté presente en la clase principal

### Error: Timeout en llamadas Feign

**Síntoma:** `feign.RetryableException: Read timed out`

**Soluciones:**
- Aumenta `readTimeout` en la configuración de Feign
- Verifica que User Service esté respondiendo correctamente
- Revisa los logs del User Service

### Error: Usuario no encontrado al crear pedido

**Síntoma:** `400 Bad Request` al crear pedido

**Soluciones:**
- Verifica que el `userId` exista en el User Service
- Asegúrate de que User Service esté disponible
- Revisa los logs para ver el error específico

### Los pedidos no muestran información del usuario

**Síntoma:** Los pedidos tienen `userName` y `userEmail` como `null`

**Soluciones:**
- Verifica que User Service esté disponible
- Revisa los logs para errores de Feign Client
- Asegúrate de que el `userId` en el pedido sea válido

---

## 📚 Conceptos Clave

### ¿Qué es Feign Client?

Feign es un cliente HTTP declarativo que simplifica la comunicación entre microservicios. Permite definir llamadas HTTP como interfaces Java.

### ¿Cómo funciona?

1. Defines una interfaz con `@FeignClient`
2. Feign genera automáticamente la implementación
3. Usas la interfaz como un bean normal de Spring
4. Feign hace las llamadas HTTP automáticamente

### Ventajas

- ✅ Código declarativo y limpio
- ✅ Integración automática con Eureka
- ✅ Configuración flexible (timeouts, interceptors, etc.)
- ✅ Menos código boilerplate

---

## 📚 Recursos Relacionados

- [Spring Cloud OpenFeign Documentation](https://spring.io/projects/spring-cloud-openfeign)
- [Feign GitHub Repository](https://github.com/OpenFeign/feign)
- [Eureka Service Discovery](https://spring.io/projects/spring-cloud-netflix)
- [User Service README](../user-service/README.md)

---

**Nota**: Este servicio demuestra el uso práctico de Feign Client para comunicación entre microservicios. Ver el código fuente para ejemplos completos de implementación.
