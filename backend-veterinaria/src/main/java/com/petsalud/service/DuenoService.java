package com.petsalud.service;

import com.petsalud.model.Dueno;
import com.petsalud.repository.DuenoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de Dueños de mascotas
 * RF-01: Registro de dueños y mascotas
 */
@Service
@Transactional
public class DuenoService {

    @Autowired
    private DuenoRepository duenoRepository;

    /**
     * Listar todos los dueños
     */
    public List<Dueno> listarTodos() {
        return duenoRepository.findAll();
    }

    /**
     * Obtener dueño por ID
     */
    public Dueno obtenerPorId(Long id) {
        return duenoRepository.findById(id).orElse(null);
    }

    /**
     * Buscar dueño por DNI
     */
    public Dueno buscarPorDni(String dni) {
        return duenoRepository.findByDni(dni);
    }

    /**
     * Buscar dueños por nombre o apellido
     */
    public List<Dueno> buscarPorNombre(String nombre) {
        return duenoRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                nombre, nombre);
    }

    /**
     * Buscar dueño por email
     */
    public Dueno buscarPorEmail(String email) {
        return duenoRepository.findByEmail(email);
    }

    /**
     * Guardar o actualizar dueño
     */
    public Dueno guardar(Dueno dueno) {
        // Validar que no exista otro dueño con el mismo DNI
        if (dueno.getIdDueno() == null) {
            Dueno existente = duenoRepository.findByDni(dueno.getDni());
            if (existente != null) {
                throw new RuntimeException("Ya existe un dueño registrado con el DNI: " + dueno.getDni());
            }
        }
        return duenoRepository.save(dueno);
    }

    /**
     * Eliminar dueño
     */
    public void eliminar(Long id) {
        Dueno dueno = obtenerPorId(id);
        if (dueno != null) {
            // Verificar que no tenga mascotas asociadas
            if (!dueno.getMascotas().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el dueño porque tiene mascotas asociadas");
            }
            duenoRepository.deleteById(id);
        }
    }

    /**
     * Verificar si existe un dueño por DNI
     */
    public boolean existePorDni(String dni) {
        return duenoRepository.findByDni(dni) != null;
    }

    /**
     * Contar total de dueños registrados
     */
    public long contarTotal() {
        return duenoRepository.count();
    }

    /**
     * Obtener dueños con mascotas
     */
    public List<Dueno> obtenerDuenosConMascotas() {
        return duenoRepository.findAll().stream()
                .filter(d -> !d.getMascotas().isEmpty())
                .toList();
    }

    /**
     * Buscar dueños por teléfono
     */
    public List<Dueno> buscarPorTelefono(String telefono) {
        return duenoRepository.findByTelefonoContaining(telefono);
    }
}