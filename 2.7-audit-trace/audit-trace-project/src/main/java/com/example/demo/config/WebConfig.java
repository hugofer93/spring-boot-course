package com.example.demo.config;

import com.example.demo.interceptor.AuditInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de interceptores.
 * 
 * Esta clase registra los interceptores que queremos usar
 * en nuestra aplicación.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuditInterceptor auditInterceptor;

    /**
     * Registra los interceptores.
     * 
     * Los interceptores se aplican a todas las rutas que coincidan
     * con el patrón especificado.
     * 
     * @param registry El registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditInterceptor)
                .addPathPatterns("/api/**") // Aplicar a todas las rutas /api/**
                .excludePathPatterns("/", "/health"); // Excluir rutas específicas
    }
}
