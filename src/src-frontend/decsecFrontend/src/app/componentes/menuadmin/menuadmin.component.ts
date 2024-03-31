import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { BotonTemaComponent } from '../boton-tema/boton-tema.component';

@Component({
  selector: 'app-menuadmin',
  standalone: true,
  imports: [CardModule, BotonTemaComponent],
  templateUrl: './menuadmin.component.html',
  styleUrl: './menuadmin.component.scss'
})
export class MenuadminComponent {

}
