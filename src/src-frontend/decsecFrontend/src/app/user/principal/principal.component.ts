import { Component, OnInit } from '@angular/core';
import { usuarioSesion, usuarioSearch } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';
import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { PublicacionService } from '../../servicios/publicacion.service';
import { Publicacion } from '../../interfaces/Publicacion';

interface AutoCompleteCompleteEvent {
  originalEvent: Event;
  query: string;
}
@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrl: './principal.component.scss'
})
export class PrincipalComponent implements OnInit {

  usuariosSearch: usuarioSearch[] | undefined;

  publicacionesFeed: Publicacion[] | undefined;


  selectedusuariosSearchAdvanced: any | undefined;

  filteredusuariosSearch: any[] = [];

  usuario!: usuarioSesion;

  constructor(private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private messageService: MessageService, private router: Router) { }

  ngOnInit(): void {
    this.usuarioService.obtenerUsuariosSearch().subscribe((usuariosSearch) => {
      this.usuariosSearch = usuariosSearch;
    });

    this.publicacionesService.obtenerPublicacionesFeed().subscribe((publicacionesFeed) => {
      this.publicacionesFeed = publicacionesFeed;
      console.log(this.publicacionesFeed);
    });

    this.usuarioService.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data
      }
    )
  }

  filterCountry(event: AutoCompleteCompleteEvent) {
    let filtered: any[] = [];
    let query = event.query;

    for (let i = 0; i < (this.usuariosSearch as any[]).length; i++) {
      let country = (this.usuariosSearch as any[])[i];
      if (country.nick.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(country);
      }
    }

    this.filteredusuariosSearch = filtered;
  }

  buscarUsuario() {
    if (this.selectedusuariosSearchAdvanced && this.selectedusuariosSearchAdvanced.hasOwnProperty('nick')) {
      this.router.navigate(['/user/perfil', this.selectedusuariosSearchAdvanced.nick]);
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Usuario no encontrado: ' + this.selectedusuariosSearchAdvanced });
    }
  }

  visitarPerfil() {
    const usuarioStr = sessionStorage.getItem('currentUser');
    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr);
      this.router.navigate(['/user/perfil', usuario.nick]);
    }
  }

}
