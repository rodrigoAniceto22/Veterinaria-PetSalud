import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards (crear después)
// import { AuthGuard } from './guards/auth.guard';

// Components
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

// Dueños
import { DuenoListComponent } from './components/dueno/dueno-list.component';
import { DuenoFormComponent } from './components/dueno/dueno-form.component';

// Mascotas
import { MascotaListComponent } from './components/mascota/mascota-list.component';
import { MascotaFormComponent } from './components/mascota/mascota-form.component';

// Veterinarios
import { VeterinarioListComponent } from './components/veterinario/veterinario-list.component';
import { VeterinarioFormComponent } from './components/veterinario/veterinario-form.component';

// Técnicos
import { TecnicoListComponent } from './components/tecnico/tecnico-list.component';
import { TecnicoFormComponent } from './components/tecnico/tecnico-form.component';

// Órdenes
import { OrdenListComponent } from './components/orden/orden-list.component';
import { OrdenFormComponent } from './components/orden/orden-form.component';

// Toma Muestras
import { TomaMuestraListComponent } from './components/toma-muestra/toma-muestra-list.component';
import { TomaMuestraFormComponent } from './components/toma-muestra/toma-muestra-form.component';

// Resultados
import { ResultadoListComponent } from './components/resultado/resultado-list.component';
import { ResultadoFormComponent } from './components/resultado/resultado-form.component';
import { ResultadoPdfComponent } from './components/resultado/resultado-pdf.component';

// Facturas
import { FacturaListComponent } from './components/factura/factura-list.component';
import { FacturaFormComponent } from './components/factura/factura-form.component';

// Reportes
import { ReporteKpiComponent } from './components/reporte/reporte-kpi.component';
import { ReporteLaboratorioComponent } from './components/reporte/reporte-laboratorio.component';

const routes: Routes = [
  // Ruta por defecto
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  
  // Login
  { path: 'login', component: LoginComponent },
  
  // Dashboard (requiere autenticación)
  { path: 'dashboard', component: DashboardComponent },
  
  // Dueños
  { path: 'duenos', component: DuenoListComponent },
  { path: 'duenos/nuevo', component: DuenoFormComponent },
  { path: 'duenos/editar/:id', component: DuenoFormComponent },
  { path: 'duenos/ver/:id', component: DuenoFormComponent },
  
  // Mascotas
  { path: 'mascotas', component: MascotaListComponent },
  { path: 'mascotas/nueva', component: MascotaFormComponent },
  { path: 'mascotas/editar/:id', component: MascotaFormComponent },
  { path: 'mascotas/ver/:id', component: MascotaFormComponent },
  
  // Veterinarios
  { path: 'veterinarios', component: VeterinarioListComponent },
  { path: 'veterinarios/nuevo', component: VeterinarioFormComponent },
  { path: 'veterinarios/editar/:id', component: VeterinarioFormComponent },
  { path: 'veterinarios/ver/:id', component: VeterinarioFormComponent },
  
  // Técnicos
  { path: 'tecnicos', component: TecnicoListComponent },
  { path: 'tecnicos/nuevo', component: TecnicoFormComponent },
  { path: 'tecnicos/editar/:id', component: TecnicoFormComponent },
  { path: 'tecnicos/ver/:id', component: TecnicoFormComponent },
  
  // Órdenes
  { path: 'ordenes', component: OrdenListComponent },
  { path: 'ordenes/nueva', component: OrdenFormComponent },
  { path: 'ordenes/editar/:id', component: OrdenFormComponent },
  { path: 'ordenes/ver/:id', component: OrdenFormComponent },
  
  // Toma de Muestras
  { path: 'toma-muestras', component: TomaMuestraListComponent },
  { path: 'toma-muestras/nueva', component: TomaMuestraFormComponent },
  { path: 'toma-muestras/editar/:id', component: TomaMuestraFormComponent },
  { path: 'toma-muestras/ver/:id', component: TomaMuestraFormComponent },
  
  // Resultados
  { path: 'resultados', component: ResultadoListComponent },
  { path: 'resultados/nuevo', component: ResultadoFormComponent },
  { path: 'resultados/editar/:id', component: ResultadoFormComponent },
  { path: 'resultados/ver/:id', component: ResultadoFormComponent },
  { path: 'resultados/pdf/:id', component: ResultadoPdfComponent },
  
  // Facturas
  { path: 'facturas', component: FacturaListComponent },
  { path: 'facturas/nueva', component: FacturaFormComponent },
  { path: 'facturas/editar/:id', component: FacturaFormComponent },
  { path: 'facturas/ver/:id', component: FacturaFormComponent },
  
  // Reportes
  { path: 'reportes/kpis', component: ReporteKpiComponent },
  { path: 'reportes/laboratorio', component: ReporteLaboratorioComponent },
  
  // Ruta 404
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }