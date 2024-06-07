import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent implements OnInit{
  constructor(){}

  imagenes: string[] = ['assets/fondo/guts.webp', 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ,'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ,'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ];

ngOnInit() {
}
}
