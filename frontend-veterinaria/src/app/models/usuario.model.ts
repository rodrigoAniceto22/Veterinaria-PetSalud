export interface Usuario {
  idUsuario?: number;
  nombreUsuario: string;
  contrasena?: string;
  rol: string;
  email?: string;
  nombres?: string;
  apellidos?: string;
  activo?: boolean;
  fechaCreacion?: string;
  ultimoAcceso?: string;
  intentosFallidos?: number;
  bloqueado?: boolean;
  nombreCompleto?: string;
}