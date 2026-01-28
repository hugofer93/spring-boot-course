# Introducción a Pruebas (Unitarias e Integración) con Spring Boot

Proyecto simple para entender cómo escribir **pruebas unitarias** y **pruebas de integración** en Spring Boot.

## 🎯 Objetivo

Este proyecto demuestra:
- **Pruebas unitarias**: Probar lógica de negocio de forma aislada
- **Pruebas de integración**: Probar la integración entre componentes
- **MockMvc**: Probar controladores REST sin levantar un servidor
- **Mockito**: Crear mocks para aislar dependencias

## 📚 Conceptos Clave

### ¿Qué son las Pruebas Unitarias?

Las **pruebas unitarias** prueban una unidad de código (método, clase) de forma **aislada**, sin depender de otros componentes.

**Características:**
- ✅ Rápidas de ejecutar (milisegundos)
- ✅ No requieren contexto de Spring
- ✅ Prueban lógica de negocio pura
- ✅ Fáciles de mantener

**Ejemplo en este proyecto:**
- `CalculatorServiceTest.java` - Prueba el servicio sin depender de Spring

### ¿Qué son las Pruebas de Integración?

Las **pruebas de integración** prueban cómo **múltiples componentes trabajan juntos**.

**Características:**
- ⚠️ Más lentas (segundos)
- ✅ Requieren contexto de Spring
- ✅ Prueban la integración real entre componentes
- ✅ Más realistas

**Ejemplo en este proyecto:**
- `CalculatorIntegrationTest.java` - Prueba el flujo completo desde el controlador hasta el servicio

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **JUnit 5** - Framework de pruebas
- **Mockito** - Framework para crear mocks
- **MockMvc** - Probar controladores REST
- **AssertJ** - Assertions más legibles (incluido en spring-boot-starter-test)

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── service/
│   │   │   └── CalculatorService.java      # Lógica de negocio
│   │   ├── controller/
│   │   │   ├── CalculatorController.java   # Controlador REST
│   │   │   └── HomeController.java          # Endpoint raíz
│   │   └── DemoApplication.java
│   └── resources/
│       └── application.yml
│
└── test/
    └── java/com/example/demo/
        ├── service/
        │   └── CalculatorServiceTest.java           # Pruebas unitarias
        ├── controller/
        │   └── CalculatorControllerTest.java       # Pruebas con MockMvc
        ├── integration/
        │   └── CalculatorIntegrationTest.java       # Pruebas de integración
        └── DemoApplicationTests.java                 # Prueba de contexto
