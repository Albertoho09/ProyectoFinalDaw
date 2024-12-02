import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../servicios/usuario.service';
import { usuarioSesion } from '../interfaces/Usuario';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit {

  constructor(private usuarioService: UsuarioService){}

  usuario!: usuarioSesion;

  ngOnInit(): void {
    this.usuarioService.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data;
      }
    )
  }
}
