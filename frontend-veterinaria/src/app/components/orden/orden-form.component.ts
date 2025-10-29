// ===================================================
// src/app/components/orden/orden-form.component.ts
// ===================================================
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrdenService } from '../../services/orden.service';
import { MascotaService } from '../../services/mascota.service';
import { VeterinarioService } from '../../services/veterinario.service';
import { Mascota } from '../../models/mascota.model';
import { Veterinario } from '../../models/veterinario.model';

@Component({
  selector: 'app-orden-form',
  templateUrl: './orden-form.component.html',
  styleUrls: ['./orden-form.component.css']
})
export class OrdenFormComponent implements OnInit {
  ordenForm: FormGroup;
  mascotas: Mascota[] = [];
  veterinarios: Veterinario[] = [];
  loading = false;

  tiposExamen = [
    'HEMOGRAMA',
    'BIOQUIMICA',
    'URIANALISIS',
    'COPROPARASITOLOGICO',
    'PERFIL_TIROIDEO',
    'PERFIL_COMPLETO',
    'CULTIVO',
    'RADIOGRAFIA',
    'ECOGRAFIA',
    'ELECTROCARDIOGRAMA'
  ];

  prioridades = ['NORMAL', 'ALTA', 'URGENTE'];
  estados = ['PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA'];

  constructor(
    private fb: FormBuilder,
    private ordenService: OrdenService,
    private mascotaService: MascotaService,
    private veterinarioService: VeterinarioService,
    private router: Router
  ) {
    this.ordenForm = this.fb.group({
      idMascota: ['', Validators.required],
      idVeterinario: ['', Validators.required],
      tipoExamen: ['', Validators.required],
      prioridad: ['NORMAL', Validators.required],
      estado: ['PENDIENTE'],
      sintomas: [''],
      diagnosticoPresuntivo: [''],
      observaciones: [''],
      fechaOrden: [new Date().toISOString().split('T')[0], Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarMascotas();
    this.cargarVeterinarios();
  }

  cargarMascotas(): void {
    this.mascotaService.getAll().subscribe({
      next: (data) => this.mascotas = data,
      error: (err) => console.error('Error al cargar mascotas', err)
    });
  }

  cargarVeterinarios(): void {
    this.veterinarioService.getDisponibles().subscribe({
      next: (data) => this.veterinarios = data,
      error: (err) => {
        console.error('Error al cargar veterinarios', err);
        // Fallback: intentar obtener todos
        this.veterinarioService.getAll().subscribe({
          next: (all) => this.veterinarios = all,
          error: () => console.error('No se pudieron cargar veterinarios')
        });
      }
    });
  }

  onSubmit(): void {
    if (this.ordenForm.invalid) {
      this.ordenForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    const orden = this.ordenForm.value;

    this.ordenService.create(orden).subscribe({
      next: () => {
        alert('✅ Orden creada exitosamente');
        this.router.navigate(['/ordenes']);
      },
      error: (err) => {
        alert('❌ Error al crear orden');
        console.error(err);
        this.loading = false;
      }
    });
  }

  volver(): void {
    this.router.navigate(['/ordenes']);
  }
}
