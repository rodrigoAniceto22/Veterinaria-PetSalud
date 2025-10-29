// ===================================================
// src/app/models/factura.model.ts
// ===================================================
import { Dueno } from './dueno.model';
import { DetalleFactura } from './detalle-factura.model';

export interface Factura {
  idFactura?: number;
  numeroFactura: string;
  fechaEmision: string;
  fechaVencimiento?: string;
  subtotal: number;
  igv: number;
  total: number;
  estado?: string;
  metodoPago?: string;
  fechaPago?: string;
  observaciones?: string;
  dueno?: Dueno;
  idDueno?: number;
  detalles?: DetalleFactura[];
}
