import { Component, OnInit } from '@angular/core';
import { PublicacionService } from '../../servicios/publicacion.service';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioDTO } from '../../interfaces/Usuario';
import { publicacionAdmin } from '../../interfaces/Publicacion';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-tablapublicacion',
  templateUrl: './tablapublicacion.component.html',
  styleUrl: './tablapublicacion.component.scss'
})
export class TablapublicacionComponent{

  publicaciones!: publicacionAdmin[]; // Array para almacenar las publicaciones
  usuario!: usuarioDTO; // Objeto para almacenar el usuario
  responsiveOptions: any[] = [ // Opciones de visualización responsiva
    {
      breakpoint: '1500px',
      numVisible: 5
    },
    {
      breakpoint: '1024px',
      numVisible: 3
    },
    {
      breakpoint: '768px',
      numVisible: 2
    },
    {
      breakpoint: '560px',
      numVisible: 1
    }
  ];

  constructor(private servicioPubli: PublicacionService, private servicioUsu: UsuarioService, private messageService: MessageService) {
    // Obtener las publicaciones
    this.servicioPubli.obtenerPublicaciones().subscribe(
      (data) => {
        this.publicaciones = data; // Asignación de las publicaciones obtenidas al array
        this.publicaciones = data.map((publicacion: any) => ({
          ...publicacion,
          displayBasic: false
        }));
        this.publicaciones.forEach(element => {
          console.log(element);
        });
      }
    )

    // Obtener el usuario
    this.servicioUsu.obtenerUsuarioToken().then((observable) => {
      observable.subscribe({
        next: (data) => {
          this.usuario = data; // Asignación del usuario obtenido al objeto
        },
        error: (error) => {
          console.error(error);
        }
      });
    });
  }

  handleClick(publicacion: any) {
    // Establecer todas las galerías como no visibles
    this.publicaciones.forEach(p => p.displayBasic = false);
    // Mostrar solo la galería de la publicación clicada
    publicacion.displayBasic = true;
  }

  mostrarImagen() {
    throw new Error('Method not implemented.');
  }
  deleteProduct(idPublicacion: number) {
    // Borrar una publicación
    this.servicioPubli.borrarPublicacion(idPublicacion).subscribe(
      response => {
        this.publicaciones = this.publicaciones.filter(publicacion => publicacion.id !== idPublicacion); // Filtrado del array para eliminar la publicación
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al borrar publicacion'}); // Mensaje de error
      }
    );
  }

}
