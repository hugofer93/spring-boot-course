# Comunicación entre Servicios con Feign Client

Este proyecto demuestra cómo implementar comunicación entre microservicios usando **Spring Cloud OpenFeign** (Feign Client) en Spring Boot.

## 📋 Tabla de Contenidos

- [¿Qué es Feign Client?](#qué-es-feign-client)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Componentes del Proyecto](#componentes-del-proyecto)
- [Características Principales](#características-principales)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Uso del Proyecto](#uso-del-proyecto)
- [Conceptos Clave](#conceptos-clave)
- [Configuración Avanzada](#configuración-avanzada)
- [Mejores Prácticas](#mejores-prácticas)
- [Troubleshooting](#troubleshooting)

## ¿Qué es Feign Client?

**Feign** es un cliente HTTP declarativo desarrollado por Netflix que simplifica la comunicación entre microservicios. Permite crear clientes REST de forma declarativa usando interfaces Java, eliminando la necesidad de escribir código boilerplate para llamadas HTTP.

### Ventajas de Feign Client

- ✅ **Declarativo**: Define endpoints como interfaces Java
- ✅ **Integración con Eureka**: Descubrimiento automático de servicios
- ✅ **Balanceo de carga**: Integración con Ribbon (incluido en Spring Cloud)
- ✅ **Manejo de errores**: Soporte para fallbacks y circuit breakers
- ✅ **Configuración flexible**: Timeouts, interceptors, logging, etc.
- ✅ **Menos código**: No necesitas escribir RestTemplate o WebClient manualmente

## Arquitectura del Proyecto

```
┌─────────────────┐
│  Eureka Server  │  (Puerto 8761)
│                 │
│  Descubrimiento │
│  de Servicios   │
└────────┬────────┘
         │
         │ Registro
         │
    ┌────┴────┐
    │         │
┌───▼───┐ ┌──▼──────┐
│ User  │ │ Order  │
│Service│ │Service │
│       │ │        │
│ 8081  │ │ 8082   │
└───┬───┘ └──┬─────┘
    │        │
    │        │ Feign Client
    │        │ (HTTP Calls)
    └────────┘
```

### Flujo de Comunicación

1. **Eureka Server** se inicia y espera registros de servicios
2. **User Service** se registra en Eureka
3. **Order Service** se registra en Eureka
4. **Order Service** usa Feign Client para llamar a **User Service**
5. Feign resuelve la URL del servicio desde Eureka automáticamente

## Componentes del Proyecto

### 1. Eureka Server (`eureka-server/`)

Servidor de descubrimiento de servicios que mantiene un registro de todos los microservicios disponibles.

**Puerto**: 8761  
**Dashboard**: http://localhost:8761

### 2. User Service (`user-service/`)

Microservicio que gestiona usuarios. Expone endpoints REST que serán consumidos por otros servicios.

**Puerto**: 8081  
**Endpoints**:
- `GET /users` - Obtener todos los usuarios
- `GET /users/{id}` - Obtener usuario por ID
- `POST /users` - Crear nuevo usuario

### 3. Order Service (`order-service/`)

Microservicio que gestiona pedidos. **Consume el User Service usando Feign Client**.

**Puerto**: 8082  
**Endpoints**:
- `GET /orders` - Obtener todos los pedidos (con información de usuario)
- `GET /orders/{id}` - Obtener pedido por ID (con información de usuario)
- `POST /orders` - Crear nuevo pedido (valida usuario)
- `GET /orders/users` - Obtener usuarios usando Feign Client directamente

## Características Principales

### ✨ Implementación de Feign Client

El proyecto muestra:

1. **Configuración básica de Feign**
   - Habilitación con `@EnableFeignClients`
   - Creación de interfaces `@FeignClient`

2. **Comunicación entre servicios**
   - Llamadas HTTP declarativas
   - Integración con Eureka para descubrimiento de servicios

3. **Configuración avanzada**
   - Timeouts personalizados
   - Interceptors para headers
   - Logging configurable
   - Compresión de requests/responses

4. **Manejo de errores**
   - Validación de usuarios antes de crear pedidos
   - Manejo de excepciones en llamadas Feign

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.6+**
- **Docker** y **Docker Compose** (opcional, para ejecutar con Docker)

## Instalación y Ejecución

### Opción 1: Ejecución Local (Recomendado para desarrollo)

#### Paso 1: Compilar los proyectos

```bash
# Compilar Eureka Server
cd eureka-server
mvn clean package
cd ..

# Compilar User Service
cd user-service
mvn clean package
cd ..

# Compilar Order Service
cd order-service
mvn clean package
cd ..
```

#### Paso 2: Ejecutar los servicios (en orden)

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
java -jar target/eureka-server-1.0.0.jar
```

**Terminal 2 - User Service:**
```bash
cd user-service
java -jar target/user-service-1.0.0.jar
```

**Terminal 3 - Order Service:**
```bash
cd order-service
java -jar target/order-service-1.0.0.jar
```

#### Paso 3: Verificar que los servicios están registrados

Abre tu navegador en: http://localhost:8761

Deberías ver ambos servicios registrados:
- `USER-SERVICE`
- `ORDER-SERVICE`

### Opción 2: Ejecución con Docker Compose

```bash
# Desde la raíz del proyecto
docker-compose up --build
```

Esto construirá e iniciará todos los servicios automáticamente.

## Uso del Proyecto

### 1. Verificar Eureka Dashboard

```bash
# Abre en tu navegador
http://localhost:8761
```

Deberías ver ambos servicios registrados en el dashboard de Eureka.

### 2. Probar User Service directamente

```bash
# Obtener todos los usuarios
curl http://localhost:8081/users

# Obtener usuario por ID
curl http://localhost:8081/users/1

# Crear nuevo usuario
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ana Martínez",
    "email": "ana.martinez@example.com",
    "address": "Calle Nueva 321"
  }'
```

### 3. Probar Order Service (usa Feign Client)

```bash
# Obtener todos los pedidos (con información de usuario)
curl http://localhost:8082/orders

# Obtener usuarios usando Feign Client directamente
curl http://localhost:8082/orders/users

# Crear nuevo pedido (valida usuario usando Feign)
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Laptop",
    "amount": 999.99
  }'
```

### 4. Ejemplo de Flujo Completo

```bash
# 1. Ver usuarios disponibles
curl http://localhost:8081/users

# 2. Crear un pedido para el usuario ID 1
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "Smartphone",
    "amount": 599.99
  }'

# 3. Ver el pedido creado (con información del usuario obtenida vía Feign)
curl http://localhost:8082/orders/1
```

## Conceptos Clave

### 1. @EnableFeignClients

Anotación que habilita el escaneo de interfaces `@FeignClient` en la aplicación.

```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    // ...
}
```

### 2. @FeignClient

Anotación que marca una interfaz como cliente Feign.

```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
```

**Parámetros importantes**:
- `name`: Nombre del servicio en Eureka (debe coincidir con `spring.application.name`)
- `url`: URL base del servicio (opcional si usas Eureka)
- `fallback`: Clase para manejar fallbacks (circuit breaker)

### 3. Integración con Eureka

Feign se integra automáticamente con Eureka. Cuando defines:

```java
@FeignClient(name = "user-service")
```

Feign busca el servicio `user-service` en Eureka y obtiene su URL automáticamente.

### 4. Modelos DTO

Los modelos en el servicio consumidor deben coincidir con los del servicio proveedor para que la deserialización funcione correctamente.

**User Service:**
```java
public class User {
    private Long id;
    private String name;
    private String email;
}
```

**Order Service (debe ser igual):**
```java
public class User {
    private Long id;
    private String name;
    private String email;
}
```

## Configuración Avanzada

### Configuración de Timeouts

En `application.yml`:

```yaml
feign:
  client:
    config:
      default:
        connectTimeout: 5000    # Timeout de conexión (ms)
        readTimeout: 10000      # Timeout de lectura (ms)
      user-service:              # Configuración específica
        connectTimeout: 5000
        readTimeout: 10000
```

### Configuración de Logging

```yaml
feign:
  logging:
    level: FULL  # NONE, BASIC, HEADERS, FULL
```

Niveles disponibles:
- **NONE**: Sin logging
- **BASIC**: Método, URL, status y tiempo
- **HEADERS**: BASIC + headers
- **FULL**: HEADERS + body completo

### Interceptors Personalizados

Ver `order-service/src/main/java/com/example/orderservice/config/FeignConfig.java`:

```java
@Bean
public RequestInterceptor requestInterceptor() {
    return template -> {
        template.header("X-Request-Id", UUID.randomUUID().toString());
        template.header("X-Service-Name", "order-service");
    };
}
```

### Compresión

```yaml
feign:
  compression:
    request:
      enabled: true
    response:
      enabled: true
```

## Mejores Prácticas

### ✅ DO (Hacer)

1. **Usar nombres consistentes**: El `name` en `@FeignClient` debe coincidir con `spring.application.name`
2. **Definir timeouts apropiados**: Configurar timeouts según las necesidades de tu aplicación
3. **Manejar errores**: Implementar manejo de excepciones para llamadas Feign
4. **Usar DTOs compartidos**: Considerar un módulo común para DTOs compartidos entre servicios
5. **Configurar logging en desarrollo**: Usar `FULL` en desarrollo, `BASIC` en producción
6. **Validar datos**: Validar que los servicios remotos existan antes de usarlos

### ❌ DON'T (No hacer)

1. **No hardcodear URLs**: Usar Eureka para descubrimiento de servicios
2. **No ignorar timeouts**: Configurar timeouts apropiados para evitar bloqueos
3. **No exponer modelos internos**: Usar DTOs específicos para comunicación entre servicios
4. **No hacer llamadas síncronas bloqueantes**: Considerar async/CompletableFuture para operaciones largas
5. **No olvidar manejo de errores**: Siempre manejar excepciones de Feign

### Estructura Recomendada

```
order-service/
├── client/              # Interfaces Feign Client
│   └── UserClient.java
├── config/              # Configuración de Feign
│   └── FeignConfig.java
├── model/               # Modelos/DTOs
│   ├── Order.java
│   └── User.java       # DTO compartido
├── service/             # Lógica de negocio
│   └── OrderService.java
└── controller/          # Controladores REST
    └── OrderController.java
```

## Troubleshooting

### Problema: Feign no encuentra el servicio

**Síntomas**: `java.net.UnknownHostException: user-service`

**Soluciones**:
1. Verificar que el servicio esté registrado en Eureka (http://localhost:8761)
2. Verificar que el `name` en `@FeignClient` coincida con `spring.application.name`
3. Verificar que `@EnableFeignClients` esté presente en la clase principal

### Problema: Timeout en llamadas Feign

**Síntomas**: `feign.RetryableException: Read timed out`

**Soluciones**:
1. Aumentar `readTimeout` en la configuración de Feign
2. Verificar que el servicio remoto esté respondiendo correctamente
3. Revisar logs del servicio remoto

### Problema: Error de deserialización

**Síntomas**: `com.fasterxml.jackson.databind.exc.MismatchedInputException`

**Soluciones**:
1. Verificar que los modelos DTO coincidan entre servicios
2. Verificar que los campos tengan los mismos nombres
3. Verificar que los tipos de datos sean compatibles

### Problema: Servicios no se registran en Eureka

**Síntomas**: No aparecen servicios en el dashboard de Eureka

**Soluciones**:
1. Verificar que Eureka Server esté ejecutándose
2. Verificar la URL de Eureka en `application.yml`
3. Verificar que la dependencia `spring-cloud-starter-netflix-eureka-client` esté presente

## Estructura del Proyecto

```
communication-project/
├── eureka-server/              # Servidor de descubrimiento
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/eureka/
│   │   │   │       └── EurekaServerApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-docker.yml
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── user-service/              # Servicio proveedor
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/userservice/
│   │   │   │       ├── UserServiceApplication.java
│   │   │   │       ├── controller/
│   │   │   │       │   └── UserController.java
│   │   │   │       ├── model/
│   │   │   │       │   └── User.java
│   │   │   │       └── service/
│   │   │   │           └── UserService.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-docker.yml
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── order-service/             # Servicio consumidor (Feign Client)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/orderservice/
│   │   │   │       ├── OrderServiceApplication.java
│   │   │   │       ├── client/              # Feign Clients
│   │   │   │       │   └── UserClient.java
│   │   │   │       ├── config/              # Configuración Feign
│   │   │   │       │   └── FeignConfig.java
│   │   │   │       ├── controller/
│   │   │   │       │   └── OrderController.java
│   │   │   │       ├── model/
│   │   │   │       │   ├── Order.java
│   │   │   │       │   └── User.java       # DTO compartido
│   │   │   │       └── service/
│   │   │   │           └── OrderService.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-docker.yml
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── compose.yml                # Docker Compose
├── .gitignore
└── README.md                  # Esta documentación
```

## Próximos Pasos

Para profundizar en Feign Client, considera:

1. **Circuit Breaker**: Integrar Resilience4j o Hystrix para manejo de fallos
2. **Fallbacks**: Implementar fallbacks para servicios no disponibles
3. **Retry**: Configurar reintentos automáticos
4. **Load Balancing**: Explorar balanceo de carga con Ribbon
5. **Request/Response Interceptors**: Personalizar headers y logging
6. **Feign con OAuth2**: Agregar autenticación a las llamadas Feign

## Recursos Adicionales

- [Spring Cloud OpenFeign Documentation](https://spring.io/projects/spring-cloud-openfeign)
- [Feign GitHub Repository](https://github.com/OpenFeign/feign)
- [Eureka Service Discovery](https://spring.io/projects/spring-cloud-netflix)

## Licencia

Este proyecto es parte de un curso educativo de Spring Boot.

---

**Nota**: Este proyecto está diseñado para fines educativos y demostración. Para producción, considera implementar circuit breakers, métricas, y otras características de resiliencia.
