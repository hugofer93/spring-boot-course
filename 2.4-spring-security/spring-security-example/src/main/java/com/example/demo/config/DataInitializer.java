package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "admin";
            String password = "admin";
            String email = "admin@example.com";

            // Find user or create new instance if not exists
            User user = userRepository.findByUsername(username).orElse(new User());

            // Always update critical data to ensure access
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password)); // Generate correct hash
            user.setRole("ADMIN");
            user.setEnabled(true);

            userRepository.save(user);
            System.out.println(">>> Admin user updated/verified successfully. Password: " + password);
        };
    }
}