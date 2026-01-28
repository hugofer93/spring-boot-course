package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un Post de JSONPlaceholder API.
 * 
 * Ejemplo de respuesta de la API:
 * {
 *   "userId": 1,
 *   "id": 1,
 *   "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
 *   "body": "quia et suscipit..."
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("body")
    private String body;
}
