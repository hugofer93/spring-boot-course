# Config Client

Aplicación Spring Boot que obtiene su configuración del **Config Server** al arrancar.

## Rol

- Cliente de Spring Cloud Config: se conecta al Config Server (`http://config-server:8888` en Docker) y carga las propiedades antes de iniciar el contexto.
- Expone un endpoint de ejemplo para ver las propiedades cargadas.

## Cómo ejecutar

Desde la raíz del proyecto:

```bash
docker compose up --build
```

El config-client arranca en el puerto **8080** después de que el Config Server esté listo (healthcheck).

## Endpoint de ejemplo

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `http://localhost:8080/api/config-demo` | Devuelve las propiedades cargadas del Config Server (`appName`, `description`, `sharedMessage`, `customGreeting`, `customVersion`, etc.). El contenido depende del perfil activo (`SPRING_PROFILES_ACTIVE` en compose). |

## Configuración

- **application.yml**: `spring.application.name=config-client` (el Config Server sirve `config-client.yml` y `config-client-{profile}.yml`).
- **application-docker.yml**: `spring.config.import=configserver:http://config-server:8888`; perfil activo en Docker: `docker` (cambiable a `dev`, `stag`, `prod` en compose).

Ver el [README principal](../README.md) del proyecto para más contexto.
