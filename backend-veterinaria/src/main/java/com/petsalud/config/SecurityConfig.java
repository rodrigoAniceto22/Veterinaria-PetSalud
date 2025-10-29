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
 * VERSIÓN PARA PRUEBAS - Seguridad deshabilitada
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración de la cadena de filtros de seguridad
     * IMPORTANTE: Esta configuración permite TODAS las peticiones sin autenticación
     * Solo para desarrollo/pruebas
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // PERMITIR TODAS LAS PETICIONES (para pruebas)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // Configuración de sesión (stateless para REST API)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Headers de seguridad
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    /**
     * Bean para codificación de contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}