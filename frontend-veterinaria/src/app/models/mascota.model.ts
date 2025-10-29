// src/app/models/mascota.model.ts
export interface Mascota {
  idMascota?: number;
  nombre: string;
  especie: string;
  raza?: string;
  edad?: number;
  sexo?: string;
  peso?: number;
  color?: string;
  observaciones?: string;
  dueno?: Dueno;
  idDueno?: number;
}