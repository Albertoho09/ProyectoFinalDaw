import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioDTO } from '../../interfaces/Usuario';

@Component({
  selector: 'app-tablausuario',
  templateUrl: './tablausuario.component.html',
  styleUrl: './tablausuario.component.scss'
})
export class TablausuarioComponent{

  usuarios: usuarioDTO[] | null = [];

  constructor(private servicio: UsuarioService) { 
    this.servicio.obtenerUsuarios().then((observable) => {
      observable.subscribe({
        next: (response: usuarioDTO[]) => {
          this.usuarios = response;
        },
        error: (error) => {
          console.error(error);
        }
      });
    });
  }


  getSeverityPrivado(status: boolean) {
    switch (status) {
      case false:
        return 'success';
      case true:
        return 'danger';
    }
  }
  getSeverityRol(status: string) {
    switch (status) {
      case 'ROLE_USER':
        return 'success';
      case 'ROLE_ADMIN':
        return 'warning';
    }
    return 'danger';
  }
  deleteProduct(_t25: any) {
    throw new Error('Method not implemented.');
  }
}
