package com.example.demo.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * EJEMPLO DE CÓDIGO "BUENO" - SIGUIENDO PRINCIPIOS CLEAN CODE
 * 
 * Demuestra:
 * - Nombres descriptivos y significativos
 * - Constantes en lugar de magic numbers
 * - Separación de responsabilidades
 * - Validación clara y explícita
 * - Código fácil de leer y entender
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final int MINIMUM_AGE = 18;
    private static final int MAXIMUM_AGE = 100;
    private static final double HIGH_PURCHASE_DISCOUNT = 0.1;
    private static final double MEDIUM_PURCHASE_DISCOUNT = 0.05;
    private static final double HIGH_PURCHASE_THRESHOLD = 100.0;
    private static final double MEDIUM_PURCHASE_THRESHOLD = 50.0;

    /**
     * Valida y procesa información de usuario.
     * 
     * @param name Nombre del usuario
     * @param age Edad del usuario
     * @param email Email del usuario
     * @return Mensaje de validación
     */
    public String validateAndProcessUser(String name, int age, String email) {
        if (!isValidUser(name, age, email)) {
            return "Usuario inválido";
        }
        
        return buildValidationMessage(name);
    }

    private boolean isValidUser(String name, int age, String email) {
        return isValidName(name) 
                && isValidAge(age) 
                && isValidEmail(email);
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidAge(int age) {
        return age >= MINIMUM_AGE && age <= MAXIMUM_AGE;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    private String buildValidationMessage(String name) {
        return "Usuario válido: " + name;
    }

    /**
     * Calcula el descuento aplicable según el precio de compra.
     * 
     * @param purchasePrice Precio de la compra
     * @return Precio final con descuento aplicado
     */
    public double calculateFinalPrice(double purchasePrice) {
        double discount = calculateDiscount(purchasePrice);
        return purchasePrice - discount;
    }

    private double calculateDiscount(double purchasePrice) {
        if (purchasePrice > HIGH_PURCHASE_THRESHOLD) {
            return purchasePrice * HIGH_PURCHASE_DISCOUNT;
        }
        if (purchasePrice > MEDIUM_PURCHASE_THRESHOLD) {
            return purchasePrice * MEDIUM_PURCHASE_DISCOUNT;
        }
        return 0;
    }

    /**
     * Formatea la información del usuario de manera legible.
     * 
     * @param firstName Nombre
     * @param lastName Apellido
     * @param age Edad
     * @param email Email
     * @return Información formateada
     */
    public String formatUserInformation(String firstName, String lastName, int age, String email) {
        String fullName = buildFullName(firstName, lastName);
        String ageInfo = buildAgeInfo(age);
        String formattedInfo = String.format("%s (%s) - %s", fullName, ageInfo, email);
        
        if (isMinor(age)) {
            formattedInfo += " [MENOR]";
        }
        
        return formattedInfo.toUpperCase();
    }

    private String buildFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    private String buildAgeInfo(int age) {
        return age + " años";
    }

    private boolean isMinor(int age) {
        return age < MINIMUM_AGE;
    }
}
