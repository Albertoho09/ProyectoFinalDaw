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
  responsiveOptions: any[] = [
    {
        breakpoint: '1500px',
        numVisible: 5
    },
    {
        breakpoint: '1024px',
        numVisible: 3
    },
    {
        breakpoint: '768px',
        numVisible: 2
    },
    {
        breakpoint: '560px',
        numVisible: 1
    }
];

  constructor(private servicioPubli: PublicacionService, private servicioUsu: UsuarioService) { }
  ngOnInit(): void {
    this.servicioPubli.obtenerPublicaciones().subscribe(
      (data) => {
        this.publicaciones = data;
        this.publicaciones = data.map((publicacion: any) => ({
          ...publicacion,
          displayBasic: false
        }));
        this.publicaciones.forEach(element => {
          console.log(element);
        });
      }
    )

    this.servicioUsu.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data
      }
    )
  }

  handleClick(publicacion: any) {
    // Establecer todas las galerías como no visibles
    this.publicaciones.forEach(p => p.displayBasic = false);
    // Mostrar solo la galería de la publicación clicada
    publicacion.displayBasic = true;
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
