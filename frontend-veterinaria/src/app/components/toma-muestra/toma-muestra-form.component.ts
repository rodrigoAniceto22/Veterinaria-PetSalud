import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TomaMuestraService } from '../../services/toma-muestra.service';
import { OrdenService } from '../../services/orden.service';
import { TecnicoService } from '../../services/tecnico.service';
import { NotificacionService } from '../../services/notificacion.service';
import { TomaMuestra } from '../../models/toma-muestra.model';
import { OrdenVeterinaria } from '../../models/orden.model';
import { TecnicoVeterinario } from '../../models/tecnico.model';

@Component({
  selector: 'app-toma-muestra-form',
  templateUrl: './toma-muestra-form.component.html',
  styleUrls: ['./toma-muestra-form.component.css']
})
export class TomaMuestraFormComponent implements OnInit {
  tomaMuestraForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  tomaId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  ordenes: OrdenVeterinaria[] = [];
  tecnicos: TecnicoVeterinario[] = [];
  
  tiposMuestra = ['Sangre', 'Orina', 'Heces', 'Piel', 'Pelo', 'Saliva', 'Tejido', 'Fluido Corporal', 'Otro'];
  
  metodosObtencion = [
    'Venopunción',
    'Punción arterial',
    'Cistocentesis',
    'Micción natural',
    'Recolección directa',
    'Biopsia',
    'Raspado cutáneo',
    'Hisopado',
    'Aspiración',
    'Otro'
  ];
  
  estados = ['PROGRAMADA', 'REALIZADA', 'PROCESANDO', 'COMPLETADA'];
  
  condicionesMuestra = [
    'Temperatura ambiente',
    'Refrigerada (2-8°C)',
    'Congelada (-20°C)',
    'Con anticoagulante',
    'Sin anticoagulante',
    'Protegida de luz',
    'Otro'
  ];

  constructor(
    private fb: FormBuilder,
    private tomaMuestraService: TomaMuestraService,
    private ordenService: OrdenService,
    private tecnicoService: TecnicoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarCatalogos();
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    // Obtener fecha/hora actual en formato local
    const now = new Date();
    const fechaHoraLocal = now.toISOString().slice(0, 16);

    this.tomaMuestraForm = this.fb.group({
      fechaHora: [fechaHoraLocal, [Validators.required]],
      tipoMuestra: ['', [Validators.required]],
      metodoObtencion: [''],
      volumenMuestra: ['', [Validators.maxLength(50)]],
      condicionesMuestra: [''],
      observaciones: [''],
      estado: ['PROGRAMADA', [Validators.required]],
      codigoMuestra: ['', [Validators.maxLength(50)]],
      orden: [null, [Validators.required]],
      tecnico: [null, [Validators.required]]
    });
  }

  cargarCatalogos(): void {
    // Cargar órdenes pendientes o en proceso
    this.ordenService.obtenerPendientes().subscribe({
      next: (data) => {
        this.ordenes = data;
      },
      error: (error) => {
        console.error('Error al cargar órdenes:', error);
        this.notificacionService.errorGeneral('No se pudieron cargar las órdenes');
      }
    });

    // Cargar técnicos disponibles
    this.tecnicoService.listarTodos().subscribe({
      next: (data) => {
        this.tecnicos = data.filter(t => t.activo);
      },
      error: (error) => {
        console.error('Error al cargar técnicos:', error);
        this.notificacionService.errorGeneral('No se pudieron cargar los técnicos');
      }
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.tomaId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarTomaMuestra(this.tomaId);
      }
    });

    // Verificar si viene orden pre-seleccionada
    this.route.queryParams.subscribe(params => {
      if (params['orden']) {
        this.tomaMuestraForm.patchValue({ orden: +params['orden'] });
      }
    });

