import { Component, OnInit } from '@angular/core';
import { PublicacionService } from '../../servicios/publicacion.service';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioAdmin } from '../../interfaces/Usuario';
import { publicacionAdmin } from '../../interfaces/Publicacion';

@Component({
  selector: 'app-tablapublicacion',
  templateUrl: './tablapublicacion.component.html',
  styleUrl: './tablapublicacion.component.scss'
})
export class TablapublicacionComponent implements OnInit {

  publicaciones!: publicacionAdmin[];
  usuario!: usuarioAdmin;

  constructor(private servicioPubli: PublicacionService, private servicioUsu: UsuarioService) { }
  ngOnInit(): void {
    this.servicioPubli.obtenerPublicaciones().subscribe(
      (data) => {
        this.publicaciones = data;
      }
    )

    this.servicioUsu.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data
      }
    )
  }

  obtenerUsuarioToken(): usuarioAdmin | null {
    return null;
  }

  mostrarImagen() {
    throw new Error('Method not implemented.');
  }
  deleteProduct(_t24: any) {
    throw new Error('Method not implemented.');
  }

}
