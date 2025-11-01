export interface Inventario {
  idInventario?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  categoria?: string;
  precioCompra: number;
  precioVenta: number;
  stockActual: number;
  stockMinimo?: number;
  stockMaximo?: number;
  unidadMedida?: string;
  fechaVencimiento?: Date;
  proveedor?: string;
  activo?: boolean;
  observaciones?: string;
}

export interface InventarioAlertas {
  productosStockBajo: Inventario[];
  productosVencidos: Inventario[];
  productosProximosAVencer: Inventario[];
}