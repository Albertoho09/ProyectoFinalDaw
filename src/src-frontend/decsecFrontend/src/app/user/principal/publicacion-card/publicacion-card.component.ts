import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-publicacion-card',
  templateUrl: './publicacion-card.component.html',
  styleUrl: './publicacion-card.component.scss'
})
export class PublicacionCardComponent implements OnInit {

  visible: boolean = false;
  abrirComentarios() {
    this.visible = true;
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
