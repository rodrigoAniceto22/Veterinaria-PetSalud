import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Factura } from '../models/factura.model';

@Injectable({
  providedIn: 'root'
})
export class FacturaService {
  private apiUrl = 'http://localhost:8080/api/facturas';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<Factura[]> {
    return this.http.get<Factura[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/${id}`);
  }

  buscarPorDueno(idDueno: number): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/dueno/${idDueno}`);
  }

  buscarPorFecha(fecha: string): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/fecha?fecha=${fecha}`);
  }

  buscarPorRangoFechas(inicio: string, fin: string): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/rango?inicio=${inicio}&fin=${fin}`);
  }

  buscarPorMetodoPago(metodo: string): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/metodo-pago/${metodo}`);
  }

  obtenerPendientes(): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.apiUrl}/pendientes`);
  }

  crear(factura: Factura): Observable<Factura> {
    return this.http.post<Factura>(this.apiUrl, factura);
  }

  actualizar(id: number, factura: Factura): Observable<Factura> {
    return this.http.put<Factura>(`${this.apiUrl}/${id}`, factura);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  marcarComoPagada(id: number): Observable<Factura> {
    return this.http.patch<Factura>(`${this.apiUrl}/${id}/pagar`, {});
  }

  calcularVentasDia(fecha: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/ventas-dia?fecha=${fecha}`);
  }

  calcularVentasMes(mes: number, anio: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/ventas-mes?mes=${mes}&anio=${anio}`);
  }
}