import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FacturaService } from '../../services/factura.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Factura } from '../../models/factura.model';

@Component({
  selector: 'app-factura-list',
  templateUrl: './factura-list.component.html',
  styleUrls: ['./factura-list.component.css']
})
export class FacturaListComponent implements OnInit {
  facturas: Factura[] = [];
  facturasFiltradas: Factura[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEstado: string = 'TODAS';

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  // Estadísticas
  totalFacturado: number = 0;
  totalPagado: number = 0;
  totalPendiente: number = 0;

  constructor(
    private facturaService: FacturaService,
    private notificacionService: NotificacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarFacturas();
  }

  cargarFacturas(): void {
    this.loading = true;
    this.facturaService.listarTodas().subscribe({
      next: (data) => {
        this.facturas = data;
        this.aplicarFiltros();
        this.calcularEstadisticas();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar facturas:', error);
        this.notificacionService.errorConexion();
        this.loading = false;
      }
    });
  }

  aplicarFiltros(): void {
    let resultado = this.facturas;

    // Filtro por estado
    if (this.filtroEstado !== 'TODAS') {
      resultado = resultado.filter(f => f.estado === this.filtroEstado);
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(f =>
        f.numeroFactura.toLowerCase().includes(term) ||
        (f.dueno && (
          f.dueno.nombres?.toLowerCase().includes(term) ||
          f.dueno.apellidos?.toLowerCase().includes(term) ||
          f.dueno.dni?.toLowerCase().includes(term)
        ))
      );
    }

    this.facturasFiltradas = resultado;
    this.calcularPaginacion();
    this.paginaActual = 1;
  }

  calcularEstadisticas(): void {
    this.totalFacturado = this.facturas.reduce((sum, f) => sum + (f.total || 0), 0);
    this.totalPagado = this.facturas
      .filter(f => f.estado === 'PAGADA')
      .reduce((sum, f) => sum + (f.total || 0), 0);
    this.totalPendiente = this.facturas
      .filter(f => f.estado === 'PENDIENTE')
      .reduce((sum, f) => sum + (f.total || 0), 0);
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

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.facturasFiltradas.length / this.itemsPorPagina);
  }

  get facturasPaginadas(): Factura[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.facturasFiltradas.slice(inicio, fin);
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
    this.router.navigate(['/facturas/nueva']);
  }

  ver(id: number): void {
    this.router.navigate(['/facturas/ver', id]);
  }

  editar(id: number): void {
    this.router.navigate(['/facturas/editar', id]);
  }

  marcarComoPagada(factura: Factura): void {
    if (this.notificacionService.confirmarAccion(`¿Marcar factura ${factura.numeroFactura} como pagada?`)) {
      this.facturaService.marcarComoPagada(factura.idFactura!).subscribe({
        next: () => {
          this.notificacionService.success('Factura marcada como pagada');
          this.cargarFacturas();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral();
        }
      });
    }
  }

  eliminar(factura: Factura): void {
    if (this.notificacionService.confirmarAccion(`¿Está seguro de eliminar la factura ${factura.numeroFactura}?`)) {
      this.facturaService.eliminar(factura.idFactura!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Factura');
          this.cargarFacturas();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral();
        }
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    const badges: any = {
      'PENDIENTE': 'bg-warning',
      'PAGADA': 'bg-success',
      'ANULADA': 'bg-danger'
    };
    return badges[estado] || 'bg-secondary';
  }

  imprimirFactura(id: number): void {
    // TODO: Implementar impresión
    this.notificacionService.info('Función de impresión en desarrollo');
  }

  exportarExcel(): void {
    this.notificacionService.info('Exportación a Excel en desarrollo');
  }
}