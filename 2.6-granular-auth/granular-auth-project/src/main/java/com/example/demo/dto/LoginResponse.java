package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO simple para respuestas de login.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
}
