import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DuenoService } from '../../services/dueno.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Dueno } from '../../models/dueno.model';

@Component({
  selector: 'app-dueno-form',
  templateUrl: './dueno-form.component.html',
  styleUrls: ['./dueno-form.component.css'],
})
export class DuenoFormComponent implements OnInit {
  duenoForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  duenoId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  constructor(
    private fb: FormBuilder,
    private duenoService: DuenoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.duenoForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      email: ['', [Validators.email, Validators.maxLength(100)]],
      direccion: ['', [Validators.maxLength(200)]]
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.duenoId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarDueno(this.duenoId);
      }
    });

    if (this.modoVista) {
      this.duenoForm.disable();
    }
  }

  cargarDueno(id: number): void {
    this.loading = true;
    this.duenoService.obtenerPorId(id).subscribe({
      next: (dueno) => {
        this.duenoForm.patchValue(dueno);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar dueño:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el dueño');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.duenoForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Por favor complete todos los campos requeridos');
      return;
    }

    this.guardando = true;
    const dueno: Dueno = this.duenoForm.value;

    const operacion = this.modoEdicion
      ? this.duenoService.actualizar(this.duenoId!, dueno)
      : this.duenoService.crear(dueno);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'Dueño actualizado' : 'Dueño creado';
        this.notificacionService.success(mensaje + ' exitosamente');
        this.volver();
      },
      error: (error) => {
        console.error('Error al guardar:', error);
        this.notificacionService.errorGeneral('No se pudo guardar el dueño');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.duenoForm.controls).forEach(key => {
      this.duenoForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/duenos']);
  }

  editar(): void {
    this.router.navigate(['/duenos/editar', this.duenoId]);
  }

  validarDNI(): void {
    const dni = this.duenoForm.get('dni')?.value;
    if (dni && dni.length === 8) {
      // Aquí podrías agregar validación contra API de RENIEC
      console.log('Validando DNI:', dni);
    }
  }

  // Getters para validaciones
  get dni() { return this.duenoForm.get('dni'); }
  get nombres() { return this.duenoForm.get('nombres'); }
  get apellidos() { return this.duenoForm.get('apellidos'); }
  get telefono() { return this.duenoForm.get('telefono'); }
  get email() { return this.duenoForm.get('email'); }
  get direccion() { return this.duenoForm.get('direccion'); }
}