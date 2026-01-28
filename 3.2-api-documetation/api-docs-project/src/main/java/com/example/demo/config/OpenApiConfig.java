package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI 3 (Swagger).
 * 
 * Esta clase define la información general de la API que aparecerá
 * en la documentación de Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Productos",
        version = "1.0.0",
        description = "API RESTful para gestión de productos. " +
                     "Esta API permite crear, leer, actualizar y eliminar productos. " +
                     "Incluye validaciones y documentación completa con Swagger/OpenAPI 3.",
        contact = @Contact(
            name = "Equipo de Desarrollo",
            email = "dev@example.com",
            url = "https://example.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de desarrollo local"
        ),
        @Server(
            url = "https://api.example.com",
            description = "Servidor de producción"
        )
    }
)
public class OpenApiConfig {
    // La configuración se hace mediante anotaciones
}
