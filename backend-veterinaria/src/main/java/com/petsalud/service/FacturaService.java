package com.petsalud.service;

import com.petsalud.model.Factura;
import com.petsalud.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de Facturas
 * Sistema de facturación y cobros
 */
@Service
@Transactional
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Listar todas las facturas
     */
    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    /**
     * Obtener factura por ID
     */
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    /**
     * Buscar factura por número
     */
    public Factura buscarPorNumeroFactura(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }

    /**
     * Buscar facturas por dueño
     */
    public List<Factura> buscarPorDueno(Long idDueno) {
        return facturaRepository.findByDueno_IdDueno(idDueno);
    }

    /**
     * Buscar facturas por fecha de emisión
     */
    public List<Factura> buscarPorFecha(LocalDate fecha) {
        return facturaRepository.findByFechaEmision(fecha);
    }

    /**
     * Buscar facturas por rango de fechas
     */
    public List<Factura> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return facturaRepository.findByFechaEmisionBetween(inicio, fin);
    }

    /**
     * Buscar facturas por método de pago
     */
    public List<Factura> buscarPorMetodoPago(String metodoPago) {
        return facturaRepository.findByMetodoPagoIgnoreCase(metodoPago);
    }

    /**
     * Buscar facturas por estado
     */
    public List<Factura> buscarPorEstado(String estado) {
        return facturaRepository.findByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener facturas pendientes
     */
    public List<Factura> obtenerFacturasPendientes() {
        return facturaRepository.findByEstadoIgnoreCase("PENDIENTE");
    }

    /**
     * Obtener facturas pagadas
     */
    public List<Factura> obtenerFacturasPagadas() {
        return facturaRepository.findByEstadoIgnoreCase("PAGADA");
    }

    /**
     * Guardar o actualizar factura
     */
    public Factura guardar(Factura factura) {
        // Validaciones básicas
        if (factura.getDueno() == null) {
            throw new RuntimeException("La factura debe estar asociada a un dueño");
        }
        
        // Si es una nueva factura
        if (factura.getIdFactura() == null) {
            // Generar número de factura si no existe
            if (factura.getNumeroFactura() == null || factura.getNumeroFactura().trim().isEmpty()) {
                factura.setNumeroFactura(generarNumeroFactura());
            }
            
            // Establecer fecha de emisión si no existe
            if (factura.getFechaEmision() == null) {
                factura.setFechaEmision(LocalDate.now());
            }
            
            // Establecer estado inicial
            if (factura.getEstado() == null) {
                factura.setEstado("PENDIENTE");
            }
        }
        
        // Calcular totales
        factura.calcularTotales();
        
        return facturaRepository.save(factura);
    }

    /**
     * Eliminar factura
     */
    public void eliminar(Long id) {
        Factura factura = obtenerPorId(id);
        if (factura != null) {
            if ("PAGADA".equalsIgnoreCase(factura.getEstado())) {
                throw new RuntimeException("No se puede eliminar una factura pagada");
            }
            facturaRepository.deleteById(id);
        }
    }

    /**
     * Marcar factura como pagada
     */
    public Factura marcarComoPagada(Long id) {
        Factura factura = obtenerPorId(id);
        if (factura != null) {
            if ("PAGADA".equalsIgnoreCase(factura.getEstado())) {
                throw new RuntimeException("La factura ya está pagada");
            }
            
            factura.setEstado("PAGADA");
            factura.setFechaPago(LocalDate.now());
            
            return facturaRepository.save(factura);
        }
        return null;
    }

    /**
     * Calcular ventas del día
     */
    public Double calcularVentasDia(LocalDate fecha) {
        List<Factura> facturas = facturaRepository.findByFechaEmisionAndEstadoIgnoreCase(fecha, "PAGADA");
        return facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
    }

    /**
     * Calcular ventas del mes
     */
    public Double calcularVentasMes(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        
        List<Factura> facturas = facturaRepository.findByFechaEmisionBetweenAndEstadoIgnoreCase(
                inicio, fin, "PAGADA");
        
        return facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
    }

    /**
     * Generar número de factura único
     */
    private String generarNumeroFactura() {
        String prefijo = "F";
        String anio = String.valueOf(LocalDate.now().getYear());
        long contador = facturaRepository.count() + 1;
        String numero = String.format("%06d", contador);
        return prefijo + anio + "-" + numero;
    }

    /**
     * Contar facturas por estado
     */
    public long contarPorEstado(String estado) {
        return facturaRepository.countByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener facturas del día
     */
    public List<Factura> obtenerFacturasDelDia() {
        return facturaRepository.findByFechaEmision(LocalDate.now());
    }

    /**
     * Calcular total de ingresos (facturas pagadas)
     */
    public Double calcularTotalIngresos() {
        List<Factura> facturasPagadas = obtenerFacturasPagadas();
        return facturasPagadas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
    }

    /**
     * Calcular total pendiente de cobro
     */
    public Double calcularTotalPendiente() {
        List<Factura> facturasPendientes = obtenerFacturasPendientes();
        return facturasPendientes.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
    }

    /**
     * Anular factura
     */
    public Factura anularFactura(Long id, String motivo) {
        Factura factura = obtenerPorId(id);
        if (factura != null) {
            if ("ANULADA".equalsIgnoreCase(factura.getEstado())) {
                throw new RuntimeException("La factura ya está anulada");
            }
            
            factura.setEstado("ANULADA");
            factura.setObservaciones("ANULADA - Motivo: " + motivo);
            
            return facturaRepository.save(factura);
        }
        return null;
    }
}