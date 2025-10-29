import { Component, OnInit } from '@angular/core';
import { ReporteService } from '../../services/reporte.service';
import { NotificacionService } from '../../services/notificacion.service';

@Component({
  selector: 'app-reporte-kpi',
  templateUrl: './reporte-kpi.component.html',
  styleUrls: ['./reporte-kpi.component.css']
})
export class ReporteKpiComponent implements OnInit {
  loading: boolean = true;
  
  // KPIs Generales
  kpis: any = {
    totalOrdenes: 0,
    ordenesPendientes: 0,
    ordenesCompletadas: 0,
    resultadosValidados: 0,
    resultadosPendientes: 0,
    totalFacturado: 0,
    facturasPendientes: 0,
    totalDuenos: 0,
    totalMascotas: 0,
    totalVeterinarios: 0,
    totalTecnicos: 0
  };

  // Dashboard completo
  dashboard: any = null;

  // Órdenes por estado
  ordenesPorEstado: any = {
    labels: [],
    data: [],
    colors: []
  };

  // Especies más atendidas (últimos 30 días)
  especiesAtendidas: any = {
    labels: [],
    data: [],
    colors: []
  };

  // Ingresos mensuales (últimos 6 meses)
  ingresosMensuales: any = {
    labels: [],
    data: []
  };

  // Rangos de fechas para filtros
  fechaInicio: string = '';
  fechaFin: string = '';

  // Tiempo promedio
  tiempoPromedio: any = null;

  // Análisis repetidos
  analisisRepetidos: any = null;

  // Tratamientos mismo día
  tratamientosMismoDia: any = null;

  constructor(
    private reporteService: ReporteService,
    private notificacionService: NotificacionService
  ) {}

  ngOnInit(): void {
    this.inicializarFechas();
    this.cargarDashboard();
  }

  inicializarFechas(): void {
    const hoy = new Date();
    const hace30Dias = new Date();
    hace30Dias.setDate(hoy.getDate() - 30);

    this.fechaFin = this.formatearFecha(hoy);
    this.fechaInicio = this.formatearFecha(hace30Dias);
  }

  formatearFecha(fecha: Date): string {
    const year = fecha.getFullYear();
    const month = String(fecha.getMonth() + 1).padStart(2, '0');
    const day = String(fecha.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  cargarDashboard(): void {
    this.loading = true;
    
    // Cargar dashboard completo
    this.reporteService.obtenerDashboard().subscribe({
      next: (data) => {
        this.dashboard = data;
        this.procesarDashboard(data);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar dashboard:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el dashboard');
        this.loading = false;
      }
    });

    // Cargar KPIs generales
    this.cargarKPIs();

    // Cargar órdenes por estado
    this.cargarOrdenesPorEstado();

    // Cargar especies atendidas
    this.cargarEspeciesAtendidas();
  }

  cargarKPIs(): void {
    this.reporteService.obtenerKPIs().subscribe({
      next: (data) => {
        this.kpis = data;
      },
      error: (error) => {
        console.error('Error al cargar KPIs:', error);
      }
    });
  }

  cargarOrdenesPorEstado(): void {
    this.reporteService.obtenerOrdenesPorEstado().subscribe({
      next: (data) => {
        if (data && data.ordenesPorEstado) {
          this.ordenesPorEstado = {
            labels: Object.keys(data.ordenesPorEstado),
            data: Object.values(data.ordenesPorEstado),
            colors: this.getColoresEstado(Object.keys(data.ordenesPorEstado))
          };
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  cargarEspeciesAtendidas(): void {
    this.reporteService.obtenerEspeciesAtendidas(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        if (data && data.especiesPorCantidad) {
          this.especiesAtendidas = {
            labels: Object.keys(data.especiesPorCantidad),
            data: Object.values(data.especiesPorCantidad),
            colors: this.getColoresEspecies(Object.keys(data.especiesPorCantidad))
          };
        }
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  cargarTiempoPromedio(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.notificacionService.warning('Seleccione un rango de fechas');
      return;
    }

    this.reporteService.obtenerTiempoPromedio(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.tiempoPromedio = data;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo calcular el tiempo promedio');
      }
    });
  }

  cargarAnalisisRepetidos(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.notificacionService.warning('Seleccione un rango de fechas');
      return;
    }

    this.reporteService.obtenerAnalisisRepetidos(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.analisisRepetidos = data;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo calcular los análisis repetidos');
      }
    });
  }

  cargarTratamientosMismoDia(): void {
    if (!this.fechaFin) {
      this.notificacionService.warning('Seleccione una fecha');
      return;
    }

    this.reporteService.obtenerTratamientosMismoDia(this.fechaFin).subscribe({
      next: (data) => {
        this.tratamientosMismoDia = data;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo calcular los tratamientos del día');
      }
    });
  }

  procesarDashboard(data: any): void {
    // Procesar datos del dashboard según estructura recibida
    if (data) {
      // Aquí puedes procesar los datos adicionales del dashboard
      console.log('Dashboard cargado:', data);
    }
  }

  aplicarFiltros(): void {
    this.cargarEspeciesAtendidas();
    this.cargarTiempoPromedio();
    this.cargarAnalisisRepetidos();
  }

  limpiarFiltros(): void {
    this.inicializarFechas();
    this.tiempoPromedio = null;
    this.analisisRepetidos = null;
    this.tratamientosMismoDia = null;
    this.cargarEspeciesAtendidas();
  }

  getColoresEstado(estados: string[]): string[] {
    const colores: any = {
      'PENDIENTE': '#ffc107',
      'EN_PROCESO': '#17a2b8',
      'COMPLETADA': '#28a745',
      'CANCELADA': '#dc3545'
    };
    return estados.map(estado => colores[estado] || '#6c757d');
  }

  getColoresEspecies(especies: string[]): string[] {
    const colores: any = {
      'Perro': '#007bff',
      'Gato': '#ffc107',
      'Ave': '#17a2b8',
      'Conejo': '#28a745',
      'Hamster': '#6c757d',
      'Otro': '#343a40'
    };
    return especies.map(especie => colores[especie] || '#6c757d');
  }

  exportarPDF(): void {
    this.notificacionService.info('Generando PDF...');
    // TODO: Implementar exportación a PDF
  }

  exportarExcel(): void {
    this.notificacionService.info('Generando Excel...');
    // TODO: Implementar exportación a Excel
  }

  refrescar(): void {
    this.cargarDashboard();
    this.notificacionService.success('Dashboard actualizado');
  }

  // Helper para obtener porcentaje
  calcularPorcentaje(valor: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((valor / total) * 100);
  }

  // Helper para formatear moneda
  formatearMoneda(valor: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(valor);
  }

  // Helper para formatear tiempo
  formatearTiempo(horas: number): string {
    if (horas < 1) {
      return `${Math.round(horas * 60)} minutos`;
    } else if (horas < 24) {
      return `${horas.toFixed(1)} horas`;
    } else {
      const dias = Math.floor(horas / 24);
      const horasRestantes = Math.round(horas % 24);
      return `${dias} día${dias > 1 ? 's' : ''} ${horasRestantes}h`;
    }
  }
}