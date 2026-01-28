package com.example.demo.service;

import com.example.demo.model.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple de auditoría.
 * 
 * En un proyecto real, esto podría guardar los logs en:
 * - Base de datos
 * - Sistema de logging centralizado (ELK, Splunk, etc.)
 * - Archivos de log
 * 
 * Para este ejemplo, solo los guarda en memoria y los imprime en consola.
 */
@Service
@Slf4j
public class AuditService {

    // En memoria para este ejemplo simple
    // En producción, usar base de datos o sistema de logging
    private final List<AuditLog> auditLogs = new ArrayList<>();

    /**
     * Registra un evento de auditoría.
     * 
     * @param auditLog El log de auditoría a registrar
     */
    public void log(AuditLog auditLog) {
        auditLogs.add(auditLog);
        
        // Imprimir en consola (en producción, usar logger apropiado)
        log.info("AUDIT: {} {} {} - {} - {}ms", 
                auditLog.getTimestamp(),
                auditLog.getMethod(),
                auditLog.getPath(),
                auditLog.getAction(),
                auditLog.getDuration());
    }

    /**
     * Obtiene todos los logs de auditoría.
     * 
     * @return Lista de logs de auditoría
     */
    public List<AuditLog> getAllLogs() {
        return new ArrayList<>(auditLogs);
    }

    /**
     * Limpia los logs (útil para testing).
     */
    public void clearLogs() {
        auditLogs.clear();
    }
}
