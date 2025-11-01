import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdenService } from '../../services/orden.service';
import { MascotaService } from '../../services/mascota.service';
import { VeterinarioService } from '../../services/veterinario.service';
import { NotificacionService } from '../../services/notificacion.service';
import { OrdenVeterinaria } from '../../models/orden.model';
import { Mascota } from '../../models/mascota.model';
import { Veterinario } from '../../models/veterinario.model';

@Component({
  selector: 'app-orden-form',
  templateUrl: './orden-form.component.html',
  styleUrls: ['./orden-form.component.css'],
})
export class OrdenFormComponent implements OnInit {
  ordenForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  ordenId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Listas para selectores
  mascotas: Mascota[] = [];
  veterinarios: Veterinario[] = [];

  // Opciones
  tiposExamen: string[] = [
    'Hemograma Completo',
    'Bioquímica Sanguínea',
    'Urianálisis',
    'Copropasitario',
    'Perfil Renal',
    'Perfil Hepático',
    'Perfil Tiroideo',
    'Glucosa',
    'Electrolitos',
    'Pruebas de Coagulación',
    'Cultivo y Antibiograma',
    'Citología',
    'Histopatología',
    'Otros'
  ];

  prioridades: string[] = ['URGENTE', 'NORMAL', 'BAJA'];
  estados: string[] = ['PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA'];

  constructor(
    private fb: FormBuilder,
    private ordenService: OrdenService,
    private mascotaService: MascotaService,
    private veterinarioService: VeterinarioService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.cargarDatosIniciales();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.ordenForm = this.fb.group({
      fechaOrden: [new Date().toISOString().split('T')[0], Validators.required],
      tipoExamen: ['', [Validators.required, Validators.maxLength(100)]],
      prioridad: ['NORMAL', Validators.required],
      estado: ['PENDIENTE', Validators.required],
      observaciones: ['', Validators.maxLength(500)],
      diagnosticoPresuntivo: ['', Validators.maxLength(500)],
      sintomas: ['', Validators.maxLength(500)],
      idMascota: [null, Validators.required],
      idVeterinario: [null, Validators.required]
    });
  }

  cargarDatosIniciales(): void {
    console.log(' Cargando datos iniciales...');
    
    // Cargar mascotas
    this.mascotaService.listarTodas().subscribe({
      next: (data) => {
        console.log(' Mascotas cargadas:', data);
        this.mascotas = data;
        console.log(' Total mascotas:', this.mascotas.length);
      },
      error: (error) => {
        console.error(' Error al cargar mascotas:', error);
        console.error('Detalles del error:', error.message, error.status);
        this.notificacionService.errorGeneral('No se pudieron cargar las mascotas');
      }
    });

    // Cargar veterinarios
    this.veterinarioService.listarTodos().subscribe({
      next: (data) => {
        console.log(' Veterinarios cargados:', data);
        this.veterinarios = data.filter(v => v.activo);
        console.log(' Total veterinarios activos:', this.veterinarios.length);
      },
      error: (error) => {
        console.error(' Error al cargar veterinarios:', error);
        console.error('Detalles del error:', error.message, error.status);
        this.notificacionService.errorGeneral('No se pudieron cargar los veterinarios');
      }
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');
    
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.ordenId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarOrden(this.ordenId);
      }
    });

    if (this.modoVista) {
      this.ordenForm.disable();
    }
  }

  cargarOrden(id: number): void {
    this.loading = true;
    this.ordenService.obtenerPorId(id).subscribe({
      next: (orden) => {
        this.ordenForm.patchValue({
          fechaOrden: orden.fechaOrden,
          tipoExamen: orden.tipoExamen,
          prioridad: orden.prioridad,
          estado: orden.estado,
          observaciones: orden.observaciones,
          diagnosticoPresuntivo: orden.diagnosticoPresuntivo,
          sintomas: orden.sintomas,
          idMascota: orden.mascota?.idMascota,
          idVeterinario: orden.veterinario?.idVeterinario
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar orden:', error);
        this.notificacionService.errorGeneral('No se pudo cargar la orden');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.ordenForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Por favor complete todos los campos requeridos');
      return;
    }

    this.guardando = true;
    
    // Preparar el objeto orden
    const ordenData: any = {
      fechaOrden: this.ordenForm.value.fechaOrden,
      tipoExamen: this.ordenForm.value.tipoExamen,
      prioridad: this.ordenForm.value.prioridad,
      estado: this.ordenForm.value.estado,
      observaciones: this.ordenForm.value.observaciones,
      diagnosticoPresuntivo: this.ordenForm.value.diagnosticoPresuntivo,
      sintomas: this.ordenForm.value.sintomas,
      mascota: { idMascota: this.ordenForm.value.idMascota },
      veterinario: { idVeterinario: this.ordenForm.value.idVeterinario }
    };

    const operacion = this.modoEdicion
      ? this.ordenService.actualizar(this.ordenId!, ordenData)
      : this.ordenService.crear(ordenData);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'Orden actualizada' : 'Orden creada';
        this.notificacionService.success(mensaje + ' exitosamente');
        this.volver();
      },
      error: (error) => {
        console.error('Error al guardar:', error);
        this.notificacionService.errorGeneral('No se pudo guardar la orden');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.ordenForm.controls).forEach(key => {
      this.ordenForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/ordenes']);
  }

  editar(): void {
    this.router.navigate(['/ordenes/editar', this.ordenId]);
  }

  programarTomaMuestra(): void {
    this.router.navigate(['/toma-muestras/nuevo'], { 
      queryParams: { orden: this.ordenId } 
    });
  }

  verResultados(): void {
    this.router.navigate(['/resultados'], { 
      queryParams: { orden: this.ordenId } 
    });
  }

  // Getters para validaciones
  get fechaOrden() { return this.ordenForm.get('fechaOrden'); }
  get tipoExamen() { return this.ordenForm.get('tipoExamen'); }
  get prioridad() { return this.ordenForm.get('prioridad'); }
  get estado() { return this.ordenForm.get('estado'); }
  get observaciones() { return this.ordenForm.get('observaciones'); }
  get diagnosticoPresuntivo() { return this.ordenForm.get('diagnosticoPresuntivo'); }
  get sintomas() { return this.ordenForm.get('sintomas'); }
  get idMascota() { return this.ordenForm.get('idMascota'); }
  get idVeterinario() { return this.ordenForm.get('idVeterinario'); }
}