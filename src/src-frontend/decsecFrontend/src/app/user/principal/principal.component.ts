import { Component, OnInit } from '@angular/core';
import { usuarioDTO, usuarioSearch } from '../../interfaces/Usuario'; // Importación de interfaces para usuarioDTO y usuarioSearch
import { UsuarioService } from '../../servicios/usuario.service'; // Importación del servicio de usuario
import { MessageService } from 'primeng/api'; // Importación del servicio de mensajes de PrimeNG
import { Router } from '@angular/router'; // Importación del router de Angular
import { PublicacionService } from '../../servicios/publicacion.service'; // Importación del servicio de publicaciones
import { Publicacion } from '../../interfaces/Publicacion'; // Importación de la interfaz de Publicacion

// Definición de la interfaz para eventos de autocompletado
interface AutoCompleteCompleteEvent {
  originalEvent: Event;
  query: string;
}
// Definición de la interfaz para los días
interface Days {
  name: string;
  code: number;
}


@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrl: './principal.component.scss'
})
export class PrincipalComponent {

  usuariosSearch: usuarioSearch[] | null = null; // Variable para almacenar los usuarios de búsqueda

  publicacionesFeed: Publicacion[] | null = []; // Variable para almacenar las publicaciones del feed

  selectedusuariosSearchAdvanced: any | null = null; // Variable para almacenar el usuario seleccionado avanzado

  filteredusuariosSearch: any[] = []; // Variable para almacenar los usuarios filtrados

  usuario: usuarioDTO | null = null; // Variable para almacenar el usuario actual

  days!: Days[]; // Variable para almacenar los días

  selectedDay: Days = { name: '7 Dias', code: 7 }; // Día seleccionado por defecto

  constructor(private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private messageService: MessageService, private router: Router) {
    const currentUser = sessionStorage.getItem('currentUser'); // Obtener el usuario actual de la sesión
    this.usuario = currentUser ? JSON.parse(currentUser) : null; // Asignar el usuario actual si existe

    // Inicialización de los días disponibles
    this.days = [
      { name: '7 Dias', code: 7 },
      { name: '15 Dias', code: 15 },
      { name: '1 Mes', code: 30 },
      { name: '3 Meses', code: 90 }
    ];

    // Obtener usuarios de búsqueda al iniciar el componente
    this.usuarioService.obtenerUsuariosSearch().then((observable) => {
      observable.subscribe((usuariosSearch) => {
        this.usuariosSearch = usuariosSearch;
      });
    });
    // Obtener publicaciones del feed al iniciar el componente
    this.publicacionesService.obtenerPublicacionesFeed(this.selectedDay.code).subscribe((publicacionesFeed) => {
      this.publicacionesFeed = publicacionesFeed;
      console.log(this.publicacionesFeed); // Log para verificar las publicaciones obtenidas
    });
  }

  // Método para manejar la eliminación de una publicación principal
  manejarEliminacionPrincipal(id: number) {
    this.publicacionesFeed = this.publicacionesFeed!.filter(pub => pub.id !== id); // Filtrar publicaciones para eliminar la seleccionada
    this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Publicación borrada', life: 3000 }); // Mostrar mensaje de éxito
  }

  // Método para recargar las publicaciones
  recargarPublicaciones() {
    this.publicacionesService.obtenerPublicacionesFeed(this.selectedDay.code).subscribe((publicacionesFeed) => {
      this.publicacionesFeed = publicacionesFeed; // Asignar las nuevas publicaciones
    });
  }

  // Método para filtrar usuarios por país
  filterCountry(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = []; // Array para almacenar los usuarios filtrados
    let query = event.query; // Obtener la consulta del evento

    // Filtrar usuarios por el nombre del país
    for (let i = 0; i < (this.usuariosSearch as any[]).length; i++) {
      let country = (this.usuariosSearch as any[])[i];
      if (country.nick.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(country);
      }
    }

    this.filteredusuariosSearch = filtered; // Asignar los usuarios filtrados
  }

  // Método para buscar un usuario
  buscarUsuario() {
    if (this.selectedusuariosSearchAdvanced) {
      // Verificar si el usuario seleccionado avanzado tiene una propiedad 'nick'
      if (this.selectedusuariosSearchAdvanced.hasOwnProperty('nick')) {
        this.usuarioService.ObtenerUsuarioNick(this.selectedusuariosSearchAdvanced.nick).then((observable) => {
          observable.subscribe({
            next: (user: usuarioDTO) => {
              this.router.navigate(['/user/perfil', user.nick]); // Navegar al perfil del usuario
            },
            error: (error) => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener el usuario' }); // Mostrar mensaje de error
            }
          });
        });
      } else {
        // Si no tiene 'nick', asumir que es el nick directamente
        this.usuarioService.ObtenerUsuarioNick(this.selectedusuariosSearchAdvanced).then((observable) => {
          observable.subscribe({
            next: (user: usuarioDTO) => {
              this.router.navigate(['/user/perfil', user.nick]); // Navegar al perfil del usuario
            },
            error: (error) => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error || 'Error desconocido' }); // Mostrar mensaje de error
            }
          });
        });
      }
    }
  }

  // Método para visitar el perfil del usuario actual
  visitarPerfil() {
    const usuarioStr = sessionStorage.getItem('currentUser'); // Obtener el usuario actual de la sesión
    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr); // Parsear el usuario de la sesión
      this.router.navigate(['/user/perfil', usuario.nick]); // Navegar al perfil del usuario
    }
  }

}
