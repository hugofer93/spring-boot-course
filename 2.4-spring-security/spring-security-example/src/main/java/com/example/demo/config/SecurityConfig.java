package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF porque es una API REST stateless accesible desde clientes no navegador (como cURL/Postman)
                .csrf(AbstractHttpConfigurer::disable)
            
            // Configuración de rutas
                .authorizeHttpRequests(auth -> auth
                        // Permitir POST a /api/users (Registro de usuarios) sin autenticación
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // Permitir GET a la ruta raíz ("/") sin autenticación
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
            
            // Habilitar autenticación HTTP Basic (usuario y contraseña en headers)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Bean para encriptar contraseñas.
     * BCrypt es el estándar actual recomendado por Spring Security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
