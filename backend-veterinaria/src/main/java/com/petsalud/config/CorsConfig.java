package com.petsalud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuración CORS para permitir peticiones desde el frontend Angular
 * Veterinaria PetSalud
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales (cookies, headers de autenticación)
        config.setAllowCredentials(true);
        
        // Orígenes permitidos (frontend Angular)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://localhost:4201",
            "https://petsalud.com"
        ));
        
        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "PATCH",
            "OPTIONS"
        ));
        
        // Headers expuestos al cliente
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition"
        ));
        
        // Tiempo de cache para preflight requests (1 hora)
        config.setMaxAge(3600L);
        
        // Aplicar configuración a todas las rutas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}