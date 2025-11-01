import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Pago } from '../../models/pago.model';
import { PagoService } from '../../services/pago.service';

@Component({
  selector: 'app-pago-list',
  templateUrl: './pago-list.component.html',
  styleUrls: ['./pago-list.component.css']
})
export class PagoListComponent implements OnInit {
  pagos: Pago[] = [];
  filtro: string = 'TODOS';
  cargando: boolean = false;
  totalPendiente: number = 0;

  filtros: string[] = ['TODOS', 'PENDIENTE', 'PAGADO', 'VENCIDO', 'PARCIAL'];

  constructor(
    private pagoService: PagoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarPagos();
  }

  cargarPagos(): void {
    this.cargando = true;
    
    if (this.filtro === 'TODOS') {
      this.pagoService.listarTodos().subscribe({
        next: (data) => {
          this.pagos = data;
          this.cargando = false;
        },
        error: (error) => {
          console.error('Error al cargar pagos:', error);
          this.cargando = false;
        }
      });
    } else if (this.filtro === 'PENDIENTE') {
      this.pagoService.obtenerPendientes().subscribe({
        next: (data) => {
          this.pagos = data;
          this.cargando = false;
        },
        error: (error) => {
          console.error('Error al cargar pagos pendientes:', error);
          this.cargando = false;
        }
      });
    } else if (this.filtro === 'VENCIDO') {
      this.pagoService.obtenerVencidos().subscribe({
        next: (data) => {
          this.pagos = data;
          this.cargando = false;
        },
        error: (error) => {
          console.error('Error al cargar pagos vencidos:', error);
          this.cargando = false;
        }
      });
    } else {
      this.pagoService.listarTodos().subscribe({
        next: (data) => {
          this.pagos = data.filter(p => p.estado === this.filtro);
          this.cargando = false;
        },
        error: (error) => {
          console.error('Error al cargar pagos:', error);
          this.cargando = false;
        }
      });
    }
  }

  cambiarFiltro(nuevoFiltro: string): void {
    this.filtro = nuevoFiltro;
    this.cargarPagos();
  }

  nuevo(): void {
    this.router.navigate(['/pagos/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/pagos/editar', id]);
  }

  registrarPago(pago: Pago): void {
    const montoPendiente = pago.monto - (pago.montoPagado || 0);
    const monto = prompt(`Ingrese el monto a pagar (Pendiente: S/ ${montoPendiente.toFixed(2)}):`);
    
    if (monto) {
      const montoPagado = parseFloat(monto);
      if (isNaN(montoPagado) || montoPagado <= 0) {
        alert('Monto inválido');
        return;
      }

      const metodoPago = prompt('Método de pago (EFECTIVO/TARJETA/TRANSFERENCIA):');
      if (!metodoPago) return;

      this.pagoService.registrarPago(pago.idPago!, montoPagado, metodoPago.toUpperCase()).subscribe({
        next: () => {
          alert('Pago registrado exitosamente');
          this.cargarPagos();
        },
        error: (error) => {
          console.error('Error al registrar pago:', error);
          alert('Error al registrar el pago');
        }
      });
    }
  }

  eliminar(id: number): void {
    if (confirm('¿Está seguro de eliminar este pago?')) {
      this.pagoService.eliminar(id).subscribe({
        next: () => {
          alert('Pago eliminado exitosamente');
          this.cargarPagos();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          alert('Error al eliminar el pago');
        }
      });
    }
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado) {
      case 'PAGADO': return 'bg-success';
      case 'PENDIENTE': return 'bg-warning';
      case 'VENCIDO': return 'bg-danger';
      case 'PARCIAL': return 'bg-info';
      default: return 'bg-secondary';
    }
  }

  esVencido(pago: Pago): boolean {
    if (!pago.fechaVencimiento) return false;
    const hoy = new Date();
    const vencimiento = new Date(pago.fechaVencimiento);
    return vencimiento < hoy && pago.estado !== 'PAGADO';
  }
}