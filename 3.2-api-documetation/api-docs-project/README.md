# Documentación de APIs con Swagger / OpenAPI 3

Proyecto simple para entender **documentación de APIs con Swagger/OpenAPI 3** en Spring Boot.

## 🎯 Objetivo

Este proyecto demuestra cómo documentar APIs RESTful de manera profesional usando **Swagger/OpenAPI 3**:

1. **Configuración de SpringDoc OpenAPI**: Integración de Swagger UI en Spring Boot 3
2. **Anotaciones de documentación**: Cómo documentar endpoints, parámetros y respuestas
3. **DTOs documentados**: Documentación de modelos de datos
4. **Interfaz interactiva**: Swagger UI para probar la API directamente
5. **MapStruct**: Mapper type-safe para convertir entre entidades y DTOs

## 📋 Conceptos Clave

### 1. ¿Qué es Swagger/OpenAPI?

**OpenAPI** (anteriormente Swagger) es una especificación estándar para describir APIs RESTful:

- ✅ **Especificación estándar**: Formato JSON/YAML para describir APIs
- ✅ **Herramientas**: Genera documentación interactiva automáticamente
- ✅ **Interfaz visual**: Swagger UI permite probar la API sin código
- ✅ **Generación de clientes**: Puede generar código cliente en múltiples lenguajes

### 2. SpringDoc OpenAPI vs Swagger 2

**SpringDoc OpenAPI** es la solución moderna para Spring Boot 3:

| Característica | Swagger 2 (antiguo) | SpringDoc OpenAPI (moderno) |
|----------------|---------------------|----------------------------|
| **Especificación** | OpenAPI 2.0 | OpenAPI 3.x |
| **Spring Boot 3** | ❌ No compatible | ✅ Compatible |
| **Configuración** | Más compleja | Más simple |
| **Dependencia** | `springfox` | `springdoc-openapi` |

**Este proyecto usa SpringDoc OpenAPI** (recomendado para Spring Boot 3).

### 3. MapStruct

Este proyecto usa **MapStruct** para convertir entre entidades y DTOs:

- ✅ **Muy rápido**: Genera código en tiempo de compilación (sin reflexión)
- ✅ **Type-safe**: Errores detectados en tiempo de compilación
- ✅ **Fácil de depurar**: Código generado visible
- ✅ **Automático**: Mapea campos con el mismo nombre

**Ejemplo de uso:**
```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDTO dto);
    
    ProductResponseDTO toResponseDTO(Product product);
}
```

MapStruct genera automáticamente la implementación en tiempo de compilación.

### 4. Anotaciones Principales

#### `@OpenAPIDefinition`
Define información general de la API (título, versión, descripción, contacto, licencia).

```java
@OpenAPIDefinition(
    info = @Info(
        title = "API de Productos",
        version = "1.0.0",
        description = "API RESTful para gestión de productos"
    )
)
```

#### `@Tag`
Agrupa endpoints relacionados en la documentación.

```java
@Tag(
    name = "Productos",
    description = "API para gestión de productos"
)
```

#### `@Operation`
Documenta un endpoint individual.

```java
@Operation(
    summary = "Listar todos los productos",
    description = "Obtiene una lista de todos los productos disponibles"
)
```

#### `@ApiResponse` / `@ApiResponses`
Documenta las respuestas posibles de un endpoint.

```java
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Éxito"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
```

#### `@Parameter`
Documenta parámetros de path, query o header.

```java
@Parameter(
    description = "ID único del producto",
    required = true,
    example = "1"
)
```

#### `@Schema`
Documenta modelos de datos (DTOs).

```java
@Schema(description = "Datos requeridos para crear un producto")
public class ProductRequestDTO {
    @Schema(description = "Nombre del producto", example = "Laptop", required = true)
    private String name;
}
```

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **SpringDoc OpenAPI 2.3.0** (Swagger UI)
- **MapStruct 1.5.5** (Mapper)
- **Spring Data JPA** · **PostgreSQL**
- **Lombok** · **Bean Validation**

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   ├── OpenApiConfig.java        # Configuración de OpenAPI
│   └── DataInitializer.java      # Datos de ejemplo
├── controller/
│   ├── HomeController.java        # Endpoint raíz
│   └── ProductController.java     # Controlador con anotaciones Swagger
├── dto/
│   ├── ProductRequestDTO.java     # DTO con @Schema
│   └── ProductResponseDTO.java   # DTO con @Schema
├── mapper/
│   └── ProductMapper.java        # Mapper MapStruct
├── model/
│   └── Product.java              # Entidad de dominio
├── repository/
│   └── ProductRepository.java   # Repositorio JPA
├── service/
│   └── ProductService.java      # Lógica de negocio
└── exception/
    └── GlobalExceptionHandler.java # Manejo de errores
```

## 🚀 Cómo arrancar

### Opción 1: Con Docker Compose

```bash
# 1. Copiar archivo de configuración
cp .env.sample .env

# 2. Iniciar servicios
docker compose up -d --build

