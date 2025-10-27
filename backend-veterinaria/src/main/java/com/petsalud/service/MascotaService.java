package com.petsalud.service;

import com.petsalud.model.Mascota;
import com.petsalud.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de Mascotas
 * RF-01: Registro de dueños y mascotas
 */
@Service
@Transactional
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Listar todas las mascotas
     */
    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    /**
     * Obtener mascota por ID
     */
    public Mascota obtenerPorId(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    /**
     * Buscar mascotas por nombre
     */
    public List<Mascota> buscarPorNombre(String nombre) {
        return mascotaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Buscar mascotas por especie
     */
    public List<Mascota> buscarPorEspecie(String especie) {
        return mascotaRepository.findByEspecieIgnoreCase(especie);
    }

    /**
     * Buscar mascotas por raza
     */
    public List<Mascota> buscarPorRaza(String raza) {
        return mascotaRepository.findByRazaContainingIgnoreCase(raza);
    }

    /**
     * Buscar mascotas por dueño
     */
    public List<Mascota> buscarPorDueno(Long idDueno) {
        return mascotaRepository.findByDueno_IdDueno(idDueno);
    }

    /**
     * Guardar o actualizar mascota
     */
    public Mascota guardar(Mascota mascota) {
        // Validaciones básicas
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la mascota es obligatorio");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new RuntimeException("La especie de la mascota es obligatoria");
        }
        if (mascota.getDueno() == null) {
            throw new RuntimeException("La mascota debe tener un dueño asignado");
        }
        return mascotaRepository.save(mascota);
    }

    /**
     * Eliminar mascota
     */
    public void eliminar(Long id) {
        Mascota mascota = obtenerPorId(id);
        if (mascota != null) {
            // Verificar que no tenga órdenes asociadas
            if (!mascota.getOrdenes().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la mascota porque tiene órdenes asociadas");
            }
            mascotaRepository.deleteById(id);
        }
    }

    /**
     * Contar total de mascotas
     */
    public long contarTotal() {
        return mascotaRepository.count();
    }

    /**
     * Contar mascotas por especie
     */
    public long contarPorEspecie(String especie) {
        return mascotaRepository.countByEspecieIgnoreCase(especie);
    }

    /**
     * Obtener mascotas por sexo
     */
    public List<Mascota> buscarPorSexo(String sexo) {
        return mascotaRepository.findBySexoIgnoreCase(sexo);
    }

    /**
     * Obtener mascotas por edad mínima
     */
    public List<Mascota> buscarPorEdadMinima(Integer edadMinima) {
        return mascotaRepository.findByEdadGreaterThanEqual(edadMinima);
    }

    /**
     * Obtener mascotas por edad máxima
     */
    public List<Mascota> buscarPorEdadMaxima(Integer edadMaxima) {
        return mascotaRepository.findByEdadLessThanEqual(edadMaxima);
    }

    /**
     * Obtener mascotas activas (con dueño activo)
     */
    public List<Mascota> obtenerMascotasActivas() {
        return mascotaRepository.findAll().stream()
                .filter(m -> m.getDueno() != null)
                .toList();
    }
}