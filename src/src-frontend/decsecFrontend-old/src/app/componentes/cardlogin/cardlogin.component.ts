import { Component} from '@angular/core';
import { StyleClassModule } from 'primeng/styleclass';
import { RippleModule } from 'primeng/ripple';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { FileUploadModule } from 'primeng/fileupload';
import { AvatarModule } from 'primeng/avatar';
import { InputSwitchModule } from 'primeng/inputswitch';
import { CardModule } from 'primeng/card';
import { BotonTemaComponent } from '../boton-tema/boton-tema.component';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { PasswordModule } from 'primeng/password';
import { CalendarModule } from 'primeng/calendar';
import { Router } from '@angular/router';




@Component({
  selector: 'app-cardlogin',
  standalone: true,
  imports: [StyleClassModule, RippleModule, ButtonModule,
     InputTextModule, DialogModule, FileUploadModule, AvatarModule, InputSwitchModule,
      CardModule, BotonTemaComponent, ToastModule, PasswordModule, CalendarModule],
  templateUrl: './cardlogin.component.html',
  styleUrl: './cardlogin.component.scss',
  providers : [MessageService]
})
export class CardloginComponent {
  constructor(private router: Router) {}

  visible: boolean = false;

  showDialog() {
      this.visible = true;
  }

  iniciarSesion(){
    this.router.navigate(['adminConsole']);
  }

}
