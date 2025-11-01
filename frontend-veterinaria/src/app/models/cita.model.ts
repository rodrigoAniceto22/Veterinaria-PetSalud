export interface Cita {
  idCita?: number;
  mascota: {
    idMascota: number;
    nombre?: string;
  };
  veterinario?: {
    idVeterinario: number;
    nombres?: string;
    apellidos?: string;
  };
  fechaHora: Date;
  motivo: string;
  tipoCita?: string;
  estado: string;
  duracionMinutos?: number;
  observaciones?: string;
  costoConsulta?: number;
  recordatorioEnviado?: boolean;
  nivelAlerta?: string;
}

export interface CitaAlertas {
  citasCriticas: Cita[];
  citasProximas: Cita[];
  citasDelDia: Cita[];
  citasPendientesConfirmacion: Cita[];
}