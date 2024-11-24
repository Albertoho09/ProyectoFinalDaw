import { Component, OnInit, Input } from '@angular/core';
import { Publicacion } from '../../../interfaces/Publicacion';

@Component({
  selector: 'app-publicacion-card',
  templateUrl: './publicacion-card.component.html',
  styleUrl: './publicacion-card.component.scss'
})
export class PublicacionCardComponent implements OnInit {

  visible: boolean = false;

  @Input() publicacion!: Publicacion;



  abrirComentarios() {
    this.visible = true;
  }

  getTiempoTranscurrido(): string {
    const ahora = new Date();
    const tiempoTranscurrido = ahora.getTime() - new Date(this.publicacion.fechaPublicacion).getTime();
    const minutos = Math.floor(tiempoTranscurrido / (1000 * 60));
    const horas = Math.floor(tiempoTranscurrido / (1000 * 60 * 60));
    const dias = Math.floor(tiempoTranscurrido / (1000 * 60 * 60 * 24));

    if (dias > 0) {
      return `Hace ${dias} día${dias > 1 ? 's' : ''}`;
    } else if (horas > 0) {
      return `Hace ${horas} hora${horas > 1 ? 's' : ''}`;
    } else {
      return `Hace ${minutos} minuto${minutos > 1 ? 's' : ''}`;
    }
  }

  responsiveOptions: any[] = [
    {
      breakpoint: '1024px',
      numVisible: 5
    },
    {
      breakpoint: '768px',
      numVisible: 3
    },
    {
      breakpoint: '560px',
      numVisible: 1
    }
  ];
  images: any[] | undefined;

  ngOnInit(): void {
    this.images = [
      { url: 'assets/fondo/screenshot.png' },
      { url: 'assets/fondo/guts.webp' },
      { url: 'assets/fondo/fondoOscuro.jpg' }
    ];
  }
}
