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

  obtenerTiempoPromedio(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/tiempo-promedio?inicio=${inicio}&fin=${fin}`);
  }

  obtenerAnalisisRepetidos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/analisis-repetidos?inicio=${inicio}&fin=${fin}`);
  }

  obtenerTratamientosMismoDia(fecha: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/tratamientos-mismo-dia?fecha=${fecha}`);
  }

  obtenerAnalisisPorTipo(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/analisis-por-tipo?inicio=${inicio}&fin=${fin}`);
  }

  obtenerProductividadVeterinarios(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/productividad-veterinarios?inicio=${inicio}&fin=${fin}`);
  }

  obtenerProductividadTecnicos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/productividad-tecnicos?inicio=${inicio}&fin=${fin}`);
  }

  obtenerIngresos(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ingresos?inicio=${inicio}&fin=${fin}`);
  }

  obtenerEspeciesAtendidas(inicio: string, fin: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/especies-atendidas?inicio=${inicio}&fin=${fin}`);
  }

  obtenerOrdenesPorEstado(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ordenes-estado`);
  }

  reporteTiempoPromedio(inicio: string, fin: string): Observable<any> {
    return this.obtenerTiempoPromedio(inicio, fin);
  }

  reporteAnalisisRepetidos(inicio: string, fin: string): Observable<any> {
    return this.obtenerAnalisisRepetidos(inicio, fin);
  }

  reporteSatisfaccionCliente(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/satisfaccion-cliente`);
  }

  reporteTratamientosMismoDia(fecha: string): Observable<any> {
    return this.obtenerTratamientosMismoDia(fecha);
  }

  reporteAnalisisPorTipo(inicio: string, fin: string): Observable<any> {
    return this.obtenerAnalisisPorTipo(inicio, fin);
  }

  reporteProductividadVeterinarios(inicio: string, fin: string): Observable<any> {
    return this.obtenerProductividadVeterinarios(inicio, fin);
  }

  reporteProductividadTecnicos(inicio: string, fin: string): Observable<any> {
    return this.obtenerProductividadTecnicos(inicio, fin);
  }

  reporteIngresos(inicio: string, fin: string): Observable<any> {
    return this.obtenerIngresos(inicio, fin);
  }

  reporteEspeciesAtendidas(inicio: string, fin: string): Observable<any> {
    return this.obtenerEspeciesAtendidas(inicio, fin);
  }

  reporteOrdenesPorEstado(): Observable<any> {
    return this.obtenerOrdenesPorEstado();
  }
}