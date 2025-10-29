// src/app/models/tecnico.model.ts
export interface Tecnico {
  idTecnico?: number;
  nombres: string;
  apellidos: string;
  especialidad?: string;
  telefono?: string;
  email?: string;
  certificacion?: string;
  activo?: boolean;
}