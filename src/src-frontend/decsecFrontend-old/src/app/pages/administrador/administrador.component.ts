import { Component } from '@angular/core';
import { MenuadminComponent } from '../../componentes/menuadmin/menuadmin.component';
import { BotonTemaComponent } from '../../componentes/boton-tema/boton-tema.component';
import { CardUsuarioAdminComponent } from '../../componentes/card-usuario-admin/card-usuario-admin.component';

@Component({
  selector: 'app-administrador',
  standalone: true,
  imports: [MenuadminComponent, BotonTemaComponent, CardUsuarioAdminComponent],
  templateUrl: './administrador.component.html',
  styleUrl: './administrador.component.scss'
})
export class AdministradorComponent {

}