    if (this.modoVista) {
      this.tomaMuestraForm.disable();
    }
  }

  cargarTomaMuestra(id: number): void {
    this.loading = true;
    this.tomaMuestraService.obtenerPorId(id).subscribe({
      next: (toma) => {
        // Convertir fecha a formato input datetime-local
        const fecha = new Date(toma.fechaHora);
        const fechaLocal = fecha.toISOString().slice(0, 16);

        this.tomaMuestraForm.patchValue({
          fechaHora: fechaLocal,
          tipoMuestra: toma.tipoMuestra,
          metodoObtencion: toma.metodoObtencion,
          volumenMuestra: toma.volumenMuestra,
          condicionesMuestra: toma.condicionesMuestra,
          observaciones: toma.observaciones,
          estado: toma.estado,
          codigoMuestra: toma.codigoMuestra,
          orden: toma.orden?.idOrden,
          tecnico: toma.tecnico?.idTecnico
        });

        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo cargar la toma de muestra');
        this.volver();
      }
    });
  }

  generarCodigoMuestra(): void {
    const fecha = new Date();
    const year = fecha.getFullYear().toString().slice(-2);
    const month = ('0' + (fecha.getMonth() + 1)).slice(-2);
    const day = ('0' + fecha.getDate()).slice(-2);
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
    
    const codigo = `TM${year}${month}${day}-${random}`;
    this.tomaMuestraForm.patchValue({ codigoMuestra: codigo });
  }

  onSubmit(): void {
    if (this.tomaMuestraForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Complete todos los campos requeridos');
      return;
    }

    this.guardando = true;

    const formValue = this.tomaMuestraForm.value;
    
    const tomaMuestra: TomaMuestra = {
      fechaHora: formValue.fechaHora,
      tipoMuestra: formValue.tipoMuestra,
      metodoObtencion: formValue.metodoObtencion,
      volumenMuestra: formValue.volumenMuestra,
      condicionesMuestra: formValue.condicionesMuestra,
      observaciones: formValue.observaciones,
      estado: formValue.estado,
      codigoMuestra: formValue.codigoMuestra,
      orden: { idOrden: formValue.orden },
      tecnico: { idTecnico: formValue.tecnico }
    };

    const operacion = this.modoEdicion
      ? this.tomaMuestraService.actualizar(this.tomaId!, tomaMuestra)
      : this.tomaMuestraService.crear(tomaMuestra);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'actualizada' : 'creada';
        this.notificacionService.success(`Toma de muestra ${mensaje} exitosamente`);
        this.volver();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo guardar la toma de muestra');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.tomaMuestraForm.controls).forEach(key => {
      this.tomaMuestraForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/toma-muestras']);
  }

  editar(): void {
    this.router.navigate(['/toma-muestras/editar', this.tomaId]);
  }

  verOrden(): void {
    const ordenId = this.tomaMuestraForm.get('orden')?.value;
    if (ordenId) {
      this.router.navigate(['/ordenes/ver', ordenId]);
    }
  }

  getOrdenSeleccionada(): OrdenVeterinaria | undefined {
    const ordenId = this.tomaMuestraForm.get('orden')?.value;
    return this.ordenes.find(o => o.idOrden === ordenId);
  }

  // Getters para validaciones
  get fechaHora() { return this.tomaMuestraForm.get('fechaHora'); }
  get tipoMuestra() { return this.tomaMuestraForm.get('tipoMuestra'); }
  get metodoObtencion() { return this.tomaMuestraForm.get('metodoObtencion'); }
  get volumenMuestra() { return this.tomaMuestraForm.get('volumenMuestra'); }
  get condicionesMuestra() { return this.tomaMuestraForm.get('condicionesMuestra'); }
  get estado() { return this.tomaMuestraForm.get('estado'); }
  get codigoMuestra() { return this.tomaMuestraForm.get('codigoMuestra'); }
  get orden() { return this.tomaMuestraForm.get('orden'); }
  get tecnico() { return this.tomaMuestraForm.get('tecnico'); }
}