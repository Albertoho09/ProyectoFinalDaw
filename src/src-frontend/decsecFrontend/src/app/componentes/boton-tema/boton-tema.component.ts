import { Component } from '@angular/core';
import { TemasServiceService } from '../../servicios/temas-service.service'

@Component({
  selector: 'app-boton-tema',
  templateUrl: './boton-tema.component.html',
  styleUrl: './boton-tema.component.scss'
})
export class BotonTemaComponent {
  constructor(private themeService: TemasServiceService) {
    this.aplicarTemaDesdeStorage(); // Llama al método al inicializar el componente
  }

  temaActual: string = 'claro'; // Inicialmente, establece el tema claro
  iconoTema: string = 'pi pi-sun'; // Inicialmente, establece el tema claro
  cambiarTema() {
    const container = document.getElementById("bodyId") as HTMLElement;
    if (this.temaActual == 'claro') {
        this.iconoTema = 'pi pi-moon iconoClaro';
        this.themeService.switchTheme("bootstrap4-dark-purple");
        container.classList.remove("bodyClaro");
        container.classList.add("bodyOscuro");
        this.temaActual = 'oscuro';
        sessionStorage.setItem('temaActual', 'oscuro');
    } else {
        this.iconoTema = 'pi pi-sun iconoOscuro';
        this.themeService.switchTheme("bootstrap4-light-purple");
        container.classList.remove("bodyOscuro");
        container.classList.add("bodyClaro");
        this.temaActual = 'claro';
        sessionStorage.setItem('temaActual', 'claro');
    }
  }

  aplicarTemaDesdeStorage() {
    const temaGuardado = sessionStorage.getItem('temaActual');
    const container = document.getElementById("bodyId") as HTMLElement;

    if (temaGuardado === 'oscuro') {
      this.iconoTema = 'pi pi-moon iconoClaro';
      this.themeService.switchTheme("bootstrap4-dark-purple");
      container.classList.remove("bodyClaro");
      container.classList.add("bodyOscuro");
      this.temaActual = 'oscuro';
      sessionStorage.setItem('temaActual', 'oscuro');
    } else {
      this.iconoTema = 'pi pi-sun iconoOscuro';
      this.themeService.switchTheme("bootstrap4-light-purple");
      container.classList.remove("bodyOscuro");
      container.classList.add("bodyClaro");
      this.temaActual = 'claro';
      sessionStorage.setItem('temaActual', 'claro');
    }
  }
}
