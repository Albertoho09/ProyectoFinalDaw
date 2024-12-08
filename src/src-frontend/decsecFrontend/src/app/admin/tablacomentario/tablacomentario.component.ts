import { Component, OnInit } from '@angular/core';
import { ComentarioService } from '../../servicios/comentario.service'; // Importación del servicio de comentarios
import { ComentarioAdmin } from '../../interfaces/Comentario'; // Importación de la interfaz de comentarios
import { MessageService } from 'primeng/api'; // Importación del servicio de mensajes de PrimeNG

@Component({
  selector: 'app-tablacomentario',
  templateUrl: './tablacomentario.component.html',
  styleUrl: './tablacomentario.component.scss'
})
export class TablacomentarioComponent implements OnInit {

  comentarios!: ComentarioAdmin[]; // Array para almacenar los comentarios

  constructor(private servicioComentario: ComentarioService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.servicioComentario.obtenerComentarios().subscribe(
      (data) => {
        this.comentarios = data; // Asignación de los comentarios obtenidos al array
      }
    )
  }

  deleteProduct(idComentario: number) {
    this.servicioComentario.borrarComentario(idComentario).subscribe(
      data => {
        this.comentarios = this.comentarios.filter(comentario => comentario.id !== idComentario); // Filtrado del array para eliminar el comentario
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se ha podido borrar Comentario.' }); // Mensaje de error
      }
    )
  }
}
