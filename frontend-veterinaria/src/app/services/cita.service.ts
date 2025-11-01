import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cita, CitaAlertas } from '../models/cita.model';

@Injectable({
  providedIn: 'root'
})
export class CitaService {
  private apiUrl = 'http://localhost:8080/api/citas';

  constructor(private http: HttpClient) { }

  listarTodas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Cita> {
    return this.http.get<Cita>(`${this.apiUrl}/${id}`);
  }

  buscarPorMascota(idMascota: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/mascota/${idMascota}`);
  }

  buscarPorVeterinario(idVeterinario: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/veterinario/${idVeterinario}`);
  }

  buscarPorEstado(estado: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/estado/${estado}`);
  }

  obtenerCitasDelDia(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/hoy`);
  }

  obtenerCitasProximas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/proximas`);
  }

  obtenerCitasCriticas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/criticas`);
  }

  obtenerCitasConAlertas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/con-alertas`);
  }

  obtenerDashboardAlertas(): Observable<CitaAlertas> {
    return this.http.get<CitaAlertas>(`${this.apiUrl}/dashboard-alertas`);
  }

  crear(cita: Cita): Observable<Cita> {
    return this.http.post<Cita>(this.apiUrl, cita);
  }

  actualizar(id: number, cita: Cita): Observable<Cita> {
    return this.http.put<Cita>(`${this.apiUrl}/${id}`, cita);
  }

  confirmar(id: number): Observable<Cita> {
    return this.http.patch<Cita>(`${this.apiUrl}/${id}/confirmar`, null);
  }

  cancelar(id: number, motivo?: string): Observable<Cita> {
    const params = motivo ? new HttpParams().set('motivo', motivo) : undefined;
    return this.http.patch<Cita>(`${this.apiUrl}/${id}/cancelar`, null, { params });
  }

  completar(id: number): Observable<Cita> {
    return this.http.patch<Cita>(`${this.apiUrl}/${id}/completar`, null);
  }

  marcarRecordatorioEnviado(id: number): Observable<Cita> {
    return this.http.patch<Cita>(`${this.apiUrl}/${id}/recordatorio-enviado`, null);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}