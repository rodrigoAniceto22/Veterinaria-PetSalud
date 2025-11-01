import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Inventario, InventarioAlertas } from '../../models/inventario.model';
import { InventarioService } from '../../services/inventario.service';

@Component({
  selector: 'app-inventario-list',
  templateUrl: './inventario-list.component.html',
  styleUrls: ['./inventario-list.component.css']
})
export class InventarioListComponent implements OnInit {
  inventarios: Inventario[] = [];
  alertas: InventarioAlertas | null = null;
  filtro: string = '';
  cargando: boolean = false;
  mostrarAlertas: boolean = false;

  constructor(
    private inventarioService: InventarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarInventarios();
    this.cargarAlertas();
  }

  cargarInventarios(): void {
    this.cargando = true;
    this.inventarioService.listarActivos().subscribe({
      next: (data) => {
        this.inventarios = data;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar inventarios:', error);
        this.cargando = false;
      }
    });
  }

  cargarAlertas(): void {
    this.inventarioService.obtenerAlertas().subscribe({
      next: (data) => {
        this.alertas = data;
      },
      error: (error) => {
        console.error('Error al cargar alertas:', error);
      }
    });
  }

  buscar(): void {
    if (this.filtro.trim()) {
      this.inventarioService.buscarPorNombre(this.filtro).subscribe({
        next: (data) => {
          this.inventarios = data;
        },
        error: (error) => {
          console.error('Error en búsqueda:', error);
        }
      });
    } else {
      this.cargarInventarios();
    }
  }

  nuevo(): void {
    this.router.navigate(['/inventario/nuevo']);
  }

  editar(id: number): void {
    this.router.navigate(['/inventario/editar', id]);
  }

  eliminar(id: number): void {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.inventarioService.eliminar(id).subscribe({
        next: () => {
          alert('Producto eliminado exitosamente');
          this.cargarInventarios();
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          alert('Error al eliminar el producto');
        }
      });
    }
  }

  toggleAlertas(): void {
    this.mostrarAlertas = !this.mostrarAlertas;
  }

  getTotalAlertas(): number {
    if (!this.alertas) return 0;
    return (this.alertas.productosStockBajo?.length || 0) +
           (this.alertas.productosVencidos?.length || 0) +
           (this.alertas.productosProximosAVencer?.length || 0);
  }
}