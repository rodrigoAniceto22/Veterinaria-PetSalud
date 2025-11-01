import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Inventario } from '../../models/inventario.model';
import { InventarioService } from '../../services/inventario.service';

@Component({
  selector: 'app-inventario-form',
  templateUrl: './inventario-form.component.html',
  styleUrls: ['./inventario-form.component.css']
})
export class InventarioFormComponent implements OnInit {
  inventario: Inventario = {
    codigo: '',
    nombre: '',
    descripcion: '',
    categoria: '',
    precioCompra: 0,
    precioVenta: 0,
    stockActual: 0,
    stockMinimo: 10,
    stockMaximo: 100,
    unidadMedida: 'Unidad',
    proveedor: '',
    activo: true,
    observaciones: ''
  };

  esEdicion: boolean = false;
  enviando: boolean = false;

  categorias: string[] = [
    'Medicamentos',
    'Vacunas',
    'Alimentos',
    'Accesorios',
    'Higiene',
    'Instrumental',
    'Otros'
  ];

  unidades: string[] = [
    'Unidad',
    'Caja',
    'Frasco',
    'Bolsa',
    'Kg',
    'Litro',
    'ml'
  ];

  constructor(
    private inventarioService: InventarioService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.esEdicion = true;
      this.cargarInventario(id);
    }
  }

  cargarInventario(id: number): void {
    this.inventarioService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.inventario = data;
      },
      error: (error) => {
        console.error('Error al cargar producto:', error);
        alert('Error al cargar el producto');
        this.volver();
      }
    });
  }

  guardar(): void {
    if (!this.validar()) {
      return;
    }

    this.enviando = true;

    if (this.esEdicion) {
      this.inventarioService.actualizar(this.inventario.idInventario!, this.inventario).subscribe({
        next: () => {
          alert('Producto actualizado exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al actualizar:', error);
          alert('Error al actualizar el producto');
          this.enviando = false;
        }
      });
    } else {
      this.inventarioService.crear(this.inventario).subscribe({
        next: () => {
          alert('Producto creado exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al crear:', error);
          alert('Error al crear el producto');
          this.enviando = false;
        }
      });
    }
  }

  validar(): boolean {
    if (!this.inventario.codigo || !this.inventario.nombre) {
      alert('Código y nombre son obligatorios');
      return false;
    }

    if (this.inventario.precioCompra < 0 || this.inventario.precioVenta < 0) {
      alert('Los precios no pueden ser negativos');
      return false;
    }

    if (this.inventario.stockActual < 0) {
      alert('El stock no puede ser negativo');
      return false;
    }

    return true;
  }

  volver(): void {
    this.router.navigate(['/inventario']);
  }
}