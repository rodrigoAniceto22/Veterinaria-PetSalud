// src/app/models/orden.model.ts
export interface Orden {
  idOrden?: number;
  fechaOrden: string;
  tipoExamen: string;
  prioridad?: string;
  estado?: string;
  observaciones?: string;
  diagnosticoPresuntivo?: string;
  sintomas?: string;
  mascota?: Mascota;
  veterinario?: Veterinario;
  idMascota?: number;
  idVeterinario?: number;
}