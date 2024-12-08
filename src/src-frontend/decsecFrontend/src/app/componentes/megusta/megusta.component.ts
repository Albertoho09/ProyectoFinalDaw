// Importaciones necesarias
import { Component, Input } from '@angular/core';
import { PublicacionService } from '../../servicios/publicacion.service';

// Decorador del componente
@Component({
  selector: 'app-megusta',
  templateUrl: './megusta.component.html',
  styleUrl: './megusta.component.scss'
})
// Clase del componente
export class MegustaComponent {
  // Propiedades de entrada
  @Input() public numeroMeGusta!: number;
  @Input() public idPublicacion!: number;
  @Input() public estadoBoton!: Boolean;

  // Constructor del componente
  constructor(private publicacionService: PublicacionService){}
  // Método para cambiar el estado del botón
  cambiarEstadoBoton(){
    if(this.estadoBoton){
      this.publicacionService.darMegusta(this.idPublicacion).subscribe((data) => {
        console.log(data);
      });      
      this.numeroMeGusta++;
    }else{
      this.publicacionService.quitarMegusta(this.idPublicacion).subscribe((data) => {
        console.log(data);
      });     
      this.numeroMeGusta--;
    }
  }
}
