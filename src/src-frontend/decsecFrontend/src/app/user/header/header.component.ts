import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { iif } from 'rxjs';
import { AuthServiceService } from '../../servicios/auth-service.service';
import { Router } from '@angular/router';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioDTO } from '../../interfaces/Usuario';
import { Peticion } from '../../interfaces/Peticion';
import { PeticionService } from '../../servicios/peticion.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  // Variable para almacenar el usuario actual, puede ser nulo
  usuario: usuarioDTO | null = null;
  // Variables para controlar la visibilidad de los diálogos de peticiones y amigos
  visiblePeticiones: boolean = false;
  visibleAmigos: boolean = false;

  // Arrays para almacenar las peticiones
  misPeticiones: Peticion[] = []
  todasPeticiones: Peticion[] = []


  constructor(private authservice: AuthServiceService, private router: Router, private peticionService: PeticionService, private messageService: MessageService) {
    // Obtener el usuario actual de la sesión y asignarlo a la variable usuario
    const currentUser = sessionStorage.getItem('currentUser');
    this.usuario = currentUser ? JSON.parse(currentUser) : null;
  }

  // Método para cambiar la visibilidad del menú
  cambiar() {
    // Seleccionar todos los elementos del menú
    const menu = document.querySelectorAll('.surface-overlay'); // Selector que identifica tu menú
    // Iterar sobre los elementos del menú y cambiar su visibilidad
    menu.forEach(element => {
      if (element.classList.contains('hidden')) {
        element.classList.remove('hidden');
      } else {
        element.classList.add('hidden');
      }
    });
  }

  // Método para cerrar la sesión del usuario
  cerrarSesion() {
    this.authservice.logout();
  }

  // Método para abrir el diálogo de peticiones
  abrirDialogPeticiones() {
    // Limpiar los arrays de peticiones
    this.todasPeticiones = [];
    this.misPeticiones = [];
    // Servicio para obtener mis peticiones
    this.peticionService.obtenerMisPeticiones().then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion[]) => {
          // Asignar las peticiones obtenidas al array misPeticiones y mostrar el diálogo
          this.misPeticiones = peticiones;
          this.visiblePeticiones = true;
        },
        error: (error) => {
          // Mostrar mensaje de error si falla la obtención de peticiones
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener las peticiones.' });
        }
      });
    });

  }

  // Método para abrir el diálogo de amigos
  abrirDialogAmigos() {
    // Limpiar los arrays de peticiones
    this.todasPeticiones = [];
    this.misPeticiones = [];
    // Servicio para obtener mis amigos
    this.peticionService.obtenerMisAmigos().then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion[]) => {
          // Asignar las peticiones obtenidas al array misPeticiones y mostrar el diálogo
          this.misPeticiones = peticiones;
          this.visibleAmigos = true;
        },
        error: (error) => {
          // Mostrar mensaje de error si falla la obtención de amigos
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener las peticiones.' });
        }
      });
    });
  }

  // Método para navegar al menú principal
  menuPrincipal() {
    this.router.navigate(['/user/principal']);
  }

  // Método para modificar el estado de una petición
  modificarPeticion(peticion?: Peticion, estado?: string) {
    // Servicio para cambiar el estado de una petición
    this.peticionService.cambiarEstado(peticion?.id, estado).then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion) => {
          // Filtrar el array misPeticiones para eliminar la petición modificada y agregarla al array todasPeticiones
          this.misPeticiones = this.misPeticiones.filter(p => p.id !== peticiones.id);
          this.todasPeticiones.push(peticiones);
        },
        error: (error) => {
          // Mostrar mensaje de error si falla la modificación del estado de la petición
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al aceptar o denegar la petición.' });
        }
      });
    });
  }

  // Método para enviar al perfil de un usuario
  enviarAlPerfil(nick: String){
    // Ocultar el diálogo de amigos y navegar al perfil del usuario especificado
    this.visibleAmigos = false;
    this.router.navigate(['/user/perfil', nick]);
  }
}
