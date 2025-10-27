package com.petsalud.service;

import com.petsalud.model.Veterinario;
import com.petsalud.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de Veterinarios
 * Personal médico veterinario del sistema
 */
@Service
@Transactional
public class VeterinarioService {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    /**
     * Listar todos los veterinarios
     */
    public List<Veterinario> listarTodos() {
        return veterinarioRepository.findAll();
    }

    /**
     * Obtener veterinario por ID
     */
    public Veterinario obtenerPorId(Long id) {
        return veterinarioRepository.findById(id).orElse(null);
    }

    /**
     * Buscar veterinarios por nombre
     */
    public List<Veterinario> buscarPorNombre(String nombre) {
        return veterinarioRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                nombre, nombre);
    }

    /**
     * Buscar veterinarios por especialidad
     */
    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        return veterinarioRepository.findByEspecialidadContainingIgnoreCase(especialidad);
    }

    /**
     * Buscar veterinario por colegiatura
     */
    public Veterinario buscarPorColegiatura(String colegiatura) {
        return veterinarioRepository.findByColegiatura(colegiatura);
    }

    /**
     * Obtener veterinarios activos
     */
    public List<Veterinario> obtenerActivos() {
        return veterinarioRepository.findByActivoTrue();
    }

    /**
     * Guardar o actualizar veterinario
     */
    public Veterinario guardar(Veterinario veterinario) {
        // Validar que no exista otro veterinario con la misma colegiatura
        if (veterinario.getIdVeterinario() == null && veterinario.getColegiatura() != null) {
            Veterinario existente = veterinarioRepository.findByColegiatura(veterinario.getColegiatura());
            if (existente != null) {
                throw new RuntimeException("Ya existe un veterinario con la colegiatura: " + veterinario.getColegiatura());
            }
        }
        
        // Validaciones básicas
        if (veterinario.getNombres() == null || veterinario.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre del veterinario es obligatorio");
        }
        if (veterinario.getApellidos() == null || veterinario.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del veterinario son obligatorios");
        }
        
        return veterinarioRepository.save(veterinario);
    }

    /**
     * Eliminar veterinario
     */
    public void eliminar(Long id) {
        Veterinario veterinario = obtenerPorId(id);
        if (veterinario != null) {
            // Verificar que no tenga órdenes asociadas
            if (!veterinario.getOrdenes().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el veterinario porque tiene órdenes asociadas");
            }
            veterinarioRepository.deleteById(id);
        }
    }

    /**
     * Activar/Desactivar veterinario
     */
    public Veterinario toggleEstado(Long id) {
        Veterinario veterinario = obtenerPorId(id);
        if (veterinario != null) {
            veterinario.setActivo(!veterinario.getActivo());
            return veterinarioRepository.save(veterinario);
        }
        return null;
    }

    /**
     * Contar total de veterinarios
     */
    public long contarTotal() {
        return veterinarioRepository.count();
    }

    /**
     * Contar veterinarios activos
     */
    public long contarActivos() {
        return veterinarioRepository.countByActivoTrue();
    }

    /**
     * Buscar veterinarios por email
     */
    public Veterinario buscarPorEmail(String email) {
        return veterinarioRepository.findByEmail(email);
    }

    /**
     * Obtener veterinarios disponibles (activos)
     */
    public List<Veterinario> obtenerDisponibles() {
        return veterinarioRepository.findByActivoTrue();
    }

    /**
     * Verificar si existe por colegiatura
     */
    public boolean existePorColegiatura(String colegiatura) {
        return veterinarioRepository.findByColegiatura(colegiatura) != null;
    }
}