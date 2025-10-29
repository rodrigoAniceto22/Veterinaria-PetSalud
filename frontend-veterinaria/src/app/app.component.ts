// src/app/app.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Veterinaria PetSalud';
}

<!-- src/app/app.component.html -->
<app-navbar></app-navbar>
<div class="container">
  <router-outlet></router-outlet>
</div>

/* src/app/app.component.css */
.container {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}