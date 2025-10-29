import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ResultadoService } from '../../services/resultado.service';
import { NotificacionService } from '../../services/notificacion.service';
import { ResultadoVeterinario } from '../../models/resultado.model';

@Component({
  selector: 'app-resultado-list',
  templateUrl: './resultado-list.component.html',
  styleUrls: ['./resultado-list.component.css']
})
export class ResultadoListComponent implements OnInit {
  resultados: ResultadoVeterinario[] = [];
  resultadosFiltrados: ResultadoVeterinario[] = [];
  loading: boolean = true;
  searchTerm: string = '';
  filtroEstado: string = 'TODOS';
  filtroOrdenId: number | null = null;

  // Filtros de estado
  estados = ['TODOS', 'PENDIENTE', 'VALIDADO', 'ENTREGADO'];

  // Paginación
  paginaActual: number = 1;
  itemsPorPagina: number = 10;
  totalPaginas: number = 0;

  constructor(
    private resultadoService: ResultadoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Verificar si viene filtro por orden
    this.route.queryParams.subscribe(params => {
      if (params['orden']) {
        this.filtroOrdenId = +params['orden'];
      }
    });
    this.cargarResultados();
  }

  cargarResultados(): void {
    this.loading = true;
    
    if (this.filtroOrdenId) {
      this.resultadoService.buscarPorOrden(this.filtroOrdenId).subscribe({
        next: (data) => {
          this.procesarResultados(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    } else {
      this.resultadoService.listarTodos().subscribe({
        next: (data) => {
          this.procesarResultados(data);
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorConexion();
          this.loading = false;
        }
      });
    }
  }

  procesarResultados(data: ResultadoVeterinario[]): void {
    this.resultados = data;
    this.aplicarFiltros();
    this.loading = false;
  }

  aplicarFiltros(): void {
    let resultado = this.resultados;

    // Filtro por estado
    if (this.filtroEstado !== 'TODOS') {
      resultado = resultado.filter(r => {
        if (this.filtroEstado === 'PENDIENTE') return !r.validado;
        if (this.filtroEstado === 'VALIDADO') return r.validado && !r.entregado;
        if (this.filtroEstado === 'ENTREGADO') return r.entregado;
        return true;
      });
    }

    // Filtro por búsqueda
    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      resultado = resultado.filter(r =>
        r.descripcion?.toLowerCase().includes(term) ||
        r.valores?.toLowerCase().includes(term) ||
        r.conclusiones?.toLowerCase().includes(term)
      );
    }

    this.resultadosFiltrados = resultado;
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

  limpiarFiltroOrden(): void {
    this.filtroOrdenId = null;
    this.router.navigate(['/resultados']);
    this.cargarResultados();
  }

  calcularPaginacion(): void {
    this.totalPaginas = Math.ceil(this.resultadosFiltrados.length / this.itemsPorPagina);
  }

  get resultadosPaginados(): ResultadoVeterinario[] {
    const inicio = (this.paginaActual - 1) * this.itemsPorPagina;
    const fin = inicio + this.itemsPorPagina;
    return this.resultadosFiltrados.slice(inicio, fin);
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
    this.router.navigate(['/resultados/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/resultados/editar', id]);
  }

  ver(id: number): void {
    this.router.navigate(['/resultados/ver', id]);
  }

  validar(resultado: ResultadoVeterinario): void {
    if (resultado.validado) {
      this.notificacionService.warning('Este resultado ya está validado');
      return;
    }

    if (this.notificacionService.confirmarAccion('¿Está seguro de validar este resultado?')) {
      this.resultadoService.validar(resultado.idResultado!).subscribe({
        next: () => {
          this.notificacionService.success('Resultado validado exitosamente');
          this.cargarResultados();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo validar el resultado');
        }
      });
    }
  }

  marcarComoEntregado(resultado: ResultadoVeterinario): void {
    if (resultado.entregado) {
      this.notificacionService.warning('Este resultado ya fue entregado');
      return;
    }

    if (!resultado.validado) {
      this.notificacionService.warning('Debe validar el resultado antes de marcarlo como entregado');
      return;
    }

    if (this.notificacionService.confirmarAccion('¿Confirmar entrega del resultado?')) {
      this.resultadoService.marcarComoEntregado(resultado.idResultado!).subscribe({
        next: () => {
          this.notificacionService.success('Resultado marcado como entregado');
          this.cargarResultados();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo marcar como entregado');
        }
      });
    }
  }

  descargarPDF(id: number): void {
    this.resultadoService.descargarPDF(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `resultado_${id}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.notificacionService.success('PDF descargado exitosamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo descargar el PDF');
      }
    });
  }

  eliminar(resultado: ResultadoVeterinario): void {
    if (resultado.validado) {
      this.notificacionService.warning('No se puede eliminar un resultado validado');
      return;
    }

    if (this.notificacionService.confirmarAccion('¿Está seguro de eliminar este resultado?')) {
      this.resultadoService.eliminar(resultado.idResultado!).subscribe({
        next: () => {
          this.notificacionService.exitoEliminar('Resultado');
          this.cargarResultados();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo eliminar el resultado');
        }
      });
    }
  }

  getEstadoBadge(resultado: ResultadoVeterinario): string {
    if (resultado.entregado) return 'success';
    if (resultado.validado) return 'warning';
    return 'secondary';
  }

  getEstadoTexto(resultado: ResultadoVeterinario): string {
    if (resultado.entregado) return 'Entregado';
    if (resultado.validado) return 'Validado';
    return 'Pendiente';
  }

  getEstadoIcono(resultado: ResultadoVeterinario): string {
    if (resultado.entregado) return 'fa-check-double';
    if (resultado.validado) return 'fa-check';
    return 'fa-clock';
  }

  exportarExcel(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }

  exportarPDF(): void {
    this.notificacionService.info('Función de exportación en desarrollo');
  }
}