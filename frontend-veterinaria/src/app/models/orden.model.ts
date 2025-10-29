export interface OrdenVeterinaria {
  idOrden?: number;
  fechaOrden: string;
  tipoExamen: string;
  prioridad?: string;
  estado?: string;
  observaciones?: string;
  diagnosticoPresuntivo?: string;
  sintomas?: string;
  mascota?: any;
  veterinario?: any;
  tomaMuestra?: any;
  resultados?: any[];
}