// ===================================================
// src/app/models/detalle-factura.model.ts
// ===================================================
export interface DetalleFactura {
  idDetalle?: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  tipoServicio?: string;
  idFactura?: number;
}
