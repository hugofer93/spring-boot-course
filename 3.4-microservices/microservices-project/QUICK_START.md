# Guía de Inicio Rápido

## 🚀 Ejecutar con Docker Compose (Recomendado)

```bash
# 1. Navegar al directorio del proyecto
cd 3.4-microservices/microservices-project

# 2. Construir y levantar todos los servicios
docker-compose up --build

# 3. En otra terminal, verificar que todos los servicios estén corriendo
docker-compose ps

# 4. Acceder a Eureka Dashboard
# Abre tu navegador en: http://localhost:8761
```

## 📝 Ejecutar Manualmente (Sin Docker)

### Orden de Inicio (IMPORTANTE)

**1. Eureka Server (puerto 8761)**
```bash
cd eureka-server
mvn spring-boot:run
```
Espera a que veas: "Started EurekaServerApplication"

**2. User Service (puerto 8081)**
```bash
# En una nueva terminal
cd user-service
mvn spring-boot:run
```

**3. Product Service (puerto 8082)**
```bash
# En una nueva terminal
cd product-service
mvn spring-boot:run
```

**4. API Gateway (puerto 8080)**
```bash
# En una nueva terminal
cd api-gateway
mvn spring-boot:run
```

## ✅ Verificar que Todo Funciona

### 1. Verificar Eureka Dashboard
- Abre: http://localhost:8761
- Deberías ver 3 servicios registrados:
  - API-GATEWAY
  - USER-SERVICE
  - PRODUCT-SERVICE

### 2. Probar Endpoints vía Gateway

**Listar usuarios:**
```bash
curl http://localhost:8080/api/users
```

**Listar productos:**
```bash
curl http://localhost:8080/api/products
```

**Crear usuario:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "test@example.com"}'
```

### 3. Probar Acceso Directo a Microservicios

**User Service:**
```bash
curl http://localhost:8081/users
```

**Product Service:**
```bash
curl http://localhost:8082/products
```

## 🐛 Troubleshooting

### Los servicios no aparecen en Eureka

1. Verifica que Eureka esté corriendo en el puerto 8761
2. Verifica los logs de cada servicio para ver errores de conexión
3. Asegúrate de que los servicios tengan la configuración correcta en `application.yml`

### Error "Connection refused"

1. Verifica que todos los servicios estén corriendo
2. Verifica que no haya conflictos de puertos
3. En Docker, verifica que todos los contenedores estén en la misma red

### El Gateway no puede encontrar servicios

1. Espera unos segundos después de iniciar los servicios (necesitan registrarse en Eureka)
2. Verifica en Eureka Dashboard que los servicios estén registrados
3. Revisa los logs del Gateway

## 📊 Puertos de los Servicios

| Servicio | Puerto | URL |
|----------|--------|-----|
| Eureka Server | 8761 | http://localhost:8761 |
| API Gateway | 8080 | http://localhost:8080 |
| User Service | 8081 | http://localhost:8081 |
| Product Service | 8082 | http://localhost:8082 |

## 🛑 Detener los Servicios

### Con Docker Compose
```bash
docker-compose down
```

### Manualmente
Presiona `Ctrl+C` en cada terminal donde esté corriendo un servicio.
