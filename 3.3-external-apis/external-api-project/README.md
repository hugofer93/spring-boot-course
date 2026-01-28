# Consumo de APIs Externas desde Spring Boot

Proyecto simple para entender **consumo de APIs externas** en Spring Boot usando **RestTemplate** y **WebClient**.

## 🎯 Objetivo

Este proyecto demuestra cómo consumir APIs externas desde Spring Boot de manera profesional:

1. **RestTemplate**: Cliente HTTP bloqueante (tradicional)
2. **WebClient**: Cliente HTTP reactivo (moderno, recomendado)
3. **Configuración de timeouts**: Manejo de tiempos de espera
4. **Manejo de errores**: Tratamiento de excepciones HTTP
5. **Interceptores y filtros**: Logging y monitoreo
6. **Ejemplos prácticos**: Integración con APIs públicas reales

## 📋 Conceptos Clave

### 1. ¿Qué es RestTemplate?

**RestTemplate** es el cliente HTTP bloqueante tradicional de Spring:

- ✅ **Simple y directo**: Fácil de usar para casos simples
- ✅ **Síncrono**: Bloquea el hilo hasta recibir respuesta
- ✅ **Maduro**: Disponible desde Spring 3.0
- ❌ **Bloqueante**: Menos eficiente para múltiples peticiones concurrentes
- ❌ **En mantenimiento**: Spring recomienda usar WebClient para nuevos proyectos

**Cuándo usar RestTemplate:**
- Aplicaciones síncronas simples
- Migraciones de código legacy
- Casos donde no necesitas reactividad

### 2. ¿Qué es WebClient?

**WebClient** es el cliente HTTP reactivo moderno de Spring:

- ✅ **No bloqueante**: No bloquea hilos durante peticiones
- ✅ **Reactivo**: Integración con programación reactiva (Mono/Flux)
- ✅ **Eficiente**: Mejor rendimiento para múltiples peticiones concurrentes
- ✅ **Recomendado**: Spring recomienda WebClient para nuevos proyectos
- ✅ **Moderno**: Parte del ecosistema Spring WebFlux

**Cuándo usar WebClient:**
- Nuevos proyectos
- Aplicaciones que necesitan alta concurrencia
- Integración con Spring WebFlux
- Microservicios modernos

### 3. Comparación: RestTemplate vs WebClient

| Característica | RestTemplate | WebClient |
|----------------|--------------|-----------|
| **Tipo** | Bloqueante | No bloqueante (reactivo) |
| **Hilos** | Bloquea hilos | No bloquea hilos |
| **Concurrencia** | Limitada | Alta |
| **Rendimiento** | Bueno para casos simples | Mejor para múltiples peticiones |
| **Complejidad** | Simple | Más complejo (pero más poderoso) |
| **Recomendación** | Mantenimiento | ✅ Recomendado para nuevos proyectos |
| **Spring Boot 3** | ✅ Compatible | ✅ Compatible |

**Recomendación**: Usa **WebClient** para nuevos proyectos, RestTemplate solo para migraciones.

### 4. Configuración de Timeouts

Es **crítico** configurar timeouts para evitar que tu aplicación se quede esperando indefinidamente:

```yaml
http:
  client:
    timeout:
      connect: 5000    # 5 segundos para conectar
      read: 10000      # 10 segundos para leer respuesta
```

**¿Por qué son importantes los timeouts?**
- Evitan que tu aplicación se quede bloqueada
- Mejoran la experiencia del usuario
- Previenen problemas de recursos

### 5. Manejo de Errores

Al consumir APIs externas, debes manejar diferentes tipos de errores:

- **4xx (Cliente)**: Errores del cliente (404, 400, etc.)
- **5xx (Servidor)**: Errores del servidor externo (500, 503, etc.)
- **Timeout**: La API externa no responde a tiempo
- **Conexión**: No se puede conectar con la API externa

Este proyecto incluye un `GlobalExceptionHandler` que maneja todos estos casos.

## 🛠️ Tecnologías

- **Java 17** · **Spring Boot 3.2**
- **RestTemplate** (bloqueante)
- **WebClient** (reactivo)
- **Spring WebFlux** (para WebClient)
- **Lombok**

