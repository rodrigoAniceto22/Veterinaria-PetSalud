export interface Veterinario {
  idVeterinario?: number;
  nombres: string;
  apellidos: string;
  especialidad?: string;
  telefono?: string;
  email?: string;
  colegiatura?: string;
  activo?: boolean;
  ordenes?: any[];
}