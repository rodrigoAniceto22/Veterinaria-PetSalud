import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TecnicoVeterinario } from '../models/tecnico.model';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {
  private apiUrl = 'http://localhost:8080/api/tecnicos';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<TecnicoVeterinario[]> {
    return this.http.get<TecnicoVeterinario[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<TecnicoVeterinario> {
    return this.http.get<TecnicoVeterinario>(`${this.apiUrl}/${id}`);
  }

  buscarPorEspecialidad(especialidad: string): Observable<TecnicoVeterinario[]> {
    return this.http.get<TecnicoVeterinario[]>(`${this.apiUrl}/especialidad/${especialidad}`);
  }

  buscarPorNombre(nombre: string): Observable<TecnicoVeterinario[]> {
    return this.http.get<TecnicoVeterinario[]>(`${this.apiUrl}/buscar?nombre=${nombre}`);
  }

  obtenerDisponibles(): Observable<TecnicoVeterinario[]> {
    return this.http.get<TecnicoVeterinario[]>(`${this.apiUrl}/disponibles`);
  }

  crear(tecnico: TecnicoVeterinario): Observable<TecnicoVeterinario> {
    return this.http.post<TecnicoVeterinario>(this.apiUrl, tecnico);
  }

  actualizar(id: number, tecnico: TecnicoVeterinario): Observable<TecnicoVeterinario> {
    return this.http.put<TecnicoVeterinario>(`${this.apiUrl}/${id}`, tecnico);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  obtenerTomasMuestra(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/tomas-muestra`);
  }
}