# Demo Client

Microservicio que se registra en Eureka y **demuestra el descubrimiento**: descubre `greeting-service` por nombre y llama a su endpoint `/api/hello` sin conocer host ni puerto.

- **Puerto:** 8080
- **Endpoint:** `GET /api/discover-and-call` — descubre greeting-service y devuelve la respuesta del saludo.

En Docker usa el perfil `docker` y se conecta a `eureka-server:8761`. Debe arrancar después de **eureka-server** y **greeting-service**.
