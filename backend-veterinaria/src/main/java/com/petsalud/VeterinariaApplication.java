package com.petsalud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CAMBIO IMPORTANTE: usar jakarta en lugar de javax
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Clase principal de la aplicación
 * Sistema de Gestión Veterinaria PetSalud
 * 
 * @author PetSalud Development Team
 * @version 1.0
 * @since 2025-01-01
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class VeterinariaApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot
     */
    public static void main(String[] args) {
        // Banner personalizado
        System.out.println("\n" +
                "╔═══════════════════════════════════════════════════════════╗\n" +
                "║                                                           ║\n" +
                "║              🐾  SISTEMA VETERINARIA PETSALUD  🐾         ║\n" +
                "║                                                           ║\n" +
                "║            Sistema de Gestión de Exámenes y              ║\n" +
                "║              Tratamientos Veterinarios                   ║\n" +
                "║                                                           ║\n" +
                "║                    Versión 1.0.0                         ║\n" +
                "║                                                           ║\n" +
                "╚═══════════════════════════════════════════════════════════╝\n"
        );

        // Iniciar aplicación
        SpringApplication.run(VeterinariaApplication.class, args);

        System.out.println("\n" +
                "✅ Aplicación iniciada correctamente\n" +
                "🌐 API REST disponible en: http://localhost:8080\n" +
                "📚 Documentación: http://localhost:8080/swagger-ui.html\n" +
                "🗄️ Base de datos: MySQL\n" +
                "🔒 Autenticación: Spring Security\n\n" +
                "═══════════════════════════════════════════════════════════\n"
        );
    }

    /**
     * Configuración inicial de la aplicación
     * Establecer zona horaria por defecto
     */
    @PostConstruct
    public void init() {
        // Establecer zona horaria de Lima, Perú
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        System.out.println("⏰ Zona horaria configurada: " + TimeZone.getDefault().getID());
        
        // Logging de información del sistema
        logSystemInfo();
    }

    /**
     * Configuración adicional de CORS a nivel de aplicación
     * (complementa CorsConfig.java)
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200", "http://localhost:4201")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    /**
     * Mostrar información del sistema al iniciar
     */
    private void logSystemInfo() {
        System.out.println("\n═══════════════ INFORMACIÓN DEL SISTEMA ═══════════════");
        System.out.println("📌 Java Version: " + System.getProperty("java.version"));
        System.out.println("📌 Spring Boot: 3.2.x");
        System.out.println("📌 OS: " + System.getProperty("os.name"));
        System.out.println("📌 Arquitectura: " + System.getProperty("os.arch"));
        System.out.println("📌 Usuario: " + System.getProperty("user.name"));
        System.out.println("📌 Directorio: " + System.getProperty("user.dir"));
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    /**
     * Bean para configuración de timeout de conexiones
     */
    @Bean
    public org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory tomcatFactory() {
        return new org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(org.apache.catalina.Context context) {
                // Configurar timeout de sesión (30 minutos)
                context.setSessionTimeout(30);
            }
        };
    }
}

