import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrdenService } from '../../services/orden.service';
import { NotificacionService } from '../../services/notificacion.service';
import { OrdenVeterinaria } from '../../models/orden.model';

@Component({
  selector: 'app-orden-list',
  templateUrl: './orden-list.component.html',
  styleUrls: ['./orden-list.component.css']
})
export class OrdenListComponent implements OnInit {
  ordenes: OrdenVeterinaria[] = [];
  ordenesFiltradas: OrdenVeterinaria[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEstado: string = 'TODOS';

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  // Estadísticas
  estadisticas = {
    pendientes: 0,
    enProceso: 0,
    completadas: 0,
    canceladas: 0
  };

  constructor(
    private ordenService: OrdenService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarOrdenes();
  }

  cargarOrdenes(): void {
    this.loading = true;
    this.ordenService.listarTodas().subscribe({
      next: (data) => {
        this.ordenes = data;
        this.ordenesFiltradas = data;
        this.calcularEstadisticas();
        this.calcularPaginacion();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar órdenes:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  calcularEstadisticas(): void {
    this.estadisticas.pendientes = this.ordenes.filter(o => o.estado === 'PENDIENTE').length;
    this.estadisticas.enProceso = this.ordenes.filter(o => o.estado === 'EN_PROCESO').length;
    this.estadisticas.completadas = this.ordenes.filter(o => o.estado === 'COMPLETADA').length;
    this.estadisticas.canceladas = this.ordenes.filter(o => o.estado === 'CANCELADA').length;
  }

  buscar(): void {
    let resultados = this.ordenes;

    // Filtrar por término de búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultados = resultados.filter(orden =>
        orden.tipoExamen.toLowerCase().includes(term) ||
        orden.mascota?.nombre.toLowerCase().includes(term) ||
        orden.veterinario?.nombres.toLowerCase().includes(term) ||
        orden.idOrden?.toString().includes(term)
      );
    }

    // Filtrar por estado
    if (this.filtroEstado !== 'TODOS') {
      resultados = resultados.filter(orden => orden.estado === this.filtroEstado);
    }

    this.ordenesFiltradas = resultados;
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  filtrarPorEstado(estado: string): void {
    this.filtroEstado = estado;
    this.buscar();
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.filtroEstado = 'TODOS';
    this.buscar();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.ordenesFiltradas.length / this.itemsPorPagina);
  }

  get ordenesPaginadas(): OrdenVeterinaria[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.ordenesFiltradas.slice(inicio, fin);
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
    }
  }

  getNumeroPaginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  nueva(): void {
    this.router.navigate(['/ordenes/nueva']);
  }

  editar(id: number): void {
    this.router.navigate(['/ordenes/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/ordenes/ver', id]);
  }

  verTomaMuestra(id: number): void {
    this.router.navigate(['/toma-muestras'], { queryParams: { orden: id } });
  }

  verResultados(id: number): void {
    this.router.navigate(['/resultados'], { queryParams: { orden: id } });
  }

  cambiarEstado(orden: OrdenVeterinaria, nuevoEstado: string): void {
    if (confirm(`¿Cambiar estado de la orden a ${nuevoEstado}?`)) {
      this.ordenService.cambiarEstado(orden.idOrden!, nuevoEstado).subscribe({
        next: () => {
          this.notificacionService.success(`Estado actualizado a ${nuevoEstado}`);
          this.cargarOrdenes();
        },
        error: (error) => {
          console.error('Error al cambiar estado:', error);
          this.notificacionService.errorGeneral('No se pudo cambiar el estado');
        }
      });
    }
  }

  eliminar(orden: OrdenVeterinaria): void {
    if (this.notificacionService.confirmarAccion(
      `¿Está seguro de eliminar la orden #${orden.idOrden} - ${orden.tipoExamen}?`
    )) {
      this.ordenService.eliminar(orden.idOrden!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Orden');
          this.cargarOrdenes();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar la orden');
        }
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    const clases: { [key: string]: string } = {
      'PENDIENTE': 'bg-warning',
      'EN_PROCESO': 'bg-info',
      'COMPLETADA': 'bg-success',
      'CANCELADA': 'bg-danger'
    };
    return clases[estado] || 'bg-secondary';
  }

  getPrioridadBadgeClass(prioridad: string): string {
    const clases: { [key: string]: string } = {
      'URGENTE': 'bg-danger',
      'NORMAL': 'bg-primary',
      'BAJA': 'bg-secondary'
    };
    return clases[prioridad] || 'bg-secondary';
  }

  exportarExcel(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  exportarPDF(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  get Math() {
    return Math;
  }
}