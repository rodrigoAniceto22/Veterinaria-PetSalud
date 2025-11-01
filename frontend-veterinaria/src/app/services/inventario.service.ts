import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventario, InventarioAlertas } from '../models/inventario.model';

@Injectable({
  providedIn: 'root'
})
export class InventarioService {
  private apiUrl = 'http://localhost:8080/api/inventario';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(this.apiUrl);
  }

  listarActivos(): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(`${this.apiUrl}/activos`);
  }

  obtenerPorId(id: number): Observable<Inventario> {
    return this.http.get<Inventario>(`${this.apiUrl}/${id}`);
  }

  buscarPorCodigo(codigo: string): Observable<Inventario> {
    return this.http.get<Inventario>(`${this.apiUrl}/codigo/${codigo}`);
  }

  buscarPorNombre(nombre: string): Observable<Inventario[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<Inventario[]>(`${this.apiUrl}/buscar`, { params });
  }

  buscarPorCategoria(categoria: string): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(`${this.apiUrl}/categoria/${categoria}`);
  }

  obtenerStockBajo(): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(`${this.apiUrl}/alertas/stock-bajo`);
  }

  obtenerVencidos(): Observable<Inventario[]> {
    return this.http.get<Inventario[]>(`${this.apiUrl}/alertas/vencidos`);
  }

  obtenerProximosAVencer(dias: number = 30): Observable<Inventario[]> {
    const params = new HttpParams().set('dias', dias.toString());
    return this.http.get<Inventario[]>(`${this.apiUrl}/alertas/proximos-vencer`, { params });
  }

  obtenerAlertas(): Observable<InventarioAlertas> {
    return this.http.get<InventarioAlertas>(`${this.apiUrl}/alertas`);
  }

  crear(inventario: Inventario): Observable<Inventario> {
    return this.http.post<Inventario>(this.apiUrl, inventario);
  }

  actualizar(id: number, inventario: Inventario): Observable<Inventario> {
    return this.http.put<Inventario>(`${this.apiUrl}/${id}`, inventario);
  }

  actualizarStock(id: number, cantidad: number, operacion: string): Observable<Inventario> {
    const params = new HttpParams()
      .set('cantidad', cantidad.toString())
      .set('operacion', operacion);
    return this.http.patch<Inventario>(`${this.apiUrl}/${id}/stock`, null, { params });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  calcularValorTotal(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/estadisticas/valor-total`);
  }

  contarActivos(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/estadisticas/total-activos`);
  }
}