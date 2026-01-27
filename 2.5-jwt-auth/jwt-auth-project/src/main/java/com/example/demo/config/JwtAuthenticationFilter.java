package com.example.demo.config;

import com.example.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado para autenticación JWT.
 * 
 * Este filtro intercepta todas las peticiones HTTP y:
 * 1. Extrae el token JWT del header "Authorization"
 * 2. Valida el token usando JwtService
 * 3. Carga los detalles del usuario desde la base de datos
 * 4. Establece la autenticación en el SecurityContext de Spring Security
 * 
 * Extiende OncePerRequestFilter para asegurar que el filtro se ejecute
 * solo una vez por petición.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Método principal del filtro que se ejecuta en cada petición.
     * 
     * @param request Petición HTTP
     * @param response Respuesta HTTP
     * @param filterChain Cadena de filtros de Spring Security
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Extraer el token del header Authorization
        final String authHeader = request.getHeader("Authorization");
        
        // Si no hay header Authorization o no empieza con "Bearer ", continuar sin autenticación
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token (remover el prefijo "Bearer ")
        final String jwt = authHeader.substring(7);
        
        // Extraer el username del token
        final String username = jwtService.extractUsername(jwt);

        // Si hay username y no hay autenticación en el contexto actual
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargar los detalles del usuario desde la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validar el token con los detalles del usuario
            if (jwtService.validateToken(jwt, userDetails)) {
                // Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials (no necesarios después de la autenticación)
                        userDetails.getAuthorities()
                );
                
                // Agregar detalles de la petición HTTP
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establecer la autenticación en el SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
