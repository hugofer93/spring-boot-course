# Spring Boot Example - Proyecto de Ejemplo

Este proyecto es un ejemplo de estructura y buenas prácticas para proyectos Java con Spring Boot.

## 📋 Características

- ✅ Estructura de proyecto Maven estándar
- ✅ Separación de responsabilidades (Controller, Service, Repository)
- ✅ Validación de datos con Jakarta Validation
- ✅ Base de datos H2 (desarrollo) y PostgreSQL (producción)
- ✅ Configuración con Docker y Docker Compose
- ✅ Actuator para monitoreo
- ✅ Lombok para reducir boilerplate
- ✅ CORS configurado
- ✅ Logging configurado

## 🏗️ Estructura del Proyecto

```
spring-boot-example/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java          # Clase principal
│   │   │   ├── controller/                   # Controladores REST
│   │   │   │   ├── HomeController.java       # Controlador raíz
│   │   │   │   └── UserController.java
│   │   │   ├── service/                       # Lógica de negocio
│   │   │   │   └── UserService.java
│   │   │   ├── repository/                    # Acceso a datos
│   │   │   │   └── UserRepository.java
│   │   │   ├── model/                         # Entidades JPA
│   │   │   │   └── User.java
│   │   │   └── config/                        # Configuraciones
│   │   │       └── WebConfig.java
│   │   └── resources/
│   │       ├── application.yml                # Configuración desarrollo
│   │       └── application-docker.yml         # Configuración Docker
│   └── test/                                  # Tests
├── Dockerfile                                 # Imagen Docker
├── compose.yml                                # Orquestación Docker
├── pom.xml                                    # Configuración Maven
├── .env.sample                                # Plantilla de variables de entorno
├── .env                                       # Variables de entorno (crear desde .env.sample)
└── README.md                                  # Este archivo
```

## 🚀 Cómo Ejecutar

### Opción 1: Ejecución Local (con H2)

1. **Requisitos previos:**
   - Java 17 o superior
   - Maven 3.6+

2. **Compilar y ejecutar:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Acceder a la aplicación:**
   - Raíz: http://localhost:8080/ (información de la API)
   - API: http://localhost:8080/api/users
   - H2 Console: http://localhost:8080/h2-console
   - Actuator Health: http://localhost:8080/actuator/health

### Opción 2: Con Docker Compose

1. **Requisitos previos:**
   - Docker
   - Docker Compose

2. **Configurar variables de entorno:**
   
   Copia el archivo `.env.sample` a `.env` y ajusta los valores según tus necesidades:
   ```bash
   cp .env.sample .env
   ```
   
   El archivo `.env.sample` contiene una plantilla con todas las variables necesarias:
   ```env
   # Base de datos PostgreSQL
   POSTGRES_DB=demo_db
   POSTGRES_USER=demo_user
   POSTGRES_PASSWORD=demo-$3CR37
   
   # Spring Boot
   SPRING_PROFILES_ACTIVE=docker
   SPRING_CONFIG_LOCATION=classpath:/application-docker.yml
   DEBUG=True
   LOG_LEVEL=DEBUG
   ```
   
   **Nota:** `LOG_LEVEL` controla el nivel de logging (valores válidos: `DEBUG`, `INFO`, `WARN`, `ERROR`). `DEBUG` es un flag booleano para otras configuraciones.
   
   **Nota:** El archivo `.env` está en `.gitignore` y no se sube al repositorio por seguridad. El archivo `.env.sample` sí está en el repositorio como plantilla.

3. **Construir y ejecutar:**
   ```bash
   docker compose up -d --build
   ```

4. **Acceder a la aplicación:**
   - Raíz: http://localhost:8080/ (información de la API)
   - API: http://localhost:8080/api/users
   - Actuator Health: http://localhost:8080/actuator/health

### Opción 3: Solo la aplicación con Docker

```bash
docker build -t spring-boot-example .
docker run -p 8080:8080 spring-boot-example
```

## 📡 Endpoints de la API

### Información de la API

- `GET /` - Muestra información general de la API, versión, estado y endpoints disponibles

### Usuarios

- `GET /api/users` - Obtiene todos los usuarios
- `GET /api/users/{id}` - Obtiene un usuario por ID
- `POST /api/users` - Crea un nuevo usuario
- `PUT /api/users/{id}` - Actualiza un usuario
- `DELETE /api/users/{id}` - Elimina un usuario

