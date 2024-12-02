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
interface Days {
  name: string;
  code: number;
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

  days!: Days[];

  selectedDay: Days = { name: '7 Dias', code: 7 };

  constructor(private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private messageService: MessageService, private router: Router) { }

  ngOnInit(): void {

    this.days = [
      { name: '7 Dias', code: 7 },
      { name: '15 Dias', code: 15 },
      { name: '1 Mes', code: 30 },
      { name: '3 Meses', code: 90 }
    ];

    this.usuarioService.obtenerUsuarioToken().subscribe(
      (data) => {
        this.usuario = data;
      }
    )

    this.usuarioService.obtenerUsuariosSearch().subscribe((usuariosSearch) => {
      this.usuariosSearch = usuariosSearch;
    });

    this.publicacionesService.obtenerPublicacionesFeed(this.selectedDay.code).subscribe((publicacionesFeed) => {
      this.publicacionesFeed = publicacionesFeed;
      console.log(this.publicacionesFeed);
    });
  }

  manejarEliminacionPrincipal(id: number) {
    this.publicacionesFeed = this.publicacionesFeed?.filter(pub => pub.id !== id);
    this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Publicación borrada', life: 3000 });
  }

  recargarPublicaciones() {
    this.publicacionesService.obtenerPublicacionesFeed(this.selectedDay.code).subscribe((publicacionesFeed) => {
      this.publicacionesFeed = publicacionesFeed;
    });
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
    if(this.selectedusuariosSearchAdvanced){
      if (this.selectedusuariosSearchAdvanced.hasOwnProperty('nick')) {
        this.usuarioService.ObtenerUsuarioNick(this.selectedusuariosSearchAdvanced.nick).subscribe(
          (user) => {
            this.router.navigate(['/user/perfil', user.nick]);
          },
          (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error || 'Error desconocido' });
          }
        )
      } else {
        this.usuarioService.ObtenerUsuarioNick(this.selectedusuariosSearchAdvanced).subscribe(
          (user) => {
            this.router.navigate(['/user/perfil', user.nick]);
          },
          (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error || 'Error desconocido' });
          }
        )
      }
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