```

## 🧪 Tipos de Pruebas en este Proyecto

### 1. Pruebas Unitarias (`CalculatorServiceTest`)

**Anotaciones:**
- `@Test` - Marca un método como prueba
- `@BeforeEach` - Se ejecuta antes de cada prueba
- `@DisplayName` - Nombre descriptivo para la prueba

**Características:**
- No usa `@SpringBootTest` (no necesita contexto de Spring)
- Crea instancias directamente: `new CalculatorService()`
- Prueba la lógica pura del servicio

**Ejemplo:**
```java
@Test
void testAdd_PositiveNumbers() {
    CalculatorService service = new CalculatorService();
    double result = service.add(5.0, 3.0);
    assertEquals(8.0, result, 0.001);
}
```

### 2. Pruebas con MockMvc (`CalculatorControllerTest`)

**Anotaciones:**
- `@WebMvcTest(CalculatorController.class)` - Carga solo el contexto web
- `@MockBean` - Crea un mock del servicio
- `@Autowired MockMvc` - Para hacer peticiones HTTP simuladas

**Características:**
- Más rápido que `@SpringBootTest`
- Prueba solo el controlador (el servicio es un mock)
- Útil para probar la capa de presentación

**Ejemplo:**
```java
@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {
    @MockBean
    private CalculatorService calculatorService;
    
    @Test
    void testAdd() throws Exception {
        when(calculatorService.add(5.0, 3.0)).thenReturn(8.0);
        
        mockMvc.perform(get("/api/calculator/add")
                .param("a", "5.0")
                .param("b", "3.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(8.0));
    }
}
```

### 3. Pruebas de Integración (`CalculatorIntegrationTest`)

**Anotaciones:**
- `@SpringBootTest` - Carga el contexto completo de Spring
- `@AutoConfigureMockMvc` - Configura MockMvc automáticamente

**Características:**
- Prueba la integración real entre componentes
- El servicio NO es un mock, es la instancia real
- Más lento pero más realista

**Ejemplo:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class CalculatorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testAdd_Integration() throws Exception {
        // Usa el servicio REAL, no un mock
        mockMvc.perform(get("/api/calculator/add")
                .param("a", "10.0")
                .param("b", "5.0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value(15.0));
    }
}
```

## 🚀 Cómo Ejecutar las Pruebas

### Opción 1: Desde la línea de comandos (local)

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar una clase de prueba específica
mvn test -Dtest=CalculatorServiceTest

# Ejecutar un método de prueba específico
mvn test -Dtest=CalculatorServiceTest#testAdd_PositiveNumbers
```

### Opción 2: Desde el IDE

1. Click derecho en la clase de prueba → "Run"
2. O click derecho en un método → "Run"

### Opción 3: Ejecutar pruebas cuando el proyecto está en Docker

Si tienes el proyecto ejecutándose en un contenedor Docker, tienes varias opciones:

#### 3.1. Ejecutar pruebas en tu máquina local (Recomendado)

**Las pruebas NO necesitan que el contenedor esté corriendo.** Puedes ejecutarlas directamente en tu máquina:

```bash
# Asegúrate de tener Maven instalado localmente
mvn test

# O si prefieres usar el contenedor de Maven sin levantar la app
docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-17 mvn test
```

**Ventajas:**
- ✅ Más rápido (no necesita levantar el contenedor completo)
- ✅ Las pruebas son independientes de la aplicación corriendo
- ✅ Puedes ejecutar pruebas mientras la app está en producción

#### 3.2. Ejecutar pruebas dentro del contenedor

Si quieres ejecutar las pruebas dentro del contenedor donde está corriendo la app:

```bash
# Ejecutar pruebas en el contenedor que está corriendo
docker exec -it testing-project-app mvn test

# O si el contenedor no tiene Maven instalado, usar un contenedor temporal
docker run --rm \
  -v "$(pwd)":/app \
  -w /app \
  maven:3.9-eclipse-temurin-17 \
  mvn test
```

#### 3.3. Ejecutar pruebas usando Docker Compose

Tienes dos opciones para ejecutar pruebas con Docker Compose:

**Opción A: Usar el servicio en `compose.yml` con perfil `test`**

```bash
# Ejecutar pruebas usando el servicio de pruebas
docker compose --profile test run --rm test
```

**Opción B: Usar el archivo `compose.test.yml` separado**

```bash
# Ejecutar pruebas usando el archivo de pruebas dedicado
docker compose -f compose.test.yml run --rm test
```

**Ventajas de ambas opciones:**
- ✅ Usa caché de Maven para acelerar ejecuciones
- ✅ No interfiere con el servicio principal
- ✅ Configuración lista para usar

**Diferencia:**
- `compose.yml` con perfil: El servicio está en el mismo archivo pero solo se ejecuta con `--profile test`
- `compose.test.yml`: Archivo separado dedicado solo para pruebas, más simple de usar

### Opción 4: Ejecutar la aplicación

```bash
# Iniciar la aplicación
mvn spring-boot:run

# O con Docker Compose
docker compose up

# Probar endpoints manualmente
curl http://localhost:8080/api/calculator/add?a=5&b=3
```

### ⚠️ Nota Importante

**Las pruebas unitarias e integración NO requieren que la aplicación esté corriendo.**

- Las pruebas unitarias (`CalculatorServiceTest`) no usan Spring, son completamente independientes
- Las pruebas de integración (`CalculatorIntegrationTest`, `CalculatorControllerTest`) cargan su propio contexto de Spring para las pruebas
- Solo necesitas ejecutar la aplicación si quieres probar manualmente los endpoints con `curl` o herramientas similares

## 📖 Patrón AAA (Arrange-Act-Assert)

Las pruebas siguen el patrón **AAA**:

```java
@Test
void testAdd() {
    // Arrange (Preparar)
    CalculatorService service = new CalculatorService();
    double a = 5.0;
    double b = 3.0;
    double expected = 8.0;
    
    // Act (Ejecutar)
    double result = service.add(a, b);
    
    // Assert (Verificar)
    assertEquals(expected, result, 0.001);
}
```

## 🎓 Buenas Prácticas

### 1. Nombres Descriptivos

✅ **Bueno:**
```java
@Test
@DisplayName("Debería sumar dos números positivos correctamente")
void testAdd_PositiveNumbers() { ... }
```

❌ **Malo:**
```java
@Test
void test1() { ... }
```

### 2. Una Aserción por Concepto

✅ **Bueno:**
```java
@Test
void testDivide_ByZero() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> calculatorService.divide(10.0, 0.0)
    );
    assertEquals("No se puede dividir por cero", exception.getMessage());
}
```

### 3. Pruebas Independientes

Cada prueba debe poder ejecutarse de forma independiente, sin depender de otras.

### 4. Usar @DisplayName

Hace que los reportes de pruebas sean más legibles:
```java
@DisplayName("Debería lanzar excepción al dividir por cero")
```

### 5. Organizar por Tipo de Prueba

- Pruebas unitarias en `service/`
- Pruebas de controladores en `controller/`
- Pruebas de integración en `integration/`

## 🔍 Assertions Comunes

### JUnit 5

```java
// Verificar igualdad
assertEquals(expected, actual);
assertEquals(expected, actual, 0.001); // Con delta para doubles

