import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResultadoVeterinario } from '../models/resultado.model';

@Injectable({
  providedIn: 'root'
})
export class ResultadoService {
  private apiUrl = 'http://localhost:8080/api/resultados';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<ResultadoVeterinario[]> {
    return this.http.get<ResultadoVeterinario[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<ResultadoVeterinario> {
    return this.http.get<ResultadoVeterinario>(`${this.apiUrl}/${id}`);
  }

  buscarPorOrden(idOrden: number): Observable<ResultadoVeterinario[]> {
    return this.http.get<ResultadoVeterinario[]>(`${this.apiUrl}/orden/${idOrden}`);
  }

  obtenerValidados(): Observable<ResultadoVeterinario[]> {
    return this.http.get<ResultadoVeterinario[]>(`${this.apiUrl}/validados`);
  }

  obtenerPendientes(): Observable<ResultadoVeterinario[]> {
    return this.http.get<ResultadoVeterinario[]>(`${this.apiUrl}/pendientes`);
  }

  crear(resultado: ResultadoVeterinario): Observable<ResultadoVeterinario> {
    return this.http.post<ResultadoVeterinario>(this.apiUrl, resultado);
  }

  actualizar(id: number, resultado: ResultadoVeterinario): Observable<ResultadoVeterinario> {
    return this.http.put<ResultadoVeterinario>(`${this.apiUrl}/${id}`, resultado);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  validarResultado(id: number): Observable<ResultadoVeterinario> {
    return this.http.patch<ResultadoVeterinario>(`${this.apiUrl}/${id}/validar`, {});
  }

  // Alias para compatibilidad con componentes
  validar(id: number): Observable<ResultadoVeterinario> {
    return this.validarResultado(id);
  }

  marcarComoEntregado(id: number): Observable<ResultadoVeterinario> {
    return this.http.patch<ResultadoVeterinario>(`${this.apiUrl}/${id}/entregar`, {});
  }

  generarPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  // Método que retorna Observable para uso en componentes
  descargarPDF(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  // Método helper para descarga directa
  descargarPdf(id: number): void {
    this.generarPdf(id).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `resultado_${id}.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);
    });
  }
}