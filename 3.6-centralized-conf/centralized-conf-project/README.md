# Configuración centralizada con Spring Cloud Config

## Contenido del tema

### ¿Qué es la configuración centralizada?

En aplicaciones distribuidas o microservicios, cada servicio suele tener su propia configuración (propiedades, URLs, credenciales, feature flags). Mantenerla en cada aplicación genera:

- **Duplicación**: la misma propiedad en muchos `application.yml`
- **Riesgo**: cambios requieren redesplegar cada servicio
- **Inconsistencia**: entornos (dev, test, prod) difíciles de alinear

La **configuración centralizada** consiste en tener un único punto donde se almacenan y sirven las propiedades; los clientes las obtienen al arrancar (y opcionalmente al refrescar).

### Spring Cloud Config

**Spring Cloud Config** ofrece:

1. **Config Server**: servidor que expone configuración (desde Git, sistema de archivos o Vault).
2. **Config Client**: librería que usan las aplicaciones para obtener su configuración del servidor al iniciar.

Ventajas principales:

- Una sola fuente de verdad para la configuración
- Soporte de **perfiles** (dev, test, prod) y **etiquetas/ramas** en Git
- **Refresco** de propiedades en caliente (con `@RefreshScope`) sin reiniciar
- Cifrado de valores sensibles (opcional)
- Integración con ecosistema Spring (Boot, Cloud)

### Componentes

| Componente        | Rol                                                                 |
|-------------------|---------------------------------------------------------------------|
| **Config Server** | Sirve propiedades desde un backend (Git, sistema de archivos, etc.) |
| **Config Client** | Arranca, solicita su config al server y la usa como si fuera local  |
| **Repositorio**   | Origen de los ficheros (Git repo o carpetas en modo "native")       |

### Modos de backend del Config Server

- **Git**: configuración en un repositorio Git (recomendado en producción).
- **Native**: ficheros en el sistema de archivos o en el classpath del server (útil para demos y desarrollo).
- **Vault**: secretos en HashiCorp Vault (para datos sensibles).

### Flujo básico

1. El **Config Client** arranca y lee `spring.config.import` (o la propiedad legacy `spring.cloud.config.uri`).
2. Realiza una petición al Config Server, por ejemplo:  
   `GET /{application}/{profile}[/{label}]`  
   donde `application` = nombre de la aplicación, `profile` = perfil activo (dev, prod), `label` = rama/etiqueta (en Git).
3. El Config Server resuelve la configuración (p. ej. `{application}-{profile}.yml`) y la devuelve.
4. El cliente integra esas propiedades en su `Environment` y la aplicación arranca con la config centralizada.

### Buenas prácticas

- **Nomenclatura**: usar `{application}.yml` y `{application}-{profile}.yml` para que el server resuelva por nombre y perfil.
- **Secretos**: no subir contraseñas o API keys en texto plano; usar cifrado del Config Server o un backend como Vault.
- **Alta disponibilidad**: en producción, desplegar el Config Server con redundancia y un backend fiable (Git remoto, almacenamiento compartido).
- **Refresco**: usar `@RefreshScope` en beans que deban recargar propiedades sin reiniciar la aplicación.

---

## Proyecto de ejemplo

Este directorio contiene un ejemplo **mínimo** con:

- **config-repo**: carpeta con los YAML de configuración; en Docker se monta en el Config Server (sin Git).
- **config-server**: Config Server en modo **native**; en Docker lee la config desde `config-repo/`.
- **config-client**: aplicación Spring Boot que obtiene su configuración del Config Server.

### Estructura

