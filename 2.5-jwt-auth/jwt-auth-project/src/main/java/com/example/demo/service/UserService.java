package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de negocio para la gestión de usuarios.
 * 
 * Contiene la lógica de negocio relacionada con usuarios:
 * - Validaciones de negocio
 * - Transformaciones entre DTOs y entidades
 * - Operaciones de base de datos
 * 
 * Sigue el principio de responsabilidad única: solo maneja lógica de usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario.
     * 
     * Validaciones:
     * - El email no debe estar registrado
     * - El username no debe estar registrado
     * 
     * @param userRequestDTO Datos del usuario a crear
     * @return UserResponseDTO con los datos del usuario creado (sin password)
     */
    @Transactional
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        // Validar que el email no esté registrado
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new BusinessRuleException("Email is already registered");
        }

        // Validar que el username no esté registrado
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new BusinessRuleException("Username is already taken");
        }

        // Crear la entidad User desde el DTO
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Encriptar contraseña
        user.setRole("ROLE_USER"); // Rol por defecto
        user.setEnabled(true);

        // Guardar en la base de datos
        User savedUser = userRepository.save(user);

        // Convertir a DTO para la respuesta (sin password)
        return toResponseDTO(savedUser);
    }

    /**
     * Obtiene todos los usuarios.
     * 
     * @return Lista de UserResponseDTO (sin passwords)
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad User a UserResponseDTO.
     * 
     * Este método asegura que nunca expongamos la contraseña
     * en las respuestas HTTP.
     * 
     * @param user Entidad User
     * @return UserResponseDTO sin password
     */
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }
}