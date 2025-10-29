import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Veterinario } from '../models/veterinario.model';

@Injectable({
  providedIn: 'root'
})
export class VeterinarioService {
  private apiUrl = 'http://localhost:8080/api/veterinarios';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Veterinario> {
    return this.http.get<Veterinario>(`${this.apiUrl}/${id}`);
  }

  buscarPorEspecialidad(especialidad: string): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(`${this.apiUrl}/especialidad/${especialidad}`);
  }

  buscarPorNombre(nombre: string): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }

  obtenerDisponibles(): Observable<Veterinario[]> {
    return this.http.get<Veterinario[]>(`${this.apiUrl}/disponibles`);
  }

  crear(veterinario: Veterinario): Observable<Veterinario> {
    return this.http.post<Veterinario>(this.apiUrl, veterinario);
  }

  actualizar(id: number, veterinario: Veterinario): Observable<Veterinario> {
    return this.http.put<Veterinario>(`${this.apiUrl}/${id}`, veterinario);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerOrdenes(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/ordenes`);
  }
}