## 📁 Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   └── HttpClientConfig.java        # Configuración de RestTemplate y WebClient
├── controller/
│   ├── HomeController.java           # Endpoint raíz
│   ├── JsonPlaceholderController.java # Ejemplos con JSONPlaceholder API
│   └── HttpBinController.java        # Ejemplos con httpbin.org API
├── dto/
│   ├── PostDTO.java                  # DTO para posts
│   ├── UserDTO.java                  # DTO para usuarios
│   └── HttpBinResponseDTO.java       # DTO para respuestas de httpbin
├── service/
│   ├── JsonPlaceholderService.java   # Servicio con ejemplos RestTemplate/WebClient
│   └── HttpBinService.java           # Servicio con ejemplos avanzados
├── exception/
│   ├── ExternalApiException.java     # Excepción personalizada
│   └── GlobalExceptionHandler.java   # Manejador global de excepciones
└── DemoApplication.java              # Clase principal
```

## 🚀 Cómo arrancar

### Opción 1: Con Docker Compose

```bash
# 1. Copiar archivo de configuración
cp .env.sample .env

# 2. Iniciar aplicación
docker compose up -d --build

# 3. La API estará disponible en http://localhost:8080
```

### Opción 2: Sin Docker

```bash
# 1. Compilar el proyecto
mvn clean package

# 2. Ejecutar la aplicación
mvn spring-boot:run

# 3. La API estará disponible en http://localhost:8080
```

## 📖 APIs Externas Utilizadas

Este proyecto usa APIs públicas gratuitas para ejemplos:

### 1. JSONPlaceholder
- **URL**: https://jsonplaceholder.typicode.com
- **Propósito**: API REST de prueba con posts y usuarios
- **Endpoints usados**:
  - `GET /posts` - Listar posts
  - `GET /posts/{id}` - Obtener post por ID
  - `POST /posts` - Crear post
  - `GET /users/{id}` - Obtener usuario por ID

### 2. httpbin.org
- **URL**: https://httpbin.org
- **Propósito**: API de prueba para diferentes aspectos HTTP
- **Endpoints usados**:
  - `GET /get` - Ver información de la petición
  - `POST /post` - Probar POST requests
  - Query parameters y headers personalizados

## 🧪 Cómo probar

### 1. Información de la API

```bash
curl http://localhost:8080/
```

### 2. Ejemplos con RestTemplate

#### Obtener un post por ID
```bash
curl http://localhost:8080/api/jsonplaceholder/resttemplate/posts/1
```

#### Obtener todos los posts
```bash
curl http://localhost:8080/api/jsonplaceholder/resttemplate/posts
```

#### Crear un post
```bash
curl -X POST http://localhost:8080/api/jsonplaceholder/resttemplate/posts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "Mi nuevo post",
    "body": "Contenido del post"
  }'
```

#### Obtener un usuario
```bash
curl http://localhost:8080/api/jsonplaceholder/resttemplate/users/1
```

#### GET request a httpbin
```bash
curl http://localhost:8080/api/httpbin/resttemplate/get
```

#### POST request a httpbin
```bash
curl -X POST http://localhost:8080/api/httpbin/resttemplate/post \
  -H "Content-Type: application/json" \
  -d '{"key": "value", "number": 123}'
```

### 3. Ejemplos con WebClient

#### Obtener un post por ID
```bash
curl http://localhost:8080/api/jsonplaceholder/webclient/posts/1
```

#### Obtener todos los posts
```bash
curl http://localhost:8080/api/jsonplaceholder/webclient/posts
```

#### Crear un post
```bash
curl -X POST http://localhost:8080/api/jsonplaceholder/webclient/posts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "Mi nuevo post con WebClient",
    "body": "Contenido del post"
  }'
```

#### Obtener múltiples posts en paralelo
```bash
curl "http://localhost:8080/api/jsonplaceholder/webclient/posts/batch?ids=1,2,3,4,5"
```

#### GET request a httpbin con WebClient
```bash
curl http://localhost:8080/api/httpbin/webclient/get
```

#### GET request con query parameters
```bash
curl "http://localhost:8080/api/httpbin/webclient/get-with-params?param1=value1&param2=value2"
```

#### GET request con headers personalizados
```bash
curl http://localhost:8080/api/httpbin/webclient/get-with-headers \
  -H "X-Custom-Header: mi-header-personalizado"
