import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'PetSalud - Sistema Veterinario';
  mostrarLayout: boolean = true;
  isLoggedIn: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Verificar autenticación
    this.isLoggedIn = this.authService.isLoggedIn();
    
    // Suscribirse a cambios de autenticación
    this.authService.usuarioActual$.subscribe(usuario => {
      this.isLoggedIn = !!usuario;
    });

    // Detectar cambios de ruta para mostrar/ocultar layout
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.mostrarLayout = !event.url.includes('/login');
      });
  }
}