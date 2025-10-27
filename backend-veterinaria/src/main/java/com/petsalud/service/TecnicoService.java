package com.petsalud.service;

import com.petsalud.model.TecnicoVeterinario;
import com.petsalud.repository.TecnicoVeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de Técnicos Veterinarios
 * RF-04: Registro de toma de muestra
 * RF-05: Gestión de análisis
 */
@Service
@Transactional
public class TecnicoService {

    @Autowired
    private TecnicoVeterinarioRepository tecnicoRepository;

    /**
     * Listar todos los técnicos
     */
    public List<TecnicoVeterinario> listarTodos() {
        return tecnicoRepository.findAll();
    }

    /**
     * Obtener técnico por ID
     */
    public TecnicoVeterinario obtenerPorId(Long id) {
        return tecnicoRepository.findById(id).orElse(null);
    }

    /**
     * Buscar técnicos por nombre
     */
    public List<TecnicoVeterinario> buscarPorNombre(String nombre) {
        return tecnicoRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                nombre, nombre);
    }

    /**
     * Buscar técnicos por especialidad
     */
    public List<TecnicoVeterinario> buscarPorEspecialidad(String especialidad) {
        return tecnicoRepository.findByEspecialidadContainingIgnoreCase(especialidad);
    }

    /**
     * Obtener técnicos activos
     */
    public List<TecnicoVeterinario> obtenerActivos() {
        return tecnicoRepository.findByActivoTrue();
    }

    /**
     * Guardar o actualizar técnico
     */
    public TecnicoVeterinario guardar(TecnicoVeterinario tecnico) {
        // Validaciones básicas
        if (tecnico.getNombres() == null || tecnico.getNombres().trim().isEmpty()) {
            throw new RuntimeException("El nombre del técnico es obligatorio");
        }
        if (tecnico.getApellidos() == null || tecnico.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del técnico son obligatorios");
        }
        
        return tecnicoRepository.save(tecnico);
    }

    /**
     * Eliminar técnico
     */
    public void eliminar(Long id) {
        TecnicoVeterinario tecnico = obtenerPorId(id);
        if (tecnico != null) {
            // Verificar que no tenga tomas de muestra asociadas
            if (!tecnico.getTomasMuestra().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el técnico porque tiene tomas de muestra asociadas");
            }
            tecnicoRepository.deleteById(id);
        }
    }

    /**
     * Activar/Desactivar técnico
     */
    public TecnicoVeterinario toggleEstado(Long id) {
        TecnicoVeterinario tecnico = obtenerPorId(id);
        if (tecnico != null) {
            tecnico.setActivo(!tecnico.getActivo());
            return tecnicoRepository.save(tecnico);
        }
        return null;
    }

    /**
     * Contar total de técnicos
     */
    public long contarTotal() {
        return tecnicoRepository.count();
    }

    /**
     * Contar técnicos activos
     */
    public long contarActivos() {
        return tecnicoRepository.countByActivoTrue();
    }

    /**
     * Buscar técnico por email
     */
    public TecnicoVeterinario buscarPorEmail(String email) {
        return tecnicoRepository.findByEmail(email);
    }

    /**
     * Obtener técnicos disponibles para toma de muestra
     */
    public List<TecnicoVeterinario> obtenerDisponibles() {
        return tecnicoRepository.findByActivoTrue();
    }

    /**
     * Buscar técnicos por certificación
     */
    public List<TecnicoVeterinario> buscarPorCertificacion(String certificacion) {
        return tecnicoRepository.findByCertificacionContainingIgnoreCase(certificacion);
    }

    /**
     * Obtener técnico con menos tomas de muestra asignadas (para balanceo de carga)
     */
    public TecnicoVeterinario obtenerTecnicoConMenosCarga() {
        List<TecnicoVeterinario> tecnicos = obtenerActivos();
        if (tecnicos.isEmpty()) {
            throw new RuntimeException("No hay técnicos activos disponibles");
        }
        
        return tecnicos.stream()
                .min((t1, t2) -> Integer.compare(
                        t1.getTomasMuestra().size(), 
                        t2.getTomasMuestra().size()))
                .orElse(tecnicos.get(0));
    }
}