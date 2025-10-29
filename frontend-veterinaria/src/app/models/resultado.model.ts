// src/app/models/resultado.model.ts
export interface Resultado {
  idResultado?: number;
  fechaResultado: string;
  descripcion?: string;
  valores?: string;
  valoresReferencia?: string;
  conclusiones?: string;
  recomendaciones?: string;
  validado?: boolean;
  fechaValidacion?: string;
  entregado?: boolean;
  fechaEntrega?: string;
  metodoAnalisis?: string;
  observacionesTecnicas?: string;
  orden?: Orden;
  idOrden?: number;
}