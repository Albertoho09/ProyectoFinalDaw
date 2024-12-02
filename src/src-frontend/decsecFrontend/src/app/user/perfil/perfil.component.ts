import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Usuario, usuarioPerfil, usuarioSesion } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';
import { Publicacion } from '../../interfaces/Publicacion';
import { PublicacionService } from '../../servicios/publicacion.service';
import { PeticionService } from '../../servicios/peticion.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent implements OnInit {

  constructor(private route: ActivatedRoute, private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private peticionesService: PeticionService, private messageService: MessageService) { }

  imagenes: string[] = ['assets/fondo/guts.webp', 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ];
  usuarioPerfil!: usuarioPerfil;
  usuarioSesion!: usuarioSesion;
  estadoMenu: String = 'publicaciones';
  estadoPerfil: String = 'PUBLICO';
  Publicaciones: Publicacion[] | undefined;
  ngOnInit() {
    this.route.params.subscribe(params => {
      const userNick = params['nick'];
      const userJson = sessionStorage.getItem('currentUser');
      this.usuarioSesion = userJson ? JSON.parse(userJson) : null;

      this.usuarioService.ObtenerUsuarioNick(userNick).subscribe(
        (data) => {
          this.usuarioPerfil = data;
          this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe(
            response => {
              this.Publicaciones = response;
            },
            error => {
              if (error.error == "Publicaciones no disponibles, usuario privado.") {
                this.estadoPerfil = 'PRIVADO';
              }
            }
          );
        }
      )
    });
  }

  manejarEliminacionPerfil(id: number) {
    this.Publicaciones = this.Publicaciones?.filter(pub => pub.id !== id);
    this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Publicación borrada', life: 3000 });
  }

  cambiarVista(vista: string) {
    // Lógica para cambiar la vista
    console.log('Cambiando a la vista:', vista);

    if (vista == 'publicaciones') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    } else if (vista == 'media') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.Publicaciones = this.Publicaciones?.filter(publicacion => publicacion.imagenes && publicacion.imagenes.length > 0);
        this.estadoMenu = vista
      });
    } else if (vista == 'meGustan') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email, true).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    }
  }
}
