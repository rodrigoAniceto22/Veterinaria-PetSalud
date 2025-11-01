import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Cita } from '../../models/cita.model';
import { CitaService } from '../../services/cita.service';
import { MascotaService } from '../../services/mascota.service';
import { Mascota } from '../../models/mascota.model';

@Component({
  selector: 'app-cita-form',
  templateUrl: './cita-form.component.html',
  styleUrls: ['./cita-form.component.css']

})
export class CitaFormComponent implements OnInit {
  cita: Cita = {
    mascota: { idMascota: 0 },
    fechaHora: new Date(),
    motivo: '',
    estado: 'PROGRAMADA',
    duracionMinutos: 30
  };

  mascotas: Mascota[] = [];
  esEdicion: boolean = false;
  enviando: boolean = false;

  tiposCita: string[] = ['CONSULTA', 'VACUNACION', 'CIRUGIA', 'CONTROL', 'EMERGENCIA', 'OTRO'];
  estados: string[] = ['PROGRAMADA', 'CONFIRMADA', 'COMPLETADA', 'CANCELADA', 'NO_ASISTIO'];
  duraciones: number[] = [15, 30, 45, 60, 90, 120];

  constructor(
    private citaService: CitaService,
    private mascotaService: MascotaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarMascotas();
    
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.esEdicion = true;
      this.cargarCita(id);
    }
  }

  cargarMascotas(): void {
    this.mascotaService.listarTodas().subscribe({
      next: (data) => {
        this.mascotas = data;
      },
      error: (error) => {
        console.error('Error al cargar mascotas:', error);
      }
    });
  }

  cargarCita(id: number): void {
    this.citaService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.cita = data;
      },
      error: (error) => {
        console.error('Error al cargar cita:', error);
        alert('Error al cargar la cita');
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
      this.citaService.actualizar(this.cita.idCita!, this.cita).subscribe({
        next: () => {
          alert('Cita actualizada exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al actualizar:', error);
          alert('Error al actualizar la cita');
          this.enviando = false;
        }
      });
    } else {
      this.citaService.crear(this.cita).subscribe({
        next: () => {
          alert('Cita creada exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al crear:', error);
          alert('Error al crear la cita');
          this.enviando = false;
        }
      });
    }
  }

  validar(): boolean {
    if (!this.cita.mascota.idMascota || this.cita.mascota.idMascota === 0) {
      alert('Debe seleccionar una mascota');
      return false;
    }

    if (!this.cita.motivo || this.cita.motivo.trim() === '') {
      alert('El motivo es obligatorio');
      return false;
    }

    if (!this.cita.fechaHora) {
      alert('La fecha y hora son obligatorias');
      return false;
    }

    return true;
  }

  volver(): void {
    this.router.navigate(['/citas']);
  }
}