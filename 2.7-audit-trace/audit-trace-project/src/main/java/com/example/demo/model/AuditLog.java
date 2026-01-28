package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Modelo simple para representar un log de auditoría.
 * 
 * En un proyecto real, esto podría ser una entidad JPA
 * que se persiste en base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    private LocalDateTime timestamp;
    private String method;
    private String path;
    private String user;
    private String action;
    private String details;
    private Long duration; // en milisegundos
}
