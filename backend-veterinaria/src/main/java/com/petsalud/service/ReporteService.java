package com.petsalud.service;

import com.petsalud.model.*;
import com.petsalud.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generación de Reportes y KPIs
 * RF-10: Reportes de laboratorio veterinario
 */
@Service
@Transactional(readOnly = true)
public class ReporteService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private TomaMuestraVetRepository tomaMuestraRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private TecnicoVeterinarioRepository tecnicoRepository;

    /**
     * Obtener KPIs generales del sistema
     */
    public Map<String, Object> obtenerKPIsGenerales() {
        Map<String, Object> kpis = new HashMap<>();
        
        kpis.put("totalOrdenes", ordenRepository.count());
        kpis.put("ordenesPendientes", ordenRepository.countByEstadoIgnoreCase("PENDIENTE"));
        kpis.put("ordenesEnProceso", ordenRepository.countByEstadoIgnoreCase("EN_PROCESO"));
        kpis.put("ordenesCompletadas", ordenRepository.countByEstadoIgnoreCase("COMPLETADA"));
        
        kpis.put("totalResultados", resultadoRepository.count());
        kpis.put("resultadosValidados", resultadoRepository.countByValidadoTrue());
        kpis.put("resultadosPendientes", resultadoRepository.countByValidadoFalse());
        
        kpis.put("totalMascotas", mascotaRepository.count());
        kpis.put("totalVeterinarios", veterinarioRepository.countByActivoTrue());
        kpis.put("totalTecnicos", tecnicoRepository.countByActivoTrue());
        
        return kpis;
    }

    /**
     * Calcular tiempo promedio entre toma de muestra y entrega de resultados
     * KPI: Tiempo promedio (objetivo: < 12 horas para pruebas rápidas)
     */
    public Map<String, Object> calcularTiempoPromedio(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<ResultadoVeterinario> resultados = resultadoRepository
                .findByFechaResultadoBetween(inicio.atStartOfDay(), fin.atTime(23, 59, 59));
        
        List<Long> tiemposEnHoras = new ArrayList<>();
        
        for (ResultadoVeterinario resultado : resultados) {
            TomaMuestraVet toma = tomaMuestraRepository
                    .findByOrden_IdOrden(resultado.getOrden().getIdOrden());
            
            if (toma != null && resultado.getFechaEntrega() != null) {
                Duration duracion = Duration.between(toma.getFechaHora(), resultado.getFechaEntrega());
                tiemposEnHoras.add(duracion.toHours());
            }
        }
        
        double promedioHoras = tiemposEnHoras.isEmpty() ? 0 : 
                tiemposEnHoras.stream().mapToLong(Long::longValue).average().orElse(0);
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalMuestras", tiemposEnHoras.size());
        reporte.put("tiempoPromedioHoras", promedioHoras);
        reporte.put("tiempoPromedioDias", promedioHoras / 24);
        reporte.put("cumpleObjetivo", promedioHoras < 12);
        reporte.put("objetivoHoras", 12);
        
        return reporte;
    }

    /**
     * Calcular porcentaje de análisis repetidos por error
     * KPI: Porcentaje de análisis repetidos
     */
    public Map<String, Object> calcularAnalisisRepetidos(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenes = ordenRepository.findByFechaOrdenBetween(inicio, fin);
        
        // Agrupar órdenes por mascota y tipo de examen
        Map<String, List<OrdenVeterinaria>> ordenesAgrupadas = ordenes.stream()
                .collect(Collectors.groupingBy(o -> 
                    o.getMascota().getIdMascota() + "-" + o.getTipoExamen()));
        
        long ordenesRepetidas = ordenesAgrupadas.values().stream()
                .filter(lista -> lista.size() > 1)
                .count();
        
        double porcentaje = ordenes.isEmpty() ? 0 : 
                (ordenesRepetidas * 100.0) / ordenes.size();
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalOrdenes", ordenes.size());
        reporte.put("ordenesRepetidas", ordenesRepetidas);
        reporte.put("porcentajeRepeticion", porcentaje);
        reporte.put("cumpleObjetivo", porcentaje < 5); // Objetivo: < 5%
        
        return reporte;
    }

    /**
     * Obtener nivel de satisfacción del cliente
     * KPI: Satisfacción del cliente (simulado)
     */
    public Map<String, Object> obtenerSatisfaccionCliente() {
        Map<String, Object> reporte = new HashMap<>();
        
        // Factores de satisfacción (simulados)
        long ordenesCompletadas = ordenRepository.countByEstadoIgnoreCase("COMPLETADA");
        long resultadosEntregados = resultadoRepository.countByEntregadoTrue();
        long facturasPagadas = facturaRepository.countByEstadoIgnoreCase("PAGADA");
        
        double tasaCompletitud = ordenRepository.count() == 0 ? 0 : 
                (ordenesCompletadas * 100.0) / ordenRepository.count();
        
        // Satisfacción simulada basada en eficiencia
        double satisfaccion = (tasaCompletitud + 80) / 2; // Simulación
        
        reporte.put("ordenesCompletadas", ordenesCompletadas);
        reporte.put("resultadosEntregados", resultadosEntregados);
        reporte.put("facturasPagadas", facturasPagadas);
        reporte.put("tasaCompletitud", tasaCompletitud);
        reporte.put("nivelSatisfaccion", satisfaccion);
        reporte.put("calificacion", satisfaccion >= 90 ? "EXCELENTE" : 
                                    satisfaccion >= 75 ? "BUENO" : "REGULAR");
        
        return reporte;
    }

    /**
     * Calcular tratamientos iniciados el mismo día de la consulta
     * KPI: Número de tratamientos iniciados el mismo día
     */
    public Map<String, Object> calcularTratamientosMismoDia(LocalDate fecha) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenesDelDia = ordenRepository.findByFechaOrden(fecha);
        
        long tratamientosMismoDia = ordenesDelDia.stream()
                .filter(o -> "COMPLETADA".equalsIgnoreCase(o.getEstado()))
                .count();
        
        double porcentaje = ordenesDelDia.isEmpty() ? 0 : 
                (tratamientosMismoDia * 100.0) / ordenesDelDia.size();
        
        reporte.put("fecha", fecha);
        reporte.put("totalOrdenes", ordenesDelDia.size());
        reporte.put("tratamientosMismoDia", tratamientosMismoDia);
        reporte.put("porcentaje", porcentaje);
        reporte.put("cumpleObjetivo", porcentaje >= 70); // Objetivo: 70%
        
        return reporte;
    }

    /**
     * Obtener análisis realizados por tipo
     */
    public Map<String, Object> obtenerAnalisisPorTipo(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenes = ordenRepository.findByFechaOrdenBetween(inicio, fin);
        
        Map<String, Long> analisisPorTipo = ordenes.stream()
                .collect(Collectors.groupingBy(
                    OrdenVeterinaria::getTipoExamen,
                    Collectors.counting()
                ));
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalOrdenes", ordenes.size());
        reporte.put("analisisPorTipo", analisisPorTipo);
        
        return reporte;
    }

    /**
     * Obtener productividad de veterinarios
     */
    public Map<String, Object> obtenerProductividadVeterinarios(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenes = ordenRepository.findByFechaOrdenBetween(inicio, fin);
        
        Map<String, Long> ordenesPorVeterinario = ordenes.stream()
                .collect(Collectors.groupingBy(
                    o -> o.getVeterinario().getNombreCompleto(),
                    Collectors.counting()
                ));
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalOrdenes", ordenes.size());
        reporte.put("ordenesPorVeterinario", ordenesPorVeterinario);
        
        return reporte;
    }

    /**
     * Obtener productividad de técnicos
     */
    public Map<String, Object> obtenerProductividadTecnicos(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<TomaMuestraVet> tomas = tomaMuestraRepository.findByFechaHoraBetween(
                inicio.atStartOfDay(), fin.atTime(23, 59, 59));
        
        Map<String, Long> tomasPorTecnico = tomas.stream()
                .collect(Collectors.groupingBy(
                    t -> t.getTecnico().getNombreCompleto(),
                    Collectors.counting()
                ));
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalTomas", tomas.size());
        reporte.put("tomasPorTecnico", tomasPorTecnico);
        
        return reporte;
    }

    /**
     * Calcular ingresos por período
     */
    public Map<String, Object> calcularIngresos(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<Factura> facturas = facturaRepository.findByFechaEmisionBetween(inicio, fin);
        
        double totalFacturado = facturas.stream()
                .mapToDouble(Factura::getTotal)
                .sum();
        
        double totalPagado = facturas.stream()
                .filter(f -> "PAGADA".equalsIgnoreCase(f.getEstado()))
                .mapToDouble(Factura::getTotal)
                .sum();
        
        double totalPendiente = facturas.stream()
                .filter(f -> "PENDIENTE".equalsIgnoreCase(f.getEstado()))
                .mapToDouble(Factura::getTotal)
                .sum();
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalFacturas", facturas.size());
        reporte.put("totalFacturado", totalFacturado);
        reporte.put("totalPagado", totalPagado);
        reporte.put("totalPendiente", totalPendiente);
        reporte.put("tasaCobro", totalFacturado == 0 ? 0 : (totalPagado * 100) / totalFacturado);
        
        return reporte;
    }

    /**
     * Obtener especies más atendidas
     */
    public Map<String, Object> obtenerEspeciesAtendidas(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenes = ordenRepository.findByFechaOrdenBetween(inicio, fin);
        
        Map<String, Long> especiesAtendidas = ordenes.stream()
                .collect(Collectors.groupingBy(
                    o -> o.getMascota().getEspecie(),
                    Collectors.counting()
                ));
        
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        reporte.put("totalOrdenes", ordenes.size());
        reporte.put("especiesAtendidas", especiesAtendidas);
        
        return reporte;
    }

    /**
     * Obtener órdenes por estado
     */
    public Map<String, Object> obtenerOrdenesPorEstado() {
        Map<String, Object> reporte = new HashMap<>();
        
        reporte.put("pendientes", ordenRepository.countByEstadoIgnoreCase("PENDIENTE"));
        reporte.put("enProceso", ordenRepository.countByEstadoIgnoreCase("EN_PROCESO"));
        reporte.put("completadas", ordenRepository.countByEstadoIgnoreCase("COMPLETADA"));
        reporte.put("canceladas", ordenRepository.countByEstadoIgnoreCase("CANCELADA"));
        reporte.put("total", ordenRepository.count());
        
        return reporte;
    }

    /**
     * Dashboard completo con métricas principales
     */
    public Map<String, Object> obtenerDashboardCompleto() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // KPIs generales
        dashboard.put("kpisGenerales", obtenerKPIsGenerales());
        
        // Órdenes por estado
        dashboard.put("ordenesPorEstado", obtenerOrdenesPorEstado());
        
        // Análisis del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now();
        
        dashboard.put("analisisMes", obtenerAnalisisPorTipo(inicioMes, finMes));
        dashboard.put("ingresosMes", calcularIngresos(inicioMes, finMes));
        dashboard.put("especiesMes", obtenerEspeciesAtendidas(inicioMes, finMes));
        
        // Tiempo promedio
        dashboard.put("tiempoPromedio", calcularTiempoPromedio(inicioMes, finMes));
        
        // Satisfacción del cliente
        dashboard.put("satisfaccion", obtenerSatisfaccionCliente());
        
        // Fecha de generación
        dashboard.put("fechaGeneracion", LocalDateTime.now());
        
        return dashboard;
    }

    /**
     * Reporte de órdenes urgentes pendientes
     */
    public Map<String, Object> obtenerOrdenesUrgentes() {
        Map<String, Object> reporte = new HashMap<>();
        
        List<OrdenVeterinaria> ordenesUrgentes = ordenRepository
                .findByPrioridadIgnoreCase("URGENTE")
                .stream()
                .filter(o -> !"COMPLETADA".equalsIgnoreCase(o.getEstado()))
                .collect(Collectors.toList());
        
        reporte.put("totalOrdenesUrgentes", ordenesUrgentes.size());
        reporte.put("ordenes", ordenesUrgentes);
        
        return reporte;
    }

    /**
     * Reporte de eficiencia del laboratorio
     */
    public Map<String, Object> obtenerEficienciaLaboratorio(LocalDate inicio, LocalDate fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        // Tiempo promedio
        Map<String, Object> tiempoPromedio = calcularTiempoPromedio(inicio, fin);
        
        // Análisis repetidos
        Map<String, Object> analisisRepetidos = calcularAnalisisRepetidos(inicio, fin);
        
        // Productividad
        Map<String, Object> productividadTecnicos = obtenerProductividadTecnicos(inicio, fin);
        
        reporte.put("tiempoPromedio", tiempoPromedio);
        reporte.put("analisisRepetidos", analisisRepetidos);
        reporte.put("productividadTecnicos", productividadTecnicos);
        reporte.put("periodoInicio", inicio);
        reporte.put("periodoFin", fin);
        
        return reporte;
    }
}