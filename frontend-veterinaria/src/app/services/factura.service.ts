
// ===================================================
// src/app/services/factura.service.ts
// ===================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Factura } from '../models/factura.model';

@Injectable({
  providedIn: 'root'
})
export class FacturaService {
  private apiUrl = `${environment.apiUrl}/facturas`;
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  getAll(): Observable<Factura[]> {
    return this.http.get<Factura[]>(this.apiUrl, { headers: this.headers });
  }

  getById(id: number): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }

  getPendientes(): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/pendientes`, { headers: this.headers });
  }

  getByDueno(idDueno: number): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/dueno/${idDueno}`, { headers: this.headers });
  }

  create(factura: Factura): Observable<Factura> {
    return this.http.post<Factura>(this.apiUrl, factura, { headers: this.headers });
  }

  update(id: number, factura: Factura): Observable<Factura> {
    return this.http.put<Factura>(`${this.apiUrl}/${id}`, factura, { headers: this.headers });
  }

  marcarPagada(id: number): Observable<Factura> {
    return this.http.patch<Factura>(`${this.apiUrl}/${id}/pagar`, {}, { headers: this.headers });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }
}