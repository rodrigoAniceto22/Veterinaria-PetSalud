export interface Dueno {
  idDueno?: number;
  dni: string;
  nombres: string;
  apellidos: string;
  telefono: string;
  email?: string;
  direccion?: string;
  mascotas?: any[];
  facturas?: any[];
}