# Diseño Avanzado de APIs RESTful - ModelMapper

Proyecto simple para entender **versionamiento de APIs**, **DTOs** y **Mappers (ModelMapper)** en Spring Boot.

## 🎯 Objetivo

Este proyecto demuestra las mejores prácticas para diseñar APIs RESTful profesionales:

1. **Versionamiento de APIs**: Cómo mantener múltiples versiones de una API sin romper compatibilidad
2. **DTOs (Data Transfer Objects)**: Separación entre la capa de presentación y la capa de dominio
3. **Mappers con ModelMapper**: Conversión flexible entre entidades y DTOs usando reflexión

## 📋 Conceptos Clave

### 1. Versionamiento de APIs

El versionamiento permite evolucionar una API sin romper clientes existentes:

- **v1**: Versión inicial con campos básicos
- **v2**: Versión mejorada que agrega nuevos campos (ej: `category`)

**Estrategias de versionamiento:**
- **URL Path**: `/api/v1/products`, `/api/v2/products` ✅ (usado en este proyecto)
- **Query Parameter**: `/api/products?version=1`
- **Header**: `Accept: application/vnd.api.v1+json`

### 2. DTOs (Data Transfer Objects)

Los DTOs separan la estructura de la API de la estructura de la base de datos:

**Ventajas:**
- ✅ Cambiar la API sin modificar la entidad
- ✅ Validar datos de entrada independientemente
- ✅ Ocultar información sensible
- ✅ Diferentes DTOs para diferentes versiones

**Estructura:**
```
dto/
├── v1/
│   ├── ProductRequestDTO.java   # Para crear/actualizar (v1)
│   └── ProductResponseDTO.java  # Para respuestas (v1)
└── v2/
    ├── ProductRequestDTO.java   # Para crear/actualizar (v2)
    └── ProductResponseDTO.java  # Para respuestas (v2)
```

### 3. Mappers (ModelMapper)

ModelMapper usa reflexión en tiempo de ejecución para convertir entre entidades y DTOs:

**Ventajas:**
- 🔧 **Flexible**: No requiere compilación, configuración simple
- 📝 **Menos código**: Mapeo automático de campos con el mismo nombre
- 🎯 **Fácil de usar**: Configuración mínima necesaria
- ⚠️ **Menos performante**: Usa reflexión (más lento que MapStruct)

**Comparación con MapStruct:**
- **ModelMapper**: Reflexión en tiempo de ejecución, más flexible, más lento
- **MapStruct**: Código generado en compilación, más rápido, type-safe

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **ModelMapper 3.1.1** (Mapper)
- **Spring Data JPA** · **PostgreSQL**
- **Lombok** · **Bean Validation**

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── controller/
│   ├── HomeController.java          # Endpoint raíz
│   ├── v1/
│   │   └── ProductControllerV1.java # Controlador v1
│   └── v2/
│       └── ProductControllerV2.java # Controlador v2
├── dto/
│   ├── v1/                          # DTOs versión 1
│   │   ├── ProductRequestDTO.java
│   │   └── ProductResponseDTO.java
│   └── v2/                          # DTOs versión 2
│       ├── ProductRequestDTO.java
│       └── ProductResponseDTO.java
├── mapper/
│   └── ProductMapper.java           # Mapper ModelMapper
├── model/
│   └── Product.java                 # Entidad de dominio
├── repository/
│   └── ProductRepository.java      # Repositorio JPA
├── service/
│   └── ProductService.java         # Lógica de negocio
├── config/
│   └── DataInitializer.java        # Datos de ejemplo
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

## 🧪 Cómo probar

### 1. Ver información de la API

```bash
curl http://localhost:8080/
```

### 2. API Versión 1 (v1)

#### Listar productos
```bash
curl http://localhost:8080/api/v1/products
```

#### Crear producto (v1)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "Laptop de alta gama",
    "price": 1299.99,
    "stock": 10
  }'
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "Laptop de alta gama",
  "price": 1299.99,
  "stock": 10,
  "createdAt": "2026-01-27T10:00:00"
}
```

#### Obtener producto por ID
```bash
curl http://localhost:8080/api/v1/products/1
```

### 3. API Versión 2 (v2)

#### Listar productos (v2)
```bash
curl http://localhost:8080/api/v2/products
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "Laptop de alta gama",
    "price": 1299.99,
    "stock": 10,
    "category": "Electrónica",
    "createdAt": "2026-01-27T10:00:00",
    "updatedAt": "2026-01-27T10:00:00"
  },
  {
    "id": 2,
    "name": "Mouse",
    "description": "Mouse inalámbrico",
    "price": 29.99,
    "stock": 50,
    "category": "Accesorios",
    "createdAt": "2026-01-27T10:00:00",
    "updatedAt": "2026-01-27T10:00:00"
  }
]
```

#### Crear producto (v2) - Con categoría
```bash
curl -X POST http://localhost:8080/api/v2/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor",
    "description": "Monitor 4K",
    "price": 399.99,
    "stock": 20,
    "category": "Electrónica"
  }'
