import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';
import { DuenoService } from '../../services/dueno.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Mascota } from '../../models/mascota.model';
import { Dueno } from '../../models/dueno.model';

@Component({
  selector: 'app-mascota-form',
  templateUrl: './mascota-form.component.html',
  styleUrls: ['./mascota-form.component.css']
})
export class MascotaFormComponent implements OnInit {
  mascotaForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  mascotaId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  duenos: Dueno[] = [];
  especies = ['Perro', 'Gato', 'Ave', 'Conejo', 'Hamster', 'Reptil', 'Otro'];
  sexos = ['Macho', 'Hembra'];
  
  // Razas por especie
  razasPorEspecie: any = {
    'Perro': ['Labrador', 'Golden Retriever', 'Pastor Alemán', 'Bulldog', 'Chihuahua', 'Husky', 'Beagle', 'Mestizo', 'Otro'],
    'Gato': ['Persa', 'Siamés', 'Angora', 'Bengalí', 'Maine Coon', 'Mestizo', 'Otro'],
    'Ave': ['Canario', 'Periquito', 'Loro', 'Cotorra', 'Cacatúa', 'Otro'],
    'Conejo': ['Mini Lop', 'Holandés', 'Angora', 'Cabeza de León', 'Otro'],
    'Hamster': ['Sirio', 'Ruso', 'Chino', 'Roborovski', 'Otro'],
    'Reptil': ['Iguana', 'Tortuga', 'Gecko', 'Serpiente', 'Otro'],
    'Otro': ['Otro']
  };
  
  razasDisponibles: string[] = [];

  constructor(
    private fb: FormBuilder,
    private mascotaService: MascotaService,
    private duenoService: DuenoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarDuenos();
    this.inicializarFormulario();
    this.verificarModo();
    this.configurarCambioEspecie();
  }

  inicializarFormulario(): void {
    this.mascotaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      especie: ['', [Validators.required]],
      raza: [''],
      edad: [null, [Validators.min(0), Validators.max(30)]],
      sexo: [''],
      peso: [null, [Validators.min(0.1), Validators.max(500)]],
      color: ['', [Validators.maxLength(50)]],
      observaciones: [''],
      dueno: [null, [Validators.required]]
    });
  }

  configurarCambioEspecie(): void {
    this.mascotaForm.get('especie')?.valueChanges.subscribe(especie => {
      this.razasDisponibles = this.razasPorEspecie[especie] || [];
      this.mascotaForm.patchValue({ raza: '' });
    });
  }

  cargarDuenos(): void {
    this.duenoService.listarTodos().subscribe({
      next: (data) => {
        this.duenos = data;
      },
      error: (error) => {
        console.error('Error al cargar dueños:', error);
        this.notificacionService.errorGeneral('No se pudieron cargar los dueños');
      }
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.mascotaId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarMascota(this.mascotaId);
      }
    });

    if (this.modoVista) {
      this.mascotaForm.disable();
    }
  }

  cargarMascota(id: number): void {
    this.loading = true;
    this.mascotaService.obtenerPorId(id).subscribe({
      next: (mascota) => {
        this.razasDisponibles = this.razasPorEspecie[mascota.especie] || [];
        
        this.mascotaForm.patchValue({
          nombre: mascota.nombre,
          especie: mascota.especie,
          raza: mascota.raza,
          edad: mascota.edad,
          sexo: mascota.sexo,
          peso: mascota.peso,
          color: mascota.color,
          observaciones: mascota.observaciones,
          dueno: mascota.dueno?.idDueno
        });
        
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo cargar la mascota');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.mascotaForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Complete todos los campos requeridos');
      return;
    }

    this.guardando = true;
    
    const mascota: Mascota = {
      ...this.mascotaForm.value,
      dueno: { idDueno: this.mascotaForm.value.dueno }
    };

    const operacion = this.modoEdicion
      ? this.mascotaService.actualizar(this.mascotaId!, mascota)
      : this.mascotaService.crear(mascota);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'actualizada' : 'creada';
        this.notificacionService.success(`Mascota ${mensaje} exitosamente`);
        this.volver();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo guardar la mascota');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.mascotaForm.controls).forEach(key => {
      this.mascotaForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/mascotas']);
  }

  editar(): void {
    this.router.navigate(['/mascotas/editar', this.mascotaId]);
  }

  // Getters
  get nombre() { return this.mascotaForm.get('nombre'); }
  get especie() { return this.mascotaForm.get('especie'); }
  get raza() { return this.mascotaForm.get('raza'); }
  get edad() { return this.mascotaForm.get('edad'); }
  get sexo() { return this.mascotaForm.get('sexo'); }
  get peso() { return this.mascotaForm.get('peso'); }
  get color() { return this.mascotaForm.get('color'); }
  get dueno() { return this.mascotaForm.get('dueno'); }
}