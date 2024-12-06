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

  usuario: usuarioDTO | null = null;
  visiblePeticiones: boolean = false;
  visibleAmigos: boolean = false;

  misPeticiones: Peticion[] = []
  todasPeticiones: Peticion[] = []


  constructor(private authservice: AuthServiceService, private router: Router, private peticionService: PeticionService, private messageService: MessageService) {
    const currentUser = sessionStorage.getItem('currentUser');
    this.usuario = currentUser ? JSON.parse(currentUser) : null;
  }

  cambiar() {
    const menu = document.querySelectorAll('.surface-overlay'); // Selector que identifica tu menú
    menu.forEach(element => {
      if (element.classList.contains('hidden')) {
        element.classList.remove('hidden');
      } else {
        element.classList.add('hidden');
      }
    });
  }

  cerrarSesion() {
    this.authservice.logout();
  }

  abrirDialogPeticiones() {
    this.todasPeticiones = [];
    this.misPeticiones = [];
    this.peticionService.obtenerMisPeticiones().then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion[]) => {
          this.misPeticiones = peticiones;
          this.visiblePeticiones = true;
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener las peticiones.' });
        }
      });
    });

  }

  abrirDialogAmigos() {
    this.todasPeticiones = [];
    this.misPeticiones = [];
    this.peticionService.obtenerMisAmigos().then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion[]) => {
          this.misPeticiones = peticiones;
          this.visibleAmigos = true;
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener las peticiones.' });
        }
      });
    });
  }

  menuPrincipal() {
    this.router.navigate(['/user/principal']);
  }

  modificarPeticion(peticion?: Peticion, estado?: string) {
    this.peticionService.cambiarEstado(peticion?.id, estado).then((observable) => {
      observable.subscribe({
        next: (peticiones: Peticion) => {
          this.misPeticiones = this.misPeticiones.filter(p => p.id !== peticiones.id);
          this.todasPeticiones.push(peticiones);
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al aceptar o denegar la petición.' });
        }
      });
    });
  }

  enviarAlPerfil(nick: String){
    this.visibleAmigos = false;
    this.router.navigate(['/user/perfil', nick]);
  }
}
