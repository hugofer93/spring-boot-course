package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para representar respuesta de httpbin.org.
 * 
 * httpbin.org es una API de prueba útil para:
 * - Probar diferentes métodos HTTP
 * - Ver headers enviados
 * - Probar autenticación
 * - Simular delays y errores
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpBinResponseDTO {
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("headers")
    private Map<String, String> headers;
    
    @JsonProperty("origin")
    private String origin;
    
    @JsonProperty("args")
    private Map<String, String> args;
    
    @JsonProperty("data")
    private String data;
    
    @JsonProperty("json")
    private Map<String, Object> json;
}
