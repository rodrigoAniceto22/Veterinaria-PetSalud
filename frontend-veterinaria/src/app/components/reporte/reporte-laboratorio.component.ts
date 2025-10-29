import { Component, OnInit } from '@angular/core';
import { ReporteService } from '../../services/reporte.service';
import { NotificacionService } from '../../services/notificacion.service';

@Component({
  selector: 'app-reporte-laboratorio',
  templateUrl: './reporte-laboratorio.component.html',
  styleUrls: ['./reporte-laboratorio.component.css']
})
export class ReporteLaboratorioComponent implements OnInit {
  loading: boolean = true;

  // Filtros
  fechaInicio: string = '';
  fechaFin: string = '';
  tipoExamen: string = '';
  veterinario: string = '';
  tecnico: string = '';

  // Datos del reporte
  analisisPorTipo: any = null;
  productividadVeterinarios: any = null;
  productividadTecnicos: any = null;
  ingresos: any = null;

  // Listas para filtros
  tiposExamen = [
    'TODOS',
    'Hemograma',
    'Bioquímica',
    'Urianálisis',
    'Coprológico',
    'Microbiología',
    'Parasitología',
    'Citología',
    'Histopatología',
    'Otro'
  ];

  veterinarios: any[] = [];
  tecnicos: any[] = [];

  // Datos para gráficos
  chartAnalisisPorTipo: any = {
    labels: [],
    data: [],
    colors: []
  };

  chartProductividadVet: any = {
    labels: [],
    data: []
  };

  chartProductividadTec: any = {
    labels: [],
    data: []
  };

  chartIngresosMensuales: any = {
    labels: [],
    data: []
  };

  // Tabla de análisis detallada
  tablaAnalisis: any[] = [];
  tablaAnalisisFiltrada: any[] = [];
  
  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  // Ordenamiento
  ordenarPor: string = 'fecha';
  ordenAscendente: boolean = false;

  constructor(
    private reporteService: ReporteService,
    private notificacionService: NotificacionService
  ) {}