```

**Respuesta esperada:**
```json
{
  "id": 4,
  "name": "Monitor",
  "description": "Monitor 4K",
  "price": 399.99,
  "stock": 20,
  "category": "Electrónica",
  "createdAt": "2026-01-27T10:00:00",
  "updatedAt": "2026-01-27T10:00:00"
}
```

**Diferencias entre v1 y v2:**
- ✅ v2 incluye el campo `category`
- ✅ v2 incluye `updatedAt` en las respuestas
- ✅ v1 sigue funcionando para clientes que no han migrado

## 📚 Explicación de Conceptos

### ¿Por qué usar DTOs?

**Sin DTOs (❌ Mal):**
```java
@PostMapping
public Product createProduct(@RequestBody Product product) {
    return productService.save(product);
}
```
- Expone la estructura interna de la base de datos
- No permite validaciones específicas de la API
- Dificulta el versionamiento

**Con DTOs (✅ Bien):**
```java
@PostMapping
public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO dto) {
    Product product = productMapper.toEntity(dto);
    Product saved = productService.save(product);
    return productMapper.toResponseDTO(saved);
}
```
- Separa la capa de presentación de la de dominio
- Permite validaciones específicas
- Facilita el versionamiento

### ¿Cómo funciona ModelMapper?

ModelMapper usa reflexión en tiempo de ejecución para mapear objetos automáticamente:

**Implementación:**
```java
@Component
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    public Product toEntity(ProductRequestDTO dto) {
        Product product = modelMapper.map(dto, Product.class);
        product.setId(null); // Ignorar campos que no deben mapearse
        return product;
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        return modelMapper.map(product, ProductResponseDTO.class);
    }
}
```

**Cómo funciona:**
1. ModelMapper analiza los campos de ambas clases usando reflexión
2. Mapea automáticamente campos con el mismo nombre
3. Permite configuración personalizada para casos especiales

**Ventajas:**
- ✅ No requiere compilación adicional
- ✅ Configuración simple
- ✅ Funciona automáticamente para campos con el mismo nombre

**Desventajas:**
- ⚠️ Menos performante que MapStruct (usa reflexión)
- ⚠️ Errores solo se detectan en tiempo de ejecución

### Estrategias de Versionamiento

1. **URL Path** (usado en este proyecto):
   - `/api/v1/products`
   - `/api/v2/products`
   - ✅ Más explícito y fácil de entender

2. **Query Parameter**:
   - `/api/products?version=1`
   - Menos común, puede ser confuso

3. **Header**:
   - `Accept: application/vnd.api.v1+json`
   - Más complejo, pero más RESTful

## 🔍 Endpoints Disponibles

| Método | Endpoint | Versión | Descripción |
|--------|----------|---------|-------------|
| GET | `/` | - | Información de la API |
| GET | `/api/v1/products` | v1 | Listar productos |
| GET | `/api/v1/products/{id}` | v1 | Obtener producto |
| POST | `/api/v1/products` | v1 | Crear producto |
| PUT | `/api/v1/products/{id}` | v1 | Actualizar producto |
| DELETE | `/api/v1/products/{id}` | v1 | Eliminar producto |
| GET | `/api/v2/products` | v2 | Listar productos (con category) |
| GET | `/api/v2/products/{id}` | v2 | Obtener producto (con category) |
| POST | `/api/v2/products` | v2 | Crear producto (con category) |
| PUT | `/api/v2/products/{id}` | v2 | Actualizar producto (con category) |
| DELETE | `/api/v2/products/{id}` | v2 | Eliminar producto |

## 📝 Notas Importantes

1. **ModelMapper no requiere compilación**: A diferencia de MapStruct, ModelMapper funciona en tiempo de ejecución usando reflexión.

2. **Versionamiento**: v1 y v2 comparten la misma entidad `Product`, pero usan DTOs diferentes.

3. **Categoría en v2**: Por simplicidad, la categoría se almacena en el campo `description` con el formato `[Category: ...]`. En producción, agregarías un campo `category` a la entidad.

4. **Compatibilidad**: v1 sigue funcionando aunque v2 esté disponible, permitiendo migración gradual.

5. **Productos de ejemplo**: El `DataInitializer` crea productos con categorías predefinidas:
   - Laptop → "Electrónica"
   - Mouse → "Accesorios"
   - Teclado → "Accesorios"

## 🎓 Aprendizajes Clave

- ✅ **DTOs** separan la API de la base de datos
- ✅ **Versionamiento** permite evolucionar sin romper clientes
- ✅ **ModelMapper** hace el mapeo flexible y fácil de configurar
- ✅ **Estructura clara** facilita el mantenimiento

## 🔄 Comparación: ModelMapper vs MapStruct

| Característica | ModelMapper | MapStruct |
|----------------|-------------|-----------|
| **Tipo** | Reflexión (runtime) | Generación de código (compile-time) |
| **Rendimiento** | Más lento | Más rápido |
| **Type-safety** | Errores en runtime | Errores en compile-time |
| **Configuración** | Simple | Requiere annotation processors |
| **Flexibilidad** | Alta | Media |
| **Depuración** | Más difícil | Más fácil (código visible) |

**Cuándo usar cada uno:**
- **ModelMapper**: Proyectos pequeños, prototipos rápidos, cuando la flexibilidad es más importante que el rendimiento
- **MapStruct**: Proyectos grandes, cuando el rendimiento es crítico, cuando prefieres type-safety

## 📖 Recursos Adicionales

- [ModelMapper Documentation](http://modelmapper.org/)
- [REST API Versioning Best Practices](https://restfulapi.net/versioning/)
- [Spring Boot Validation](https://spring.io/guides/gs/validating-form-input/)
