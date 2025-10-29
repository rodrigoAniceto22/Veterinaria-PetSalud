export interface Factura {
  idFactura?: number;
  numeroFactura: string;
  fechaEmision: string;
  fechaVencimiento?: string;
  subtotal: number;
  igv?: number;
  total: number;
  estado?: string;
  metodoPago?: string;
  fechaPago?: string;
  observaciones?: string;
  dueno?: any;
  detalles?: any[];
}