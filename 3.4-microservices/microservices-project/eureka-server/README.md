# Eureka Server

Servicio de descubrimiento (Service Discovery) para la arquitectura de microservicios.

## 🎯 Propósito

**Eureka Server** actúa como registro centralizado donde todos los microservicios se registran y pueden descubrir otros servicios disponibles. Es el componente fundamental de la arquitectura de microservicios con Spring Cloud.

## 📋 Funcionalidades

- ✅ **Registro de servicios**: Los microservicios se registran automáticamente al iniciar
- ✅ **Descubrimiento de servicios**: Los servicios pueden encontrar otros servicios consultando Eureka
- ✅ **Health checks**: Detecta servicios caídos y los elimina del registro
- ✅ **Dashboard web**: Interfaz visual para ver todos los servicios registrados

## 🛠️ Tecnologías

- **Spring Boot 3.2**
- **Spring Cloud Netflix Eureka Server**
- **Java 17**

## ⚙️ Configuración

### application.yml

```yaml
spring:
  application:
    name: eureka-server

server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false  # Eureka no se registra a sí mismo
    fetch-registry: false        # No necesita obtener registro de otros servidores
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Configuración Clave

- **Puerto**: 8761 (puerto estándar de Eureka)
- **register-with-eureka: false**: Eureka Server no se registra a sí mismo
- **fetch-registry: false**: No necesita sincronizar con otros servidores Eureka

## 🚀 Ejecución

### Opción 1: Maven

```bash
mvn spring-boot:run
```

### Opción 2: Docker

```bash
docker build -t eureka-server .
docker run -p 8761:8761 eureka-server
```

### Opción 3: Docker Compose

```bash
# Desde el directorio raíz del proyecto
docker-compose up eureka-server
```

## 🌐 Acceso

Una vez iniciado, puedes acceder a:

- **Dashboard**: http://localhost:8761
- **API REST**: http://localhost:8761/eureka/

## 📊 Dashboard de Eureka

El dashboard muestra:

- **Instances currently registered with Eureka**: Lista de servicios registrados
- **General Info**: Información general del servidor
- **DS Replicas**: Réplicas del servidor (si hay múltiples instancias)

### Ejemplo de Servicios Registrados

Cuando los microservicios están corriendo, verás:

```
Application          AMIs        Availability Zones  Status
API-GATEWAY         n/a (1)     (1)                UP (1) - api-gateway:8080
PRODUCT-SERVICE     n/a (1)     (1)                UP (1) - product-service:8082
USER-SERVICE        n/a (1)     (1)                UP (1) - user-service:8081
```

## 🔍 Verificación

### Verificar que Eureka está corriendo

```bash
curl http://localhost:8761/eureka/apps
```

### Ver servicios registrados (JSON)

```bash
curl http://localhost:8761/eureka/apps | jq
```

## 📝 Notas Importantes

1. **Orden de inicio**: Eureka Server debe iniciarse **ANTES** que los otros microservicios
2. **Puerto**: El puerto 8761 debe estar disponible
3. **Red**: En Docker, todos los servicios deben estar en la misma red para comunicarse

## 🐛 Troubleshooting

### El dashboard no carga

- Verifica que el puerto 8761 esté disponible
- Revisa los logs para ver errores de inicio
- Asegúrate de que no haya conflictos de puertos

### Los servicios no aparecen en el dashboard

- Verifica que los servicios tengan la configuración correcta de Eureka Client
- Asegúrate de que los servicios puedan alcanzar Eureka en `http://localhost:8761/eureka/`
- Espera unos segundos después de iniciar los servicios (necesitan tiempo para registrarse)

### Error de conexión desde otros servicios

- En Docker: Verifica que todos los servicios estén en la misma red
- En local: Verifica que la URL de Eureka sea accesible desde cada servicio
- Revisa los logs de los servicios para ver errores de conexión

## 📚 Recursos

- [Spring Cloud Netflix Eureka Documentation](https://spring.io/projects/spring-cloud-netflix)
- [Eureka GitHub Repository](https://github.com/Netflix/eureka)

---

**Importante**: Este servicio debe estar corriendo antes de iniciar cualquier otro microservicio.