```

## 📚 Explicación de Conceptos

### Configuración de RestTemplate

**Archivo: `HttpClientConfig.java`**

```java
@Bean
public RestTemplate restTemplate() {
    // Configurar factory con timeouts
    HttpComponentsClientHttpRequestFactory factory = 
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(connectTimeout);
    factory.setReadTimeout(readTimeout);
    
    // Crear RestTemplate con factory configurada
    RestTemplate restTemplate = new RestTemplate(factory);
    
    // Agregar interceptor para logging
    restTemplate.setInterceptors(interceptors);
    
    return restTemplate;
}
```

**Características:**
- ✅ Configuración de timeouts
- ✅ Interceptores para logging
- ✅ Manejo de errores

### Configuración de WebClient

**Archivo: `HttpClientConfig.java`**

```java
@Bean
public WebClient webClient() {
    return WebClient.builder()
            // Configurar timeouts
            .clientConnector(
                reactor.netty.http.client.HttpClient.create()
                    .responseTimeout(Duration.ofMillis(readTimeout))
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
            )
            // Agregar filtros para logging
            .filter(logRequest())
            .filter(logResponse())
            .build();
}
```

**Características:**
- ✅ Configuración de timeouts reactiva
- ✅ Filtros para logging
- ✅ No bloqueante

### Uso de RestTemplate

**Ejemplo básico:**

```java
public PostDTO getPostById(Long id) {
    String url = baseUrl + "/posts/" + id;
    ResponseEntity<PostDTO> response = 
        restTemplate.getForEntity(url, PostDTO.class);
    return response.getBody();
}
```

**Manejo de errores:**

```java
try {
    ResponseEntity<PostDTO> response = 
        restTemplate.getForEntity(url, PostDTO.class);
    return response.getBody();
} catch (HttpClientErrorException e) {
    // Error 4xx
    throw new ExternalApiException("API", "Error del cliente", 
        e.getStatusCode().value());
} catch (HttpServerErrorException e) {
    // Error 5xx
    throw new ExternalApiException("API", "Error del servidor", 
        e.getStatusCode().value());
} catch (ResourceAccessException e) {
    // Timeout o conexión
    throw new ExternalApiException("API", "Error de conexión", 0);
}
```

### Uso de WebClient

**Ejemplo básico:**

```java
public Mono<PostDTO> getPostById(Long id) {
    return webClient
            .get()
            .uri(baseUrl + "/posts/{id}", id)
            .retrieve()
            .bodyToMono(PostDTO.class);
}
```

**Manejo de errores:**

```java
return webClient
        .get()
        .uri(baseUrl + "/posts/{id}", id)
        .retrieve()
        .bodyToMono(PostDTO.class)
        .onErrorMap(WebClientResponseException.class, ex -> 
            new ExternalApiException("API", 
                "Error: " + ex.getMessage(), 
                ex.getStatusCode().value()))
        .onErrorMap(WebClientException.class, ex -> 
            new ExternalApiException("API", 
                "Error de conexión", 0));
```

**Ejemplo avanzado: Múltiples peticiones en paralelo**

```java
public Flux<PostDTO> getMultiplePosts(List<Long> ids) {
    return Flux.fromIterable(ids)
            .flatMap(id -> 
                webClient
                    .get()
                    .uri(baseUrl + "/posts/{id}", id)
                    .retrieve()
                    .bodyToMono(PostDTO.class)
            );
}
```

Esto hace todas las peticiones en paralelo sin bloquear hilos.

### Interceptores y Filtros

**RestTemplate - Interceptor:**

```java
interceptors.add((request, body, execution) -> {
    log.info("Request: {} {}", request.getMethod(), request.getURI());
    long startTime = System.currentTimeMillis();
    try {
        var response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Response: {} - Duration: {}ms", 
            response.getStatusCode(), duration);
        return response;
    } catch (Exception e) {
        log.error("Error: {}", request.getURI(), e);
        throw e;
    }
});
```

**WebClient - Filtro:**

```java
private ExchangeFilterFunction logRequest() {
    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
        log.info("Request: {} {}", 
            clientRequest.method(), clientRequest.url());
        return Mono.just(clientRequest);
    });
}
```

### Manejo de Excepciones

Este proyecto incluye un `GlobalExceptionHandler` que maneja:

- `ExternalApiException`: Excepciones personalizadas
- `HttpClientErrorException`: Errores 4xx de RestTemplate
- `HttpServerErrorException`: Errores 5xx de RestTemplate
- `ResourceAccessException`: Errores de conexión/timeout de RestTemplate
- `WebClientResponseException`: Errores de respuesta de WebClient
- `WebClientException`: Otros errores de WebClient

**Ejemplo de respuesta de error:**

```json
{
  "timestamp": "2026-01-28T10:30:00",
  "status": 404,
  "error": "Error al consumir API externa",
  "message": "Error al consumir API 'JSONPlaceholder': Post no encontrado (Status: 404)",
  "api": "JSONPlaceholder"
}
```

## 🎓 Buenas Prácticas

### 1. Siempre configura timeouts

```yaml
http:
  client:
    timeout:
      connect: 5000
      read: 10000
