# Quick Start - Comunicación con Feign Client

Guía rápida para ejecutar el proyecto de comunicación entre servicios usando Feign Client.

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 17+
- Maven 3.6+
- Docker (opcional)

### Ejecución Local

#### 1. Compilar todos los servicios

```bash
# Desde la raíz del proyecto
cd eureka-server && mvn clean package && cd ..
cd user-service && mvn clean package && cd ..
cd order-service && mvn clean package && cd ..
```

#### 2. Iniciar servicios (en orden)

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

### Ejecución con Docker

```bash
docker-compose up --build
```

## ✅ Verificación

### 1. Verificar Eureka Dashboard
```
http://localhost:8761
```
Deberías ver `USER-SERVICE` y `ORDER-SERVICE` registrados.

### 2. Probar User Service
```bash
curl http://localhost:8081/users
```

### 3. Probar Order Service (usa Feign)
```bash
# Obtener pedidos (llama a user-service vía Feign)
curl http://localhost:8082/orders

# Obtener usuarios directamente usando Feign Client
curl http://localhost:8082/orders/users

# Crear pedido (valida usuario vía Feign)
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productName": "Laptop", "amount": 999.99}'
```

## 📋 Puertos

- **Eureka Server**: 8761
- **User Service**: 8081
- **Order Service**: 8082

## 🔍 Conceptos Clave

### Feign Client Interface
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);
}
```

### Habilitar Feign
```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    // ...
}
```

## 📚 Documentación Completa

Ver [README.md](README.md) para documentación detallada, configuración avanzada y troubleshooting.