### Ejemplo de creación de usuario:

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com"
  }'
```

## 🎯 Buenas Prácticas Implementadas

### 1. **Estructura de Paquetes**
- Separación clara de responsabilidades
- Paquetes por capa (controller, service, repository, model)
- Convenciones de nombres estándar

### 2. **Capa de Controladores**
- Uso de `@RestController` para APIs REST
- Validación de entrada con `@Valid`
- Códigos HTTP apropiados
- Manejo de excepciones

### 3. **Capa de Servicios**
- Lógica de negocio separada de los controladores
- Uso de `@Transactional` para operaciones de base de datos
- Inyección de dependencias mediante constructor

### 4. **Capa de Repositorios**
- Extensión de `JpaRepository` para operaciones CRUD
- Métodos personalizados con convenciones de Spring Data JPA

### 5. **Modelos/Entidades**
- Uso de Lombok para reducir boilerplate
- Validaciones con Jakarta Validation
- Campos de auditoría (createdAt, updatedAt)
- Callbacks JPA (@PrePersist, @PreUpdate)

### 6. **Configuración**
- Archivos YAML para configuración
- Perfiles de Spring (default, docker)
- Configuración centralizada

### 7. **Docker**
- Multi-stage build para optimizar tamaño
- Usuario no-root para seguridad
- Health checks
- Docker Compose para orquestación

## 🔧 Configuración

### Variables de Entorno

El proyecto utiliza un archivo `.env` para gestionar las variables de entorno. Docker Compose lee automáticamente este archivo.

#### Variables para Docker Compose

**Base de datos PostgreSQL:**
- `POSTGRES_DB`: Nombre de la base de datos (default: `demo_db`)
- `POSTGRES_USER`: Usuario de PostgreSQL (default: `demo_user`)
- `POSTGRES_PASSWORD`: Contraseña de PostgreSQL (default: `demo-$3CR37`)

**Spring Boot:**
- `SPRING_PROFILES_ACTIVE`: Perfil activo (default: `docker` para Docker, `default` para desarrollo local)
- `SPRING_CONFIG_LOCATION`: Ubicación del archivo de configuración (default: `classpath:/application-docker.yml`)
- `DEBUG`: Flag booleano para habilitar modo debug (valores: `True` o `False`)
- `LOG_LEVEL`: Nivel de logging (valores válidos: `DEBUG`, `INFO`, `WARN`, `ERROR`, `TRACE`, `OFF` - default: `INFO` para root, `DEBUG` para paquetes específicos)

#### Crear archivo `.env`

Para crear tu archivo `.env`, copia la plantilla `.env.sample`:

```bash
cp .env.sample .env
```

Luego edita el archivo `.env` y ajusta los valores según tus necesidades. El archivo `.env.sample` contiene valores de ejemplo que puedes usar como punto de partida.

**Importante:** 
- El archivo `.env` está en `.gitignore` y no debe subirse al repositorio por seguridad.
- El archivo `.env.sample` sí está en el repositorio como plantilla de referencia.
- Para producción, configura estas variables directamente en tu plataforma de despliegue.

### Perfiles de Spring

- **default**: Usa H2 en memoria (desarrollo local)
- **docker**: Usa PostgreSQL (producción/Docker)

## 📚 Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (desarrollo)
- **PostgreSQL** (producción)
- **Lombok**
- **Jakarta Validation**
- **Spring Actuator**
- **Maven**

## 🧪 Testing

Para ejecutar los tests:

```bash
mvn test
```

## 📝 Notas Adicionales

- El proyecto usa Java 17 (LTS)
- H2 Console está habilitada solo para desarrollo
- Los logs están configurados para mostrar SQL en desarrollo
- CORS está configurado para permitir requests desde localhost:3000 y localhost:8080
- Las variables de entorno se gestionan mediante el archivo `.env` (no incluido en el repositorio)
- Docker Compose utiliza health checks para asegurar que PostgreSQL esté listo antes de iniciar la aplicación

## 🤝 Contribuir

Este es un proyecto de ejemplo para aprendizaje. Siéntete libre de usarlo como base para tus proyectos.

## 📄 Licencia

Este proyecto es de código abierto y está disponible para uso educativo.
