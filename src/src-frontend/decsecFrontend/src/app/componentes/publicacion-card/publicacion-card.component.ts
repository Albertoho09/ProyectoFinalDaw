import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { Publicacion } from '../../interfaces/Publicacion';
import { Comentario } from '../../interfaces/Comentario';
import { ComentarioService } from '../../servicios/comentario.service';
import { UsuarioService } from '../../servicios/usuario.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { PublicacionService } from '../../servicios/publicacion.service';
import { usuarioDTO } from '../../interfaces/Usuario';

@Component({
  selector: 'app-publicacion-card',
  templateUrl: './publicacion-card.component.html',
  styleUrl: './publicacion-card.component.scss'
})
export class PublicacionCardComponent{

  // Variable para controlar la visibilidad de los comentarios
  visible: boolean = false;
  // Variable para almacenar el usuario actual
  usuario: usuarioDTO | undefined;

  // Entrada para la publicación
  @Input() publicacion!: Publicacion;
  // Entrada para indicar si se muestra la media
  @Input() media!: Boolean;
  // Salida para emitir el evento de eliminación
  @Output() eliminar = new EventEmitter<number>();
  // Array para almacenar los comentarios
  comentarios: Comentario[] = [];
  // Variable para el mensaje a enviar
  mensaje: String = '';
  
  // Constructor del componente
  constructor(private comentarioService: ComentarioService, private publicacionesService: PublicacionService, private usuarioService: UsuarioService, private messageService: MessageService, private confirmationService: ConfirmationService) {
    // Obtener el usuario actual de la sesión
    const userJson = sessionStorage.getItem('currentUser');
    this.usuario = userJson ? JSON.parse(userJson) : null;

   }
  // Referencia al componente de confirmación
  @ViewChild(ConfirmPopup) confirmPopup!: ConfirmPopup;

  // Método para aceptar la confirmación
  accept() {
    this.confirmPopup.accept();
  }

  // Método para rechazar la confirmación
  reject() {
    this.confirmPopup.reject();
  }

  // Método para mostrar la confirmación de eliminación
  confirm(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Estas seguro que deseas borrar esta publicación?',
      accept: () => {
        // Servicio para borrar la publicación
        this.publicacionesService.borrarPublicacion(this.publicacion.id).subscribe(
          response => {
            // Emitir el evento de eliminación
            this.eliminar.emit(this.publicacion.id);
          },
          error => {
            console.error('Error capturado:', error);
            // Mostrar mensaje de error
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al borrar', life: 3000 });
          }
        );
      },
      reject: () => {
      }
    });
  }

  // Método para abrir los comentarios
  abrirComentarios() {
    // Servicio para obtener los comentarios de la publicación
    this.comentarioService.ObtenerComentariosPublicacion(this.publicacion.id).subscribe((comentariosSearch) => {
      this.comentarios = comentariosSearch;
    });
    // Mostrar los comentarios
    this.visible = true;
  }

  // Método para enviar un mensaje
  enviarMensaje() {
    // Servicio para crear un nuevo comentario
    this.comentarioService.CrearComentario(this.publicacion.id, this.mensaje).subscribe((nuevoComentario) => {
      // Agregar el nuevo comentario al array
      this.comentarios.push(nuevoComentario);
      // Limpiar el mensaje
      this.mensaje = '';
    });
  }

  // Método para calcular el tiempo transcurrido desde la fecha de publicación
  getTiempoTranscurrido(fecha: Date): string {
    const ahora = new Date();
    const tiempoTranscurrido = ahora.getTime() - new Date(fecha).getTime();
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

  // Opciones de visualización responsiva
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

    // Nuevas variables
    editandoComentario: boolean = false;
    nuevoComentario: string = '';
  
    // Método para activar el modo de edición
    editarComentario() {
      this.editandoComentario = true;
      this.nuevoComentario = this.publicacion.comentarioUsuario; // Inicializa el nuevo comentario
    }
  
    // Método para guardar el nuevo comentario
    confirmarEdicion() {
      const updates = { comentarioUsuario: this.nuevoComentario }; // Cambios a aplicar

      this.publicacionesService.actualizarParcialmente(this.publicacion.id, updates).subscribe(
        response => {
          console.log('Publicación actualizada:', response);
          this.publicacion.comentarioUsuario = this.nuevoComentario; // Actualizar localmente
          this.editandoComentario = false; // Desactiva el modo de edición
        },
        error => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al editar', life: 3000 });
        }
      );
    }
  
    // Método para cancelar la edición
    cancelarEdicion() {
      this.editandoComentario = false; // Simplemente desactiva el modo de edición
    }

}
