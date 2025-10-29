// ===================================================
// src/app/services/veterinario.service.ts
// ===================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Veterinario } from '../models/veterinario.model';

@Injectable({
  providedIn: 'root'
})
export class VeterinarioService {
  private apiUrl = `${environment.apiUrl}/veterinarios`;
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {}

  getAll(): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(this.apiUrl, { headers: this.headers });
  }

  getById(id: number): Observable<Veterinario> {
    return this.http.get<Veterinario>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }

  getDisponibles(): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(`${this.apiUrl}/disponibles`, { headers: this.headers });
  }

  create(veterinario: Veterinario): Observable<Veterinario> {
    return this.http.post<Veterinario>(this.apiUrl, veterinario, { headers: this.headers });
  }

  update(id: number, veterinario: Veterinario): Observable<Veterinario> {
    return this.http.put<Veterinario>(`${this.apiUrl}/${id}`, veterinario, { headers: this.headers });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.headers });
  }
}