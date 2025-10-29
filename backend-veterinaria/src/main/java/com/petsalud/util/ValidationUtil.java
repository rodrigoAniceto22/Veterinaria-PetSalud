package com.petsalud.util;

import java.util.regex.Pattern;

/**
 * Utilidades para validación de datos
 * Sistema de Gestión Veterinaria PetSalud
 */
public class ValidationUtil {

    // Patrones de expresiones regulares
    private static final Pattern PATTERN_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PATTERN_DNI_PERU = Pattern.compile("^[0-9]{8}$");
    
    private static final Pattern PATTERN_RUC_PERU = Pattern.compile("^[0-9]{11}$");
    
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9]{7,15}$");
    
    private static final Pattern PATTERN_SOLO_LETRAS = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    
    private static final Pattern PATTERN_ALFANUMERICO = Pattern.compile("^[a-zA-Z0-9]+$");

    /**
     * Validar que un String no sea null ni vacío
     */
    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Validar formato de email
     */
    public static boolean esEmailValido(String email) {
        if (!esTextoValido(email)) return false;
        return PATTERN_EMAIL.matcher(email.trim()).matches();
    }

    /**
     * Validar DNI peruano (8 dígitos)
     */
    public static boolean esDNIValido(String dni) {
        if (!esTextoValido(dni)) return false;
        return PATTERN_DNI_PERU.matcher(dni.trim()).matches();
    }

    /**
     * Validar RUC peruano (11 dígitos)
     */
    public static boolean esRUCValido(String ruc) {
        if (!esTextoValido(ruc)) return false;
        return PATTERN_RUC_PERU.matcher(ruc.trim()).matches();
    }

    /**
     * Validar número de teléfono
     */
    public static boolean esTelefonoValido(String telefono) {
        if (!esTextoValido(telefono)) return false;
        String telefonoLimpio = telefono.replaceAll("[\\s()-]", "");
        return PATTERN_TELEFONO.matcher(telefonoLimpio).matches();
    }

    /**
     * Validar que solo contenga letras (y espacios)
     */
    public static boolean esSoloLetras(String texto) {
        if (!esTextoValido(texto)) return false;
        return PATTERN_SOLO_LETRAS.matcher(texto.trim()).matches();
    }

    /**
     * Validar que sea alfanumérico
     */
    public static boolean esAlfanumerico(String texto) {
        if (!esTextoValido(texto)) return false;
        return PATTERN_ALFANUMERICO.matcher(texto.trim()).matches();
    }

    /**
     * Validar longitud mínima de texto
     */
    public static boolean tieneLongitudMinima(String texto, int longitudMinima) {
        return esTextoValido(texto) && texto.trim().length() >= longitudMinima;
    }

    /**
     * Validar longitud máxima de texto
     */
    public static boolean tieneLongitudMaxima(String texto, int longitudMaxima) {
        return esTextoValido(texto) && texto.trim().length() <= longitudMaxima;
    }

    /**
     * Validar rango de longitud
     */
    public static boolean tieneRangoLongitud(String texto, int min, int max) {
        return tieneLongitudMinima(texto, min) && tieneLongitudMaxima(texto, max);
    }

    /**
     * Validar número positivo
     */
    public static boolean esNumeroPositivo(Number numero) {
        return numero != null && numero.doubleValue() > 0;
    }

    /**
     * Validar número no negativo (>= 0)
     */
    public static boolean esNumeroNoNegativo(Number numero) {
        return numero != null && numero.doubleValue() >= 0;
    }

    /**
     * Validar que un número esté en un rango
     */
    public static boolean estaEnRango(Number numero, double min, double max) {
        if (numero == null) return false;
        double valor = numero.doubleValue();
        return valor >= min && valor <= max;
    }

    /**
     * Validar contraseña segura
     * Mínimo 8 caracteres, al menos una mayúscula, una minúscula y un número
     */
    public static boolean esContrasenaSegura(String contrasena) {
        if (!tieneLongitudMinima(contrasena, 8)) return false;
        
        boolean tieneMayuscula = contrasena.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasena.matches(".*[a-z].*");
        boolean tieneNumero = contrasena.matches(".*[0-9].*");
        
        return tieneMayuscula && tieneMinuscula && tieneNumero;
    }

    /**
     * Validar edad de mascota (entre 0 y 30 años)
     */
    public static boolean esEdadMascotaValida(Integer edad) {
        return edad != null && edad >= 0 && edad <= 30;
    }

    /**
     * Validar peso de mascota (entre 0.1 y 200 kg)
     */
    public static boolean esPesoMascotaValido(Double peso) {
        return peso != null && peso >= 0.1 && peso <= 200.0;
    }

    /**
     * Validar especie de mascota
     */
    public static boolean esEspecieValida(String especie) {
        if (!esTextoValido(especie)) return false;
        String especieLower = especie.toLowerCase().trim();
        return especieLower.equals("perro") || 
               especieLower.equals("gato") || 
               especieLower.equals("ave") || 
               especieLower.equals("roedor") ||
               especieLower.equals("reptil") ||
               especieLower.equals("otro");
    }

    /**
     * Validar sexo de mascota
     */
    public static boolean esSexoValido(String sexo) {
        if (!esTextoValido(sexo)) return false;
        String sexoLower = sexo.toLowerCase().trim();
        return sexoLower.equals("macho") || sexoLower.equals("hembra");
    }

    /**
     * Validar estado de orden
     */
    public static boolean esEstadoOrdenValido(String estado) {
        if (!esTextoValido(estado)) return false;
        String estadoUpper = estado.toUpperCase().trim();
        return estadoUpper.equals("PENDIENTE") || 
               estadoUpper.equals("EN_PROCESO") || 
               estadoUpper.equals("COMPLETADA") ||
               estadoUpper.equals("CANCELADA");
    }

    /**
     * Validar prioridad de orden
     */
    public static boolean esPrioridadValida(String prioridad) {
        if (!esTextoValido(prioridad)) return false;
        String prioridadUpper = prioridad.toUpperCase().trim();
        return prioridadUpper.equals("BAJA") || 
               prioridadUpper.equals("NORMAL") || 
               prioridadUpper.equals("ALTA") ||
               prioridadUpper.equals("URGENTE");
    }

    /**
     * Validar método de pago
     */
    public static boolean esMetodoPagoValido(String metodoPago) {
        if (!esTextoValido(metodoPago)) return false;
        String metodoUpper = metodoPago.toUpperCase().trim();
        return metodoUpper.equals("EFECTIVO") || 
               metodoUpper.equals("TARJETA") || 
               metodoUpper.equals("TRANSFERENCIA") ||
               metodoUpper.equals("YAPE") ||
               metodoUpper.equals("PLIN");
    }

    /**
     * Validar rol de usuario
     */
    public static boolean esRolValido(String rol) {
        if (!esTextoValido(rol)) return false;
        String rolUpper = rol.toUpperCase().trim();
        return rolUpper.equals("ADMIN") || 
               rolUpper.equals("VETERINARIO") || 
               rolUpper.equals("TECNICO") ||
               rolUpper.equals("RECEPCIONISTA");
    }

    /**
     * Validar monto de factura (mayor a 0 y menor a 100,000)
     */
    public static boolean esMontoFacturaValido(Double monto) {
        return monto != null && monto > 0 && monto < 100000.0;
    }

    /**
     * Sanitizar texto (eliminar caracteres especiales peligrosos)
     */
    public static String sanitizarTexto(String texto) {
        if (!esTextoValido(texto)) return "";
        return texto.trim()
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#x27;")
                    .replaceAll("/", "&#x2F;");
    }

    /**
     * Validar formato de código de muestra
     */
    public static boolean esCodigoMuestraValido(String codigo) {
        if (!esTextoValido(codigo)) return false;
        // Formato: TM-XXXXXX-XXXXXXXX (prefijo-timestamp-uuid)
        return codigo.matches("^TM-[0-9]{6}-[A-Z0-9]{8}$");
    }

    /**
     * Validar número de factura
     */
    public static boolean esNumeroFacturaValido(String numeroFactura) {
        if (!esTextoValido(numeroFactura)) return false;
        // Formato: FAAAA-NNNNNN (F + año + guión + número)
        return numeroFactura.matches("^F[0-9]{4}-[0-9]{6}$");
    }

    /**
     * Validar colegiatura de veterinario
     */
    public static boolean esColegiaturaValida(String colegiatura) {
        return esTextoValido(colegiatura) && tieneRangoLongitud(colegiatura, 5, 20);
    }

    /**
     * Validar nombre de usuario
     */
    public static boolean esNombreUsuarioValido(String nombreUsuario) {
        if (!esTextoValido(nombreUsuario)) return false;
        // Alfanumérico, guion bajo, punto. Mínimo 4, máximo 20 caracteres
        return nombreUsuario.matches("^[a-zA-Z0-9._]{4,20}$");
    }

    /**
     * Limpiar espacios extras de un texto
     */
    public static String limpiarEspacios(String texto) {
        if (!esTextoValido(texto)) return "";
        return texto.trim().replaceAll("\\s+", " ");
    }

    /**
     * Capitalizar primera letra de cada palabra
     */
    public static String capitalizarPalabras(String texto) {
        if (!esTextoValido(texto)) return "";
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (String palabra : palabras) {
            if (palabra.length() > 0) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                         .append(palabra.substring(1).toLowerCase())
                         .append(" ");
            }
        }
        
        return resultado.toString().trim();
    }

    /**
     * Validar URL
     */
    public static boolean esURLValida(String url) {
        if (!esTextoValido(url)) return false;
        return url.matches("^(http|https)://.*$");
    }

    /**
     * Validar que un objeto no sea null
     */
    public static boolean esObjetoValido(Object objeto) {
        return objeto != null;
    }

    /**
     * Validar que un ID sea válido (mayor a 0)
     */
    public static boolean esIdValido(Long id) {
        return id != null && id > 0;
    }

    /**
     * Generar mensaje de error para campo requerido
     */
    public static String mensajeCampoRequerido(String nombreCampo) {
        return "El campo '" + nombreCampo + "' es requerido";
    }

    /**
     * Generar mensaje de error para formato inválido
     */
    public static String mensajeFormatoInvalido(String nombreCampo) {
        return "El formato del campo '" + nombreCampo + "' es inválido";
    }

    /**
     * Generar mensaje de error para longitud
     */
    public static String mensajeLongitudInvalida(String nombreCampo, int min, int max) {
        return "El campo '" + nombreCampo + "' debe tener entre " + min + " y " + max + " caracteres";
    }

    /**
     * Validar tipo de muestra
     */
    public static boolean esTipoMuestraValido(String tipoMuestra) {
        if (!esTextoValido(tipoMuestra)) return false;
        String tipoLower = tipoMuestra.toLowerCase().trim();
        return tipoLower.equals("sangre") || 
               tipoLower.equals("orina") || 
               tipoLower.equals("heces") ||
               tipoLower.equals("piel") ||
               tipoLower.equals("pelo") ||
               tipoLower.equals("saliva") ||
               tipoLower.equals("otro");
    }

    /**
     * Validar tipo de examen
     */
    public static boolean esTipoExamenValido(String tipoExamen) {
        if (!esTextoValido(tipoExamen)) return false;
        String tipoLower = tipoExamen.toLowerCase().trim();
        return tipoLower.contains("hemograma") || 
               tipoLower.contains("bioquímica") || 
               tipoLower.contains("urianálisis") ||
               tipoLower.contains("copro") ||
               tipoLower.contains("cultivo") ||
               tipoLower.contains("citología") ||
               tipoLower.contains("histopatología");
    }
}