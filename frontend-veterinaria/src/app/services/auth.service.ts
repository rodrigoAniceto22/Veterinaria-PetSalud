import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  private usuarioActualSubject = new BehaviorSubject<Usuario | null>(null);
  public usuarioActual$ = this.usuarioActualSubject.asObservable();

  constructor(private http: HttpClient) {
    this.cargarUsuarioDesdeStorage();
  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response.success) {
          this.guardarUsuario(response.usuario);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('usuario');
    localStorage.removeItem('token');
    this.usuarioActualSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('usuario');
  }

  getUsuarioActual(): Usuario | null {
    return this.usuarioActualSubject.value;
  }

  getRolUsuario(): string {
    const usuario = this.getUsuarioActual();
    return usuario ? usuario.rol : '';
  }

  tieneRol(roles: string[]): boolean {
    const rolActual = this.getRolUsuario();
    return roles.includes(rolActual);
  }

  cambiarPassword(idUsuario: number, passwords: { passwordActual: string; passwordNueva: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${idUsuario}/cambiar-password`, passwords);
  }

  verificarUsername(username: string): Observable<{ disponible: boolean }> {
    return this.http.get<{ disponible: boolean }>(`${this.apiUrl}/verificar-username/${username}`);
  }

  private guardarUsuario(usuario: Usuario): void {
    localStorage.setItem('usuario', JSON.stringify(usuario));
    this.usuarioActualSubject.next(usuario);
  }

  private cargarUsuarioDesdeStorage(): void {
    const usuarioStr = localStorage.getItem('usuario');
    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr);
      this.usuarioActualSubject.next(usuario);
    }
  }
}