  ngOnInit(): void {
    this.inicializarFechas();
    this.cargarReportes();
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

  cargarReportes(): void {
    this.loading = true;
    this.cargarAnalisisPorTipo();
    this.cargarProductividadVeterinarios();
    this.cargarProductividadTecnicos();
    this.cargarIngresos();
  }

  cargarAnalisisPorTipo(): void {
    this.reporteService.obtenerAnalisisPorTipo(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.analisisPorTipo = data;
        this.procesarAnalisisPorTipo(data);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar análisis por tipo:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el reporte de análisis');
        this.loading = false;
      }
    });
  }

  cargarProductividadVeterinarios(): void {
    this.reporteService.obtenerProductividadVeterinarios(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.productividadVeterinarios = data;
        this.procesarProductividadVeterinarios(data);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  cargarProductividadTecnicos(): void {
    this.reporteService.obtenerProductividadTecnicos(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.productividadTecnicos = data;
        this.procesarProductividadTecnicos(data);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  cargarIngresos(): void {
    this.reporteService.obtenerIngresos(this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.ingresos = data;
        this.procesarIngresos(data);
      },
      error: (error) => {
        console.error('Error:', error);
      }
    });
  }

  procesarAnalisisPorTipo(data: any): void {
    if (data && data.analisisPorTipo) {
      const tipos = Object.keys(data.analisisPorTipo);
      const cantidades = Object.values(data.analisisPorTipo);
      
      this.chartAnalisisPorTipo = {
        labels: tipos,
        data: cantidades,
        colors: this.generarColores(tipos.length)
      };

      // Crear tabla detallada
      this.tablaAnalisis = tipos.map((tipo, index) => ({
        tipo: tipo,
        cantidad: cantidades[index],
        porcentaje: this.calcularPorcentaje(cantidades[index] as number, data.totalAnalisis || 1)
      }));
      
      this.tablaAnalisisFiltrada = [...this.tablaAnalisis];
      this.calcularPaginacion();
    }
  }

  procesarProductividadVeterinarios(data: any): void {
    if (data && data.productividadPorVeterinario) {
      const veterinarios = Object.keys(data.productividadPorVeterinario);
      const ordenes = Object.values(data.productividadPorVeterinario);
      
      this.chartProductividadVet = {
        labels: veterinarios,
        data: ordenes
      };
    }
  }

  procesarProductividadTecnicos(data: any): void {
    if (data && data.productividadPorTecnico) {
      const tecnicos = Object.keys(data.productividadPorTecnico);
      const tomas = Object.values(data.productividadPorTecnico);
      
      this.chartProductividadTec = {
        labels: tecnicos,
        data: tomas
      };
    }
  }

  procesarIngresos(data: any): void {
    if (data && data.ingresosPorPeriodo) {
      this.chartIngresosMensuales = {
        labels: Object.keys(data.ingresosPorPeriodo),
        data: Object.values(data.ingresosPorPeriodo)
      };
    }
  }

  aplicarFiltros(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.notificacionService.warning('Seleccione un rango de fechas válido');
      return;
    }

    const inicio = new Date(this.fechaInicio);
    const fin = new Date(this.fechaFin);

    if (inicio > fin) {
      this.notificacionService.warning('La fecha de inicio debe ser menor a la fecha fin');
      return;
    }

    this.cargarReportes();
    this.notificacionService.success('Filtros aplicados correctamente');
  }

  limpiarFiltros(): void {
    this.inicializarFechas();
    this.tipoExamen = '';
    this.veterinario = '';
    this.tecnico = '';
    this.cargarReportes();
    this.notificacionService.info('Filtros limpiados');
  }

  aplicarFiltrosTabla(): void {
    let resultado = [...this.tablaAnalisis];

    if (this.tipoExamen && this.tipoExamen !== 'TODOS') {
      resultado = resultado.filter(item => item.tipo === this.tipoExamen);
    }

    this.tablaAnalisisFiltrada = resultado;
    this.ordenarTabla();
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  ordenarTabla(): void {
    this.tablaAnalisisFiltrada.sort((a, b) => {
      let valorA = a[this.ordenarPor];
      let valorB = b[this.ordenarPor];

      if (typeof valorA === 'string') {
        valorA = valorA.toLowerCase();
        valorB = valorB.toLowerCase();
      }

      if (this.ordenAscendente) {
        return valorA > valorB ? 1 : -1;
      } else {
        return valorA < valorB ? 1 : -1;
      }
    });
  }

  cambiarOrden(campo: string): void {
    if (this.ordenarPor === campo) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenarPor = campo;
      this.ordenAscendente = false;
    }
    this.ordenarTabla();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.tablaAnalisisFiltrada.length / this.itemsPorPagina);
  }

  get tablaAnalisisPaginada(): any[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.tablaAnalisisFiltrada.slice(inicio, fin);
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
    }
  }

  getNumeroPaginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  calcularPorcentaje(valor: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((valor / total) * 100);
  }

  formatearMoneda(valor: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(valor);
  }

  generarColores(cantidad: number): string[] {
    const colores = [
      '#667eea', '#764ba2', '#f093fb', '#4facfe',
      '#43e97b', '#fa709a', '#fee140', '#30cfd0',
      '#a8edea', '#fed6e3', '#ff9a9e', '#fecfef'
    ];
    return colores.slice(0, cantidad);
  }

  exportarPDF(): void {
    this.notificacionService.info('Generando reporte PDF...');
    // TODO: Implementar exportación
  }

  exportarExcel(): void {
    this.notificacionService.info('Generando reporte Excel...');
    // TODO: Implementar exportación
  }

  imprimirReporte(): void {
    window.print();
  }

  refrescar(): void {
    this.cargarReportes();
    this.notificacionService.success('Reporte actualizado');
  }

  // Helper para obtener icono según tipo de examen
  getIconoTipoExamen(tipo: string): string {
    const iconos: any = {
      'Hemograma': 'fa-tint',
      'Bioquímica': 'fa-flask',
      'Urianálisis': 'fa-vial',
      'Coprológico': 'fa-microscope',
      'Microbiología': 'fa-bacteria',
      'Parasitología': 'fa-bug',
      'Citología': 'fa-disease',
      'Histopatología': 'fa-notes-medical'
    };
    return iconos[tipo] || 'fa-file-medical';
  }

  // Helper para obtener color según tipo de examen
  getColorTipoExamen(tipo: string): string {
    const colores: any = {
      'Hemograma': 'danger',
      'Bioquímica': 'primary',
      'Urianálisis': 'warning',
      'Coprológico': 'secondary',
      'Microbiología': 'info',
      'Parasitología': 'success',
      'Citología': 'dark',
      'Histopatología': 'primary'
    };
    return colores[tipo] || 'secondary';
  }
}