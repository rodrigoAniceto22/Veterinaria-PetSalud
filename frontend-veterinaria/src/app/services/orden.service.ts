import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrdenVeterinaria } from '../models/orden.model';

@Injectable({
  providedIn: 'root'
})
export class OrdenService {
  private apiUrl = 'http://localhost:8080/api/ordenes';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<OrdenVeterinaria> {
    return this.http.get<OrdenVeterinaria>(`${this.apiUrl}/${id}`);
  }

  buscarPorMascota(idMascota: number): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(`${this.apiUrl}/mascota/${idMascota}`);
  }

  buscarPorVeterinario(idVeterinario: number): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(`${this.apiUrl}/veterinario/${idVeterinario}`);
  }

  buscarPorFecha(fecha: string): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(`${this.apiUrl}/fecha?fecha=${fecha}`);
  }

  buscarPorTipoExamen(tipo: string): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(`${this.apiUrl}/tipo-examen/${tipo}`);
  }

  obtenerPendientes(): Observable<OrdenVeterinaria[]> {
    return this.http.get<OrdenVeterinaria[]>(`${this.apiUrl}/pendientes`);
  }

  crear(orden: OrdenVeterinaria): Observable<OrdenVeterinaria> {
    return this.http.post<OrdenVeterinaria>(this.apiUrl, orden);
  }

  actualizar(id: number, orden: OrdenVeterinaria): Observable<OrdenVeterinaria> {
    return this.http.put<OrdenVeterinaria>(`${this.apiUrl}/${id}`, orden);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  cambiarEstado(id: number, estado: string): Observable<OrdenVeterinaria> {
    return this.http.patch<OrdenVeterinaria>(`${this.apiUrl}/${id}/estado?estado=${estado}`, {});
  }
}