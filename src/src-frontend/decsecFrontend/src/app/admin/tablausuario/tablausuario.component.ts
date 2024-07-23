import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioSesion } from '../../interfaces/Usuario';

@Component({
  selector: 'app-tablausuario',
  templateUrl: './tablausuario.component.html',
  styleUrl: './tablausuario.component.scss'
})
export class TablausuarioComponent implements OnInit {

  usuarios!: usuarioSesion[];

  constructor(private servicio: UsuarioService) { }

  ngOnInit(): void {
    this.servicio.obtenerUsuarios().subscribe(
      response => {
        this.usuarios = response;
      },
      error => {
        console.log(error.data)
      }
    )
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
