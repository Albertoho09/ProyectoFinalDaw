import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { Publicacion } from '../../interfaces/Publicacion';
import { Comentario } from '../../interfaces/Comentario';
import { ComentarioService } from '../../servicios/comentario.service';
import { usuarioSesion } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { PublicacionService } from '../../servicios/publicacion.service';

@Component({
  selector: 'app-publicacion-card',
  templateUrl: './publicacion-card.component.html',
  styleUrl: './publicacion-card.component.scss'
})
export class PublicacionCardComponent implements OnInit {

  visible: boolean = false;
  usuario: usuarioSesion | undefined;

  @Input() publicacion!: Publicacion;
  @Input() media!: Boolean;
  @Output() eliminar = new EventEmitter<number>();
  comentarios: Comentario[] = [];
  mensaje: String = '';
  
  constructor(private comentarioService: ComentarioService, private publicacionesService: PublicacionService, private usuarioService: UsuarioService, private messageService: MessageService, private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
    this.usuarioService.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data
      }
    )
  }

  @ViewChild(ConfirmPopup) confirmPopup!: ConfirmPopup;

  accept() {
    this.confirmPopup.accept();
  }

  reject() {
    this.confirmPopup.reject();
  }

  confirm(event: Event) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Estas seguro que deseas borrar esta publicación?',
      accept: () => {
        this.publicacionesService.borrarPublicacion(this.publicacion.id).subscribe(
          response => {
            this.eliminar.emit(this.publicacion.id);
          },
          error => {
            console.error('Error capturado:', error);
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al borrar', life: 3000 });
          }
        );
      },
      reject: () => {
      }
    });
  }

  abrirComentarios() {
    this.comentarioService.ObtenerComentariosPublicacion(this.publicacion.id).subscribe((comentariosSearch) => {
      this.comentarios = comentariosSearch;
    });
    this.visible = true;
  }

  enviarMensaje() {
    this.comentarioService.CrearComentario(this.publicacion.id, this.mensaje).subscribe((nuevoComentario) => {
      this.comentarios.push(nuevoComentario);
      this.mensaje = '';
    });
  }

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
