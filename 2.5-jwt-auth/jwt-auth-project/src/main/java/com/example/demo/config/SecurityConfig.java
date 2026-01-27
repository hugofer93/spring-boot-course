package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * Esta clase configura:
 * - La cadena de filtros de seguridad
 * - Las rutas públicas y protegidas
 * - El filtro JWT personalizado
 * - El proveedor de autenticación (DAO)
 * - El codificador de contraseñas (BCrypt)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * Configuración Stateless: No se crean sesiones HTTP porque usamos JWT.
     * Cada petición debe incluir el token JWT en el header Authorization.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF porque es una API REST stateless con JWT
            // CSRF protege contra ataques en aplicaciones web con sesiones, pero
            // en APIs REST stateless con JWT no es necesario
                .csrf(AbstractHttpConfigurer::disable)
            
            // Configuración de autorización de rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (no requieren autenticación)
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()      // Registro de usuarios
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()  // Login
                        .requestMatchers(HttpMethod.GET, "/").permitAll()                 // Home
                        // Permitir errores de validación y excepciones sin autenticación
                        .requestMatchers("/error").permitAll()
                        // Todas las demás rutas requieren autenticación JWT
                        .anyRequest().authenticated()
                )
            
            // Configuración Stateless: No crear sesiones HTTP
            // Cada petición debe incluir el token JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
            
            // Configurar el proveedor de autenticación
                .authenticationProvider(authenticationProvider())
            
            // Agregar el filtro JWT antes del filtro de autenticación por defecto
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura el proveedor de autenticación DAO.
     * 
     * Este proveedor carga los usuarios desde la base de datos
     * usando UserDetailsService y valida las contraseñas con PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean para el AuthenticationManager.
     * 
     * Necesario para autenticar usuarios en el endpoint de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean para encriptar contraseñas.
     * 
     * BCrypt es el estándar actual recomendado por Spring Security.
     * Genera hashes seguros con salt automático.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
