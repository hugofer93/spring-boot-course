# Integración y despliegue continuo (CI/CD) con GitHub Actions o Jenkins

## Contenido del tema

### ¿Qué es CI/CD?

**CI (Integración Continua)** consiste en integrar los cambios del código con frecuencia y comprobar automáticamente que el proyecto compila y que las pruebas pasan. Así se detectan errores pronto y el código en el repositorio se mantiene en estado válido.

**CD (Despliegue Continuo o Entrega Continua)** consiste en llevar los artefactos validados por CI hasta entornos (staging, producción) de forma automatizada o semiautomatizada, reduciendo pasos manuales y el tiempo entre “código listo” y “aplicación desplegada”.

### Ventajas de CI/CD

- **Detección temprana de fallos**: cada push o PR dispara compilación y pruebas.
- **Menos errores en producción**: solo se despliegan artefactos que han pasado los checks.
- **Reproducibilidad**: el mismo flujo (build, test, deploy) se ejecuta siempre igual.
- **Documentación implícita**: el pipeline describe cómo se construye y despliega la aplicación.

### Herramientas habituales

| Herramienta        | Rol típico                                                                 |
|--------------------|----------------------------------------------------------------------------|
| **GitHub Actions** | CI/CD integrado en GitHub; workflows en YAML en `.github/workflows/`.       |
| **Jenkins**        | Servidor CI/CD autohospedado; pipelines en Jenkinsfile (Groovy) o UI.     |
| **GitLab CI**      | CI/CD integrado en GitLab; configuración en `.gitlab-ci.yml`.               |
| **Otros**          | CircleCI, Travis CI, Azure Pipelines, etc.                                |

### Flujo típico de un pipeline CI

1. **Checkout**: obtener el código del repositorio.
2. **Build**: compilar el proyecto (p. ej. `mvn clean package` o `mvn verify`).
3. **Test**: ejecutar pruebas unitarias e integración; fallar el pipeline si fallan.
4. **(Opcional) Deploy**: publicar artefactos (JAR, imagen Docker) o desplegar en un entorno.

### Buenas prácticas

- **Fail fast**: si los tests fallan, el pipeline debe fallar y no desplegar.
- **Cache de dependencias**: usar cache de Maven/Gradle en el agente para acelerar builds.
- **Artefactos**: guardar el JAR (o imagen) generado para despliegue o inspección.
- **Secrets**: no poner contraseñas o tokens en el código; usar variables de entorno o gestores de secretos (GitHub Secrets, Jenkins Credentials).
- **Idempotencia**: el pipeline debe dar el mismo resultado ante el mismo código.

---

## Proyecto de ejemplo (cicd-project)

Este proyecto es una aplicación Spring Boot mínima con un pipeline de **CI** (compilación y pruebas) usando **GitHub Actions** y un **Jenkinsfile** de ejemplo para Jenkins. No incluye despliegue automático; el foco está en build y test.

### Estructura

```
cicd-project/
├── README.md
├── .gitignore
├── pom.xml
├── Jenkinsfile
├── .github/
│   └── workflows/
│       └── build-and-test.yml    # Workflow de GitHub Actions
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── CicdProjectApplication.java
│   │   │   └── controller/HealthController.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/example/demo/
│           ├── CicdProjectApplicationTests.java
│           └── controller/HealthControllerTest.java
```

### Cómo ejecutar la aplicación en local

**Requisitos:** Java 17 y Maven.

```bash
mvn spring-boot:run
```

La aplicación queda en **http://localhost:8080**. Endpoint de ejemplo: `GET /api/health` → `{"status":"UP"}`.

### GitHub Actions

El workflow `.github/workflows/build-and-test.yml` se ejecuta en cada **push** y **pull request** a las ramas `main` y `master`.

**Pasos del workflow:**

1. **Checkout**: clona el repositorio.
2. **Set up JDK 17**: configura Java 17 (Eclipse Temurin) y cache de Maven.
3. **Build and run tests**: ejecuta `mvn -B clean verify` (compila y ejecuta pruebas).
4. **Upload JAR** (solo en push): sube el JAR generado como artefacto del workflow.

**Cómo probarlo:**

- Sube el proyecto a un repositorio en GitHub.
- Haz un push a `main` o `master`, o abre un pull request.
- En la pestaña **Actions** del repositorio verás el workflow "Build and Test" y el resultado de cada ejecución.

**Nota:** Si el repo está en una carpeta dentro de un monorepo, el workflow se ejecuta cuando hay cambios en esa ruta solo si configuras `paths` en `on.push`/`on.pull_request`; por defecto se ejecuta en cualquier push/PR.

### Jenkins

El archivo **Jenkinsfile** en la raíz define un pipeline con las etapas: Checkout → Build → Test → Verify. Al final, si todo va bien, se archivan los JAR de `target/`.

**Requisitos en Jenkins:**

- Maven y JDK 17 configurados en **Global Tool Configuration** (Manage Jenkins → Tools). Los nombres usados en el Jenkinsfile son `Maven-3.9` y `JDK-17`; puedes cambiarlos en el `Jenkinsfile` para que coincidan con los de tu Jenkins.
- Un **Pipeline job** asociado a este repositorio (Git) y que use el Jenkinsfile del repo.

**Pasos del pipeline:**

1. **Checkout**: obtiene el código desde el SCM.
2. **Build and Test**: ejecuta `mvn -B clean verify` (compila, ejecuta pruebas y genera el JAR). Los resultados JUnit se publican.
3. **Post success**: archiva los JAR de `target/` como artefactos del build.

Ajusta los nombres de las herramientas (`maven`, `jdk`) en el bloque `tools { ... }` del Jenkinsfile según tu configuración de Jenkins.

### Resumen de archivos CI/CD

| Archivo | Uso |
|--------|-----|
| `.github/workflows/build-and-test.yml` | Workflow de GitHub Actions: build + test en push/PR. |
| `Jenkinsfile` | Pipeline de Jenkins: checkout, build, test, verify, archivar JAR. |

### Próximos pasos

- Añadir un **job o workflow de CD** que despliegue el JAR o una imagen Docker (p. ej. a un servidor o a un registro).
- Usar **GitHub Secrets** o **Jenkins Credentials** para claves y URLs de despliegue.
- Añadir paso de **construcción de imagen Docker** y publicación en un registro (Docker Hub, GHCR, etc.).
- Configurar **notificaciones** (Slack, email) en caso de fallo del pipeline.
