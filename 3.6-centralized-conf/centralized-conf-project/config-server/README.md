# Config Server

Spring Cloud Config Server: sirve la configuración centralizada desde la carpeta `config-repo/` (modo **native**).

## Rol

- Expone la configuración vía HTTP para que las aplicaciones cliente la consuman al arrancar.
- En Docker lee los YAML desde `file:/app/config-repo` (carpeta montada desde el proyecto).

## Cómo ejecutar

Desde la raíz del proyecto:

```bash
docker compose up --build
```

El Config Server arranca en el puerto **8888** y debe estar listo antes que el config-client (healthcheck).

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `http://localhost:8888/config-client/default` | Config de `config-client`, perfil default. |
| GET | `http://localhost:8888/config-client/docker` | Config de `config-client`, perfil docker. |
| GET | `http://localhost:8888/config-client/dev` | Config de `config-client`, perfil dev. |
| GET | `http://localhost:8888/config-client/stag` | Config de `config-client`, perfil stag. |
| GET | `http://localhost:8888/config-client/prod` | Config de `config-client`, perfil prod. |

Formato genérico: `GET /{application}/{profile}`.

## Configuración

- **application-docker.yml**: perfil Docker; `search-locations: file:/app/config-repo`.
- En compose se activan los perfiles `native,docker` y se monta `./config-repo:/app/config-repo`.

Ver el [README principal](../README.md) del proyecto para más contexto.
