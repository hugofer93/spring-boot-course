package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.service.CustomUserDetailsService;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * Esta clase configura:
 * - Autorización granular por roles
 * - Métodos de seguridad habilitados (@PreAuthorize)
 * - Rutas públicas y protegidas
 * - Autenticación JWT (stateless)
 * - Usuarios en base de datos (a través de CustomUserDetailsService)
 * 
 * Enfoque: Este proyecto demuestra autorización granular con JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize y @PostAuthorize
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    // Usar @Lazy para romper el ciclo de dependencias
    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                         CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para API REST stateless
                .csrf(AbstractHttpConfigurer::disable)
            
            // Configuración de autorización de rutas
            // IMPORTANTE: El orden importa. Las rutas más específicas deben ir primero
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (deben estar PRIMERO)
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        
                        // Rutas para usuarios autenticados (cualquier rol)
                        .requestMatchers("/api/user/**").authenticated()
                        
                        // Rutas específicas por rol usando hasRole()
                        .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "ADMIN")
                        
                        // Rutas solo para administradores
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        
                        // Todas las demás rutas requieren autenticación
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
     * Usa CustomUserDetailsService para cargar usuarios desde la base de datos.
     * CustomUserDetailsService ya es un bean de Spring (@Service), por lo que
     * Spring lo inyecta automáticamente.
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
     * Necesario para autenticar usuarios en el endpoint de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean para encriptar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
