// ===================================================
// src/app/components/toma-muestra/toma-muestra-list.component.ts
// ===================================================
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TomaMuestraService } from '../../services/toma-muestra.service';
import { TomaMuestra } from '../../models/toma-muestra.model';

@Component({
  selector: 'app-toma-muestra-list',
  templateUrl: './toma-muestra-list.component.html',
  styleUrls: ['./toma-muestra-list.component.css']
})
export class TomaMuestraListComponent implements OnInit {
  tomaMuestras: TomaMuestra[] = [];
  loading = false;

  constructor(
    private tomaMuestraService: TomaMuestraService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarTomaMuestras();
  }

  cargarTomaMuestras(): void {
    this.loading = true;
    this.tomaMuestraService.getAll().subscribe({
      next: (data) => {
        this.tomaMuestras = data;
        this.loading = false;
      },
      error: (err) => {
        alert('❌ Error al cargar tomas de muestra');
        this.loading = false;
        console.error(err);
      }
    });
  }

  eliminar(id: number): void {
    if (confirm('¿Eliminar esta toma de muestra?')) {
      this.tomaMuestraService.delete(id).subscribe({
        next: () => {
          alert('✅ Toma de muestra eliminada');
          this.cargarTomaMuestras();
        },
        error: () => alert('❌ Error al eliminar')
      });
    }
  }

  nueva(): void {
    this.router.navigate(['/toma-muestras/nueva']);
  }

  getEstadoClass(estado: string | undefined): string {
    switch(estado) {
      case 'PROGRAMADA': return 'badge-programada';
      case 'REALIZADA': return 'badge-realizada';
      case 'COMPLETADA': return 'badge-completada';
      default: return '';
    }
  }
}