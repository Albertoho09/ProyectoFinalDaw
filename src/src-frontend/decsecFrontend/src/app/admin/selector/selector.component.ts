import { Component } from '@angular/core';
import { SocioForm } from '../../interfaces/Socio';
import { NoSocio } from '../../interfaces/Nosocios';
import { Barco } from '../../interfaces/Barco';
import { Salida } from '../../interfaces/Salida';

@Component({
  selector: 'app-selector',
  templateUrl: './selector.component.html',
  styleUrl: './selector.component.scss'
})
export class SelectorComponent {

  stateOptions: any[] = [{ label: 'Socios', value: 'socios' }, { label: 'No Socios', value: 'nosocios' }, { label: 'Barcos', value: 'barco' }, { label: 'Salidas', value: 'salida' }];

  value: string = 'socios';
  submitted: boolean = false;
  formDialog: boolean = false;

  socio!: SocioForm;
  nosocio!: NoSocio;
  salida!: Salida;
  barco!: Barco;


  constructor() { }

  openNew() {
    this.socio = {
      nombre: '',
      apellido: '',
      edad: 0,
      numeromatricula: ''
    };
    this.submitted = false;
    this.formDialog = true;
  }
  saveProduct() {
    this.submitted = true;
  }
  hideDialog() {
    this.formDialog = false;
    this.submitted = false;
  }

}
