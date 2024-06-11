import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { iif } from 'rxjs';
import { AuthServiceService } from '../../servicios/auth-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent{


visiblePeticiones: boolean = false;
visibleAmigos: boolean = false;

  constructor(private authservice: AuthServiceService) { }

  cambiar() {
    const menu = document.querySelectorAll('.surface-overlay'); // Selector que identifica tu menú
    menu.forEach(element => {
      if(element.classList.contains('hidden')){
        element.classList.remove('hidden');
      }else{
        element.classList.add('hidden');
      }
    }); 
  }

  cerrarSesion(){
    this.authservice.logout();
  }

  abrirDialogPeticiones() {
    this.visiblePeticiones = true;
  }
  abrirDialogAmigos() {
    this.visibleAmigos = true;
  }
}