// Verificar que es verdadero/falso
assertTrue(condition);
assertFalse(condition);

// Verificar que es null/no null
assertNull(value);
assertNotNull(value);

// Verificar que se lanza una excepción
assertThrows(IllegalArgumentException.class, () -> {
    calculatorService.divide(10.0, 0.0);
});
```

### MockMvc

```java
// Verificar status HTTP
.andExpect(status().isOk())
.andExpect(status().isBadRequest())

// Verificar contenido JSON
.andExpect(jsonPath("$.result").value(8.0))
.andExpect(jsonPath("$.operation").exists())

// Verificar tipo de contenido
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
```

## 📊 Cobertura de Pruebas

Para ver la cobertura de pruebas:

```bash
# Generar reporte de cobertura (requiere plugin JaCoCo)
mvn clean test jacoco:report
```

## 🎯 Cuándo Usar Cada Tipo de Prueba

| Tipo | Cuándo Usar | Ejemplo |
|------|-------------|---------|
| **Unitarias** | Lógica de negocio pura | `CalculatorServiceTest` |
| **MockMvc** | Probar controladores aislados | `CalculatorControllerTest` |
| **Integración** | Probar flujo completo | `CalculatorIntegrationTest` |

## 🐛 Debugging de Pruebas

### Ver logs detallados

Agregar en `application.yml`:
```yaml
logging:
  level:
    com.example.demo: DEBUG
```

### Ejecutar con más información

```bash
mvn test -X  # Modo verbose
```

## 📝 Resumen

- **Pruebas unitarias**: Rápidas, aisladas, sin Spring
- **Pruebas con MockMvc**: Prueban controladores con servicios mockeados
- **Pruebas de integración**: Prueban el flujo completo con componentes reales

**Regla de oro**: Empieza con pruebas unitarias (rápidas), luego agrega pruebas de integración para casos críticos.

## 🔗 Recursos Adicionales

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
