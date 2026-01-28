# Product Service

Microservicio para la gestión de productos del sistema.

## 🎯 Propósito

**Product Service** es un microservicio independiente que maneja todas las operaciones relacionadas con productos. Se registra en Eureka para que otros servicios puedan descubrirlo y comunicarse con él.

## 📋 Funcionalidades

- ✅ **CRUD de productos**: Crear, leer, actualizar y eliminar productos
- ✅ **Service Discovery**: Se registra automáticamente en Eureka
- ✅ **API REST**: Endpoints RESTful para gestión de productos
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
    name: product-service  # Nombre con el que se registra en Eureka

server:
  port: 8082

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### Configuración Clave

- **Puerto**: 8082
- **Nombre del servicio**: `product-service` (se registra como `PRODUCT-SERVICE` en Eureka)
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
docker build -t product-service .
docker run -p 8082:8082 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ \
  product-service
```

### Opción 3: Docker Compose

```bash
# Desde el directorio raíz del proyecto
docker-compose up product-service
```

## 📡 Endpoints

### Listar todos los productos

```http
GET /products
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "Laptop de alta gama",
    "price": 1299.99
  },
  {
    "id": 2,
    "name": "Mouse",
    "description": "Mouse inalámbrico",
    "price": 29.99
  }
]
```

### Obtener producto por ID

```http
GET /products/{id}
```

**Ejemplo:**
```bash
curl http://localhost:8082/products/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "Laptop de alta gama",
  "price": 1299.99
}
```

### Crear nuevo producto

```http
POST /products
Content-Type: application/json
```

**Ejemplo:**
```bash
curl -X POST http://localhost:8082/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor",
    "description": "Monitor 4K 27 pulgadas",
    "price": 399.99
  }'
```

**Respuesta:**
```json
{
  "id": 4,
  "name": "Monitor",
  "description": "Monitor 4K 27 pulgadas",
  "price": 399.99
}
```

## 🏗️ Estructura del Proyecto

```
product-service/
├── src/main/java/com/example/productservice/
│   ├── ProductServiceApplication.java    # Clase principal
│   ├── controller/
│   │   └── ProductController.java        # Controlador REST
│   ├── model/
│   │   └── Product.java                  # Modelo de datos
│   └── service/
│       └── ProductService.java           # Lógica de negocio
└── src/main/resources/
    ├── application.yml                   # Configuración local
    └── application-docker.yml            # Configuración Docker
```

## 📦 Modelo de Datos

### Product

```java
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
```

## 🔄 Integración con Eureka

Este servicio se registra automáticamente en Eureka con el nombre `PRODUCT-SERVICE`. Otros servicios pueden descubrirlo usando:

- **Nombre del servicio**: `PRODUCT-SERVICE`
- **URL directa**: `http://localhost:8082`
- **Vía Gateway**: `http://localhost:8080/api/products`

## 🔍 Verificación

### Verificar que el servicio está corriendo

```bash
curl http://localhost:8082/products
```

### Verificar registro en Eureka

1. Abre http://localhost:8761
2. Busca `PRODUCT-SERVICE` en la lista de aplicaciones registradas

### Health Check

```bash
curl http://localhost:8082/actuator/health
```

## 📝 Notas Importantes

1. **Datos en memoria**: Los datos se pierden al reiniciar el servicio (es solo para ejemplo educativo)
2. **Eureka**: Debe estar corriendo antes de iniciar este servicio
3. **Puerto**: El puerto 8082 debe estar disponible
4. **Precios**: Se usan `BigDecimal` para manejar precios con precisión decimal

## 🐛 Troubleshooting

### El servicio no se registra en Eureka

- Verifica que Eureka esté corriendo en el puerto 8761
- Revisa la configuración de `eureka.client.service-url.defaultZone`
- Revisa los logs para ver errores de conexión

### Error "Connection refused" al acceder a endpoints

- Verifica que el servicio esté corriendo
- Verifica que el puerto 8082 esté disponible
- Revisa los logs del servicio

### El Gateway no puede encontrar el servicio

- Verifica que el servicio esté registrado en Eureka
- Espera unos segundos después de iniciar (necesita tiempo para registrarse)
- Verifica que el nombre del servicio sea `PRODUCT-SERVICE` (en mayúsculas)

## 📚 Recursos

- [Spring Cloud Eureka Client Documentation](https://spring.io/projects/spring-cloud-netflix)
- [Spring Boot REST Documentation](https://spring.io/guides/gs/rest-service/)

---

**Nota**: Este es un servicio de ejemplo educativo. En producción, considera usar una base de datos persistente.
