import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ResultadoService } from '../../services/resultado.service';
import { OrdenService } from '../../services/orden.service';
import { NotificacionService } from '../../services/notificacion.service';
import { ResultadoVeterinario } from '../../models/resultado.model';
import { OrdenVeterinaria } from '../../models/orden.model';

@Component({
  selector: 'app-resultado-form',
  templateUrl: './resultado-form.component.html',
  styleUrls: ['./resultado-form.component.css']
})
export class ResultadoFormComponent implements OnInit {
  resultadoForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  resultadoId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  ordenes: OrdenVeterinaria[] = [];

  constructor(
    private fb: FormBuilder,
    private resultadoService: ResultadoService,
    private ordenService: OrdenService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarOrdenes();
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.resultadoForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.maxLength(1000)]],
      valores: ['', [Validators.required]],
      valoresReferencia: [''],
      conclusiones: ['', [Validators.required]],
      recomendaciones: [''],
      metodoAnalisis: ['', [Validators.maxLength(200)]],
      observacionesTecnicas: [''],
      orden: [null, [Validators.required]]
    });
  }

  cargarOrdenes(): void {
    // Cargar solo órdenes completadas o en proceso
    this.ordenService.listarTodas().subscribe({
      next: (data) => {
        this.ordenes = data.filter(o => 
          o.estado === 'EN_PROCESO' || o.estado === 'COMPLETADA'
        );
      },
      error: (error) => {
        console.error('Error al cargar órdenes:', error);
        this.notificacionService.errorGeneral('No se pudieron cargar las órdenes');
      }
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.resultadoId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarResultado(this.resultadoId);
      }
    });

    if (this.modoVista) {
      this.resultadoForm.disable();
    }
  }

  cargarResultado(id: number): void {
    this.loading = true;
    this.resultadoService.obtenerPorId(id).subscribe({
      next: (resultado) => {
        this.resultadoForm.patchValue({
          descripcion: resultado.descripcion,
          valores: resultado.valores,
          valoresReferencia: resultado.valoresReferencia,
          conclusiones: resultado.conclusiones,
          recomendaciones: resultado.recomendaciones,
          metodoAnalisis: resultado.metodoAnalisis,
          observacionesTecnicas: resultado.observacionesTecnicas,
          orden: resultado.orden?.idOrden
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el resultado');
        this.volver();
      }
    });
  }

  onSubmit(): void {
    if (this.resultadoForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Complete todos los campos requeridos');
      return;
    }

    this.guardando = true;

    const resultado: ResultadoVeterinario = {
      ...this.resultadoForm.value,
      orden: { idOrden: this.resultadoForm.value.orden },
      validado: false,
      entregado: false
    };

    const operacion = this.modoEdicion
      ? this.resultadoService.actualizar(this.resultadoId!, resultado)
      : this.resultadoService.crear(resultado);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'actualizado' : 'creado';
        this.notificacionService.success(`Resultado ${mensaje} exitosamente`);
        this.volver();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo guardar el resultado');
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.resultadoForm.controls).forEach(key => {
      this.resultadoForm.get(key)?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/resultados']);
  }

  editar(): void {
    this.router.navigate(['/resultados/editar', this.resultadoId]);
  }

  validar(): void {
    if (this.resultadoId && this.notificacionService.confirmarAccion('¿Está seguro de validar este resultado?')) {
      this.resultadoService.validar(this.resultadoId).subscribe({
        next: () => {
          this.notificacionService.success('Resultado validado exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo validar el resultado');
        }
      });
    }
  }

  descargarPDF(): void {
    if (this.resultadoId) {
      this.resultadoService.descargarPDF(this.resultadoId).subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `resultado_${this.resultadoId}.pdf`;
          link.click();
          window.URL.revokeObjectURL(url);
          this.notificacionService.success('PDF descargado exitosamente');
        },
        error: (error) => {
          console.error('Error:', error);
          this.notificacionService.errorGeneral('No se pudo descargar el PDF');
        }
      });
    }
  }

  // Getters para validaciones
  get descripcion() { return this.resultadoForm.get('descripcion'); }
  get valores() { return this.resultadoForm.get('valores'); }
  get valoresReferencia() { return this.resultadoForm.get('valoresReferencia'); }
  get conclusiones() { return this.resultadoForm.get('conclusiones'); }
  get recomendaciones() { return this.resultadoForm.get('recomendaciones'); }
  get metodoAnalisis() { return this.resultadoForm.get('metodoAnalisis'); }
  get observacionesTecnicas() { return this.resultadoForm.get('observacionesTecnicas'); }
  get orden() { return this.resultadoForm.get('orden'); }
}