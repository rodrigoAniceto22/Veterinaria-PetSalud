package com.petsalud.service;

import com.petsalud.model.Usuario;
import com.petsalud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de Usuarios
 * RF-09: Consulta de resultados en línea (autenticación)
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Listar todos los usuarios
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtener usuario por ID
     */
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    /**
     * Buscar usuario por nombre de usuario
     */
    public Usuario buscarPorUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    /**
     * Buscar usuario por email
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Buscar usuarios por rol
     */
    public List<Usuario> buscarPorRol(String rol) {
        return usuarioRepository.findByRolIgnoreCase(rol);
    }

    /**
     * Obtener usuarios activos
     */
    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Guardar o actualizar usuario
     */
    public Usuario guardar(Usuario usuario) {
        // Validaciones básicas
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new RuntimeException("El rol es obligatorio");
        }
        
        // Validar que el nombre de usuario no exista (para nuevos usuarios)
        if (usuario.getIdUsuario() == null) {
            Usuario existente = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
            if (existente != null) {
                throw new RuntimeException("El nombre de usuario ya existe");
            }
            
            // Validar que el email no exista
            if (usuario.getEmail() != null) {
                Usuario existenteEmail = usuarioRepository.findByEmail(usuario.getEmail());
                if (existenteEmail != null) {
                    throw new RuntimeException("El email ya está registrado");
                }
            }
            
            // Encriptar contraseña para nuevos usuarios
            if (usuario.getContrasena() != null) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
            
            usuario.setFechaCreacion(LocalDateTime.now());
        }
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Eliminar usuario
     */
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
                // Verificar que no sea el último administrador
                long adminCount = usuarioRepository.countByRolIgnoreCase("ADMIN");
                if (adminCount <= 1) {
                    throw new RuntimeException("No se puede eliminar el último administrador del sistema");
                }
            }
            usuarioRepository.deleteById(id);
        }
    }

    /**
     * Autenticar usuario
     */
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        
        if (usuario == null) {
            return null;
        }
        
        // Verificar si está bloqueado
        if (usuario.getBloqueado()) {
            throw new RuntimeException("Usuario bloqueado por múltiples intentos fallidos");
        }
        
        // Verificar si está activo
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        // Verificar contraseña
        if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            // Login exitoso
            usuario.registrarAcceso();
            usuarioRepository.save(usuario);
            return usuario;
        } else {
            // Login fallido
            usuario.registrarIntentoFallido();
            usuarioRepository.save(usuario);
            return null;
        }
    }

    /**
     * Cambiar contraseña
     */
    public boolean cambiarContrasena(Long id, String contrasenaActual, String contrasenaNueva) {
        Usuario usuario = obtenerPorId(id);
        
        if (usuario == null) {
            return false;
        }
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
            return false;
        }
        
        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
        usuarioRepository.save(usuario);
        
        return true;
    }

    /**
     * Desbloquear usuario
     */
    public Usuario desbloquearUsuario(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.desbloquear();
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Activar/Desactivar usuario
     */
    public Usuario toggleEstado(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setActivo(!usuario.getActivo());
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Verificar si existe usuario por nombre
     */
    public boolean existePorUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario) != null;
    }

    /**
     * Contar usuarios por rol
     */
    public long contarPorRol(String rol) {
        return usuarioRepository.countByRolIgnoreCase(rol);
    }

    /**
     * Contar usuarios activos
     */
    public long contarActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    /**
     * Obtener usuarios bloqueados
     */
    public List<Usuario> obtenerBloqueados() {
        return usuarioRepository.findByBloqueadoTrue();
    }

    /**
     * Resetear contraseña
     */
    public Usuario resetearContrasena(Long id, String nuevaContrasena) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
            usuario.desbloquear(); // Desbloquear al resetear
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Actualizar último acceso
     */
    public void actualizarUltimoAcceso(Long id) {
        Usuario usuario = obtenerPorId(id);
        if (usuario != null) {
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }
    }
}