import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private apiUrl = 'http://localhost:8080/api/reportes';

  constructor(private http: HttpClient) {}

  obtenerKPIs(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/kpis`);
  }

  obtenerDashboard(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dashboard`);
  }

  reporteTiempoPromedio(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/tiempo-promedio?inicio=${inicio}&fin=${fin}`);
  }

  reporteAnalisisRepetidos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/analisis-repetidos?inicio=${inicio}&fin=${fin}`);
  }

  reporteSatisfaccionCliente(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/satisfaccion-cliente`);
  }

  reporteTratamientosMismoDia(fecha: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/tratamientos-mismo-dia?fecha=${fecha}`);
  }

  reporteAnalisisPorTipo(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/analisis-por-tipo?inicio=${inicio}&fin=${fin}`);
  }

  reporteProductividadVeterinarios(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/productividad-veterinarios?inicio=${inicio}&fin=${fin}`);
  }

  reporteProductividadTecnicos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/productividad-tecnicos?inicio=${inicio}&fin=${fin}`);
  }

  reporteIngresos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ingresos?inicio=${inicio}&fin=${fin}`);
  }

  reporteEspeciesAtendidas(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/especies-atendidas?inicio=${inicio}&fin=${fin}`);
  }

  reporteOrdenesPorEstado(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ordenes-estado`);
  }
}