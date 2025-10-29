import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Shared Components
import { NavbarComponent } from './components/shared/navbar.component';
import { SidebarComponent } from './components/shared/sidebar.component';

// Login & Dashboard
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

// Dueño Components
import { DuenoListComponent } from './components/dueno/dueno-list.component';
import { DuenoFormComponent } from './components/dueno/dueno-form.component';

// Mascota Components
import { MascotaListComponent } from './components/mascota/mascota-list.component';
import { MascotaFormComponent } from './components/mascota/mascota-form.component';

// Veterinario Components
import { VeterinarioListComponent } from './components/veterinario/veterinario-list.component';
import { VeterinarioFormComponent } from './components/veterinario/veterinario-form.component';

// Tecnico Components
import { TecnicoListComponent } from './components/tecnico/tecnico-list.component';
import { TecnicoFormComponent } from './components/tecnico/tecnico-form.component';

// Orden Components
import { OrdenListComponent } from './components/orden/orden-list.component';
import { OrdenFormComponent } from './components/orden/orden-form.component';

// Toma Muestra Components
import { TomaMuestraListComponent } from './components/toma-muestra/toma-muestra-list.component';
import { TomaMuestraFormComponent } from './components/toma-muestra/toma-muestra-form.component';

// Resultado Components
import { ResultadoListComponent } from './components/resultado/resultado-list.component';
import { ResultadoFormComponent } from './components/resultado/resultado-form.component';
import { ResultadoPdfComponent } from './components/resultado/resultado-pdf.component';

// Factura Components
import { FacturaListComponent } from './components/factura/factura-list.component';
import { FacturaFormComponent } from './components/factura/factura-form.component';

// Reporte Components
import { ReporteKpiComponent } from './components/reporte/reporte-kpi.component';
import { ReporteLaboratorioComponent } from './components/reporte/reporte-laboratorio.component';

@NgModule({
  declarations: [
    AppComponent,
    
    // Shared
    NavbarComponent,
    SidebarComponent,
    
    // Login & Dashboard
    LoginComponent,
    DashboardComponent,
    
    // Dueños
    DuenoListComponent,
    DuenoFormComponent,
    
    // Mascotas
    MascotaListComponent,
    MascotaFormComponent,
    
    // Veterinarios
    VeterinarioListComponent,
    VeterinarioFormComponent,
    
    // Técnicos
    TecnicoListComponent,
    TecnicoFormComponent,
    
    // Órdenes
    OrdenListComponent,
    OrdenFormComponent,
    
    // Toma Muestras
    TomaMuestraListComponent,
    TomaMuestraFormComponent,
    
    // Resultados
    ResultadoListComponent,
    ResultadoFormComponent,
    ResultadoPdfComponent,
    
    // Facturas
    FacturaListComponent,
    FacturaFormComponent,
    
    // Reportes
    ReporteKpiComponent,
    ReporteLaboratorioComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }