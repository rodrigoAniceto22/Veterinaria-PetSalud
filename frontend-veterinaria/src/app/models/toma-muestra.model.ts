export interface TomaMuestraVet {
  idToma?: number;
  fechaHora: string;
  tipoMuestra: string;
  metodoObtencion?: string;
  volumenMuestra?: string;
  condicionesMuestra?: string;
  observaciones?: string;
  estado?: string;
  codigoMuestra?: string;
  orden?: any;
  tecnico?: any;
}

// Alias para compatibilidad con componentes
export type TomaMuestra = TomaMuestraVet;