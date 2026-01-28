package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para consultar logs de auditoría.
 * 
 * Este endpoint permite ver los logs de auditoría registrados
 * por los filtros e interceptores.
 */
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    /**
     * Obtener todos los logs de auditoría.
     * 
     * @return Lista de logs de auditoría
     */
    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }
}
