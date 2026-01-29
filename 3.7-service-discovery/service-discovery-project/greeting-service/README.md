# Greeting Service

Microservicio que se registra en Eureka y expone un endpoint de saludo. El **demo-client** lo descubre por nombre (`greeting-service`) y lo invoca sin conocer host ni puerto.

- **Puerto:** 8081
- **Endpoint:** `GET /api/hello`

En Docker usa el perfil `docker` y se conecta a `eureka-server:8761`.