# 3. La API estará disponible en http://localhost:8080
```

### Opción 2: Sin Docker

1. Configurar PostgreSQL localmente
2. Actualizar `application.yml` con credenciales de BD
3. Ejecutar: `mvn spring-boot:run`

**Importante**: MapStruct requiere compilación. Después de modificar el mapper, ejecuta:
```bash
mvn clean compile
```

## 📖 Acceder a la Documentación

Una vez iniciada la aplicación, puedes acceder a:

### 1. Swagger UI (Interfaz Visual)
```
http://localhost:8080/swagger-ui.html
```

**Características:**
- ✅ Interfaz visual interactiva
- ✅ Probar endpoints directamente desde el navegador
- ✅ Ver esquemas de datos
- ✅ Ejemplos de peticiones y respuestas

### 2. OpenAPI JSON (Especificación)
```
http://localhost:8080/v3/api-docs
```

**Características:**
- ✅ Especificación OpenAPI en formato JSON
- ✅ Puede usarse para generar clientes
- ✅ Puede importarse en Postman, Insomnia, etc.

### 3. OpenAPI YAML (Especificación alternativa)
```
http://localhost:8080/v3/api-docs.yaml
```

## 🧪 Cómo probar

### 1. Usando Swagger UI (Recomendado)

1. Abre `http://localhost:8080/swagger-ui.html` en tu navegador
2. Expande el endpoint que quieras probar
3. Haz clic en "Try it out"
4. Completa los parámetros si es necesario
5. Haz clic en "Execute"
6. Ve la respuesta en la interfaz

### 2. Usando cURL

#### Ver información de la API
```bash
curl http://localhost:8080/
```

#### Listar productos
```bash
curl http://localhost:8080/api/products
```

#### Obtener producto por ID
```bash
curl http://localhost:8080/api/products/1
```

#### Crear producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "Laptop de alta gama",
    "price": 1299.99,
    "stock": 10
  }'
```

#### Actualizar producto
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Actualizada",
    "description": "Laptop de alta gama actualizada",
    "price": 1399.99,
    "stock": 15
  }'
```

#### Eliminar producto
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## 📚 Explicación de Conceptos

### ¿Por qué documentar APIs?

**Sin documentación (❌ Mal):**
- Los desarrolladores no saben cómo usar la API
- Requiere leer código fuente para entender endpoints
- Errores frecuentes por falta de información
- Tiempo perdido en comunicación

**Con Swagger/OpenAPI (✅ Bien):**
- ✅ Documentación automática y siempre actualizada
- ✅ Interfaz visual para probar la API
- ✅ Ejemplos claros de peticiones y respuestas
- ✅ Generación automática de clientes
- ✅ Menos errores y mejor comunicación

### Configuración de OpenAPI

**Archivo: `OpenApiConfig.java`**
```java
@OpenAPIDefinition(
    info = @Info(
        title = "API de Productos",
        version = "1.0.0",
        description = "API RESTful para gestión de productos",
        contact = @Contact(
            name = "Equipo de Desarrollo",
            email = "dev@example.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Desarrollo"),
        @Server(url = "https://api.example.com", description = "Producción")
    }
)
```

**Archivo: `application.yml`**
```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html    # Ruta para Swagger UI
    enabled: true
    operations-sorter: method # Ordenar por método HTTP
    tags-sorter: alpha        # Ordenar tags alfabéticamente
    try-it-out-enabled: true  # Habilitar "Try it out"
```

**Nota**: La especificación OpenAPI está disponible en las rutas estándar:
- `/v3/api-docs` - Formato JSON
- `/v3/api-docs.yaml` - Formato YAML

### Documentación de Endpoints

**Ejemplo completo:**
```java
@GetMapping("/{id}")
@Operation(
    summary = "Obtener un producto por ID",
    description = "Obtiene la información detallada de un producto específico"
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto encontrado",
        content = @Content(
            schema = @Schema(implementation = ProductResponseDTO.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto no encontrado"
    )
})
public ResponseEntity<ProductResponseDTO> getProductById(
    @Parameter(
        description = "ID único del producto",
        required = true,
        example = "1"
    )
    @PathVariable Long id
) {
    // ...
}
```

### Documentación de DTOs

**Ejemplo:**
```java
@Schema(description = "Datos requeridos para crear un producto")
public class ProductRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    @Schema(
        description = "Nombre del producto",
        example = "Laptop Dell XPS 15",
        required = true
    )
    private String name;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01")
    @Schema(
        description = "Precio del producto en dólares",
        example = "1299.99",
        required = true
    )
    private BigDecimal price;
}
```

**Beneficios:**
- ✅ Descripción clara de cada campo
- ✅ Ejemplos para facilitar pruebas
- ✅ Validaciones visibles en la documentación
- ✅ Tipos de datos explícitos

### ¿Cómo funciona MapStruct?

MapStruct genera código en tiempo de compilación. Por ejemplo:

**Interfaz:**
```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO dto);
    ProductResponseDTO toResponseDTO(Product product);
}
```

**Código generado (automático):**
```java
@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return product;
    }
    // ...
}
```

