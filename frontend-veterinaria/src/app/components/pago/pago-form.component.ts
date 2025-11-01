import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Pago } from '../../models/pago.model';
import { PagoService } from '../../services/pago.service';
import { DuenoService } from '../../services/dueno.service';
import { MascotaService } from '../../services/mascota.service';
import { Dueno } from '../../models/dueno.model';
import { Mascota } from '../../models/mascota.model';

@Component({
  selector: 'app-pago-form',
  templateUrl: './pago-form.component.html',
  styleUrls: ['./pago-form.component.css']
})
export class PagoFormComponent implements OnInit {
  pago: Pago = {
    dueno: { idDueno: 0 },
    concepto: '',
    monto: 0,
    estado: 'PENDIENTE',
    fechaEmision: new Date(),
    esInternamiento: false
  };

  duenos: Dueno[] = [];
  mascotas: Mascota[] = [];
  esEdicion: boolean = false;
  enviando: boolean = false;

  tiposPago: string[] = ['CONSULTA', 'INTERNAMIENTO', 'CIRUGIA', 'VACUNA', 'LABORATORIO', 'OTRO'];
  metodosPago: string[] = ['EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'YAPE', 'PLIN'];
  estados: string[] = ['PENDIENTE', 'PAGADO', 'PARCIAL', 'VENCIDO'];

  constructor(
    private pagoService: PagoService,
    private duenoService: DuenoService,
    private mascotaService: MascotaService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.cargarDuenos();
    
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.esEdicion = true;
      this.cargarPago(id);
    }
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

  onDuenoChange(): void {
    if (this.pago.dueno.idDueno) {
      this.mascotaService.buscarPorDueno(this.pago.dueno.idDueno).subscribe({
        next: (data) => {
          this.mascotas = data;
        },
        error: (error) => {
          console.error('Error al cargar mascotas:', error);
        }
      });
    } else {
      this.mascotas = [];
    }
  }

  cargarPago(id: number): void {
    this.pagoService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.pago = data;
        if (this.pago.dueno.idDueno) {
          this.onDuenoChange();
        }
      },
      error: (error) => {
        console.error('Error al cargar pago:', error);
        alert('Error al cargar el pago');
        this.volver();
      }
    });
  }

  onInternamientoChange(): void {
    if (this.pago.esInternamiento) {
      this.pago.tipoPago = 'INTERNAMIENTO';
      this.pago.concepto = 'Internamiento';
      this.pago.fechaInicioInternamiento = new Date();
    } else {
      this.pago.fechaInicioInternamiento = undefined;
      this.pago.fechaFinInternamiento = undefined;
      this.pago.diasInternamiento = undefined;
      this.pago.costoDiaInternamiento = undefined;
    }
  }

  calcularMontoInternamiento(): void {
    if (this.pago.diasInternamiento && this.pago.costoDiaInternamiento) {
      this.pago.monto = this.pago.diasInternamiento * this.pago.costoDiaInternamiento;
    }
  }

  guardar(): void {
    if (!this.validar()) {
      return;
    }

    this.enviando = true;

    if (this.esEdicion) {
      this.pagoService.actualizar(this.pago.idPago!, this.pago).subscribe({
        next: () => {
          alert('Pago actualizado exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al actualizar:', error);
          alert('Error al actualizar el pago');
          this.enviando = false;
        }
      });
    } else {
      this.pagoService.crear(this.pago).subscribe({
        next: () => {
          alert('Pago creado exitosamente');
          this.volver();
        },
        error: (error) => {
          console.error('Error al crear:', error);
          alert('Error al crear el pago');
          this.enviando = false;
        }
      });
    }
  }

  validar(): boolean {
    if (!this.pago.dueno.idDueno || this.pago.dueno.idDueno === 0) {
      alert('Debe seleccionar un dueño');
      return false;
    }

    if (!this.pago.concepto || this.pago.concepto.trim() === '') {
      alert('El concepto es obligatorio');
      return false;
    }

    if (this.pago.monto <= 0) {
      alert('El monto debe ser mayor a 0');
      return false;
    }

    return true;
  }

  volver(): void {
    this.router.navigate(['/pagos']);
  }
}