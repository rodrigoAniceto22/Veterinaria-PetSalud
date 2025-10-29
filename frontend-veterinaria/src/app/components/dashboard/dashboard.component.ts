import { Component, OnInit } from '@angular/core';
import { ReporteService } from '../../services/reporte.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  loading: boolean = true;
  dashboardData: any = null;
  usuarioRol: string = '';
  
  // KPIs principales
  totalOrdenes: number = 0;
  ordenesPendientes: number = 0;
  resultadosValidados: number = 0;
  ingresosMes: number = 0;
  
  // Gráficas
  ordenesPorEstado: any[] = [];
  especiesAtendidas: any[] = [];

  constructor(
    private reporteService: ReporteService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.usuarioRol = this.authService.getRolUsuario();
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.loading = true;
    
    this.reporteService.obtenerDashboard().subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.procesarDatos(data);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar dashboard:', error);
        this.loading = false;
      }
    });
  }

  procesarDatos(data: any): void {
    this.totalOrdenes = data.totalOrdenes || 0;
    this.ordenesPendientes = data.ordenesPendientes || 0;
    this.resultadosValidados = data.resultadosValidados || 0;
    this.ingresosMes = data.ingresosMes || 0;
    this.ordenesPorEstado = data.ordenesPorEstado || [];
    this.especiesAtendidas = data.especiesAtendidas || [];
  }

  refrescar(): void {
    this.cargarDashboard();
  }

  getBadgeClass(estado: string): string {
    const badges: any = {
      'PENDIENTE': 'bg-warning',
      'EN_PROCESO': 'bg-info',
      'COMPLETADA': 'bg-success',
      'CANCELADA': 'bg-danger'
    };
    return badges[estado] || 'bg-secondary';
  }
}