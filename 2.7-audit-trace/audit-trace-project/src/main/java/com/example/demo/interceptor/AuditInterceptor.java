package com.example.demo.interceptor;

import com.example.demo.model.AuditLog;
import com.example.demo.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * Interceptor para auditoría de métodos del controlador.
 * 
 * Los interceptores se ejecutan:
 * - preHandle: Antes de ejecutar el método del controlador
 * - postHandle: Después de ejecutar el método (pero antes de renderizar la vista)
 * - afterCompletion: Después de completar la petición (incluso si hay excepciones)
 * 
 * Este interceptor registra información sobre qué métodos del controlador
 * se están ejecutando y cuánto tiempo toman.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditInterceptor implements HandlerInterceptor {

    private final AuditService auditService;
    private static final String START_TIME_ATTRIBUTE = "startTime";

    /**
     * Se ejecuta ANTES de que se llame al método del controlador.
     * 
     * @param request La petición HTTP
     * @param response La respuesta HTTP
     * @param handler El handler (controlador) que se va a ejecutar
     * @return true para continuar, false para detener la ejecución
     */
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           jakarta.servlet.http.HttpServletResponse response, 
                           Object handler) throws Exception {
        // Guardar tiempo de inicio en un atributo de la petición
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        
        log.debug("Interceptor: preHandle - Método del controlador a ejecutar");
        return true; // Continuar con la ejecución
    }

    /**
     * Se ejecuta DESPUÉS de que se ejecute el método del controlador,
     * pero ANTES de renderizar la vista.
     * 
     * @param request La petición HTTP
     * @param response La respuesta HTTP
     * @param handler El handler que se ejecutó
     * @param modelAndView El modelo y vista (puede ser null en APIs REST)
     */
    @Override
    public void postHandle(HttpServletRequest request, 
                          jakarta.servlet.http.HttpServletResponse response, 
                          Object handler, 
                          ModelAndView modelAndView) throws Exception {
        log.debug("Interceptor: postHandle - Método del controlador ejecutado");
    }

    /**
     * Se ejecuta DESPUÉS de completar la petición (incluso si hay excepciones).
     * 
     * Aquí es donde registramos la auditoría porque sabemos que la petición
     * terminó completamente.
     * 
     * @param request La petición HTTP
     * @param response La respuesta HTTP
     * @param handler El handler que se ejecutó
     * @param ex Excepción si hubo alguna (null si todo fue bien)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               jakarta.servlet.http.HttpServletResponse response, 
                               Object handler, 
                               Exception ex) throws Exception {
        // Obtener tiempo de inicio guardado en preHandle
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime == null) {
            return;
        }

        // Calcular duración
        long duration = System.currentTimeMillis() - startTime;

        // Obtener información
        String method = request.getMethod();
        String path = request.getRequestURI();
        String handlerName = handler.toString();
        String user = getUserFromRequest(request);
        String action = "CONTROLLER_METHOD";
        String details = String.format("Handler: %s, Status: %d", handlerName, response.getStatus());
        
        if (ex != null) {
            details += ", Exception: " + ex.getClass().getSimpleName();
        }

        // Crear log de auditoría
        AuditLog auditLog = new AuditLog(
                LocalDateTime.now(),
                method,
                path,
                user,
                action,
                details,
                duration
        );

        // Registrar en el servicio de auditoría
        auditService.log(auditLog);
        
        log.debug("Interceptor: afterCompletion - Auditoría registrada");
    }

    /**
     * Extrae el usuario de la petición (similar al filtro).
     */
    private String getUserFromRequest(HttpServletRequest request) {
        String user = request.getHeader("X-User");
        return (user != null && !user.isEmpty()) ? user : "anonymous";
    }
}
