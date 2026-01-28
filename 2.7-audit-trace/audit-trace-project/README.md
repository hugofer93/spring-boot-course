# Filtros e Interceptores para Auditoría y Trazabilidad

Proyecto simple para entender **filtros HTTP** e **interceptores** en Spring para implementar auditoría y trazabilidad.

## 🎯 Objetivo

Este proyecto demuestra cómo implementar:
- **Filtros HTTP** para registrar todas las peticiones y respuestas
- **Interceptores** para auditar la ejecución de métodos del controlador
- **Servicio de auditoría** para almacenar y consultar logs

## 📋 Conceptos Clave

### Filtros (Filters)
- Se ejecutan **antes y después** de que la petición llegue al controlador
- Interceptan **todas las peticiones HTTP**
- Útiles para: logging, autenticación, compresión, CORS, etc.
- Orden de ejecución: Filtro 1 → Filtro 2 → ... → Controlador → ... → Filtro 2 → Filtro 1

### Interceptores (Interceptors)
- Se ejecutan **antes, durante y después** de la ejecución del controlador
- Solo interceptan peticiones que **coinciden con patrones configurados**
- Útiles para: auditoría específica, validaciones, transformaciones, etc.
- Métodos: `preHandle()`, `postHandle()`, `afterCompletion()`

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **Lombok** (reduce boilerplate)
- **SLF4J** (logging)

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   └── WebConfig.java              # Registro de interceptores
├── controller/
│   ├── HomeController.java         # Endpoint raíz
│   ├── UserController.java         # Endpoints de ejemplo (serán auditados)
│   └── AuditController.java        # Consultar logs de auditoría
├── filter/
│   └── AuditFilter.java            # Filtro HTTP para auditoría
├── interceptor/
│   └── AuditInterceptor.java       # Interceptor para auditoría
├── model/
│   └── AuditLog.java               # Modelo de log de auditoría
├── service/
│   └── AuditService.java           # Servicio de auditoría
└── DemoApplication.java            # Clase principal
```

## 🚀 Cómo arrancar

### Opción 1: Con Docker Compose

```bash
# 1. Construir y ejecutar
docker compose up --build

# 2. La API estará disponible en http://localhost:8080
```

### Opción 2: Sin Docker

```bash
# 1. Compilar
mvn clean package

# 2. Ejecutar
mvn spring-boot:run

# 3. La API estará disponible en http://localhost:8080
```

## 🧪 Cómo probar

### 1. Ver información de la API

```bash
curl http://localhost:8080/
```

### 2. Crear un usuario (será auditado)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "X-User: john.doe" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com"
  }'
```

### 3. Obtener usuarios (será auditado)

```bash
curl http://localhost:8080/api/users \
  -H "X-User: john.doe"
```

### 4. Ver logs de auditoría

```bash
curl http://localhost:8080/api/audit/logs
```

**Respuesta esperada:**
```json
[
  {
    "timestamp": "2026-01-27T10:30:00",
    "method": "POST",
    "path": "/api/users",
    "user": "john.doe",
    "action": "HTTP_REQUEST",
    "details": "Status: 201",
    "duration": 45
  },
  {
    "timestamp": "2026-01-27T10:30:01",
    "method": "POST",
    "path": "/api/users",
    "user": "john.doe",
    "action": "CONTROLLER_METHOD",
    "details": "Handler: ...UserController#createUser(...), Status: 201",
    "duration": 42
  }
]
```

## 📊 Flujo de Ejecución

Cuando haces una petición a `/api/users`:

1. **AuditFilter.doFilterInternal()** (pre-request)
   - Registra inicio de petición
   - Mide tiempo de inicio

2. **AuditInterceptor.preHandle()**
   - Se ejecuta antes del controlador

3. **UserController.createUser()**
   - Método del controlador se ejecuta

4. **AuditInterceptor.postHandle()**
   - Se ejecuta después del controlador

5. **AuditInterceptor.afterCompletion()**
   - Registra auditoría del interceptor
   - Calcula duración total

6. **AuditFilter.doFilterInternal()** (post-request)
   - Registra auditoría del filtro
   - Calcula duración total de la petición HTTP

## 🔍 Diferencias entre Filtro e Interceptor

| Aspecto | Filtro (Filter) | Interceptor (Interceptor) |
|---------|----------------|---------------------------|
| **Nivel** | Servlet (más bajo) | Spring MVC (más alto) |
| **Alcance** | Todas las peticiones HTTP | Solo rutas configuradas |
| **Acceso a** | Request/Response HTTP | Request/Response + Handler + ModelAndView |
| **Orden** | Se ejecuta primero | Se ejecuta después del filtro |
| **Uso típico** | Logging general, autenticación, CORS | Auditoría específica, validaciones |

## 💡 Ejemplos de Uso Real

### Filtros
- **Logging de todas las peticiones**
- **Autenticación y autorización** (JWT, OAuth)
- **Compresión de respuestas** (GZIP)
- **CORS** (Cross-Origin Resource Sharing)
- **Rate limiting** (límite de peticiones)

### Interceptores
- **Auditoría de acciones específicas**
- **Validación de headers personalizados**
- **Transformación de requests/responses**
- **Medición de rendimiento**
- **Cache de respuestas**

## 📝 Notas Importantes

1. **Orden de ejecución**: Los filtros se ejecutan antes que los interceptores
2. **ContentCachingRequestWrapper**: Necesario para leer el body del request múltiples veces
3. **ContentCachingResponseWrapper**: Necesario para leer el body del response múltiples veces
4. **@Order**: Controla el orden de ejecución de múltiples filtros
5. **addPathPatterns()**: Define qué rutas interceptar
6. **excludePathPatterns()**: Define qué rutas excluir

## 🔧 Personalización

### Cambiar el usuario en las peticiones

El proyecto busca el usuario en el header `X-User`. Puedes cambiarlo en:
- `AuditFilter.getUserFromRequest()`
- `AuditInterceptor.getUserFromRequest()`

En un proyecto real, esto podría venir de:
- Token JWT
- Sesión HTTP
- Base de datos

### Persistir logs en base de datos

1. Convertir `AuditLog` en una entidad JPA
2. Crear `AuditLogRepository`
3. Modificar `AuditService` para guardar en BD en lugar de memoria

## 📚 Recursos Adicionales

- [Spring Filters Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/filters.html)
- [Spring Interceptors Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/interceptors.html)
