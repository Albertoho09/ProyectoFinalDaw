import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { iif } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent{

  constructor() { }

  cambiar() {
    const menu = document.querySelectorAll('.surface-overlay'); // Selector que identifica tu menú
    menu.forEach(element => {
      if(element.classList.contains('hidden')){
        element.classList.remove('hidden');
      }else{
        element.classList.add('hidden');
      }
    }); 
  }
}