```

**Razón**: Evita que tu aplicación se quede bloqueada esperando respuestas.

### 2. Maneja todos los tipos de errores

- Errores 4xx (cliente)
- Errores 5xx (servidor)
- Timeouts
- Errores de conexión

**Razón**: Proporciona mejor experiencia al usuario y facilita el debugging.

### 3. Usa DTOs para respuestas

```java
public class PostDTO {
    private Long id;
    private String title;
    private String body;
}
```

**Razón**: Type-safe, fácil de mantener, desacopla tu código de la estructura de la API externa.

### 4. Implementa logging

- Log de peticiones salientes
- Log de respuestas recibidas
- Log de errores
- Métricas de tiempo de respuesta

**Razón**: Facilita debugging y monitoreo.

### 5. Prefiere WebClient para nuevos proyectos

- Más eficiente
- No bloqueante
- Mejor para alta concurrencia
- Recomendado por Spring

**Razón**: Mejor rendimiento y escalabilidad.

### 6. Usa interceptores/filtros para concerns transversales

- Logging
- Autenticación
- Métricas
- Retry logic

**Razón**: Código más limpio y reutilizable.

### 7. Configuración externa

```yaml
external:
  apis:
    jsonplaceholder:
      base-url: https://jsonplaceholder.typicode.com
      timeout:
        connect: 5000
        read: 10000
```

**Razón**: Fácil de cambiar sin recompilar, diferente por ambiente.

## 🔍 Endpoints Disponibles

### JSONPlaceholder API

| Método | Endpoint | Cliente | Descripción |
|--------|----------|---------|-------------|
| GET | `/api/jsonplaceholder/resttemplate/posts/{id}` | RestTemplate | Obtener post por ID |
| GET | `/api/jsonplaceholder/resttemplate/posts` | RestTemplate | Listar todos los posts |
| POST | `/api/jsonplaceholder/resttemplate/posts` | RestTemplate | Crear post |
| GET | `/api/jsonplaceholder/resttemplate/users/{id}` | RestTemplate | Obtener usuario por ID |
| GET | `/api/jsonplaceholder/webclient/posts/{id}` | WebClient | Obtener post por ID |
| GET | `/api/jsonplaceholder/webclient/posts` | WebClient | Listar todos los posts |
| POST | `/api/jsonplaceholder/webclient/posts` | WebClient | Crear post |
| GET | `/api/jsonplaceholder/webclient/users/{id}` | WebClient | Obtener usuario por ID |
| GET | `/api/jsonplaceholder/webclient/posts/batch?ids=1,2,3` | WebClient | Obtener múltiples posts en paralelo |

### httpbin.org API

| Método | Endpoint | Cliente | Descripción |
|--------|----------|---------|-------------|
| GET | `/api/httpbin/resttemplate/get` | RestTemplate | GET request simple |
| POST | `/api/httpbin/resttemplate/post` | RestTemplate | POST request con body |
| GET | `/api/httpbin/webclient/get` | WebClient | GET request simple |
| POST | `/api/httpbin/webclient/post` | WebClient | POST request con body |
| GET | `/api/httpbin/webclient/get-with-params?param1=v1&param2=v2` | WebClient | GET con query parameters |
| GET | `/api/httpbin/webclient/get-with-headers` | WebClient | GET con headers personalizados |

## 💡 Casos de Uso Comunes

### 1. Consumir API REST simple

**RestTemplate:**
```java
PostDTO post = restTemplate.getForObject(url, PostDTO.class);
```

**WebClient:**
```java
Mono<PostDTO> post = webClient.get()
    .uri(url)
    .retrieve()
    .bodyToMono(PostDTO.class);
```

### 2. Enviar datos (POST)

**RestTemplate:**
```java
PostDTO created = restTemplate.postForObject(url, post, PostDTO.class);
```

**WebClient:**
```java
Mono<PostDTO> created = webClient.post()
    .uri(url)
    .bodyValue(post)
    .retrieve()
    .bodyToMono(PostDTO.class);
