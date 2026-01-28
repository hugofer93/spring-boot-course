package com.example.demo.filter;

import com.example.demo.model.AuditLog;
import com.example.demo.service.AuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filtro HTTP para auditoría de requests y responses.
 * 
 * Este filtro:
 * 1. Intercepta todas las peticiones HTTP
 * 2. Registra información de la petición (método, path, usuario, etc.)
 * 3. Mide el tiempo de ejecución
 * 4. Registra la respuesta (status code)
 * 5. Envía la información al servicio de auditoría
 * 
 * @Order(1) asegura que este filtro se ejecute antes que otros filtros
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class AuditFilter extends OncePerRequestFilter {

    private final AuditService auditService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Registrar tiempo de inicio
        long startTime = System.currentTimeMillis();
        LocalDateTime timestamp = LocalDateTime.now();

        // Wrappers para poder leer el body de request/response múltiples veces
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            // Continuar con la cadena de filtros
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Calcular duración
            long duration = System.currentTimeMillis() - startTime;

            // Obtener información de la petición
            String method = request.getMethod();
            String path = request.getRequestURI();
            String user = getUserFromRequest(request); // Podría venir de JWT, sesión, etc.
            int statusCode = wrappedResponse.getStatus();

            // Crear log de auditoría
            AuditLog auditLog = new AuditLog(
                    timestamp,
                    method,
                    path,
                    user,
                    "HTTP_REQUEST",
                    String.format("Status: %d", statusCode),
                    duration
            );

            // Registrar en el servicio de auditoría
            auditService.log(auditLog);

            // Copiar el body de la respuesta (necesario cuando usamos ContentCachingResponseWrapper)
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * Extrae el usuario de la petición.
     * 
     * En un proyecto real, esto podría venir de:
     * - Token JWT en el header Authorization
     * - Sesión HTTP
     * - Header personalizado
     * 
     * @param request La petición HTTP
     * @return El nombre del usuario o "anonymous" si no está disponible
     */
    private String getUserFromRequest(HttpServletRequest request) {
        // Ejemplo simple: buscar en header personalizado
        String user = request.getHeader("X-User");
        if (user != null && !user.isEmpty()) {
            return user;
        }
        
        // Si no hay header, usar "anonymous"
        return "anonymous";
    }
}
