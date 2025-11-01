package com.petsalud.service;

import com.petsalud.model.Pago;
import com.petsalud.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Servicio para gestión de Pagos
 * Sistema de pagos y cobros con internamientos
 */
@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    /**
     * Listar todos los pagos
     */
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    /**
     * Obtener por ID
     */
    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    /**
     * Buscar por número de pago
     */
    public Pago buscarPorNumeroPago(String numeroPago) {
        return pagoRepository.findByNumeroPago(numeroPago);
    }

    /**
     * Buscar pagos por dueño
     */
    public List<Pago> buscarPorDueno(Long idDueno) {
        return pagoRepository.findByDueno_IdDueno(idDueno);
    }

    /**
     * Buscar pagos por mascota
     */
    public List<Pago> buscarPorMascota(Long idMascota) {
        return pagoRepository.findByMascota_IdMascota(idMascota);
    }

    /**
     * Obtener pagos pendientes
     */
    public List<Pago> obtenerPagosPendientes() {
        return pagoRepository.findPagosPendientes();
    }

    /**
     * Obtener pagos vencidos
     */
    public List<Pago> obtenerPagosVencidos() {
        return pagoRepository.findPagosVencidos(LocalDate.now());
    }

    /**
     * Obtener internamientos activos
     */
    public List<Pago> obtenerInternamientosActivos() {
        return pagoRepository.findInternamientosActivos();
    }

    /**
     * Guardar o actualizar pago
     */
    public Pago guardar(Pago pago) {
        // Validaciones
        if (pago.getDueno() == null) {
            throw new RuntimeException("El pago debe estar asociado a un dueño");
        }
        if (pago.getConcepto() == null || pago.getConcepto().trim().isEmpty()) {
            throw new RuntimeException("El concepto del pago es obligatorio");
        }
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        // Si es nuevo pago
        if (pago.getIdPago() == null) {
            if (pago.getNumeroPago() == null || pago.getNumeroPago().trim().isEmpty()) {
                pago.setNumeroPago(generarNumeroPago());
            }
            if (pago.getFechaEmision() == null) {
                pago.setFechaEmision(LocalDate.now());
            }
            if (pago.getEstado() == null) {
                pago.setEstado("PENDIENTE");
            }
            if (pago.getMontoPagado() == null) {
                pago.setMontoPagado(0.0);
            }
        }

        // Calcular costo de internamiento si aplica
        if (Boolean.TRUE.equals(pago.getEsInternamiento())) {
            calcularCostoInternamiento(pago);
        }

        return pagoRepository.save(pago);
    }

    /**
     * Registrar pago
     */
    public Pago registrarPago(Long idPago, Double montoPagado, String metodoPago) {
        Pago pago = obtenerPorId(idPago);
        if (pago == null) {
            throw new RuntimeException("Pago no encontrado");
        }

        double montoActual = pago.getMontoPagado() != null ? pago.getMontoPagado() : 0.0;
        double nuevoMontoPagado = montoActual + montoPagado;

        pago.setMontoPagado(nuevoMontoPagado);
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(LocalDateTime.now());

        // Actualizar estado
        if (nuevoMontoPagado >= pago.getMonto()) {
            pago.setEstado("PAGADO");
        } else if (nuevoMontoPagado > 0) {
            pago.setEstado("PARCIAL");
        }

        return pagoRepository.save(pago);
    }

    /**
     * Finalizar internamiento
     */
    public Pago finalizarInternamiento(Long idPago) {
        Pago pago = obtenerPorId(idPago);
        if (pago == null) {
            throw new RuntimeException("Pago no encontrado");
        }
        if (!Boolean.TRUE.equals(pago.getEsInternamiento())) {
            throw new RuntimeException("Este pago no corresponde a un internamiento");
        }

        pago.setFechaFinInternamiento(LocalDateTime.now());
        calcularCostoInternamiento(pago);

        return pagoRepository.save(pago);
    }

    /**
     * Calcular costo de internamiento
     */
    private void calcularCostoInternamiento(Pago pago) {
        if (pago.getFechaInicioInternamiento() != null && pago.getCostoDiaInternamiento() != null) {
            LocalDateTime fechaFin = pago.getFechaFinInternamiento() != null ? 
                pago.getFechaFinInternamiento() : LocalDateTime.now();
            
            long dias = ChronoUnit.DAYS.between(pago.getFechaInicioInternamiento(), fechaFin);
            if (dias == 0) dias = 1; // Mínimo 1 día
            
            pago.setDiasInternamiento((int) dias);
            pago.setMonto(dias * pago.getCostoDiaInternamiento());
        }
    }

    /**
     * Generar número de pago
     */
    private String generarNumeroPago() {
        return "PAG-" + System.currentTimeMillis();
    }

    /**
     * Eliminar pago
     */
    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }

    /**
     * Calcular total pendiente por dueño
     */
    public Double calcularTotalPendientePorDueno(Long idDueno) {
        Double total = pagoRepository.calcularTotalPendientePorDueno(idDueno);
        return total != null ? total : 0.0;
    }

    /**
     * Calcular total cobrado en período
     */
    public Double calcularTotalCobrado(LocalDate inicio, LocalDate fin) {
        Double total = pagoRepository.calcularTotalCobrado(inicio, fin);
        return total != null ? total : 0.0;
    }
}