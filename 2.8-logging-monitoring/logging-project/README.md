# Logging y Monitoreo con SLF4J y Logback

Proyecto simple para entender **logging y monitoreo** en aplicaciones Spring Boot usando **SLF4J** y **Logback**.

## 🎯 Objetivo

Este proyecto demuestra cómo implementar **logging y monitoreo** en aplicaciones Spring Boot, incluyendo:
- Configuración de Logback
- Diferentes niveles de logging (DEBUG, INFO, WARN, ERROR)
- Logging en archivos con rotación
- Monitoreo con Spring Boot Actuator
- Health checks y métricas
- Mejores prácticas de logging y monitoreo

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **SLF4J** (Simple Logging Facade for Java) - API de logging
- **Logback** (Implementación de SLF4J) - Motor de logging
- **Spring Boot Actuator** - Monitoreo y métricas
- **Lombok** (reduce boilerplate)

## 📋 Niveles de Logging

| Nivel | Cuándo usar | Ejemplo |
|-------|-------------|---------|
| **DEBUG** | Información detallada para desarrollo | Parámetros de métodos, valores intermedios |
| **INFO** | Información importante del flujo | Inicio de operaciones, resultados exitosos |
| **WARN** | Situaciones inusuales pero no críticas | Recursos accedidos de forma inusual |
| **ERROR** | Errores que requieren atención | Excepciones, fallos en operaciones críticas |

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── controller/
│   ├── HomeController.java          # Endpoints que demuestran diferentes niveles
│   ├── ExampleController.java       # Ejemplos prácticos de logging
│   └── MonitoringController.java    # Información sobre endpoints de monitoreo
├── service/
│   └── ExampleService.java          # Logging en capa de servicio
└── DemoApplication.java             # Clase principal

src/main/resources/
├── application.yml                  # Configuración de la aplicación
└── logback-spring.xml               # Configuración de Logback
```

## 🚀 Cómo arrancar

### Opción 1: Con Docker Compose

```bash
# 1. Copiar archivo de configuración (opcional)
cp .env.sample .env

# 2. Iniciar servicios
docker compose up --build

# 3. La API estará disponible en http://localhost:8080
# Los logs se guardan en ./logs/ (montado como volumen)
```

**Nota**: El perfil `docker` se activa automáticamente en Docker Compose. Los logs se guardan tanto en consola como en archivos en `./logs/`.

### Opción 2: Sin Docker

```bash
# Ejecutar directamente
mvn spring-boot:run
```

## 📝 Configuración de Logback

El archivo `logback-spring.xml` configura:

1. **Appender de Consola**: Muestra logs en la consola con colores
2. **Appender de Archivo**: Guarda logs en archivos con rotación diaria
3. **Appender de Errores**: Archivo separado solo para errores
4. **Niveles por Paquete**: Configuración específica por paquete
5. **Perfiles**: Configuración diferente para dev/prod

### Ubicación de Logs

- **Logs generales**: `logs/application.log`
- **Logs de errores**: `logs/application-error.log`
- **Logs rotados**: `logs/application-YYYY-MM-DD.log`

## 🧪 Cómo probar

### 1. Endpoint raíz

```bash
curl http://localhost:8080/
```

Muestra información de la API y endpoints disponibles.

### 2. Logs de nivel INFO

```bash
curl http://localhost:8080/api/info
```

**Logs generados:**
```
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] INFO  c.e.d.controller.HomeController - Endpoint /api/info llamado
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] INFO  c.e.d.controller.HomeController - Este es un mensaje de nivel INFO
```

### 3. Logs de nivel DEBUG

```bash
curl http://localhost:8080/api/debug
```

**Nota**: Los logs DEBUG solo aparecen si el nivel está configurado en DEBUG. Revisa el archivo `logs/application.log` para verlos.

### 4. Logs de nivel WARN

```bash
curl http://localhost:8080/api/warn
```

**Logs generados:**
```
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] WARN  c.e.d.controller.HomeController - Endpoint /api/warn llamado
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] WARN  c.e.d.controller.HomeController - Este es un mensaje de nivel WARN
```

### 5. Logs de nivel ERROR

```bash
curl http://localhost:8080/api/error
```

**Logs generados:**
```
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] ERROR c.e.d.controller.HomeController - Endpoint /api/error llamado
2024-01-27 10:00:00.123 [http-nio-8080-exec-1] ERROR c.e.d.controller.HomeController - Error capturado en endpoint /api/error
java.lang.RuntimeException: Ejemplo de excepción para logging
    at com.example.demo.controller.HomeController.error(HomeController.java:XX)
    ...
```

### 6. Ejemplos prácticos

```bash
# Procesar datos
curl "http://localhost:8080/api/example/process?input=hola"

# Operación costosa (con logging de rendimiento)
curl http://localhost:8080/api/example/expensive

# Logging condicional
curl "http://localhost:8080/api/example/conditional?condition=true"
```

## 📊 Monitoreo con Spring Boot Actuator

Spring Boot Actuator proporciona endpoints para monitorear y gestionar la aplicación.

### Endpoints Disponibles

#### 1. Health Check (`/actuator/health`)

Verifica el estado de salud de la aplicación.

```bash
curl http://localhost:8080/actuator/health
```

**Respuesta:**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 400000000000,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

**Estados posibles:**
- `UP`: La aplicación está funcionando correctamente
- `DOWN`: La aplicación tiene problemas
- `OUT_OF_SERVICE`: La aplicación está fuera de servicio

#### 2. Métricas (`/actuator/metrics`)

Obtiene todas las métricas disponibles.

```bash
# Listar todas las métricas disponibles
curl http://localhost:8080/actuator/metrics

