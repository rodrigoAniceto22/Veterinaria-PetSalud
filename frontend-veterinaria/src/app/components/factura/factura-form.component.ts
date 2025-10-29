import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FacturaService } from '../../services/factura.service';
import { DuenoService } from '../../services/dueno.service';
import { NotificacionService } from '../../services/notificacion.service';
import { Factura } from '../../models/factura.model';
import { DetalleFactura } from '../../models/detalle-factura.model';
import { Dueno } from '../../models/dueno.model';

@Component({
  selector: 'app-factura-form',
  templateUrl: './factura-form.component.html',
  styleUrls: ['./factura-form.component.css']
})
export class FacturaFormComponent implements OnInit {
  facturaForm!: FormGroup;
  modoEdicion: boolean = false;
  modoVista: boolean = false;
  facturaId: number | null = null;
  loading: boolean = false;
  guardando: boolean = false;

  // Catálogos
  duenos: Dueno[] = [];
  metodosPago = ['EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'YAPE', 'PLIN'];
  tiposServicio = ['EXAMEN', 'CONSULTA', 'MEDICAMENTO', 'VACUNA', 'CIRUGIA', 'OTROS'];

  // Cálculos
  subtotal: number = 0;
  igv: number = 0;
  total: number = 0;
  IGV_PORCENTAJE = 0.18;

  constructor(
    private fb: FormBuilder,
    private facturaService: FacturaService,
    private duenoService: DuenoService,
    private notificacionService: NotificacionService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarDuenos();
    this.inicializarFormulario();
    this.verificarModo();
  }

  inicializarFormulario(): void {
    this.facturaForm = this.fb.group({
      numeroFactura: ['', [Validators.required]],
      fechaEmision: [this.obtenerFechaActual(), [Validators.required]],
      fechaVencimiento: [''],
      idDueno: ['', [Validators.required]],
      estado: ['PENDIENTE'],
      metodoPago: [''],
      fechaPago: [''],
      observaciones: [''],
      detalles: this.fb.array([])
    });

    // Generar número de factura automático
    this.generarNumeroFactura();

    // Agregar un detalle inicial
    this.agregarDetalle();
  }

  cargarDuenos(): void {
    this.duenoService.listarTodos().subscribe({
      next: (data) => {
        this.duenos = data;
      },
      error: (error) => {
        console.error('Error al cargar dueños:', error);
      }
    });
  }

  verificarModo(): void {
    const url = this.router.url;
    this.modoVista = url.includes('/ver/');

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.facturaId = +params['id'];
        this.modoEdicion = !this.modoVista;
        this.cargarFactura(this.facturaId);
      }
    });

    if (this.modoVista) {
      this.facturaForm.disable();
    }
  }

  cargarFactura(id: number): void {
    this.loading = true;
    this.facturaService.obtenerPorId(id).subscribe({
      next: (factura) => {
        this.facturaForm.patchValue({
          numeroFactura: factura.numeroFactura,
          fechaEmision: factura.fechaEmision,
          fechaVencimiento: factura.fechaVencimiento,
          idDueno: factura.dueno?.idDueno,
          estado: factura.estado,
          metodoPago: factura.metodoPago,
          fechaPago: factura.fechaPago,
          observaciones: factura.observaciones
        });

        // Cargar detalles
        this.detalles.clear();
        if (factura.detalles && factura.detalles.length > 0) {
          factura.detalles.forEach(detalle => {
            this.detalles.push(this.fb.group({
              descripcion: [detalle.descripcion, Validators.required],
              cantidad: [detalle.cantidad, [Validators.required, Validators.min(1)]],
              precioUnitario: [detalle.precioUnitario, [Validators.required, Validators.min(0)]],
              tipoServicio: [detalle.tipoServicio]
            }));
          });
        }

        this.calcularTotales();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar factura:', error);
        this.notificacionService.errorGeneral();
        this.volver();
      }
    });
  }

  get detalles(): FormArray {
    return this.facturaForm.get('detalles') as FormArray;
  }

  agregarDetalle(): void {
    const detalleGroup = this.fb.group({
      descripcion: ['', Validators.required],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precioUnitario: [0, [Validators.required, Validators.min(0)]],
      tipoServicio: ['EXAMEN']
    });

    // Escuchar cambios para recalcular
    detalleGroup.valueChanges.subscribe(() => {
      this.calcularTotales();
    });

    this.detalles.push(detalleGroup);
  }

  eliminarDetalle(index: number): void {
    if (this.detalles.length > 1) {
      this.detalles.removeAt(index);
      this.calcularTotales();
    } else {
      this.notificacionService.warning('Debe haber al menos un detalle');
    }
  }

  calcularSubtotalDetalle(detalle: any): number {
    return (detalle.cantidad || 0) * (detalle.precioUnitario || 0);
  }

  calcularTotales(): void {
    this.subtotal = 0;
    
    this.detalles.controls.forEach(control => {
      const detalle = control.value;
      this.subtotal += this.calcularSubtotalDetalle(detalle);
    });

    this.igv = this.subtotal * this.IGV_PORCENTAJE;
    this.total = this.subtotal + this.igv;
  }

  generarNumeroFactura(): void {
    const fecha = new Date();
    const año = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
    this.facturaForm.patchValue({
      numeroFactura: `F${año}${mes}-${random}`
    });
  }

  obtenerFechaActual(): string {
    return new Date().toISOString().split('T')[0];
  }

  onSubmit(): void {
    if (this.facturaForm.invalid) {
      this.marcarCamposComoTocados();
      this.notificacionService.warning('Complete todos los campos requeridos');
      return;
    }

    if (this.detalles.length === 0) {
      this.notificacionService.warning('Debe agregar al menos un detalle');
      return;
    }

    this.guardando = true;

    const factura: Factura = {
      ...this.facturaForm.value,
      subtotal: this.subtotal,
      igv: this.igv,
      total: this.total,
      dueno: { idDueno: this.facturaForm.value.idDueno },
      detalles: this.detalles.value.map((d: any) => ({
        ...d,
        subtotal: this.calcularSubtotalDetalle(d)
      }))
    };

    const operacion = this.modoEdicion
      ? this.facturaService.actualizar(this.facturaId!, factura)
      : this.facturaService.crear(factura);

    operacion.subscribe({
      next: () => {
        const mensaje = this.modoEdicion ? 'actualizada' : 'creada';
        this.notificacionService.success(`Factura ${mensaje} exitosamente`);
        this.volver();
      },
      error: (error) => {
        console.error('Error al guardar:', error);
        this.notificacionService.errorGeneral();
        this.guardando = false;
      }
    });
  }

  marcarCamposComoTocados(): void {
    Object.keys(this.facturaForm.controls).forEach(key => {
      this.facturaForm.get(key)?.markAsTouched();
    });
    this.detalles.controls.forEach(control => {
      Object.keys((control as FormGroup).controls).forEach(key => {
        control.get(key)?.markAsTouched();
      });
    });
  }

  volver(): void {
    this.router.navigate(['/facturas']);
  }

  editar(): void {
    this.router.navigate(['/facturas/editar', this.facturaId]);
  }

  get numeroFactura() { return this.facturaForm.get('numeroFactura'); }
  get fechaEmision() { return this.facturaForm.get('fechaEmision'); }
  get idDueno() { return this.facturaForm.get('idDueno'); }
}