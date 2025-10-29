import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

interface MenuItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
  submenu?: MenuItem[];
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  usuarioRol: string = '';
  menuItems: MenuItem[] = [];
  menuAbierto: { [key: string]: boolean } = {};

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.usuarioRol = this.authService.getRolUsuario();
    this.construirMenu();
  }

  construirMenu(): void {
    const menuCompleto: MenuItem[] = [
      {
        label: 'Dashboard',
        icon: 'fa-home',
        route: '/dashboard'
      },
      {
        label: 'Gestión de Clientes',
        icon: 'fa-users',
        route: '#',
        submenu: [
          { label: 'Dueños', icon: 'fa-user', route: '/duenos' },
          { label: 'Mascotas', icon: 'fa-dog', route: '/mascotas' }
        ]
      },
      {
        label: 'Personal',
        icon: 'fa-user-md',
        route: '#',
        roles: ['ADMIN', 'VETERINARIO'],
        submenu: [
          { label: 'Veterinarios', icon: 'fa-stethoscope', route: '/veterinarios' },
          { label: 'Técnicos', icon: 'fa-user-nurse', route: '/tecnicos' }
        ]
      },
      {
        label: 'Laboratorio',
        icon: 'fa-flask',
        route: '#',
        submenu: [
          { label: 'Órdenes', icon: 'fa-file-medical', route: '/ordenes' },
          { label: 'Toma de Muestras', icon: 'fa-syringe', route: '/toma-muestras' },
          { label: 'Resultados', icon: 'fa-clipboard-check', route: '/resultados' }
        ]
      },
      {
        label: 'Facturación',
        icon: 'fa-file-invoice-dollar',
        route: '#',
        roles: ['ADMIN', 'RECEPCIONISTA'],
        submenu: [
          { label: 'Facturas', icon: 'fa-receipt', route: '/facturas' }
        ]
      },
      {
        label: 'Reportes',
        icon: 'fa-chart-bar',
        route: '#',
        roles: ['ADMIN', 'VETERINARIO'],
        submenu: [
          { label: 'KPIs', icon: 'fa-tachometer-alt', route: '/reportes/kpis' },
          { label: 'Laboratorio', icon: 'fa-vial', route: '/reportes/laboratorio' }
        ]
      },
      {
        label: 'Administración',
        icon: 'fa-cog',
        route: '#',
        roles: ['ADMIN'],
        submenu: [
          { label: 'Usuarios', icon: 'fa-users-cog', route: '/usuarios' }
        ]
      }
    ];

    this.menuItems = menuCompleto.filter(item => this.tienePermiso(item));
  }

  tienePermiso(item: MenuItem): boolean {
    if (!item.roles || item.roles.length === 0) return true;
    return item.roles.includes(this.usuarioRol);
  }

  toggleSubmenu(label: string): void {
    this.menuAbierto[label] = !this.menuAbierto[label];
  }

  isSubmenuOpen(label: string): boolean {
    return this.menuAbierto[label] || false;
  }
}