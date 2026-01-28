# Principios Clean Code y Control de Calidad

Proyecto simple para entender **principios Clean Code** y control de calidad con **SonarLint** y **SonarQube**.

## 🎯 Objetivo

Este proyecto demuestra cómo escribir código limpio, mantenible y de calidad, comparando ejemplos de código "malo" vs código "bueno".

## 📋 Principios Clean Code Demostrados

### 1. **Nombres Descriptivos**
- ✅ **Bueno**: `calculateFinalPrice`, `isValidUser`, `buildFullName`
- ❌ **Malo**: `calc`, `process`, `doEverything`, `x`, `y`, `data`

### 2. **Funciones Pequeñas**
- ✅ **Bueno**: Cada función tiene una sola responsabilidad
- ❌ **Malo**: Funciones largas que hacen múltiples cosas

### 3. **DRY (Don't Repeat Yourself)**
- ✅ **Bueno**: Lógica reutilizable en métodos privados
- ❌ **Malo**: Código duplicado

### 4. **Sin Magic Numbers**
- ✅ **Bueno**: Constantes con nombres descriptivos (`MINIMUM_AGE`, `HIGH_PURCHASE_THRESHOLD`)
- ❌ **Malo**: Números mágicos en el código (`18`, `100`, `0.1`)

### 5. **Validación Clara**
- ✅ **Bueno**: Validaciones separadas y explícitas
- ❌ **Malo**: Validaciones mezcladas con lógica de negocio

### 6. **Manejo de Errores**
- ✅ **Bueno**: Excepciones descriptivas y apropiadas
- ❌ **Malo**: Retornos genéricos o silenciosos

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **Lombok** (reduce boilerplate)
- **SonarLint** (análisis de código en IDE)
- **SonarQube** (análisis de calidad de código)

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── controller/
│   ├── CalculatorController.java    # Controlador REST limpio
│   ├── HomeController.java          # Endpoint raíz
│   └── UserController.java          # Controlador REST limpio
└── service/
    ├── calculator/
    │   ├── BadCalculatorService.java    # ❌ Ejemplo de código "malo"
    │   └── CalculatorService.java      # ✅ Ejemplo de código "bueno"
    └── user/
        ├── BadUserService.java          # ❌ Ejemplo de código "malo"
        └── UserService.java             # ✅ Ejemplo de código "bueno"
```

## 🚀 Cómo arrancar

### Opción 1: Con Maven

```bash
# Compilar y ejecutar
mvn clean spring-boot:run

# La API estará disponible en http://localhost:8080
```

### Opción 2: Con Docker Compose

```bash
# 1. Copiar archivo de configuración (opcional)
cp .env.sample .env

# 2. Construir y ejecutar
docker compose up --build

# 3. La API estará disponible en http://localhost:8080

# Para ejecutar en segundo plano
docker compose up -d --build

# Para ver logs
docker compose logs -f app

# Para detener
docker compose down
```

## 🧪 Cómo probar

### 1. Endpoint de información

```bash
curl http://localhost:8080/
```

### 2. Calculadora (código limpio)

```bash
# Suma
curl "http://localhost:8080/api/calculator?firstNumber=10&secondNumber=5&operation=ADD"

# Resta
curl "http://localhost:8080/api/calculator?firstNumber=10&secondNumber=5&operation=SUBTRACT"

# Multiplicación
curl "http://localhost:8080/api/calculator?firstNumber=10&secondNumber=5&operation=MULTIPLY"

# División
curl "http://localhost:8080/api/calculator?firstNumber=10&secondNumber=5&operation=DIVIDE"
```

### 3. Validación de usuario (código limpio)

```bash
curl "http://localhost:8080/api/user/validate?name=Juan&age=25&email=juan@example.com"
```

### 4. Cálculo de precio con descuento

```bash
curl "http://localhost:8080/api/user/price?purchasePrice=150.0"
```

## 🔍 SonarLint / SonarQube

### Instalación de SonarLint (IDE)

1. **VS Code / IntelliJ IDEA / Eclipse**:
   - Instala la extensión "SonarLint"
   - Se activa automáticamente al abrir el proyecto

2. **Configuración**:
   - SonarLint analiza el código en tiempo real
   - Muestra problemas directamente en el IDE
   - Sugiere mejoras siguiendo reglas de calidad

### Instalación de SonarQube (Servidor)

1. **Con Docker**:
```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:community
```

2. **Acceder**:
   - URL: http://localhost:9000
   - Usuario por defecto: `admin`
   - Contraseña por defecto: `admin`

3. **Analizar proyecto**:
```bash
# Configurar token en sonar-project.properties
mvn sonar:sonar
```

### Reglas de Calidad Importantes

SonarLint/SonarQube detecta:

- **Code Smells**: Código que funciona pero es difícil de mantener
- **Bugs**: Errores potenciales
- **Vulnerabilidades**: Problemas de seguridad
- **Duplicación**: Código duplicado
- **Complejidad**: Funciones demasiado complejas
- **Cobertura**: Porcentaje de código probado

## 📚 Comparación de Ejemplos

### Ejemplo 1: Nombres Descriptivos

**❌ Malo:**
```java
public double calc(double a, double b, String op) {
    // ...
}
```

**✅ Bueno:**
```java
public double calculate(double firstNumber, double secondNumber, Operation operation) {
    // ...
}
```

### Ejemplo 2: Magic Numbers

**❌ Malo:**
```java
if (age >= 18 && age <= 100) {
    // ...
}
```

**✅ Bueno:**
```java
private static final int MINIMUM_AGE = 18;
private static final int MAXIMUM_AGE = 100;

if (age >= MINIMUM_AGE && age <= MAXIMUM_AGE) {
    // ...
}
```

### Ejemplo 3: Funciones Pequeñas

**❌ Malo:**
```java
public String doEverything(double num1, double num2, String operation, boolean format) {
    // 50 líneas de código mezclando lógica
}
```

**✅ Bueno:**
```java
public double calculate(double firstNumber, double secondNumber, Operation operation) {
    validateInputs(firstNumber, secondNumber, operation);
    return performCalculation(firstNumber, secondNumber, operation);
}

private double performCalculation(double firstNumber, double secondNumber, Operation operation) {
    return switch (operation) {
        case ADD -> add(firstNumber, secondNumber);
        // ...
    };
}
```

## 🎓 Principios SOLID (Básicos)

### Single Responsibility Principle (SRP)
- Cada clase tiene una sola razón para cambiar
- Ejemplo: `CalculatorService` solo calcula, no formatea ni valida

### Open/Closed Principle (OCP)
- Abierto para extensión, cerrado para modificación
- Ejemplo: Usar `enum Operation` permite agregar operaciones sin modificar código existente

## 📖 Recursos Adicionales

- [Clean Code - Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [SonarLint Documentation](https://www.sonarlint.org/)

## 🔧 Comandos Útiles

```bash
# Compilar proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Analizar con SonarQube
mvn sonar:sonar

# Generar reporte de cobertura
mvn jacoco:report
```

## 📝 Notas

- Los archivos `Bad*.java` son ejemplos educativos de código "malo"
- No deben usarse en producción
- SonarLint los marcará con múltiples problemas de calidad
- Compara con los archivos sin "Bad" para ver las mejoras

---

**Recuerda**: El código limpio no es solo para que funcione, es para que otros (y tú en el futuro) puedan entenderlo y mantenerlo fácilmente.
