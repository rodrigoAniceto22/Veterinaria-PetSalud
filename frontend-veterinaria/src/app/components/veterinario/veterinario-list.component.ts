import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VeterinarioService } from '../../services/veterinario.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Veterinario } from '../../models/veterinario.model';

@Component({
  selector: 'app-veterinario-list',
  templateUrl: './veterinario-list.component.html',
  styleUrls: ['./veterinario-list.component.css']
})
export class VeterinarioListComponent implements OnInit {
  veterinarios: Veterinario[] = [];
  veterinariosFiltrados: Veterinario[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEspecialidad: string = 'TODAS';

  // Especialidades
  especialidades = ['TODAS', 'Cirugía', 'Dermatología', 'Laboratorio', 'Cardiología', 'Oftalmología', 'Otra'];

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private veterinarioService: VeterinarioService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarVeterinarios();
  }

  cargarVeterinarios(): void {
    this.loading = true;
    this.veterinarioService.listarTodos().subscribe({
      next: (data) => {
        this.veterinarios = data;
        this.aplicarFiltros();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let resultado = this.veterinarios;

    // Filtro por especialidad
    if (this.filtroEspecialidad !== 'TODAS') {
      resultado = resultado.filter(v => v.especialidad === this.filtroEspecialidad);
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(v =>
        v.nombres.toLowerCase().includes(term) ||
        v.apellidos.toLowerCase().includes(term) ||
        (v.colegiatura && v.colegiatura.toLowerCase().includes(term)) ||
        (v.email && v.email.toLowerCase().includes(term))
      );
    }

    this.veterinariosFiltrados = resultado;
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
    this.totalPaginas = Math.ceil(this.veterinariosFiltrados.length / this.itemsPorPagina);
  }

  get veterinariosPaginados(): Veterinario[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.veterinariosFiltrados.slice(inicio, fin);
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
    this.router.navigate(['/veterinarios/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/veterinarios/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/veterinarios/ver', id]);
  }

  verOrdenes(id: number): void {
    this.router.navigate(['/ordenes'], { queryParams: { veterinario: id } });
  }

  eliminar(veterinario: Veterinario): void {
    if (this.notificacionService.confirmarAccion(
      `¿Está seguro de eliminar al veterinario ${veterinario.nombres} ${veterinario.apellidos}?`
    )) {
      this.veterinarioService.eliminar(veterinario.idVeterinario!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Veterinario');
          this.cargarVeterinarios();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar el veterinario');
        }
      });
    }
  }

  getColorEspecialidad(especialidad: string | undefined): string {
    const colores: any = {
      'Cirugía': 'danger',
      'Dermatología': 'warning',
      'Laboratorio': 'info',
      'Cardiología': 'primary',
      'Oftalmología': 'success',
      'Otra': 'secondary'
    };
    return colores[especialidad || 'Otra'] || 'secondary';
  }

  getIniciales(veterinario: Veterinario): string {
    const inicial1 = veterinario.nombres.charAt(0).toUpperCase();
    const inicial2 = veterinario.apellidos.charAt(0).toUpperCase();
    return `${inicial1}${inicial2}`;
  }
}