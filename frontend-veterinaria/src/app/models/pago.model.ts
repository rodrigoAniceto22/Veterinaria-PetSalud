export interface Pago {
  idPago?: number;
  numeroPago?: string;
  dueno: {
    idDueno: number;
    nombres?: string;
    apellidos?: string;
  };
  mascota?: {
    idMascota: number;
    nombre?: string;
  };
  concepto: string;
  tipoPago?: string;
  monto: number;
  metodoPago?: string;
  estado: string;
  fechaEmision: Date;
  fechaVencimiento?: Date;
  fechaPago?: Date;
  montoPagado?: number;
  observaciones?: string;
  esInternamiento?: boolean;
  fechaInicioInternamiento?: Date;
  fechaFinInternamiento?: Date;
  diasInternamiento?: number;
  costoDiaInternamiento?: number;
}