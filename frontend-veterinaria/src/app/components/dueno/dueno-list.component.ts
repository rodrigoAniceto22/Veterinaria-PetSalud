import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DuenoService } from '../../services/dueno.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Dueno } from '../../models/dueno.model';


@Component({
  selector: 'app-dueno-list',
  templateUrl: './dueno-list.component.html',
  styleUrls: ['./dueno-list.component.css']
})
export class DuenoListComponent implements OnInit {
  duenos: Dueno[] = [];
  duenosFiltrados: Dueno[] = [];
  loading: boolean = true;
  searchTerm: string = '';

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private duenoService: DuenoService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarDuenos();
  }

  cargarDuenos(): void {
    this.loading = true;
    this.duenoService.listarTodos().subscribe({
      next: (data) => {
        this.duenos = data;
        this.duenosFiltrados = data;
        this.calcularPaginacion();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar dueños:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  buscar(): void {
    if (this.searchTerm.trim() === '') {
      this.duenosFiltrados = this.duenos;
    } else {
      const term = this.searchTerm.toLowerCase();
      this.duenosFiltrados = this.duenos.filter(dueno => 
        dueno.nombres.toLowerCase().includes(term) ||
        dueno.apellidos.toLowerCase().includes(term) ||
        dueno.dni.toLowerCase().includes(term) ||
        (dueno.telefono && dueno.telefono.toLowerCase().includes(term))
      );
    }
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.buscar();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.duenosFiltrados.length / this.itemsPorPagina);
  }

  get duenosPaginados(): Dueno[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.duenosFiltrados.slice(inicio, fin);
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
    this.router.navigate(['/duenos/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/duenos/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/duenos/ver', id]);
  }

  verMascotas(id: number): void {
    this.router.navigate(['/mascotas'], { queryParams: { dueno: id } });
  }

  eliminar(dueno: Dueno): void {
    if (this.notificacionService.confirmarAccion(`¿Está seguro de eliminar al dueño ${dueno.nombres} ${dueno.apellidos}?`)) {
      this.duenoService.eliminar(dueno.idDueno!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Dueño');
          this.cargarDuenos();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar el dueño');
        }
      });
    }
  }

  exportarExcel(): void {
    // TODO: Implementar exportación a Excel
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  exportarPDF(): void {
    // TODO: Implementar exportación a PDF
    this.notificacionService.info('Función de exportación en desarrollo');
  }
}