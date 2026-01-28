package com.example.demo.exception;

/**
 * Excepción personalizada para errores al consumir APIs externas.
 */
public class ExternalApiException extends RuntimeException {
    
    private final int statusCode;
    private final String apiName;
    
    public ExternalApiException(String apiName, String message, int statusCode) {
        super(String.format("Error al consumir API '%s': %s (Status: %d)", apiName, message, statusCode));
        this.apiName = apiName;
        this.statusCode = statusCode;
    }
    
    public ExternalApiException(String apiName, String message, Throwable cause) {
        super(String.format("Error al consumir API '%s': %s", apiName, message), cause);
        this.apiName = apiName;
        this.statusCode = 0;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getApiName() {
        return apiName;
    }
}
