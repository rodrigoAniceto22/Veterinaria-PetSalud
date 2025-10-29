// src/app/models/toma-muestra.model.ts
export interface TomaMuestra {
  idToma?: number;
  fechaHora: string;
  tipoMuestra: string;
  metodoObtencion?: string;
  volumenMuestra?: string;
  condicionesMuestra?: string;
  observaciones?: string;
  estado?: string;
  codigoMuestra?: string;
  orden?: Orden;
  tecnico?: Tecnico;
  idOrden?: number;
  idTecnico?: number;
}