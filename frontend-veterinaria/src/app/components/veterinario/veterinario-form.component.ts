import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { VeterinarioService } from '../../services/veterinario.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Veterinario } from '../../models/veterinario.model';

@Component({
  selector: 'app-veterinario-form',
  templateUrl: './veterinario-form.component.html',
  styleUrls: ['./veterinario-form.component.css']
})
export class VeterinarioFormComponent implements OnInit {
  veterinarioForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  veterinarioId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  especialidades = [
    'Cirugía',
    'Dermatología',
    'Laboratorio',
    'Cardiología',
    'Oftalmología',
    'Neurología',
    'Oncología',
    'Medicina Interna',
    'Otra'
  ];

  constructor(
    private fb: FormBuilder,
    private veterinarioService: VeterinarioService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.veterinarioForm = this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      especialidad: ['', [Validators.maxLength(100)]],
      telefono: ['', [Validators.pattern(/^\d{9}$/)]],
      email: ['', [Validators.email, Validators.maxLength(100)]],
      colegiatura: ['', [Validators.required, Validators.maxLength(50)]],
      activo: [true]
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.veterinarioId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarVeterinario(this.veterinarioId);
      }
    });

    if (this.modoVista) {
      this.veterinarioForm.disable();
    }
  }

  cargarVeterinario(id: number): void {
    this.loading = true;
    this.veterinarioService.obtenerPorId(id).subscribe({
      next: (veterinario) => {
        this.veterinarioForm.patchValue(veterinario);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el veterinario');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.veterinarioForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Complete todos los campos requeridos');
      return;
    }

    this.guardando = true;
    const veterinario: Veterinario = this.veterinarioForm.value;

    const operacion = this.modoEdicion
      ? this.veterinarioService.actualizar(this.veterinarioId!, veterinario)
      : this.veterinarioService.crear(veterinario);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'actualizado' : 'creado';
        this.notificacionService.success(`Veterinario ${mensaje} exitosamente`);
        this.volver();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo guardar el veterinario');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.veterinarioForm.controls).forEach(key => {
      this.veterinarioForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/veterinarios']);
  }

  editar(): void {
    this.router.navigate(['/veterinarios/editar', this.veterinarioId]);
  }

  // Getters
  get nombres() { return this.veterinarioForm.get('nombres'); }
  get apellidos() { return this.veterinarioForm.get('apellidos'); }
  get especialidad() { return this.veterinarioForm.get('especialidad'); }
  get telefono() { return this.veterinarioForm.get('telefono'); }
  get email() { return this.veterinarioForm.get('email'); }
  get colegiatura() { return this.veterinarioForm.get('colegiatura'); }
  get activo() { return this.veterinarioForm.get('activo'); }
}