package com.petsalud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad Spring Security
 * Sistema de Gestión Veterinaria PetSalud
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración de la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Configuración de autorización de peticiones
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (sin autenticación)
                .requestMatchers(
                    "/api/auth/**",
                    "/api/public/**",
                    "/api/duenos/registro",
                    "/api/resultados/pdf/**",
                    "/h2-console/**"
                ).permitAll()
                
                // Rutas para VETERINARIOS
                .requestMatchers(
                    "/api/ordenes/**",
                    "/api/resultados/validar/**",
                    "/api/mascotas/**"
                ).hasAnyRole("VETERINARIO", "ADMIN")
                
                // Rutas para TÉCNICOS
                .requestMatchers(
                    "/api/toma-muestras/**",
                    "/api/resultados/registrar/**"
                ).hasAnyRole("TECNICO", "ADMIN")
                
                // Rutas para RECEPCIONISTAS
                .requestMatchers(
                    "/api/duenos/**",
                    "/api/facturas/**"
                ).hasAnyRole("RECEPCIONISTA", "ADMIN")
                
                // Rutas de REPORTES (solo admin)
                .requestMatchers(
                    "/api/reportes/**"
                ).hasRole("ADMIN")
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración de sesión (stateless para REST API)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configuración de HTTP Basic (para desarrollo)
            .httpBasic(basic -> basic.realmName("Veterinaria PetSalud"))
            
            // Headers de seguridad
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // Para H2 Console
                .xssProtection(xss -> xss.disable())
                .contentSecurityPolicy(csp -> 
                    csp.policyDirectives("default-src 'self'")
                )
            );

        return http.build();
    }

    /**
     * Bean para codificación de contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Fuerza 12 (recomendado)
    }

    /**
     * Configuración adicional de CORS (ya manejado en CorsConfig)
     * Esta sección puede omitirse si CorsConfig está activo
     */
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    */
}