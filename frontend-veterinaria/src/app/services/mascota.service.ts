import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mascota } from '../models/mascota.model';

@Injectable({
  providedIn: 'root'
})
export class MascotaService {
  private apiUrl = 'http://localhost:8080/api/mascotas';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Mascota> {
    return this.http.get<Mascota>(`${this.apiUrl}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }

  buscarPorEspecie(especie: string): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/especie/${especie}`);
  }

  buscarPorDueno(idDueno: number): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${this.apiUrl}/dueno/${idDueno}`);
  }

  crear(mascota: Mascota): Observable<Mascota> {
    return this.http.post<Mascota>(this.apiUrl, mascota);
  }

  actualizar(id: number, mascota: Mascota): Observable<Mascota> {
    return this.http.put<Mascota>(`${this.apiUrl}/${id}`, mascota);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerOrdenes(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/ordenes`);
  }

  // =====================================================
  // STORED PROCEDURES
  // =====================================================

  /**
   * SP: Registrar mascota usando stored procedure
   */
  registrarMascotaSP(mascota: any): Observable<Mascota> {
    return this.http.post<Mascota>(`${this.apiUrl}/sp/registrar`, mascota);
  }

  /**
   * SP: Buscar mascotas por dueño con detalles
   */
  buscarMascotasPorDuenoSP(idDueno: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/sp/buscar-por-dueno/${idDueno}`);
  }

  /**
   * SP: Actualizar mascota usando stored procedure
   */
  actualizarMascotaSP(id: number, mascota: any): Observable<Mascota> {
    return this.http.put<Mascota>(`${this.apiUrl}/sp/actualizar/${id}`, mascota);
  }

  /**
   * SP: Obtener estadísticas de mascotas
   */
  obtenerEstadisticasSP(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/sp/estadisticas`);
  }

  // =====================================================
  // EXPORTACIONES
  // =====================================================

  /**
   * Exportar mascotas a Excel
   */
  exportarExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/excel`, {
      responseType: 'blob'
    });
  }

  /**
   * Exportar mascotas a PDF
   */
  exportarPDF(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/pdf`, {
      responseType: 'blob'
    });
  }

  /**
   * Exportar mascotas a JSON
   */
  exportarJSON(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/json`, {
      responseType: 'blob'
    });
  }

  /**
   * Descargar archivo de exportación
   */
  descargarArchivo(blob: Blob, nombreArchivo: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = nombreArchivo;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}