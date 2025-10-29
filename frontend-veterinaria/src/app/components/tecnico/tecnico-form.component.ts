import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TecnicoService } from '../../services/tecnico.service';
import { NotificacionService } from '../../services/notificacion.service';
import { TecnicoVeterinario } from '../../models/tecnico.model';

@Component({
  selector: 'app-tecnico-form',
  templateUrl: './tecnico-form.component.html',
  styleUrls: ['./tecnico-form.component.css']
})
export class TecnicoFormComponent implements OnInit {
  tecnicoForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  tecnicoId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  especialidades = [
    'Hematología',
    'Microbiología',
    'Química Clínica',
    'Parasitología',
    'Urianálisis',
    'Histopatología',
    'Inmunología',
    'Serología',
    'Otro'
  ];

  certificaciones = [
    'Técnico Laboratorista Clínico',
    'Técnico en Análisis Clínicos',
    'Técnico Veterinario',
    'Auxiliar de Laboratorio',
    'Técnico en Microbiología',
    'Otro'
  ];

  constructor(
    private fb: FormBuilder,
    private tecnicoService: TecnicoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.tecnicoForm = this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      especialidad: ['', [Validators.required]],
      certificacion: [''],
      telefono: ['', [Validators.pattern(/^\d{9}$/)]],
      email: ['', [Validators.email, Validators.maxLength(100)]],
      activo: [true]
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.tecnicoId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarTecnico(this.tecnicoId);
      }
    });

    if (this.modoVista) {
      this.tecnicoForm.disable();
    }
  }

  cargarTecnico(id: number): void {
    this.loading = true;
    this.tecnicoService.obtenerPorId(id).subscribe({
      next: (tecnico) => {
        this.tecnicoForm.patchValue(tecnico);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar técnico:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el técnico');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.tecnicoForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Por favor complete todos los campos requeridos');
      return;
    }

    this.guardando = true;
    const tecnico: TecnicoVeterinario = this.tecnicoForm.value;

    const operacion = this.modoEdicion
      ? this.tecnicoService.actualizar(this.tecnicoId!, tecnico)
      : this.tecnicoService.crear(tecnico);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'Técnico actualizado' : 'Técnico creado';
        this.notificacionService.success(mensaje + ' exitosamente');
        this.volver();
      },
      error: (error) => {
        console.error('Error al guardar:', error);
        this.notificacionService.errorGeneral('No se pudo guardar el técnico');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.tecnicoForm.controls).forEach(key => {
      this.tecnicoForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/tecnicos']);
  }

  editar(): void {
    this.router.navigate(['/tecnicos/editar', this.tecnicoId]);
  }

  // Getters para validaciones
  get nombres() { return this.tecnicoForm.get('nombres'); }
  get apellidos() { return this.tecnicoForm.get('apellidos'); }
  get especialidad() { return this.tecnicoForm.get('especialidad'); }
  get certificacion() { return this.tecnicoForm.get('certificacion'); }
  get telefono() { return this.tecnicoForm.get('telefono'); }
  get email() { return this.tecnicoForm.get('email'); }
  get activo() { return this.tecnicoForm.get('activo'); }
}