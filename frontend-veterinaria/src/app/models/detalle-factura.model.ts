export interface DetalleFactura {
  idDetalle?: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  tipoServicio?: string;
  factura?: any;
}