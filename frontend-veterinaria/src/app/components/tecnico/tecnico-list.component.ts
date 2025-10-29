import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TecnicoService } from '../../services/tecnico.service';
import { NotificacionService } from '../../services/notificacion.service';
import { TecnicoVeterinario } from '../../models/tecnico.model';

@Component({
  selector: 'app-tecnico-list',
  templateUrl: './tecnico-list.component.html',
  styleUrls: ['./tecnico-list.component.css']
})
export class TecnicoListComponent implements OnInit {
  tecnicos: TecnicoVeterinario[] = [];
  tecnicosFiltrados: TecnicoVeterinario[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEspecialidad: string = 'TODAS';

  // Especialidades comunes
  especialidades = [
    'TODAS',
    'Hematología',
    'Microbiología',
    'Química Clínica',
    'Parasitología',
    'Urianálisis',
    'Histopatología',
    'Otro'
  ];

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private tecnicoService: TecnicoService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarTecnicos();
  }

  cargarTecnicos(): void {
    this.loading = true;
    this.tecnicoService.listarTodos().subscribe({
      next: (data) => {
        this.tecnicos = data;
        this.aplicarFiltros();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar técnicos:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let resultado = this.tecnicos;

    // Filtro por especialidad
    if (this.filtroEspecialidad !== 'TODAS') {
      resultado = resultado.filter(t => 
        t.especialidad === this.filtroEspecialidad
      );
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(t =>
        t.nombres.toLowerCase().includes(term) ||
        t.apellidos.toLowerCase().includes(term) ||
        (t.especialidad && t.especialidad.toLowerCase().includes(term)) ||
        (t.certificacion && t.certificacion.toLowerCase().includes(term))
      );
    }

    this.tecnicosFiltrados = resultado;
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  cambiarFiltroEspecialidad(especialidad: string): void {
    this.filtroEspecialidad = especialidad;
    this.aplicarFiltros();
  }

  buscar(): void {
    this.aplicarFiltros();
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.aplicarFiltros();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.tecnicosFiltrados.length / this.itemsPorPagina);
  }

  get tecnicosPaginados(): TecnicoVeterinario[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.tecnicosFiltrados.slice(inicio, fin);
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
    }
  }

  getNumeroPaginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  nuevo(): void {
    this.router.navigate(['/tecnicos/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/tecnicos/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/tecnicos/ver', id]);
  }

  verTomasMuestra(id: number): void {
    this.router.navigate(['/toma-muestras'], { queryParams: { tecnico: id } });
  }

  eliminar(tecnico: TecnicoVeterinario): void {
    if (this.notificacionService.confirmarAccion(
      `¿Está seguro de eliminar al técnico ${tecnico.nombres} ${tecnico.apellidos}?`
    )) {
      this.tecnicoService.eliminar(tecnico.idTecnico!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Técnico');
          this.cargarTecnicos();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar el técnico');
        }
      });
    }
  }

  toggleEstado(tecnico: TecnicoVeterinario): void {
    const accion = tecnico.activo ? 'desactivar' : 'activar';
    if (this.notificacionService.confirmarAccion(
      `¿Está seguro de ${accion} a ${tecnico.nombres} ${tecnico.apellidos}?`
    )) {
      tecnico.activo = !tecnico.activo;
      this.tecnicoService.actualizar(tecnico.idTecnico!, tecnico).subscribe({
        next: () => {
          this.notificacionService.success(`Técnico ${accion}do exitosamente`);
          this.cargarTecnicos();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo cambiar el estado');
          tecnico.activo = !tecnico.activo; // Revertir cambio
        }
      });
    }
  }

  exportarExcel(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  exportarPDF(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  getColorEspecialidad(especialidad: string): string {
    const colores: any = {
      'Hematología': 'danger',
      'Microbiología': 'primary',
      'Química Clínica': 'success',
      'Parasitología': 'warning',
      'Urianálisis': 'info',
      'Histopatología': 'secondary'
    };
    return colores[especialidad] || 'dark';
  }
}