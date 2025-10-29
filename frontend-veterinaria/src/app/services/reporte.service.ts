// ===================================================
// src/app/services/reporte.service.ts
// ===================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private apiUrl = `${environment.apiUrl}/reportes`;
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  getKpis(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/kpis`, { headers: this.headers });
  }

  getDashboard(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dashboard`, { headers: this.headers });
  }

  getTiempoPromedio(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/tiempo-promedio`, { headers: this.headers });
  }

  getAnalisisRepetidos(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/analisis-repetidos`, { headers: this.headers });
  }

  getIngresos(params?: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ingresos`, { 
      headers: this.headers,
      params 
    });
  }
}