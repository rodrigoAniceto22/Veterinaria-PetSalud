import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Dueno } from '../models/dueno.model';

@Injectable({
  providedIn: 'root'
})
export class DuenoService {
  private apiUrl = 'http://localhost:8080/api/duenos';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Dueno[]> {
    return this.http.get<Dueno[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Dueno> {
    return this.http.get<Dueno>(`${this.apiUrl}/${id}`);
  }

  buscarPorDni(dni: string): Observable<Dueno> {
    return this.http.get<Dueno>(`${this.apiUrl}/dni/${dni}`);
  }

  buscarPorNombre(nombre: string): Observable<Dueno[]> {
    return this.http.get<Dueno[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }

  crear(dueno: Dueno): Observable<Dueno> {
    return this.http.post<Dueno>(this.apiUrl, dueno);
  }

  actualizar(id: number, dueno: Dueno): Observable<Dueno> {
    return this.http.put<Dueno>(`${this.apiUrl}/${id}`, dueno);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerMascotas(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/mascotas`);
  }

  // =====================================================
  // STORED PROCEDURES
  // =====================================================

  /**
   * SP: Registrar dueño usando stored procedure
   */
  registrarDuenoSP(dueno: Dueno): Observable<Dueno> {
    return this.http.post<Dueno>(`${this.apiUrl}/sp/registrar`, dueno);
  }

  /**
   * SP: Buscar dueño con sus mascotas
   */
  buscarDuenoConMascotasSP(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/sp/buscar-con-mascotas/${id}`);
  }

  /**
   * SP: Actualizar dueño usando stored procedure
   */
  actualizarDuenoSP(id: number, dueno: Dueno): Observable<Dueno> {
    return this.http.put<Dueno>(`${this.apiUrl}/sp/actualizar/${id}`, dueno);
  }

  /**
   * SP: Obtener estadísticas de dueños
   */
  obtenerEstadisticasSP(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/sp/estadisticas`);
  }

  // =====================================================
  // EXPORTACIONES
  // =====================================================

  /**
   * Exportar dueños a Excel
   */
  exportarExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/excel`, {
      responseType: 'blob'
    });
  }

  /**
   * Exportar dueños a PDF
   */
  exportarPDF(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/pdf`, {
      responseType: 'blob'
    });
  }

  /**
   * Exportar dueños a JSON
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