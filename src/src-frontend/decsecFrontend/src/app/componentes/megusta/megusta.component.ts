import { Component, Input } from '@angular/core';
import { PublicacionService } from '../../servicios/publicacion.service';

@Component({
  selector: 'app-megusta',
  templateUrl: './megusta.component.html',
  styleUrl: './megusta.component.scss'
})
export class MegustaComponent {
  @Input() public numeroMeGusta!: number;
  @Input() public idPublicacion!: number;
  @Input() public estadoBoton!: Boolean;

  constructor(private publicacionService: PublicacionService){}
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
