import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Cita, CitaAlertas } from '../../models/cita.model';
import { CitaService } from '../../services/cita.service';

@Component({
  selector: 'app-cita-list',
  templateUrl: './cita-list.component.html',
  styleUrls: ['./cita-list.component.css']
})
export class CitaListComponent implements OnInit {
  citas: Cita[] = [];
  alertas: CitaAlertas | null = null;
  filtro: string = 'TODAS';
  cargando: boolean = false;
  mostrarAlertas: boolean = false;

  filtros: string[] = ['TODAS', 'PROGRAMADA', 'CONFIRMADA', 'COMPLETADA', 'CANCELADA', 'HOY', 'PROXIMAS'];

  constructor(
    private citaService: CitaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCitas();
    this.cargarAlertas();
  }

  cargarCitas(): void {
    this.cargando = true;

    switch (this.filtro) {
      case 'TODAS':
        this.citaService.listarTodas().subscribe({
          next: (data) => {
            this.citas = data;
            this.cargando = false;
          },
          error: (error) => {
            console.error('Error al cargar citas:', error);
            this.cargando = false;
          }
        });
        break;

      case 'HOY':
        this.citaService.obtenerCitasDelDia().subscribe({
          next: (data) => {
            this.citas = data;
            this.cargando = false;
          },
          error: (error) => {
            console.error('Error al cargar citas del día:', error);
            this.cargando = false;
          }
        });
        break;

      case 'PROXIMAS':
        this.citaService.obtenerCitasProximas().subscribe({
          next: (data) => {
            this.citas = data;
            this.cargando = false;
          },
          error: (error) => {
            console.error('Error al cargar citas próximas:', error);
            this.cargando = false;
          }
        });
        break;

      default:
        this.citaService.buscarPorEstado(this.filtro).subscribe({
          next: (data) => {
            this.citas = data;
            this.cargando = false;
          },
          error: (error) => {
            console.error('Error al cargar citas:', error);
            this.cargando = false;
          }
        });
    }
  }

  cargarAlertas(): void {
    this.citaService.obtenerDashboardAlertas().subscribe({
      next: (data) => {
        this.alertas = data;
      },
      error: (error) => {
        console.error('Error al cargar alertas:', error);
      }
    });
  }

  cambiarFiltro(nuevoFiltro: string): void {
    this.filtro = nuevoFiltro;
    this.cargarCitas();
  }

  toggleAlertas(): void {
    this.mostrarAlertas = !this.mostrarAlertas;
  }

  nuevo(): void {
    this.router.navigate(['/citas/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/citas/editar', id]);
  }

  confirmar(id: number): void {
    if (confirm('¿Confirmar esta cita?')) {
      this.citaService.confirmar(id).subscribe({
        next: () => {
          alert('Cita confirmada exitosamente');
          this.cargarCitas();
          this.cargarAlertas();
        },
        error: (error) => {
          console.error('Error al confirmar:', error);
          alert('Error al confirmar la cita');
        }
      });
    }
  }

  cancelar(id: number): void {
    const motivo = prompt('Motivo de cancelación:');
    if (motivo) {
      this.citaService.cancelar(id, motivo).subscribe({
        next: () => {
          alert('Cita cancelada exitosamente');
          this.cargarCitas();
          this.cargarAlertas();
        },
        error: (error) => {
          console.error('Error al cancelar:', error);
          alert('Error al cancelar la cita');
        }
      });
    }
  }

  completar(id: number): void {
    if (confirm('¿Marcar esta cita como completada?')) {
      this.citaService.completar(id).subscribe({
        next: () => {
          alert('Cita completada exitosamente');
          this.cargarCitas();
        },
        error: (error) => {
          console.error('Error al completar:', error);
          alert('Error al completar la cita');
        }
      });
    }
  }

  eliminar(id: number): void {
    if (confirm('¿Está seguro de eliminar esta cita?')) {
      this.citaService.eliminar(id).subscribe({
        next: () => {
          alert('Cita eliminada exitosamente');
          this.cargarCitas();
          this.cargarAlertas();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          alert('Error al eliminar la cita');
        }
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado) {
      case 'PROGRAMADA': return 'bg-info';
      case 'CONFIRMADA': return 'bg-primary';
      case 'COMPLETADA': return 'bg-success';
      case 'CANCELADA': return 'bg-secondary';
      case 'NO_ASISTIO': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  getAlertaBadgeClass(nivel: string): string {
    switch (nivel) {
      case 'CRITICA': return 'bg-danger';
      case 'ALTA': return 'bg-warning';
      case 'MEDIA': return 'bg-info';
      default: return 'bg-secondary';
    }
  }

  getTotalAlertas(): number {
    if (!this.alertas) return 0;
    return (this.alertas.citasCriticas?.length || 0) +
           (this.alertas.citasProximas?.length || 0) +
           (this.alertas.citasPendientesConfirmacion?.length || 0);
  }
}