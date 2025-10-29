// ===================================================
// src/app/models/usuario.model.ts
// ===================================================
export interface Usuario {
  idUsuario?: number;
  nombreCompleto: string;
  username: string;
  contrasena?: string;
  email: string;
  rol: string;
  activo?: boolean;
  fechaCreacion?: string;
  ultimoAcceso?: string;
}