import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';
import { DuenoService } from '../../services/dueno.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Mascota } from '../../models/mascota.model';
import { Dueno } from '../../models/dueno.model';

@Component({
  selector: 'app-mascota-list',
  templateUrl: './mascota-list.component.html',
  styleUrls: ['./mascota-list.component.css']
})
export class MascotaListComponent implements OnInit {
  mascotas: Mascota[] = [];
  mascotasFiltradas: Mascota[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEspecie: string = 'TODAS';
  filtroDuenoId: number | null = null;
  duenoSeleccionado: Dueno | null = null;

  // Especies comunes
  especies = ['TODAS', 'Perro', 'Gato', 'Ave', 'Conejo', 'Hamster', 'Otro'];

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private mascotaService: MascotaService,
    private duenoService: DuenoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Verificar si viene filtro por dueño
    this.route.queryParams.subscribe(params => {
      if (params['dueno']) {
        this.filtroDuenoId = +params['dueno'];
        this.cargarDuenoSeleccionado(this.filtroDuenoId);
      }
    });
    this.cargarMascotas();
  }

  cargarDuenoSeleccionado(id: number): void {
    this.duenoService.obtenerPorId(id).subscribe({
      next: (dueno) => {
        this.duenoSeleccionado = dueno;
      },
      error: (error) => {
        console.error('Error al cargar dueño:', error);
      }
    });
  }

  cargarMascotas(): void {
    this.loading = true;
    
    if (this.filtroDuenoId) {
      this.mascotaService.buscarPorDueno(this.filtroDuenoId).subscribe({
        next: (data) => {
          this.procesarMascotas(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    } else {
      this.mascotaService.listarTodas().subscribe({
        next: (data) => {
          this.procesarMascotas(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    }
  }

  procesarMascotas(data: Mascota[]): void {
    this.mascotas = data;
    this.aplicarFiltros();
    this.loading = false;
  }

  aplicarFiltros(): void {
    let resultado = this.mascotas;

    // Filtro por especie
    if (this.filtroEspecie !== 'TODAS') {
      resultado = resultado.filter(m => m.especie === this.filtroEspecie);
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(m =>
        m.nombre.toLowerCase().includes(term) ||
        (m.raza && m.raza.toLowerCase().includes(term)) ||
        (m.dueno && (
          m.dueno.nombres?.toLowerCase().includes(term) ||
          m.dueno.apellidos?.toLowerCase().includes(term)
        ))
      );
    }

    this.mascotasFiltradas = resultado;
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  cambiarFiltroEspecie(especie: string): void {
    this.filtroEspecie = especie;
    this.aplicarFiltros();
  }

  buscar(): void {
    this.aplicarFiltros();
  }

  limpiarBusqueda(): void {
    this.searchTerm = '';
    this.aplicarFiltros();
  }

  limpiarFiltroDueno(): void {
    this.filtroDuenoId = null;
    this.duenoSeleccionado = null;
    this.router.navigate(['/mascotas']);
    this.cargarMascotas();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.mascotasFiltradas.length / this.itemsPorPagina);
  }

  get mascotasPaginadas(): Mascota[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.mascotasFiltradas.slice(inicio, fin);
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
    this.router.navigate(['/mascotas/nueva']);
  }

  editar(id: number): void {
    this.router.navigate(['/mascotas/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/mascotas/ver', id]);
  }

  verOrdenes(id: number): void {
    this.router.navigate(['/ordenes'], { queryParams: { mascota: id } });
  }

  eliminar(mascota: Mascota): void {
    if (this.notificacionService.confirmarAccion(`¿Está seguro de eliminar a ${mascota.nombre}?`)) {
      this.mascotaService.eliminar(mascota.idMascota!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Mascota');
          this.cargarMascotas();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar la mascota');
        }
      });
    }
  }

  getIconoEspecie(especie: string): string {
    const iconos: any = {
      'Perro': 'fa-dog',
      'Gato': 'fa-cat',
      'Ave': 'fa-dove',
      'Conejo': 'fa-rabbit',
      'Hamster': 'fa-otter',
      'Otro': 'fa-paw'
    };
    return iconos[especie] || 'fa-paw';
  }

  getColorEspecie(especie: string): string {
    const colores: any = {
      'Perro': 'primary',
      'Gato': 'warning',
      'Ave': 'info',
      'Conejo': 'success',
      'Hamster': 'secondary',
      'Otro': 'dark'
    };
    return colores[especie] || 'secondary';
  }
}