/**
 * ═══════════════════════════════════════════════════════════
 * ENDPOINTS PRINCIPALES DE LA API
 * ═══════════════════════════════════════════════════════════
 * 
 * AUTENTICACIÓN:
 * POST   /api/usuarios/login                  - Login de usuario
 * 
 * DUEÑOS:
 * GET    /api/duenos                          - Listar todos
 * GET    /api/duenos/{id}                     - Obtener por ID
 * GET    /api/duenos/dni/{dni}                - Buscar por DNI
 * POST   /api/duenos                          - Crear nuevo
 * PUT    /api/duenos/{id}                     - Actualizar
 * DELETE /api/duenos/{id}                     - Eliminar
 * 
 * MASCOTAS:
 * GET    /api/mascotas                        - Listar todas
 * GET    /api/mascotas/{id}                   - Obtener por ID
 * GET    /api/mascotas/dueno/{idDueno}        - Por dueño
 * POST   /api/mascotas                        - Crear nueva
 * PUT    /api/mascotas/{id}                   - Actualizar
 * DELETE /api/mascotas/{id}                   - Eliminar
 * 
 * VETERINARIOS:
 * GET    /api/veterinarios                    - Listar todos
 * GET    /api/veterinarios/{id}               - Obtener por ID
 * GET    /api/veterinarios/disponibles        - Activos
 * POST   /api/veterinarios                    - Crear nuevo
 * PUT    /api/veterinarios/{id}               - Actualizar
 * DELETE /api/veterinarios/{id}               - Eliminar
 * 
 * TÉCNICOS:
 * GET    /api/tecnicos                        - Listar todos
 * GET    /api/tecnicos/{id}                   - Obtener por ID
 * GET    /api/tecnicos/disponibles            - Activos
 * POST   /api/tecnicos                        - Crear nuevo
 * PUT    /api/tecnicos/{id}                   - Actualizar
 * DELETE /api/tecnicos/{id}                   - Eliminar
 * 
 * ÓRDENES:
 * GET    /api/ordenes                         - Listar todas
 * GET    /api/ordenes/{id}                    - Obtener por ID
 * GET    /api/ordenes/pendientes              - Pendientes
 * GET    /api/ordenes/mascota/{idMascota}     - Por mascota
 * POST   /api/ordenes                         - Crear nueva
 * PUT    /api/ordenes/{id}                    - Actualizar
 * PATCH  /api/ordenes/{id}/estado             - Cambiar estado
 * DELETE /api/ordenes/{id}                    - Eliminar
 * 
 * TOMA DE MUESTRAS:
 * GET    /api/toma-muestras                   - Listar todas
 * GET    /api/toma-muestras/{id}              - Obtener por ID
 * GET    /api/toma-muestras/pendientes        - Pendientes
 * GET    /api/toma-muestras/orden/{idOrden}   - Por orden
 * POST   /api/toma-muestras                   - Crear nueva
 * PUT    /api/toma-muestras/{id}              - Actualizar
 * DELETE /api/toma-muestras/{id}              - Eliminar
 * 
 * RESULTADOS:
 * GET    /api/resultados                      - Listar todos
 * GET    /api/resultados/{id}                 - Obtener por ID
 * GET    /api/resultados/pendientes           - Pendientes validación
 * GET    /api/resultados/orden/{idOrden}      - Por orden
 * GET    /api/resultados/{id}/pdf             - Generar PDF
 * POST   /api/resultados                      - Crear nuevo
 * PUT    /api/resultados/{id}                 - Actualizar
 * PATCH  /api/resultados/{id}/validar         - Validar resultado
 * PATCH  /api/resultados/{id}/entregar        - Marcar entregado
 * DELETE /api/resultados/{id}                 - Eliminar
 * 
 * FACTURAS:
 * GET    /api/facturas                        - Listar todas
 * GET    /api/facturas/{id}                   - Obtener por ID
 * GET    /api/facturas/pendientes             - Pendientes pago
 * GET    /api/facturas/dueno/{idDueno}        - Por dueño
 * POST   /api/facturas                        - Crear nueva
 * PUT    /api/facturas/{id}                   - Actualizar
 * PATCH  /api/facturas/{id}/pagar             - Marcar pagada
 * DELETE /api/facturas/{id}                   - Eliminar
 * 
 * REPORTES:
 * GET    /api/reportes/kpis                   - KPIs generales
 * GET    /api/reportes/dashboard              - Dashboard completo
 * GET    /api/reportes/tiempo-promedio        - Tiempo promedio
 * GET    /api/reportes/analisis-repetidos     - Análisis repetidos
 * GET    /api/reportes/satisfaccion-cliente   - Satisfacción cliente
 * GET    /api/reportes/ingresos               - Ingresos por período
 * 
 * ═══════════════════════════════════════════════════════════
 * ROLES Y PERMISOS
 * ═══════════════════════════════════════════════════════════
 * 
 * ADMIN           - Acceso total al sistema
 * VETERINARIO     - Órdenes, validación de resultados, mascotas
 * TECNICO         - Toma de muestras, registro de resultados
 * RECEPCIONISTA   - Dueños, facturas, consultas
 * 
 * ═══════════════════════════════════════════════════════════
 */