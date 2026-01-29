# Descubrimiento de Servicios con Eureka

## Contenido del tema

### ¿Qué es el descubrimiento de servicios?

En arquitecturas de microservicios, los servicios se despliegan de forma dinámica (escalado, reinicios, múltiples instancias). Conocer la ubicación (host y puerto) de cada servicio de forma manual se vuelve inviable. El **descubrimiento de servicios** consiste en que los servicios se **registran** en un registro central y los consumidores **descubren** dónde están las instancias disponibles sin hardcodear URLs.

### Netflix Eureka

**Eureka** es un componente de Netflix (parte de Spring Cloud Netflix) que actúa como **servidor de descubrimiento**:

1. **Eureka Server**: registro central donde los microservicios se registran y envían latidos (heartbeats).
2. **Eureka Client**: integrado en cada microservicio; al arrancar se registra en el servidor y puede consultar el registro para descubrir otros servicios.

Ventajas principales:

- No hace falta configurar URLs fijas entre servicios; se resuelven por **nombre lógico** (por ejemplo `greeting-service`).
- Soporte para **múltiples instancias** del mismo servicio (balanceo de carga a nivel de cliente o con un API Gateway).
- **Auto-registro**: los clientes se registran al arrancar y se dan de baja al parar.
- Integración natural con Spring Boot y el ecosistema Spring Cloud.

### Componentes

| Componente       | Rol                                                                 |
|------------------|---------------------------------------------------------------------|
| **Eureka Server** | Registro central; mantiene la lista de servicios e instancias.     |
| **Eureka Client** | Se registra en el servidor y consulta el registro para descubrir.  |

### Flujo básico

1. El **Eureka Server** arranca y expone un dashboard y una API REST (por defecto en el puerto 8761).
2. Cada **microservicio** con el cliente Eureka arranca, se registra en el servidor con su nombre y metadatos (host, puerto, health).
3. Periódicamente los clientes envían **heartbeats** para indicar que siguen vivos; si no hay latido, el servidor elimina la instancia del registro.
4. Cuando un servicio A quiere llamar al servicio B, consulta a Eureka por el nombre lógico de B, obtiene una o varias instancias (host:puerto) y realiza la llamada (por ejemplo con `RestTemplate`, `WebClient` o un cliente con balanceo integrado).

### Conceptos clave

- **Service ID / Application name**: nombre lógico del servicio (p. ej. `greeting-service`). Se define en `spring.application.name`.
- **Instance**: una instancia concreta (host + puerto) de un servicio registrado.
- **Default zone**: URL del Eureka Server donde se registran y consultan los clientes (`eureka.client.service-url.defaultZone`).

### Buenas prácticas

- **Alta disponibilidad**: en producción, desplegar más de un Eureka Server y configurar los clientes con varias URLs (`defaultZone` con lista separada por comas) para que se replique el registro.
- **Health checks**: usar Spring Boot Actuator y que Eureka use el health del actuator para marcar instancias como UP/DOWN.
- **Seguridad**: en entornos sensibles, proteger el dashboard y la API de Eureka (p. ej. con Spring Security).
- **Nombres estables**: usar nombres de aplicación coherentes (`spring.application.name`) para que el descubrimiento sea predecible.

---

## Proyecto de ejemplo (service-discovery-project)

Este proyecto incluye: **eureka-server** (servidor de descubrimiento, puerto 8761), **greeting-service** (microservicio que se registra y expone `GET /api/hello`), y **demo-client** (descubre y llama a `greeting-service` por nombre).

### Estructura

```
service-discovery-project/
├── README.md
├── compose.yml
├── .gitignore
├── eureka-server/          # Servidor de descubrimiento (puerto 8761)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── README.md
│   └── src/main/
│       ├── java/.../EurekaServerApplication.java
│       └── resources/
│           ├── application.yml
│           └── application-docker.yml
├── greeting-service/       # Microservicio que se registra en Eureka (puerto 8081)
│   ├── pom.xml
│   ├── Dockerfile
│   ├── README.md
│   └── src/main/
│       ├── java/.../GreetingServiceApplication.java
│       │         .../controller/GreetingController.java
│       └── resources/
│           ├── application.yml
│           └── application-docker.yml
└── demo-client/            # Cliente que descubre y llama a greeting-service (puerto 8080)
    ├── pom.xml
    ├── Dockerfile
    ├── README.md
    └── src/main/
        ├── java/.../DemoClientApplication.java
        │         .../controller/DiscoveryDemoController.java
        │         .../service/GreetingClientService.java
        └── resources/
            ├── application.yml
            └── application-docker.yml
```

## Cómo ejecutar (Docker Compose)

**Requisitos:** Docker y Docker Compose.

1. Levantar todos los servicios:

   ```bash
   docker compose up --build
   ```

