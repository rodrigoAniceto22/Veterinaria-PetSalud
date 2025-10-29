// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DuenoListComponent } from './components/dueno/dueno-list.component';
import { DuenoFormComponent } from './components/dueno/dueno-form.component';
import { MascotaListComponent } from './components/mascota/mascota-list.component';
import { MascotaFormComponent } from './components/mascota/mascota-form.component';
import { OrdenListComponent } from './components/orden/orden-list.component';
import { OrdenFormComponent } from './components/orden/orden-form.component';
import { TomaMuestraListComponent } from './components/toma-muestra/toma-muestra-list.component';
import { TomaMuestraFormComponent } from './components/toma-muestra/toma-muestra-form.component';
import { ResultadoListComponent } from './components/resultado/resultado-list.component';
import { ResultadoFormComponent } from './components/resultado/resultado-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/duenos', pathMatch: 'full' },
  
  // Dueños
  { path: 'duenos', component: DuenoListComponent },
  { path: 'duenos/nuevo', component: DuenoFormComponent },
  { path: 'duenos/editar/:id', component: DuenoFormComponent },
  
  // Mascotas
  { path: 'mascotas', component: MascotaListComponent },
  { path: 'mascotas/nueva', component: MascotaFormComponent },
  { path: 'mascotas/editar/:id', component: MascotaFormComponent },
  
  // Órdenes
  { path: 'ordenes', component: OrdenListComponent },
  { path: 'ordenes/nueva', component: OrdenFormComponent },
  
  // Toma de Muestras
  { path: 'toma-muestras', component: TomaMuestraListComponent },
  { path: 'toma-muestras/nueva', component: TomaMuestraFormComponent },
  
  // Resultados
  { path: 'resultados', component: ResultadoListComponent },
  { path: 'resultados/nuevo', component: ResultadoFormComponent },
  
  { path: '**', redirectTo: '/duenos' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }