import { Component } from '@angular/core';
import { TemasServiceService } from '../../servicios/temas-service.service'
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-boton-tema',
  standalone: true,
  imports: [NgClass],
  templateUrl: './boton-tema.component.html',
  styleUrl: './boton-tema.component.scss'
})
export class BotonTemaComponent {
  constructor(private themeService: TemasServiceService) {}

  temaActual: string = 'claro'; // Inicialmente, establece el tema claro
  iconoTema: string = 'pi pi-sun'; // Inicialmente, establece el tema claro
  cambiarTema() {
    const container = document.getElementById("bodyLogin") as HTMLElement;
      if (this.temaActual == 'claro') {
          this.iconoTema = 'pi pi-moon iconoClaro';
          this.themeService.switchTheme("bootstrap4-dark-purple");
          container.classList.remove("bodyClaro");
          container.classList.add("bodyOscuro");
          this.temaActual = 'oscuro'
      }else{
          this.iconoTema = 'pi pi-sun iconoOscuro';
          this.themeService.switchTheme("bootstrap4-light-purple");
          container.classList.remove("bodyOscuro");
          container.classList.add("bodyClaro");
          this.temaActual = 'claro'
      }
  }
}
