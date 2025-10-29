import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { TomaMuestraService } from '../../services/toma-muestra.service';
import { OrdenService } from '../../services/orden.service';
import { TecnicoService } from '../../services/tecnico.service';
import { NotificacionService } from '../../services/notificacion.service';
import { TomaMuestra } from '../../models/toma-muestra.model';
import { OrdenVeterinaria } from '../../models/orden.model';
import { TecnicoVeterinario } from '../../models/tecnico.model';

@Component({
  selector: 'app-toma-muestra-list',
  templateUrl: './toma-muestra-list.component.html',
  styleUrls: ['./toma-muestra-list.component.css']
})
export class TomaMuestraListComponent implements OnInit {
  tomasMuestra: TomaMuestra[] = [];
  tomasFiltradas: TomaMuestra[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEstado: string = 'TODAS';
  filtroTecnicoId: number | null = null;
  filtroOrdenId: number | null = null;
  
  ordenSeleccionada: OrdenVeterinaria | null = null;
  tecnicoSeleccionado: TecnicoVeterinario | null = null;

  // Estados posibles
  estados = ['TODAS', 'PROGRAMADA', 'REALIZADA', 'PROCESANDO', 'COMPLETADA'];

  // Tipos de muestra
  tiposMuestra = ['Sangre', 'Orina', 'Heces', 'Piel', 'Pelo', 'Saliva', 'Otro'];

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private tomaMuestraService: TomaMuestraService,
    private ordenService: OrdenService,
    private tecnicoService: TecnicoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Verificar filtros por URL
    this.route.queryParams.subscribe(params => {
      if (params['orden']) {
        this.filtroOrdenId = +params['orden'];
        this.cargarOrdenSeleccionada(this.filtroOrdenId);
      }
      if (params['tecnico']) {
        this.filtroTecnicoId = +params['tecnico'];
        this.cargarTecnicoSeleccionado(this.filtroTecnicoId);
      }
    });
    
    this.cargarTomasMuestra();
  }

  cargarOrdenSeleccionada(id: number): void {
    this.ordenService.obtenerPorId(id).subscribe({
      next: (orden) => {
        this.ordenSeleccionada = orden;
      },
      error: (error) => {
        console.error('Error al cargar orden:', error);
      }
    });
  }

  cargarTecnicoSeleccionado(id: number): void {
    this.tecnicoService.obtenerPorId(id).subscribe({
      next: (tecnico) => {
        this.tecnicoSeleccionado = tecnico;
      },
      error: (error) => {
        console.error('Error al cargar técnico:', error);
      }
    });
  }

  cargarTomasMuestra(): void {
    this.loading = true;
    
    if (this.filtroOrdenId) {
      this.tomaMuestraService.buscarPorOrden(this.filtroOrdenId).subscribe({
        next: (toma) => {
          this.procesarTomasMuestra(toma ? [toma] : []);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    } else if (this.filtroTecnicoId) {
      this.tomaMuestraService.buscarPorTecnico(this.filtroTecnicoId).subscribe({
        next: (data) => {
          this.procesarTomasMuestra(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    } else {
      this.tomaMuestraService.listarTodas().subscribe({
        next: (data) => {
          this.procesarTomasMuestra(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    }
  }

  procesarTomasMuestra(data: TomaMuestra[]): void {
    this.tomasMuestra = data;
    this.aplicarFiltros();
    this.loading = false;
  }

  aplicarFiltros(): void {
    let resultado = this.tomasMuestra;

    // Filtro por estado
    if (this.filtroEstado !== 'TODAS') {
      resultado = resultado.filter(t => t.estado === this.filtroEstado);
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(t =>
        t.tipoMuestra.toLowerCase().includes(term) ||
        (t.codigoMuestra && t.codigoMuestra.toLowerCase().includes(term)) ||
        (t.tecnico && (
          t.tecnico.nombres?.toLowerCase().includes(term) ||
          t.tecnico.apellidos?.toLowerCase().includes(term)
        )) ||
        (t.orden && t.orden.tipoExamen?.toLowerCase().includes(term))
      );
    }

    this.tomasFiltradas = resultado;
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  cambiarFiltroEstado(estado: string): void {
    this.filtroEstado = estado;
    this.aplicarFiltros();
  }

  buscar(): void {
    this.aplicarFiltros();
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.aplicarFiltros();
  }

  limpiarFiltros(): void {
    this.filtroOrdenId = null;
    this.filtroTecnicoId = null;
    this.ordenSeleccionada = null;
    this.tecnicoSeleccionado = null;
    this.router.navigate(['/toma-muestras']);
    this.cargarTomasMuestra();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.tomasFiltradas.length / this.itemsPorPagina);
  }

  get tomasPaginadas(): TomaMuestra[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.tomasFiltradas.slice(inicio, fin);
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
    this.router.navigate(['/toma-muestras/nueva']);
  }

  editar(id: number): void {
    this.router.navigate(['/toma-muestras/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/toma-muestras/ver', id]);
  }

  verOrden(idOrden: number): void {
    this.router.navigate(['/ordenes/ver', idOrden]);
  }

  verTecnico(idTecnico: number): void {
    this.router.navigate(['/tecnicos/ver', idTecnico]);
  }

  eliminar(toma: TomaMuestra): void {
    if (this.notificacionService.confirmarAccion(
      `¿Está seguro de eliminar la toma de muestra ${toma.codigoMuestra || 'sin código'}?`
    )) {
      this.tomaMuestraService.eliminar(toma.idToma!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Toma de muestra');
          this.cargarTomasMuestra();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar la toma de muestra');
        }
      });
    }
  }

  cambiarEstado(toma: TomaMuestra, nuevoEstado: string): void {
    const tomaActualizada = { ...toma, estado: nuevoEstado };
    
    this.tomaMuestraService.actualizar(toma.idToma!, tomaActualizada).subscribe({
      next: () => {
        this.notificacionService.success(`Estado actualizado a: ${nuevoEstado}`);
        this.cargarTomasMuestra();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo actualizar el estado');
      }
    });
  }

  obtenerPendientes(): void {
    this.loading = true;
    this.tomaMuestraService.obtenerPendientes().subscribe({
      next: (data) => {
        this.procesarTomasMuestra(data);
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  getColorEstado(estado: string): string {
    const colores: any = {
      'PROGRAMADA': 'info',
      'REALIZADA': 'primary',
      'PROCESANDO': 'warning',
      'COMPLETADA': 'success'
    };
    return colores[estado] || 'secondary';
  }

  getIconoTipoMuestra(tipo: string): string {
    const iconos: any = {
      'Sangre': 'fa-tint',
      'Orina': 'fa-flask',
      'Heces': 'fa-vial',
      'Piel': 'fa-hand-paper',
      'Pelo': 'fa-cut',
      'Saliva': 'fa-droplet',
      'Otro': 'fa-microscope'
    };
    return iconos[tipo] || 'fa-vial';
  }

  formatearFechaHora(fecha: string): string {
    if (!fecha) return '-';
    const f = new Date(fecha);
    return f.toLocaleString('es-PE', { 
      day: '2-digit', 
      month: '2-digit', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  exportarExcel(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  exportarPDF(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }
}