```

### 3. Múltiples peticiones en paralelo

**WebClient (recomendado):**
```java
Flux<PostDTO> posts = Flux.fromIterable(ids)
    .flatMap(id -> webClient.get()
        .uri(url + "/" + id)
        .retrieve()
        .bodyToMono(PostDTO.class));
```

### 4. Headers personalizados

**RestTemplate:**
```java
HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", "Bearer token");
HttpEntity<?> entity = new HttpEntity<>(headers);
ResponseEntity<PostDTO> response = restTemplate.exchange(
    url, HttpMethod.GET, entity, PostDTO.class);
```

**WebClient:**
```java
Mono<PostDTO> post = webClient.get()
    .uri(url)
    .header("Authorization", "Bearer token")
    .retrieve()
    .bodyToMono(PostDTO.class);
```

### 5. Query parameters

**RestTemplate:**
```java
String url = baseUrl + "/posts?userId=" + userId;
PostDTO[] posts = restTemplate.getForObject(url, PostDTO[].class);
```

**WebClient:**
```java
Mono<PostDTO[]> posts = webClient.get()
    .uri(uriBuilder -> uriBuilder
        .path("/posts")
        .queryParam("userId", userId)
        .build())
    .retrieve()
    .bodyToMono(PostDTO[].class);
```

## 🎯 Cuándo Usar Cada Cliente

### Usa RestTemplate cuando:
- ✅ Migras código legacy existente
- ✅ Tienes una aplicación completamente síncrona
- ✅ Necesitas simplicidad sobre rendimiento
- ✅ Haces pocas peticiones concurrentes

### Usa WebClient cuando:
- ✅ Estás creando un nuevo proyecto
- ✅ Necesitas alta concurrencia
- ✅ Quieres mejor rendimiento
- ✅ Usas Spring WebFlux
- ✅ Necesitas programación reactiva

**Recomendación general**: Usa **WebClient** para nuevos proyectos.

## 🔄 Migración de RestTemplate a WebClient

Si tienes código con RestTemplate y quieres migrar a WebClient:

**Antes (RestTemplate):**
```java
PostDTO post = restTemplate.getForObject(url, PostDTO.class);
```

**Después (WebClient):**
```java
PostDTO post = webClient.get()
    .uri(url)
    .retrieve()
    .bodyToMono(PostDTO.class)
    .block(); // Bloquea para mantener comportamiento síncrono
```

**Mejor (WebClient reactivo):**
```java
Mono<PostDTO> postMono = webClient.get()
    .uri(url)
    .retrieve()
    .bodyToMono(PostDTO.class);
// Usa el Mono en una cadena reactiva
```

## 📖 Recursos Adicionales

- [Spring RestTemplate Documentation](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate)
- [Spring WebClient Documentation](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html)
- [Project Reactor Documentation](https://projectreactor.io/docs/core/release/reference/)
- [JSONPlaceholder API](https://jsonplaceholder.typicode.com/)
- [httpbin.org](https://httpbin.org/)

## 💡 Tips y Trucos

1. **Timeouts configurables**: Usa `application.yml` para diferentes valores por ambiente.

2. **Retry logic**: Puedes agregar lógica de reintento usando interceptores (RestTemplate) o filtros (WebClient).

3. **Circuit Breaker**: Considera usar Resilience4j o Spring Cloud Circuit Breaker para APIs críticas.

4. **Caché**: Para APIs que no cambian frecuentemente, considera agregar caché.

5. **Métricas**: Usa Micrometer para exponer métricas de tus llamadas a APIs externas.

6. **Testing**: Usa `MockRestServiceServer` (RestTemplate) o `WebTestClient` (WebClient) para tests.

## 🎯 Próximos Pasos

1. **Autenticación**: Agregar ejemplos con APIs que requieren autenticación (OAuth2, API keys, etc.)
2. **Circuit Breaker**: Implementar circuit breaker para resiliencia
3. **Retry Logic**: Agregar lógica de reintento automático
4. **Caché**: Implementar caché para respuestas frecuentes
5. **Métricas**: Agregar métricas con Micrometer
6. **Testing**: Crear tests unitarios e integración

---

**¡Explora los ejemplos y experimenta con diferentes APIs externas!** 🚀
