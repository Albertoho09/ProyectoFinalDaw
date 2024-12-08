import { Component } from '@angular/core';
import { TemasServiceService } from '../../servicios/temas-service.service'

@Component({
  selector: 'app-boton-tema',
  templateUrl: './boton-tema.component.html',
  styleUrl: './boton-tema.component.scss'
})
export class BotonTemaComponent {
  constructor(private themeService: TemasServiceService) {
    this.aplicarTemaDesdeStorage(); // Llama al método al inicializar el componente para aplicar el tema guardado en el almacenamiento de sesión
  }

  temaActual: string = 'claro'; // Inicialmente, establece el tema claro como el tema actual
  iconoTema: string = 'pi pi-sun'; // Inicialmente, establece el icono del sol para el tema claro
  cambiarTema() {
    const container = document.getElementById("bodyId") as HTMLElement; // Obtener el elemento del cuerpo de la página
    if (this.temaActual == 'claro') {
        this.iconoTema = 'pi pi-moon iconoClaro'; // Cambia el icono a la luna para indicar el tema oscuro
        this.themeService.switchTheme("bootstrap4-dark-purple"); // Cambia el tema a oscuro
        container.classList.remove("bodyClaro"); // Elimina la clase del cuerpo para el tema claro
        container.classList.add("bodyOscuro"); // Agrega la clase del cuerpo para el tema oscuro
        this.temaActual = 'oscuro'; // Establece el tema actual como oscuro
        sessionStorage.setItem('temaActual', 'oscuro'); // Guarda el tema actual en el almacenamiento de sesión
    } else {
        this.iconoTema = 'pi pi-sun iconoOscuro'; // Cambia el icono al sol para indicar el tema claro
        this.themeService.switchTheme("bootstrap4-light-purple"); // Cambia el tema a claro
        container.classList.remove("bodyOscuro"); // Elimina la clase del cuerpo para el tema oscuro
        container.classList.add("bodyClaro"); // Agrega la clase del cuerpo para el tema claro
        this.temaActual = 'claro'; // Establece el tema actual como claro
        sessionStorage.setItem('temaActual', 'claro'); // Guarda el tema actual en el almacenamiento de sesión
    }
  }

  aplicarTemaDesdeStorage() {
    const temaGuardado = sessionStorage.getItem('temaActual'); // Obtener el tema guardado en el almacenamiento de sesión
    const container = document.getElementById("bodyId") as HTMLElement; // Obtener el elemento del cuerpo de la página

    if (temaGuardado === 'oscuro') {
      this.iconoTema = 'pi pi-moon iconoClaro'; // Establece el icono de la luna para el tema oscuro
      this.themeService.switchTheme("bootstrap4-dark-purple"); // Cambia el tema a oscuro
      container.classList.remove("bodyClaro"); // Elimina la clase del cuerpo para el tema claro
      container.classList.add("bodyOscuro"); // Agrega la clase del cuerpo para el tema oscuro
      this.temaActual = 'oscuro'; // Establece el tema actual como oscuro
      sessionStorage.setItem('temaActual', 'oscuro'); // Guarda el tema actual en el almacenamiento de sesión
    } else {
      this.iconoTema = 'pi pi-sun iconoOscuro'; // Establece el icono del sol para el tema claro
      this.themeService.switchTheme("bootstrap4-light-purple"); // Cambia el tema a claro
      container.classList.remove("bodyOscuro"); // Elimina la clase del cuerpo para el tema oscuro
      container.classList.add("bodyClaro"); // Agrega la clase del cuerpo para el tema claro
      this.temaActual = 'claro'; // Establece el tema actual como claro
      sessionStorage.setItem('temaActual', 'claro'); // Guarda el tema actual en el almacenamiento de sesión
    }
  }
}
