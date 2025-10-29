// src/app/models/dueno.model.ts
export interface Dueno {
  idDueno?: number;
  dni: string;
  nombres: string;
  apellidos: string;
  telefono: string;
  email?: string;
  direccion?: string;
}
