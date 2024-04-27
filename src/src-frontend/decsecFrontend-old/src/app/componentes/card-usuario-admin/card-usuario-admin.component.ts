import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { AvatarModule } from 'primeng/avatar';

@Component({
  selector: 'app-card-usuario-admin',
  standalone: true,
  imports: [CardModule, AvatarModule],
  templateUrl: './card-usuario-admin.component.html',
  styleUrl: './card-usuario-admin.component.scss'
})
export class CardUsuarioAdminComponent {

}
