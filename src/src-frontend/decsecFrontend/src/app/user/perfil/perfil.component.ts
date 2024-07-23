import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Usuario, usuarioPerfil, usuarioSesion } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent implements OnInit {

  constructor(private route: ActivatedRoute, private usuarioService: UsuarioService) { }

  imagenes: string[] = ['assets/fondo/guts.webp', 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ];
  usuarioPerfil!: usuarioPerfil;
  usuarioSesion!: usuarioSesion;
  ngOnInit() {
    this.route.params.subscribe(params => {
      const userNick = params['nick'];
      this.usuarioService.ObtenerUsuarioNick(userNick).subscribe(
        response => {
          this.usuarioPerfil = response;
        },
        error => {
          console.log(error.data)
        }
      )
    });
    const userJson = sessionStorage.getItem('currentUser');
    this.usuarioSesion = userJson ? JSON.parse(userJson) : null;
  }
}
