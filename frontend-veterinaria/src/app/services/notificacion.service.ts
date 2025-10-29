
// ===================================================
// src/app/services/notificacion.service.ts
// ===================================================
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  
  success(mensaje: string): void {
    alert(`✅ ${mensaje}`);
  }

  error(mensaje: string): void {
    alert(`❌ ${mensaje}`);
  }

  warning(mensaje: string): void {
    alert(`⚠️ ${mensaje}`);
  }

  info(mensaje: string): void {
    alert(`ℹ️ ${mensaje}`);
  }

  confirm(mensaje: string): boolean {
    return confirm(`❓ ${mensaje}`);
  }
}