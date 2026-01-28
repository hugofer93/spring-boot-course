package com.example.demo.service.user;

import org.springframework.stereotype.Service;

/**
 * EJEMPLO DE CÓDIGO "MALO" - NO USAR EN PRODUCCIÓN
 * 
 * Violaciones de Clean Code:
 * - Variables con nombres genéricos (x, y, z, data, temp)
 * - Magic numbers (18, 100)
 * - Lógica compleja sin separar
 * - Falta de validación
 * - Código difícil de entender
 */
@Service
public class BadUserService {

    public String processUser(String n, int a, String e) {
        String result = "";
        
        // Validación mezclada con lógica de negocio
        if (n != null && !n.isEmpty() && a >= 18 && a <= 100 && e != null && e.contains("@")) {
            result = "Usuario válido: " + n;
        } else {
            result = "Usuario inválido";
        }
        
        return result;
    }

    // Magic numbers y nombres poco descriptivos
    public double calculateDiscount(double p) {
        double d = 0;
        if (p > 100) {
            d = p * 0.1;
        } else if (p > 50) {
            d = p * 0.05;
        }
        return p - d;
    }

    // Lógica compleja sin separar responsabilidades
    public String formatUserInfo(String firstName, String lastName, int age, String email) {
        String fullName = firstName + " " + lastName;
        String info = fullName + " (" + age + " años) - " + email;
        if (age < 18) {
            info = info + " [MENOR]";
        }
        return info.toUpperCase();
    }
}
