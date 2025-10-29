import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  usuarioActual: Usuario | null = null;
  nombreUsuario: string = '';
  rolUsuario: string = '';

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.usuarioActual$.subscribe(usuario => {
      this.usuarioActual = usuario;
      if (usuario) {
        this.nombreUsuario = usuario.nombreCompleto || usuario.nombreUsuario;
        this.rolUsuario = usuario.rol;
      }
    });
  }

  cerrarSesion(): void {
    if (confirm('¿Está seguro que desea cerrar sesión?')) {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }

  irAPerfil(): void {
    this.router.navigate(['/perfil']);
  }

  mostrarNotificaciones(): void {
    alert('Sistema de notificaciones en desarrollo');
  }
}