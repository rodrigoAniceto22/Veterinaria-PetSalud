package com.petsalud.controller;

import com.petsalud.model.Usuario;
import com.petsalud.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de Usuarios y Autenticación
 * RF-09: Consulta de resultados en línea (con credenciales)
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Listar todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar usuario por nombre de usuario
     * GET /api/usuarios/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar usuarios por rol
     * GET /api/usuarios/rol/{rol}
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> buscarPorRol(@PathVariable String rol) {
        List<Usuario> usuarios = usuarioService.buscarPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Crear nuevo usuario
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        // Encriptar contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    /**
     * Actualizar usuario existente
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        usuario.setIdUsuario(id);
        
        // Si se envía nueva contraseña, encriptarla
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        } else {
            // Mantener la contraseña existente
            usuario.setContrasena(usuarioExistente.getContrasena());
        }
        
        Usuario usuarioActualizado = usuarioService.guardar(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * Eliminar usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Login / Autenticación
     * POST /api/usuarios/login
     * RF-09: Consulta de resultados en línea
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Usuario usuario = usuarioService.buscarPorUsername(username);
        
        Map<String, Object> response = new HashMap<>();
        
        if (usuario == null) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Verificar contraseña
        if (passwordEncoder.matches(password, usuario.getContrasena())) {
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("usuario", usuario);
            response.put("rol", usuario.getRol());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Cambiar contraseña
     * POST /api/usuarios/{id}/cambiar-password
     */
    @PostMapping("/{id}/cambiar-password")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {
        
        Usuario usuario = usuarioService.obtenerPorId(id);
        Map<String, Object> response = new HashMap<>();
        
        if (usuario == null) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.notFound().body(response);
        }

        String passwordActual = passwords.get("passwordActual");
        String passwordNueva = passwords.get("passwordNueva");

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getContrasena())) {
            response.put("success", false);
            response.put("message", "Contraseña actual incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(passwordNueva));
        usuarioService.guardar(usuario);

        response.put("success", true);
        response.put("message", "Contraseña actualizada exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Verificar disponibilidad de username
     * GET /api/usuarios/verificar-username/{username}
     */
    @GetMapping("/verificar-username/{username}")
    public ResponseEntity<Map<String, Boolean>> verificarUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", usuario == null);
        return ResponseEntity.ok(response);
    }

    /**
     * Activar/Desactivar usuario
     * PATCH /api/usuarios/{id}/toggle-estado
     */
    @PatchMapping("/{id}/toggle-estado")
    public ResponseEntity<Usuario> toggleEstado(@PathVariable Long id) {
        Usuario usuario = usuarioService.toggleEstado(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtener perfil del usuario autenticado
     * GET /api/usuarios/perfil
     */
    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfil(@RequestParam String username) {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }
}