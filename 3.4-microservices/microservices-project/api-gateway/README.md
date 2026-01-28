# API Gateway

Punto de entrada único para todos los microservicios usando Spring Cloud Gateway.

## 🎯 Propósito

**API Gateway** actúa como un único punto de entrada para todos los microservicios. Enruta las peticiones de los clientes a los microservicios correspondientes usando Service Discovery (Eureka) y proporciona load balancing automático.

## 📋 Funcionalidades

- ✅ **Enrutamiento**: Redirige peticiones al microservicio correcto
- ✅ **Service Discovery**: Descubre servicios automáticamente usando Eureka
- ✅ **Load Balancing**: Distribuye carga entre múltiples instancias
- ✅ **Strip Prefix**: Elimina prefijos de rutas antes de enviar al servicio
- ✅ **Punto único de entrada**: Los clientes solo necesitan conocer una URL

## 🛠️ Tecnologías

- **Spring Boot 3.2**
- **Spring Cloud Gateway** (basado en WebFlux)
- **Spring Cloud Netflix Eureka Client**
- **Java 17**

## ⚙️ Configuración

### application.yml

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # Ruta para User Service
        - id: user-service
          uri: lb://USER-SERVICE  # lb:// = load balancing con Eureka
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1  # Elimina /api antes de enviar
        
        # Ruta para Product Service
        - id: product-service
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/api/products/**
          filters:
            - StripPrefix=1

server:
  port: 8080

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Configuración Clave

- **Puerto**: 8080 (puerto estándar del Gateway)
- **lb://**: Prefijo que indica usar load balancing con Eureka
- **StripPrefix**: Elimina el prefijo `/api` antes de enviar la petición al servicio
- **Predicates**: Define qué rutas coinciden con cada servicio

## 🚀 Ejecución

### Prerequisitos

1. **Eureka Server** debe estar corriendo
2. Los **microservicios** (user-service, product-service) deben estar registrados en Eureka

### Opción 1: Maven

```bash
mvn spring-boot:run
```

### Opción 2: Docker

```bash
docker build -t api-gateway .
docker run -p 8080:8080 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ \
  api-gateway
```

### Opción 3: Docker Compose

```bash
# Desde el directorio raíz del proyecto
docker-compose up api-gateway
```

## 📡 Rutas Configuradas

### User Service

| Gateway Route | Servicio Real | Descripción |
|---------------|---------------|-------------|
| `GET /api/users` | `GET /users` | Listar usuarios |
| `GET /api/users/{id}` | `GET /users/{id}` | Obtener usuario |
| `POST /api/users` | `POST /users` | Crear usuario |

### Product Service

| Gateway Route | Servicio Real | Descripción |
|---------------|---------------|-------------|
| `GET /api/products` | `GET /products` | Listar productos |
| `GET /api/products/{id}` | `GET /products/{id}` | Obtener producto |
| `POST /api/products` | `POST /products` | Crear producto |

## 🔄 Flujo de Petición

### Ejemplo: Cliente solicita lista de usuarios

1. **Cliente** → `GET http://localhost:8080/api/users`
2. **API Gateway** recibe la petición
3. **Gateway** consulta Eureka: "¿Dónde está USER-SERVICE?"
4. **Eureka** responde con la ubicación del servicio
5. **Gateway** redirige a `http://user-service:8081/users` (eliminando `/api`)
6. **User Service** procesa y responde
7. **Gateway** devuelve la respuesta al cliente

## 📡 Endpoints del Gateway

### Endpoint de bienvenida

```http
GET /
```

**Respuesta:**
```json
{
  "message": "API Gateway - Microservicios con Spring Cloud",
  "status": "running",
  "endpoints": {
    "users": "/api/users",
    "products": "/api/products",
    "eureka": "http://localhost:8761"
  }
}
```

### Health Check

```http
GET /health
```

**Respuesta:**
```json
{
  "status": "UP"
}
```

### Ejemplos de Uso

#### Listar usuarios vía Gateway

```bash
curl http://localhost:8080/api/users
```

#### Crear usuario vía Gateway

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nuevo Usuario",
    "email": "nuevo@example.com"
  }'
```

#### Listar productos vía Gateway

```bash
curl http://localhost:8080/api/products
```

## 🏗️ Estructura del Proyecto

```
api-gateway/
├── src/main/java/com/example/apigateway/
│   ├── ApiGatewayApplication.java      # Clase principal
│   └── controller/
│       └── GatewayController.java       # Controlador (WebFlux)
└── src/main/resources/
    ├── application.yml                  # Configuración local
    └── application-docker.yml          # Configuración Docker
```

## 🔍 Conceptos Importantes

### Load Balancing (lb://)

El prefijo `lb://` indica que Spring Cloud Gateway debe:
1. Consultar Eureka para encontrar instancias del servicio
2. Distribuir las peticiones entre las instancias disponibles
3. Manejar automáticamente servicios que se caen o recuperan

### StripPrefix Filter

El filtro `StripPrefix=1` elimina el primer segmento del path:
- Petición: `/api/users`
- Después de StripPrefix: `/users`
- Se envía al servicio: `/users`

### Predicates

Los predicates definen qué rutas coinciden con cada servicio:
- `Path=/api/users/**` → Coincide con cualquier ruta que empiece con `/api/users/`

## 🔍 Verificación

### Verificar que el Gateway está corriendo

```bash
curl http://localhost:8080/
```

### Verificar registro en Eureka

1. Abre http://localhost:8761
2. Busca `API-GATEWAY` en la lista de aplicaciones registradas

### Probar enrutamiento

```bash
# Debe enrutar a User Service
curl http://localhost:8080/api/users

# Debe enrutar a Product Service
curl http://localhost:8080/api/products
```

## 📝 Notas Importantes

1. **WebFlux**: Spring Cloud Gateway usa WebFlux (reactivo), no Spring MVC
2. **Eureka**: Debe estar corriendo y los servicios deben estar registrados
3. **Orden de inicio**: Inicia Eureka → Microservicios → Gateway
4. **Puerto**: El puerto 8080 debe estar disponible

## 🐛 Troubleshooting

### El Gateway no puede encontrar servicios

- Verifica que los servicios estén registrados en Eureka
- Espera unos segundos después de iniciar los servicios
- Revisa los logs del Gateway para ver errores de descubrimiento
- Verifica que los nombres de los servicios en Eureka coincidan (USER-SERVICE, PRODUCT-SERVICE)

### Error 503 Service Unavailable

- El servicio destino no está disponible
- El servicio no está registrado en Eureka
- Hay un problema de red entre Gateway y el servicio

### Error 404 Not Found

- La ruta no coincide con ningún predicate configurado
- Verifica la configuración de rutas en `application.yml`
- Verifica que el path de la petición sea correcto

### El Gateway no responde

- Verifica que el Gateway esté corriendo
- Revisa los logs para ver errores
- Verifica que el puerto 8080 esté disponible

## 🎓 Extensibilidad

### Agregar nuevas rutas

Para agregar un nuevo microservicio, añade una nueva ruta en `application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: nuevo-servicio
          uri: lb://NUEVO-SERVICIO
          predicates:
            - Path=/api/nuevo/**
          filters:
            - StripPrefix=1
```

### Filtros personalizados

Puedes agregar filtros personalizados para:
- Autenticación
- Logging
- Rate limiting
- Transformación de respuestas

## 📚 Recursos

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Gateway Reference](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)

---

**Importante**: El Gateway debe iniciarse después de que Eureka y los microservicios estén corriendo.
