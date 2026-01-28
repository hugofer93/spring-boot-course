# Introducción a Microservicios con Spring Cloud

Proyecto educativo para entender **arquitectura de microservicios** usando **Spring Cloud**.

## 🎯 Objetivo

Este proyecto demuestra los conceptos fundamentales de microservicios con Spring Cloud:

1. **Service Discovery (Eureka)**: Descubrimiento automático de servicios
2. **API Gateway**: Punto de entrada único para todos los microservicios
3. **Microservicios independientes**: Servicios desacoplados y escalables
4. **Comunicación entre servicios**: Cómo los servicios se comunican entre sí
5. **Load Balancing**: Distribución de carga automática

## 📋 Conceptos Clave

### 1. ¿Qué son los Microservicios?

Los **microservicios** son una arquitectura de software donde una aplicación se divide en servicios pequeños, independientes y desacoplados:

**Características principales:**
- ✅ **Independientes**: Cada servicio puede desplegarse por separado
- ✅ **Especializados**: Cada servicio tiene una responsabilidad específica
- ✅ **Desacoplados**: Los servicios se comunican vía APIs REST
- ✅ **Escalables**: Puedes escalar cada servicio independientemente
- ✅ **Tecnología diversa**: Cada servicio puede usar diferentes tecnologías

**Ventajas:**
- Desarrollo paralelo por equipos diferentes
- Escalabilidad independiente
- Resiliencia (si un servicio falla, otros siguen funcionando)
- Tecnología adecuada para cada servicio

**Desventajas:**
- Mayor complejidad operacional
- Necesidad de orquestación
- Gestión de datos distribuidos
- Testing más complejo

### 2. Spring Cloud: ¿Qué es?

**Spring Cloud** es un conjunto de herramientas y librerías para construir microservicios:

| Componente | Propósito |
|------------|-----------|
| **Eureka** | Service Discovery (descubrimiento de servicios) |
| **Gateway** | API Gateway (punto de entrada único) |
| **Config** | Configuración centralizada |
| **Circuit Breaker** | Resiliencia y tolerancia a fallos |
| **Load Balancer** | Balanceo de carga |

### 3. Service Discovery (Eureka)

**Eureka** es el servicio de descubrimiento de Spring Cloud:

**¿Cómo funciona?**
1. Cada microservicio se registra en Eureka al iniciar
2. Eureka mantiene un registro de todos los servicios disponibles
3. Los servicios pueden descubrir otros servicios consultando Eureka
4. Eureka detecta servicios que fallan y los elimina del registro

**Beneficios:**
- No necesitas conocer las URLs de los servicios manualmente
- Balanceo de carga automático
- Detección de servicios caídos
- Escalabilidad horizontal fácil

### 4. API Gateway

El **API Gateway** es el punto de entrada único para todos los microservicios:

**Funciones principales:**
- **Enrutamiento**: Redirige peticiones al microservicio correcto
- **Load Balancing**: Distribuye carga entre instancias
- **Autenticación**: Validación centralizada
- **Rate Limiting**: Control de límites de peticiones
- **Logging**: Registro centralizado

**Ventajas:**
- Cliente solo necesita conocer una URL
- Seguridad centralizada
- Transformación de respuestas
- Monitoreo centralizado

### 5. Arquitectura del Proyecto

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │
       │ HTTP Request
       ▼
┌─────────────────┐
│   API Gateway   │  ← Punto de entrada único
│  (Puerto 8080)  │
└────────┬────────┘
         │
    ┌────┴────┐
    │         │
    ▼         ▼
┌─────────┐ ┌──────────┐
│  User   │ │ Product  │
│ Service │ │ Service  │
│  :8081  │ │  :8082   │
└────┬────┘ └────┬─────┘
     │           │
     └─────┬─────┘
           │
           ▼
    ┌──────────────┐
    │ Eureka Server│  ← Service Discovery
    │   (Puerto    │
    │    8761)     │
    └──────────────┘
```

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **Spring Cloud 2023.0.0** (Kilburn)
- **Eureka Server** (Service Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Spring Cloud OpenFeign** (Comunicación entre servicios)
- **Lombok**

## 📁 Estructura del Proyecto

```
microservices-project/
├── README.md                    # Este archivo
├── compose.yml                  # Docker Compose para todos los servicios
├── eureka-server/              # Servicio de descubrimiento
│   ├── pom.xml
│   └── src/main/java/...
├── api-gateway/                # API Gateway
│   ├── pom.xml
│   └── src/main/java/...
├── user-service/               # Microservicio de Usuarios
│   ├── pom.xml
│   └── src/main/java/...
└── product-service/            # Microservicio de Productos
    ├── pom.xml
    └── src/main/java/...
```

## 🚀 Inicio Rápido

### Opción 1: Ejecutar con Docker Compose (Recomendado)

```bash
# 1. Levantar todos los servicios
docker-compose up -d

# 2. Verificar que todos los servicios estén corriendo
docker-compose ps

# 3. Acceder a Eureka Dashboard
# http://localhost:8761

# 4. Probar el API Gateway
# http://localhost:8080
```

### Opción 2: Ejecutar Manualmente

**Orden de inicio (importante):**

1. **Eureka Server** (puerto 8761)
```bash
cd eureka-server
mvn spring-boot:run
```

2. **User Service** (puerto 8081)
```bash
cd user-service
mvn spring-boot:run
```

3. **Product Service** (puerto 8082)
```bash
cd product-service
mvn spring-boot:run
```

4. **API Gateway** (puerto 8080)
```bash
cd api-gateway
mvn spring-boot:run
```

## 📡 Endpoints Disponibles

### Eureka Dashboard
- **URL**: http://localhost:8761
- **Descripción**: Panel de control para ver todos los servicios registrados

### API Gateway (Punto de entrada único)

#### User Service
- `GET /api/users` - Listar todos los usuarios
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario

#### Product Service
- `GET /api/products` - Listar todos los productos
- `GET /api/products/{id}` - Obtener producto por ID
- `POST /api/products` - Crear nuevo producto

### Acceso Directo a Microservicios

#### User Service (puerto 8081)
- `GET http://localhost:8081/users`
- `GET http://localhost:8081/users/{id}`
- `POST http://localhost:8081/users`

#### Product Service (puerto 8082)
- `GET http://localhost:8082/products`
- `GET http://localhost:8082/products/{id}`
- `POST http://localhost:8082/products`

## 🔍 Ejemplos de Uso

### 1. Listar Usuarios (vía Gateway)

```bash
curl http://localhost:8080/api/users
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

### 2. Crear Usuario (vía Gateway)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos López",
    "email": "carlos@example.com"
  }'
```

### 3. Listar Productos (vía Gateway)

```bash
curl http://localhost:8080/api/products
```

### 4. Ver Servicios Registrados en Eureka

Abre tu navegador en: http://localhost:8761

Verás una lista de todos los servicios registrados:
- `API-GATEWAY`
- `USER-SERVICE`
- `PRODUCT-SERVICE`

## 🧩 Componentes del Proyecto

### 1. Eureka Server

**Propósito**: Servicio de descubrimiento centralizado

**Características:**
- Registra todos los microservicios
- Proporciona información de servicios disponibles
- Detecta servicios caídos automáticamente

**Configuración clave:**
```yaml
eureka:
  client:
    register-with-eureka: false  # Eureka no se registra a sí mismo
    fetch-registry: false
```

### 2. User Service

**Propósito**: Gestiona usuarios del sistema

**Características:**
- CRUD básico de usuarios
- Se registra en Eureka
- Expone endpoints REST

**Endpoints:**
- `GET /users` - Listar usuarios
- `GET /users/{id}` - Obtener usuario
- `POST /users` - Crear usuario

### 3. Product Service

**Propósito**: Gestiona productos del sistema

**Características:**
- CRUD básico de productos
- Se registra en Eureka
- Expone endpoints REST

**Endpoints:**
- `GET /products` - Listar productos
- `GET /products/{id}` - Obtener producto
- `POST /products` - Crear producto

### 4. API Gateway

**Propósito**: Punto de entrada único para todos los servicios

**Características:**
- Enrutamiento a microservicios
- Load balancing automático
- Se registra en Eureka para descubrir servicios

**Rutas configuradas:**
- `/api/users/**` → `USER-SERVICE`
- `/api/products/**` → `PRODUCT-SERVICE`

## 🔄 Flujo de Comunicación

### Ejemplo: Cliente solicita lista de usuarios

1. **Cliente** → `GET http://localhost:8080/api/users`
2. **API Gateway** recibe la petición
3. **API Gateway** consulta Eureka: "¿Dónde está USER-SERVICE?"
4. **Eureka** responde: "USER-SERVICE está en http://localhost:8081"
5. **API Gateway** redirige la petición a `http://localhost:8081/users`
6. **User Service** procesa y responde
7. **API Gateway** devuelve la respuesta al cliente

### Comunicación entre Servicios

Los servicios pueden comunicarse entre sí usando **OpenFeign**:

```java
@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    UserDTO getUser(@PathVariable Long id);
}
```

## 🎓 Conceptos Avanzados

### Load Balancing

Spring Cloud Gateway y Eureka proporcionan **load balancing automático**:

- Si tienes múltiples instancias del mismo servicio, Eureka las registra todas
- El Gateway distribuye las peticiones entre las instancias disponibles
- No necesitas configurar nada adicional

### Health Checks

Eureka verifica periódicamente la salud de los servicios:

- Si un servicio no responde, Eureka lo marca como "DOWN"
- El Gateway no enviará peticiones a servicios caídos
- Cuando el servicio se recupera, Eureka lo vuelve a marcar como "UP"

### Service Discovery vs Configuración Estática

**❌ Sin Service Discovery (configuración estática):**
```yaml
gateway:
  routes:
    - uri: http://localhost:8081  # URL hardcodeada
```

**✅ Con Service Discovery (Eureka):**
```yaml
gateway:
  routes:
    - uri: lb://USER-SERVICE  # Descubierto automáticamente
```

## 🐛 Troubleshooting

### Los servicios no aparecen en Eureka

1. Verifica que Eureka esté corriendo en el puerto 8761
2. Verifica que los servicios tengan la configuración correcta:
   ```yaml
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/
   ```

### El Gateway no puede encontrar servicios

1. Verifica que los servicios estén registrados en Eureka
2. Verifica que el Gateway tenga acceso a Eureka
3. Revisa los logs del Gateway para ver errores de descubrimiento

### Error "Connection refused"

1. Asegúrate de que todos los servicios estén corriendo
2. Verifica los puertos en los archivos `application.yml`
3. Verifica que no haya conflictos de puertos

## 📚 Recursos Adicionales

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Eureka Documentation](https://github.com/Netflix/eureka)
- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)

## 🎯 Próximos Pasos

Para profundizar en microservicios, considera:

1. **Configuración Centralizada**: Spring Cloud Config
2. **Circuit Breaker**: Resilience4j o Hystrix
3. **Distributed Tracing**: Sleuth y Zipkin
4. **API Documentation**: OpenAPI/Swagger en cada servicio
5. **Security**: OAuth2 y JWT entre servicios
6. **Message Queues**: RabbitMQ o Kafka para comunicación asíncrona

## 📝 Notas

- Este proyecto es **educativo** y simplificado para entender conceptos básicos
- En producción, considera:
  - Configuración de seguridad
  - Monitoreo y logging distribuido
  - Manejo de transacciones distribuidas
  - Testing de integración entre servicios
  - CI/CD para despliegue automatizado

---

**¡Disfruta aprendiendo sobre microservicios con Spring Cloud!** 🚀