```
centralized-conf-project/
├── README.md
├── compose.yml
├── .gitignore
├── config-repo/             # Repo de config (montado en Docker; editas aquí)
│   ├── application.yml
│   ├── config-client.yml
│   ├── config-client-docker.yml
│   ├── config-client-dev.yml
│   ├── config-client-stag.yml
│   └── config-client-prod.yml
├── config-server/          # Servidor de configuración
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/.../ConfigServerApplication.java
│       └── resources/
│           ├── application.yml
│           └── application-docker.yml   # apunta a file:/app/config-repo
└── config-client/          # Cliente que consume la config
    ├── pom.xml
    ├── Dockerfile
    └── src/main/
        ├── java/.../ConfigClientApplication.java
        │         .../controller/ConfigDemoController.java
        │         .../config/AppProperties.java
        └── resources/
            └── application.yml   # spring.config.import apunta al Config Server
```

### Cómo ejecutar (todo en Docker)

**Requisitos:** Docker y Docker Compose. No hace falta Java ni Maven en tu máquina.

1. **Levantar todos los servicios** (Config Server y Config Client en contenedores):

   ```bash
   docker compose up --build
   ```

2. **Probar** que el cliente usa la configuración centralizada (desde tu máquina, los puertos están publicados):

   ```bash
   curl http://localhost:8080/api/config-demo
   ```

   La respuesta mostrará propiedades cargadas desde el Config Server.

**Puertos:**

- Config Server: `http://localhost:8888`
- Config Client: `http://localhost:8080`

El Config Client arranca solo cuando el Config Server está listo (healthcheck). Si quieres ejecutar en segundo plano: `docker compose up -d --build`.

**Editar la configuración:** los ficheros en `config-repo/` están montados en el contenedor. Si cambias `config-repo/application.yml` o `config-repo/config-client.yml`, el Config Server leerá los nuevos valores en la siguiente petición (no hace falta reconstruir la imagen; para que el cliente vea cambios puede hacer falta reiniciarlo o usar `@RefreshScope`).

**Perfiles (docker, dev, stag, prod):** hay ficheros por entorno en `config-repo/`:
- `config-client.yml` — base
- `config-client-docker.yml` — Docker (contenedores)
- `config-client-dev.yml` — desarrollo
- `config-client-stag.yml` — staging
- `config-client-prod.yml` — producción

Para usar otro perfil, cambia en `compose.yml` la variable del config-client:
`SPRING_PROFILES_ACTIVE=dev` (o `stag`, `prod`), vuelve a levantar y llama a `curl http://localhost:8080/api/config-demo` para ver los valores del perfil elegido.

### Rutas / endpoints para probar las configuraciones

**Config Client (puerto 8080)** — propiedades que usa la aplicación:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `http://localhost:8080/api/config-demo` | Devuelve las propiedades cargadas del Config Server (`appName`, `description`, `sharedMessage`, `customGreeting`, `customVersion`, etc.). Cambia según el perfil activo (`SPRING_PROFILES_ACTIVE`). |

**Config Server (puerto 8888)** — config en crudo por aplicación y perfil:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `http://localhost:8888/config-client/default` | Config de `config-client` con perfil **default**. |
| GET | `http://localhost:8888/config-client/docker` | Config de `config-client` con perfil **docker**. |
| GET | `http://localhost:8888/config-client/dev` | Config de `config-client` con perfil **dev**. |
| GET | `http://localhost:8888/config-client/stag` | Config de `config-client` con perfil **stag**. |
| GET | `http://localhost:8888/config-client/prod` | Config de `config-client` con perfil **prod**. |

Formato genérico del Config Server: `GET http://localhost:8888/{application}/{profile}` (por ejemplo, otra app tendría su propio `{application}` y ficheros en `config-repo/`).

### Endpoints útiles del Config Server (referencia)

- `GET /{application}/{profile}` — configuración para esa aplicación y perfil.
- `GET /{application}/{profile}/{label}` — igual con rama/etiqueta (en modo Git).

### Próximos pasos

- Cambiar el Config Server a backend **Git** (`spring.cloud.config.server.git.uri`).
- Añadir **cifrado** para propiedades sensibles.
- Usar **Spring Cloud Bus** + **@RefreshScope** para refrescar config en varios clientes sin reiniciar.
