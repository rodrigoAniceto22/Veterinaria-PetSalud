// src/app/app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Components - Dueno
import { DuenoListComponent } from './components/dueno/dueno-list.component';
import { DuenoFormComponent } from './components/dueno/dueno-form.component';

// Components - Mascota
import { MascotaListComponent } from './components/mascota/mascota-list.component';
import { MascotaFormComponent } from './components/mascota/mascota-form.component';

// Components - Orden
import { OrdenListComponent } from './components/orden/orden-list.component';
import { OrdenFormComponent } from './components/orden/orden-form.component';

// Components - Toma Muestra
import { TomaMuestraListComponent } from './components/toma-muestra/toma-muestra-list.component';
import { TomaMuestraFormComponent } from './components/toma-muestra/toma-muestra-form.component';

// Components - Resultado
import { ResultadoListComponent } from './components/resultado/resultado-list.component';
import { ResultadoFormComponent } from './components/resultado/resultado-form.component';

// Components - Shared
import { NavbarComponent } from './components/shared/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    DuenoListComponent,
    DuenoFormComponent,
    MascotaListComponent,
    MascotaFormComponent,
    OrdenListComponent,
    OrdenFormComponent,
    TomaMuestraListComponent,
    TomaMuestraFormComponent,
    ResultadoListComponent,
    ResultadoFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }