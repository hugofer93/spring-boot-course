# config-repo

Carpeta con los ficheros YAML de configuración que sirve el **Config Server**. En Docker se monta en el contenedor del Config Server (`./config-repo:/app/config-repo`).

## Contenido

| Fichero | Uso |
|---------|-----|
| `application.yml` | Config compartida para todas las aplicaciones. |
| `config-client.yml` | Config base de la aplicación `config-client`. |
| `config-client-docker.yml` | Config de `config-client` para perfil **docker**. |
| `config-client-dev.yml` | Config de `config-client` para perfil **dev**. |
| `config-client-stag.yml` | Config de `config-client` para perfil **stag**. |
| `config-client-prod.yml` | Config de `config-client` para perfil **prod**. |

## Nomenclatura

- `{application}.yml` — config base de la aplicación.
- `{application}-{profile}.yml` — config por perfil (docker, dev, stag, prod).

El Config Server combina `application.yml` + `application-{profile}.yml` (si existe) y `{application}.yml` + `{application}-{profile}.yml` para cada petición `GET /{application}/{profile}`.

## Editar la configuración

Los ficheros se editan en tu máquina. En Docker están montados en el contenedor, así que el Config Server lee los cambios sin reconstruir la imagen. Para que un cliente ya en marcha vea cambios puede hacer falta reiniciarlo o usar `@RefreshScope`.

Ver el [README principal](../README.md) del proyecto para más contexto.