# Obtener una métrica específica
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
curl http://localhost:8080/actuator/metrics/system.cpu.usage
```

**Ejemplo de respuesta:**
```json
{
  "name": "jvm.memory.used",
  "description": "The amount of used memory",
  "baseUnit": "bytes",
  "measurements": [
    {
      "statistic": "VALUE",
      "value": 123456789
    }
  ],
  "availableTags": []
}
```

**Métricas útiles:**
- `jvm.memory.used`: Memoria usada por la JVM
- `jvm.memory.max`: Memoria máxima disponible
- `http.server.requests`: Peticiones HTTP recibidas
- `system.cpu.usage`: Uso de CPU del sistema
- `process.uptime`: Tiempo de ejecución de la aplicación

#### 3. Información de la Aplicación (`/actuator/info`)

Información sobre la aplicación.

```bash
curl http://localhost:8080/actuator/info
```

**Respuesta:**
```json
{
  "app": {
    "name": "logging-project",
    "description": "Proyecto simple para entender logging y monitoreo",
    "version": "1.0.0",
    "encoding": "UTF-8",
    "java": {
      "version": "17"
    }
  }
}
```

### 4. Información de Monitoreo

```bash
curl http://localhost:8080/api/monitoring
```

Muestra información sobre los endpoints de monitoreo disponibles.

## 🔍 Casos de Uso del Monitoreo

### Health Check en Producción

Los health checks son útiles para:
- **Load Balancers**: Determinar si un servidor está disponible
- **Orquestadores**: Kubernetes, Docker Swarm verifican la salud
- **Alertas**: Notificar cuando la aplicación está DOWN

### Métricas para Análisis

Las métricas permiten:
- **Rendimiento**: Ver tiempos de respuesta, uso de CPU/memoria
- **Escalabilidad**: Decidir cuándo escalar horizontalmente
- **Debugging**: Identificar problemas de rendimiento

### Información para DevOps

El endpoint `/actuator/info` ayuda a:
- **Versionado**: Saber qué versión está en producción
- **Configuración**: Verificar configuración de la aplicación

## 📚 Mejores Prácticas

### 1. Usar Logger estático final

```java
private static final Logger logger = LoggerFactory.getLogger(ClassName.class);
```

### 2. Usar placeholders en lugar de concatenación

✅ **Bien:**
```java
logger.info("Usuario {} autenticado correctamente", username);
```

❌ **Mal:**
```java
logger.info("Usuario " + username + " autenticado correctamente");
```

### 3. Verificar nivel antes de operaciones costosas

```java
if (logger.isDebugEnabled()) {
    logger.debug("Datos complejos: {}", expensiveOperation());
}
```

### 4. Incluir contexto en los logs

```java
logger.info("Procesando pedido. ID: {}, Usuario: {}", orderId, userId);
```

### 5. Usar niveles apropiados

- **DEBUG**: Solo para desarrollo
- **INFO**: Flujo normal de la aplicación
- **WARN**: Situaciones inusuales
- **ERROR**: Errores que requieren atención

## 🔍 Ver Logs

### En consola

Los logs aparecen directamente en la consola cuando ejecutas la aplicación.

### En archivos

```bash
# Ver logs generales
tail -f logs/application.log

# Ver solo errores
tail -f logs/application-error.log

# Buscar errores
grep ERROR logs/application.log
```

## ⚙️ Configuración Avanzada

### Cambiar nivel de logging

En `application.yml`:
```yaml
logging:
  level:
    com.example.demo: DEBUG  # Cambiar a INFO, WARN, ERROR según necesidad
```

### Usar perfiles

```bash
# Desarrollo (más verboso)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Docker (logs en consola y archivo)
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# Producción (menos verboso)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

**Perfiles disponibles:**
- `dev`: Desarrollo local, nivel DEBUG, logs en consola
- `docker`: Para contenedores Docker, nivel INFO/DEBUG, logs en consola y archivo
- `prod`: Producción, nivel INFO, solo logs en archivo

## 📖 Conceptos Clave

### SLF4J (Simple Logging Facade for Java)

- **API de logging**: Interfaz común para logging
- **Abstracción**: Permite cambiar la implementación sin cambiar el código
- **No es una implementación**: Solo define la API

### Logback

- **Implementación de SLF4J**: Motor de logging real
- **Rápido y flexible**: Configuración mediante XML
- **Rotación automática**: Gestiona archivos de log automáticamente

### Logger vs System.out.println

✅ **Logger:**
- Niveles configurables
- Rotación de archivos
- Filtrado por paquete/clase
- Mejor rendimiento

❌ **System.out.println:**
- No tiene niveles
- No se puede desactivar
- No se guarda en archivos
- Más lento

## 🎓 Ejercicios Sugeridos

1. **Modificar niveles**: Cambia el nivel de logging en `application.yml` y observa qué logs aparecen
2. **Agregar logging**: Añade logs en un nuevo endpoint
3. **Logging estructurado**: Experimenta con formato JSON (requiere dependencia adicional)
4. **Métricas**: Integra logging con métricas de aplicación

## 📚 Recursos Adicionales

### Logging
- [Documentación de Logback](http://logback.qos.ch/)
- [Documentación de SLF4J](http://www.slf4j.org/)
- [Spring Boot Logging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

### Monitoreo
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Actuator Endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)
- [Métricas con Micrometer](https://micrometer.io/)
