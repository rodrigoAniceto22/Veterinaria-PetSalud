import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface Notificacion {
  tipo: 'success' | 'error' | 'warning' | 'info';
  mensaje: string;
  duracion?: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private notificacionSubject = new Subject<Notificacion>();
  public notificacion$ = this.notificacionSubject.asObservable();

  constructor() {}

  mostrar(notificacion: Notificacion): void {
    this.notificacionSubject.next(notificacion);
  }

  success(mensaje: string, duracion: number = 3000): void {
    this.mostrar({ tipo: 'success', mensaje, duracion });
  }

  error(mensaje: string, duracion: number = 5000): void {
    this.mostrar({ tipo: 'error', mensaje, duracion });
  }

  warning(mensaje: string, duracion: number = 4000): void {
    this.mostrar({ tipo: 'warning', mensaje, duracion });
  }

  info(mensaje: string, duracion: number = 3000): void {
    this.mostrar({ tipo: 'info', mensaje, duracion });
  }

  // Métodos específicos del dominio
  exitoCrear(entidad: string): void {
    this.success(`${entidad} creado exitosamente`);
  }

  exitoActualizar(entidad: string): void {
    this.success(`${entidad} actualizado exitosamente`);
  }

  exitoEliminar(entidad: string): void {
    this.success(`${entidad} eliminado exitosamente`);
  }

  errorGeneral(mensaje?: string): void {
    this.error(mensaje || 'Ocurrió un error. Por favor intente nuevamente');
  }

  errorConexion(): void {
    this.error('Error de conexión con el servidor');
  }

  confirmarAccion(mensaje: string): boolean {
    return confirm(mensaje);
  }
}