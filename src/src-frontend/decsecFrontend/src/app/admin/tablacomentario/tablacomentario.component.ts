import { Component, OnInit } from '@angular/core';
import { ComentarioService } from '../../servicios/comentario.service';
import { ComentarioAdmin } from '../../interfaces/Comentario';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-tablacomentario',
  templateUrl: './tablacomentario.component.html',
  styleUrl: './tablacomentario.component.scss'
})
export class TablacomentarioComponent implements OnInit {

  comentarios!: ComentarioAdmin[];

  constructor(private servicioComentario: ComentarioService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.servicioComentario.obtenerComentarios().subscribe(
      (data) => {
        this.comentarios = data;
      }
    )
  }

  deleteProduct(idComentario: number) {
    this.servicioComentario.borrarComentario(idComentario).subscribe(
      (data) => {
        this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Comentario Borrado' });
      },
      (error) => {
        this.messageService.add({ severity: 'errror', summary: 'Error', detail: 'Comentario NO borrado.' });
      }
    )
  }
}
