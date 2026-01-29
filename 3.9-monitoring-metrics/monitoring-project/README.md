# Monitoreo de aplicaciones con Actuator y métricas

## Contenido del tema

### ¿Qué es el monitoreo de aplicaciones?

En producción necesitamos saber si la aplicación está viva, cuánta memoria usa, cuántas peticiones recibe o si hay errores. El **monitoreo** consiste en exponer **estado** (salud, información de la app) y **métricas** (contadores, tiempos, uso de recursos) de forma estándar para integrarlos con dashboards, alertas o sistemas externos (Prometheus, Grafana, etc.).

### Spring Boot Actuator

**Actuator** es un módulo de Spring Boot que añade endpoints HTTP (y JMX) para **monitoreo y gestión** de la aplicación:

1. **Health**: estado de salud (UP/DOWN) y detalles opcionales (disco, base de datos, etc.).
2. **Metrics**: métricas de la JVM, HTTP, sistema y métricas personalizadas (Micrometer).
3. **Info**: información de la aplicación (nombre, versión, variables de entorno, etc.).

Ventajas principales:

- Endpoints estándar y configurables (`/actuator/health`, `/actuator/metrics`, `/actuator/info`).
- Integración con **Micrometer** para métricas (contadores, timers, gauges) y exportación a Prometheus, Graphite, etc.
- Configuración por propiedades (`management.endpoints.web.exposure.include`).
- Útil para healthchecks en contenedores, orquestadores y balanceadores.

### Componentes

| Componente        | Rol                                                                 |
|-------------------|---------------------------------------------------------------------|
| **Actuator**      | Expone endpoints de monitoreo (health, metrics, info, etc.).      |
| **Micrometer**    | API de métricas usada por Actuator; permite contadores, timers, etc.|
| **Health indicators** | Componentes que contribuyen al estado de salud (disco, DB, etc.). |

### Endpoints útiles (resumen)

| Endpoint           | Uso típico                                              |
|--------------------|---------------------------------------------------------|
| `/actuator/health` | Estado de salud (UP/DOWN); usado en Docker/K8s healthchecks. |
| `/actuator/metrics` | Lista de métricas disponibles; `/actuator/metrics/{name}` para una métrica. |
| `/actuator/info`  | Información de la aplicación (nombre, versión, etc.).   |
| `/actuator/env`  | Variables de entorno y propiedades cargadas (valores visibles con `management.endpoint.env.show-values: always`; por defecto Spring Boot los enmascara con `******` por seguridad).   |

### Conceptos clave

- **Exposición**: por defecto solo `health` está expuesto por HTTP; para más endpoints se usa `management.endpoints.web.exposure.include` (p. ej. `health,metrics,info`).
- **Health details**: `management.endpoint.health.show-details` controla si se muestran detalles (siempre, solo autorizados, nunca).
- **Métricas**: Spring Boot registra automáticamente métricas JVM, HTTP y de sistema; puedes registrar métricas propias con `MeterRegistry` (Micrometer).

### Buenas prácticas

- **Producción**: no exponer `*` (todos los endpoints) en público; exponer solo los necesarios y proteger con seguridad (Spring Security).
- **Health**: usar `/actuator/health` en healthchecks de contenedores y orquestadores.
- **Métricas**: usar Micrometer para métricas de negocio o operativas; integrar con Prometheus/Grafana si necesitas dashboards.
- **Info**: rellenar `info.*` en `application.yml` o con `InfoContributor` para identificar versión y entorno en `/actuator/info`.

---

## Proyecto de ejemplo (monitoring-project)

Este proyecto es una única aplicación Spring Boot que expone Actuator (health, metrics, info) y una **métrica personalizada** (`demo.operations`) al llamar a `GET /api/ping`.

### Estructura

```
monitoring-project/
├── README.md
├── compose.yml
├── .gitignore
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/example/demo/
    │   ├── DemoApplication.java
    │   └── controller/DemoController.java
    └── resources/
        ├── application.yml
        └── application-docker.yml
```

### Cómo ejecutar

**Requisitos:** Java 17 y Maven, o Docker y Docker Compose.

#### Opción 1: Local (Maven)

Desde la raíz del proyecto (monitoring-project):

```bash
mvn spring-boot:run
```

#### Opción 2: Docker Compose

```bash
docker compose up --build
```

La aplicación queda en **http://localhost:8080**.

### Cómo probar Actuator y métricas

1. **Health** (estado de la aplicación):

   ```bash
   curl http://localhost:8080/actuator/health
   ```

   Respuesta esperada (ejemplo): `{"status":"UP", ...}` con detalles (disco, etc.) si `show-details` está en `always`.

2. **Lista de métricas**:

   ```bash
   curl http://localhost:8080/actuator/metrics
   ```

   Devuelve los nombres de todas las métricas disponibles (JVM, HTTP, sistema y la personalizada `demo.operations`).

3. **Una métrica concreta** (p. ej. JVM memoria, o la métrica personalizada):

   ```bash
   curl http://localhost:8080/actuator/metrics/jvm.memory.used
   curl http://localhost:8080/actuator/metrics/demo.operations
   ```

   Después de llamar varias veces a `GET /api/ping`, `demo.operations` tendrá un `count` mayor.

4. **Info** (información de la aplicación):

   ```bash
   curl http://localhost:8080/actuator/info
   ```

   Muestra la información configurada en `info.*` (nombre, descripción, versión).

5. **Generar la métrica personalizada**:

   ```bash
   curl http://localhost:8080/api/ping
   ```

   Cada llamada incrementa el contador `demo.operations`; luego comprueba con `curl http://localhost:8080/actuator/metrics/demo.operations`.

### URLs de referencia

| Uso                    | URL |
|------------------------|-----|
| Health                 | http://localhost:8080/actuator/health |
| Lista de métricas      | http://localhost:8080/actuator/metrics |
| Métrica concreta       | http://localhost:8080/actuator/metrics/{name} (p. ej. `jvm.memory.used`, `demo.operations`) |
| Info                   | http://localhost:8080/actuator/info |
| Env (variables y propiedades) | http://localhost:8080/actuator/env |
| Endpoint de ejemplo    | http://localhost:8080/api/ping (incrementa `demo.operations`) |

### Próximos pasos

- Añadir **Spring Security** y exponer Actuator solo para usuarios autorizados.
- Integrar **Prometheus** (`micrometer-registry-prometheus`) y **Grafana** para dashboards.
- Implementar **HealthIndicator** personalizado (p. ej. comprobando un servicio externo).
- Usar **tags** en métricas personalizadas para segmentar por operación o entorno.
