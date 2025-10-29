import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResultadoService } from '../../services/resultado.service';
import { NotificacionService } from '../../services/notificacion.service';
import { ResultadoVeterinario } from '../../models/resultado.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-resultado-pdf',
  templateUrl: './resultado-pdf.component.html',
  styleUrls: ['./resultado-pdf.component.css']
})
export class ResultadoPdfComponent implements OnInit {
  resultadoId: number | null = null;
  resultado: ResultadoVeterinario | null = null;
  loading: boolean = true;
  generandoPDF: boolean = false;
  pdfUrl: SafeResourceUrl | null = null;

  constructor(
    private resultadoService: ResultadoService,
    private notificacionService: NotificacionService,
    private route: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.resultadoId = +params['id'];
        this.cargarResultado();
      } else {
        this.notificacionService.errorGeneral('ID de resultado no válido');
        this.volver();
      }
    });
  }

  cargarResultado(): void {
    this.loading = true;
    this.resultadoService.obtenerPorId(this.resultadoId!).subscribe({
      next: (data) => {
        this.resultado = data;
        
        if (!this.resultado.validado) {
          this.notificacionService.warning('Este resultado aún no ha sido validado');
          this.volver();
          return;
        }
        
        this.loading = false;
        this.generarVistaPrevia();
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo cargar el resultado');
        this.volver();
      }
    });
  }

  generarVistaPrevia(): void {
    this.generandoPDF = true;
    this.resultadoService.descargarPDF(this.resultadoId!).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        this.pdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        this.generandoPDF = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo generar la vista previa del PDF');
        this.generandoPDF = false;
      }
    });
  }

  descargarPDF(): void {
    this.resultadoService.descargarPDF(this.resultadoId!).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `resultado_${this.resultadoId}_${Date.now()}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.notificacionService.success('PDF descargado exitosamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.notificacionService.errorGeneral('No se pudo descargar el PDF');
      }
    });
  }

  imprimirPDF(): void {
    if (this.pdfUrl) {
      const iframe = document.createElement('iframe');
      iframe.style.display = 'none';
      iframe.src = this.sanitizer.sanitize(1, this.pdfUrl) || '';
      document.body.appendChild(iframe);
      
      iframe.onload = () => {
        iframe.contentWindow?.print();
        setTimeout(() => {
          document.body.removeChild(iframe);
        }, 1000);
      };
    }
  }

  enviarPorEmail(): void {
    this.notificacionService.info('Función de envío por email en desarrollo');
  }

  volver(): void {
    this.router.navigate(['/resultados']);
  }

  verDetalle(): void {
    this.router.navigate(['/resultados/ver', this.resultadoId]);
  }
}