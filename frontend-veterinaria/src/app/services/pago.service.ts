import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pago } from '../models/pago.model';

@Injectable({
  providedIn: 'root'
})
export class PagoService {
  private apiUrl = 'http://localhost:8080/api/pagos';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Pago[]> {
    return this.http.get<Pago[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/${id}`);
  }

  buscarPorNumero(numeroPago: string): Observable<Pago> {
    return this.http.get<Pago>(`${this.apiUrl}/numero/${numeroPago}`);
  }

  buscarPorDueno(idDueno: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/dueno/${idDueno}`);
  }

  buscarPorMascota(idMascota: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/mascota/${idMascota}`);
  }

  obtenerPendientes(): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/pendientes`);
  }

  obtenerVencidos(): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/vencidos`);
  }

  obtenerInternamientosActivos(): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiUrl}/internamientos/activos`);
  }

  crear(pago: Pago): Observable<Pago> {
    return this.http.post<Pago>(this.apiUrl, pago);
  }

  actualizar(id: number, pago: Pago): Observable<Pago> {
    return this.http.put<Pago>(`${this.apiUrl}/${id}`, pago);
  }

  registrarPago(id: number, montoPagado: number, metodoPago: string): Observable<Pago> {
    const params = new HttpParams()
      .set('montoPagado', montoPagado.toString())
      .set('metodoPago', metodoPago);
    return this.http.post<Pago>(`${this.apiUrl}/${id}/registrar-pago`, null, { params });
  }

  finalizarInternamiento(id: number): Observable<Pago> {
    return this.http.post<Pago>(`${this.apiUrl}/${id}/finalizar-internamiento`, null);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  calcularTotalPendiente(idDueno: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/dueno/${idDueno}/total-pendiente`);
  }

  calcularTotalCobrado(inicio: string, fin: string): Observable<number> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<number>(`${this.apiUrl}/estadisticas/total-cobrado`, { params });
  }
}