2. Esperar a que Eureka, greeting-service y demo-client estén listos (demo-client depende de los otros).

3. Probar:

   - **Dashboard Eureka:** http://localhost:8761  
     Deberías ver `GREETING-SERVICE` y `DEMO-CLIENT` registrados.

   - **Saludo directo (greeting-service):**  
     ```bash
     curl http://localhost:8081/api/hello
     ```

   - **Descubrimiento (demo-client llama a greeting-service por nombre):**  
     ```bash
     curl http://localhost:8080/api/discover-and-call
     ```  
     La respuesta incluye la instancia descubierta y la respuesta del saludo.

**Puertos:**

| Servicio        | Puerto | Descripción                                      |
|-----------------|--------|--------------------------------------------------|
| Eureka Server   | 8761   | Dashboard y API de descubrimiento               |
| Greeting Service| 8081   | Endpoint `GET /api/hello`                       |
| Demo Client     | 8080   | Endpoint `GET /api/discover-and-call` (descubre y llama) |

Para ejecutar en segundo plano: `docker compose up -d --build`.

## Cómo probar el descubrimiento de servicios

Tras levantar el proyecto con Docker Compose (o en local), sigue estos pasos para verificar que Eureka descubre y resuelve los servicios por nombre.

### 1. Levantar el proyecto

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Espera a que los tres servicios estén en marcha. El **demo-client** arranca cuando Eureka y **greeting-service** ya están registrados.

### 2. Comprobar el registro en Eureka

Abre en el navegador el **dashboard de Eureka**:

| URL | Descripción |
|-----|-------------|
| http://localhost:8761 | Dashboard Eureka (registro de servicios) |

Deberías ver **GREETING-SERVICE** y **DEMO-CLIENT** con al menos una instancia cada uno. Eso confirma que ambos se han registrado en el servidor de descubrimiento.

Para ver el detalle de un servicio (instancias, host, puerto), haz clic en su nombre en el dashboard.

### 3. Probar el descubrimiento (llamada por nombre)

El **demo-client** descubre `greeting-service` por nombre en Eureka y llama a su API sin conocer host ni puerto. Ejecuta:

```bash
curl http://localhost:8080/api/discover-and-call
```

**Respuesta esperada (ejemplo):**

```json
{
  "success": true,
  "discoveredInstance": "172.x.x.x:8081",
  "greetingResponse": {
    "message": "Hola desde el servicio de saludo",
    "service": "greeting-service"
  }
}
```

- **discoveredInstance**: host y puerto de la instancia que Eureka devolvió para `greeting-service`.
- **greetingResponse**: respuesta del endpoint `GET /api/hello` de esa instancia.

Si obtienes `success: true` y un `greetingResponse` con el mensaje del greeting-service, el descubrimiento está funcionando correctamente.

### 4. Pruebas opcionales

| Acción | URL o comando |
|--------|----------------|
| Llamar directamente al greeting-service (sin descubrimiento) | `curl http://localhost:8081/api/hello` |
| Ver instancias de un servicio en Eureka | http://localhost:8761 → clic en el nombre del servicio |

### URLs de referencia para probar

| Servicio | URL | Uso |
|----------|-----|-----|
| Eureka Server (dashboard) | http://localhost:8761 | Ver servicios registrados |
| Eureka API | http://localhost:8761/eureka/ | API REST del registro |
| Greeting Service (directo) | http://localhost:8081/api/hello | Saludo sin descubrimiento |
| Demo Client (descubrimiento) | http://localhost:8080/api/discover-and-call | Descubre greeting-service y llama a /api/hello |

## Cómo ejecutar en local (sin Docker)

1. **Eureka Server:**  
   ```bash
   cd eureka-server && mvn spring-boot:run
   ```
   Esperar a que arranque (http://localhost:8761).

2. **Greeting Service:**  
   ```bash
   cd greeting-service && mvn spring-boot:run
   ```
   Se registra en Eureka en el puerto 8081.

3. **Demo Client:**  
   ```bash
   cd demo-client && mvn spring-boot:run
   ```
   Se registra en Eureka en el puerto 8080.

4. Probar igual que arriba:  
   `curl http://localhost:8080/api/discover-and-call`

## Endpoints de referencia

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | http://localhost:8761 | Dashboard Eureka |
| GET | http://localhost:8081/api/hello | Saludo del greeting-service |
| GET | http://localhost:8080/api/discover-and-call | Demo: descubre greeting-service y llama a /api/hello |

## Próximos pasos

- Añadir **balanceo de carga** (p. ej. `@LoadBalanced` RestTemplate o Spring Cloud LoadBalancer) para elegir entre varias instancias de un mismo servicio.
- Integrar con **API Gateway** (Spring Cloud Gateway) que use Eureka para enrutar por nombre.
- Configurar **varios nodos Eureka** para alta disponibilidad del registro.
