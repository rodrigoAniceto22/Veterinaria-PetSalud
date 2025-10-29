export interface TecnicoVeterinario {
  idTecnico?: number;
  nombres: string;
  apellidos: string;
  especialidad?: string;
  telefono?: string;
  email?: string;
  certificacion?: string;
  activo?: boolean;
  tomasMuestra?: any[];
}