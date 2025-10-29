
// ===================================================
// src/app/services/tecnico.service.ts
// ===================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Tecnico } from '../models/tecnico.model';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {
  private apiUrl = `${environment.apiUrl}/tecnicos`;
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  getAll(): Observable<Tecnico[]> {
    return this.http.get<Tecnico[]>(this.apiUrl, { headers: this.headers });
  }

  getById(id: number): Observable<Tecnico> {
    return this.http.get<Tecnico>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }

  getDisponibles(): Observable<Tecnico[]> {
    return this.http.get<Tecnico[]>(`${this.apiUrl}/disponibles`, { headers: this.headers });
  }

  create(tecnico: Tecnico): Observable<Tecnico> {
    return this.http.post<Tecnico>(this.apiUrl, tecnico, { headers: this.headers });
  }

  update(id: number, tecnico: Tecnico): Observable<Tecnico> {
    return this.http.put<Tecnico>(`${this.apiUrl}/${id}`, tecnico, { headers: this.headers });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }
}
