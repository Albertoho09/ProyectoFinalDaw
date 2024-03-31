import { Component } from '@angular/core';
import { CardloginComponent } from "../../componentes/cardlogin/cardlogin.component";
import { NgClass } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { BotonTemaComponent } from '../../componentes/boton-tema/boton-tema.component';


@Component({
    selector: 'app-login',
    standalone: true,
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss',
    imports: [CardloginComponent, NgClass, ButtonModule]
})
export class LoginComponent {
    
}

