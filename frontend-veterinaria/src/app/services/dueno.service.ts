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
}