### Buenas Prácticas

1. **Siempre documenta tus endpoints**
   - Usa `@Operation` para describir qué hace cada endpoint
   - Incluye ejemplos con `@Schema(example = "...")`

2. **Documenta todas las respuestas posibles**
   - Éxito (200, 201, etc.)
   - Errores (400, 404, 500, etc.)
   - Usa `@ApiResponses` para múltiples respuestas

3. **Describe parámetros claramente**
   - Usa `@Parameter` para path, query y header params
   - Incluye si son requeridos u opcionales
   - Proporciona ejemplos

4. **Documenta modelos de datos**
   - Usa `@Schema` en DTOs
   - Describe cada campo con su propósito
   - Incluye ejemplos realistas

5. **Mantén la documentación actualizada**
   - Swagger se genera automáticamente del código
   - Si cambias el código, la documentación se actualiza sola
   - Revisa Swagger UI antes de cada release

## 🔍 Endpoints Disponibles

| Método | Endpoint | Descripción | Documentación |
|--------|----------|-------------|---------------|
| GET | `/` | Información de la API | ✅ |
| GET | `/api/products` | Listar productos | ✅ |
| GET | `/api/products/{id}` | Obtener producto | ✅ |
| POST | `/api/products` | Crear producto | ✅ |
| PUT | `/api/products/{id}` | Actualizar producto | ✅ |
| DELETE | `/api/products/{id}` | Eliminar producto | ✅ |

Todos los endpoints están completamente documentados con Swagger/OpenAPI 3.

## 📝 Anotaciones Utilizadas en el Proyecto

### En Controladores

- `@Tag`: Agrupa endpoints (ej: "Productos")
- `@Operation`: Describe cada endpoint
- `@ApiResponse` / `@ApiResponses`: Documenta respuestas
- `@Parameter`: Documenta parámetros
- `@RequestBody`: Documenta el cuerpo de la petición

### En DTOs

- `@Schema`: Documenta el modelo completo
- `@Schema` en campos: Documenta cada propiedad

### En Configuración

- `@OpenAPIDefinition`: Configuración general de la API
- `@Info`: Información de la API
- `@Server`: Servidores disponibles

### En Mappers

- `@Mapper`: Marca la interfaz como mapper de MapStruct
- `@Mapping`: Configura mapeos personalizados

## 🎓 Aprendizajes Clave

- ✅ **Swagger/OpenAPI** proporciona documentación automática e interactiva
- ✅ **SpringDoc OpenAPI** es la solución moderna para Spring Boot 3
- ✅ **Anotaciones** hacen que la documentación sea parte del código
- ✅ **Swagger UI** permite probar la API sin escribir código
- ✅ **Documentación actualizada** automáticamente al cambiar el código
- ✅ **MapStruct** hace el mapeo eficiente y type-safe

## 🔄 Comparación: Swagger 2 vs OpenAPI 3

| Característica | Swagger 2 | OpenAPI 3 |
|----------------|-----------|-----------|
| **Especificación** | OpenAPI 2.0 | OpenAPI 3.x |
| **Spring Boot 3** | ❌ No compatible | ✅ Compatible |
| **Seguridad** | Menos flexible | Más flexible (OAuth2, JWT, etc.) |
| **Callbacks** | No soportado | ✅ Soportado |
| **Links** | No soportado | ✅ Soportado |
| **Bibliotecas** | `springfox` | `springdoc-openapi` |

**Recomendación**: Usa OpenAPI 3 con SpringDoc para proyectos nuevos.

## 🛡️ Seguridad en Swagger (Opcional)

Para APIs protegidas, puedes documentar autenticación:

```java
@OpenAPIDefinition(
    security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            );
    }
}
```

## 📖 Recursos Adicionales

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [MapStruct Documentation](https://mapstruct.org/)
- [OpenAPI Generator](https://openapi-generator.tech/) - Genera clientes desde OpenAPI

## 💡 Tips y Trucos

1. **Personaliza Swagger UI**: Modifica `application.yml` para cambiar colores, orden, etc.

2. **Oculta endpoints**: Usa `@Hidden` para ocultar endpoints de la documentación:
   ```java
   @Hidden
   @GetMapping("/internal")
   public String internal() { ... }
   ```

3. **Agrupa por tags**: Usa `@Tag` para organizar endpoints lógicamente.

4. **Ejemplos realistas**: Proporciona ejemplos que reflejen casos de uso reales.

5. **Documenta errores**: No olvides documentar todos los códigos de error posibles.

6. **MapStruct requiere compilación**: Después de modificar el mapper, ejecuta `mvn clean compile`.

## 🎯 Próximos Pasos

1. **Agregar autenticación**: Documenta endpoints protegidos con JWT
2. **Versionamiento**: Documenta múltiples versiones de la API
3. **Generar clientes**: Usa OpenAPI Generator para crear clientes en otros lenguajes
4. **Exportar documentación**: Genera PDF o HTML estático para compartir

---

**¡Explora la documentación interactiva en `http://localhost:8080/swagger-ui.html`!** 🚀
