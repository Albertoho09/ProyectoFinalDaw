import { Component, OnInit } from '@angular/core';
import { usuarioAdmin, usuarioSearch } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';

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

  selectedusuariosSearchAdvanced: usuarioSearch[] | undefined;

  filteredusuariosSearch: any[] = [];
  
  usuario!: usuarioAdmin;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.usuarioService.obtenerUsuariosSearch().subscribe((usuariosSearch) => {
      this.usuariosSearch = usuariosSearch;
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
}
