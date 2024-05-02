import { Component, EventEmitter, Output } from '@angular/core';
import { AuthServiceService } from '../../servicios/auth-service.service';

@Component({
  selector: 'app-menuadmin',
  templateUrl: './menuadmin.component.html',
  styleUrl: './menuadmin.component.scss'
})
export class MenuadminComponent {

  @Output() opcionSeleccionada: EventEmitter<string> = new EventEmitter<string>();
  constructor(private authservice : AuthServiceService){}
  exit(){
    this.authservice.logout();
  }
  seleccionarOpcion(opcion: string): void {
    this.opcionSeleccionada.emit(opcion);
  }
}
