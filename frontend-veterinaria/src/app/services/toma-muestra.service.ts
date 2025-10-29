import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TomaMuestraVet } from '../models/toma-muestra.model';

@Injectable({
  providedIn: 'root'
})
export class TomaMuestraService {
  private apiUrl = 'http://localhost:8080/api/toma-muestras';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<TomaMuestraVet[]> {
    return this.http.get<TomaMuestraVet[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<TomaMuestraVet> {
    return this.http.get<TomaMuestraVet>(`${this.apiUrl}/${id}`);
  }

  buscarPorOrden(idOrden: number): Observable<TomaMuestraVet> {
    return this.http.get<TomaMuestraVet>(`${this.apiUrl}/orden/${idOrden}`);
  }

  buscarPorTecnico(idTecnico: number): Observable<TomaMuestraVet[]> {
    return this.http.get<TomaMuestraVet[]>(`${this.apiUrl}/tecnico/${idTecnico}`);
  }

  buscarPorFecha(fecha: string): Observable<TomaMuestraVet[]> {
    return this.http.get<TomaMuestraVet[]>(`${this.apiUrl}/fecha?fecha=${fecha}`);
  }

  buscarPorTipoMuestra(tipo: string): Observable<TomaMuestraVet[]> {
    return this.http.get<TomaMuestraVet[]>(`${this.apiUrl}/tipo/${tipo}`);
  }

  obtenerPendientes(): Observable<TomaMuestraVet[]> {
    return this.http.get<TomaMuestraVet[]>(`${this.apiUrl}/pendientes`);
  }

  crear(tomaMuestra: TomaMuestraVet): Observable<TomaMuestraVet> {
    return this.http.post<TomaMuestraVet>(this.apiUrl, tomaMuestra);
  }

  actualizar(id: number, tomaMuestra: TomaMuestraVet): Observable<TomaMuestraVet> {
    return this.http.put<TomaMuestraVet>(`${this.apiUrl}/${id}`, tomaMuestra);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}