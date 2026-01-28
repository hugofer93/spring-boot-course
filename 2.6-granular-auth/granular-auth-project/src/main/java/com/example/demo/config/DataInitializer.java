package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inicializador de datos.
 * 
 * Crea usuarios de ejemplo en la base de datos al iniciar la aplicación.
 * Esto es útil para tener usuarios de prueba disponibles.
 */
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Usuario básico
            createOrUpdateUser(userRepository, passwordEncoder, "user", "password", "ROLE_USER");
            
            // Moderador
            createOrUpdateUser(userRepository, passwordEncoder, "moderator", "password", "ROLE_MODERATOR");
            
            // Administrador
            createOrUpdateUser(userRepository, passwordEncoder, "admin", "password", "ROLE_ADMIN");
            
            System.out.println(">>> Usuarios inicializados correctamente en la base de datos");
        };
    }

    private void createOrUpdateUser(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   String username, String password, String role) {
        User user = userRepository.findByUsername(username)
                .orElse(new User());

        // Actualizar o crear el usuario
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encriptar contraseña
        user.setRole(role);
        user.setEnabled(true);

        userRepository.save(user);
    }
}
