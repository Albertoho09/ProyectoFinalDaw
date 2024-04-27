import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { Usuario } from '../../interfaces/Usuario';
import { AuthServiceService } from '../../servicios/auth-service.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-cardlogin',
  templateUrl: './cardlogin.component.html',
  styleUrl: './cardlogin.component.scss',
})
export class CardloginComponent implements OnInit{
  
  visible: boolean = false;

  showDialog() {
      this.visible = true;
  }

  usu:Usuario = {
    "email": '',
    "password": ''
  };
  
  usuario!: FormGroup; // Cambia el tipo de la propiedad a FormGroup

  constructor(private router: Router, private servicio: AuthServiceService, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.usuario = this.formBuilder.group({
      email: [''],
      password: [''],
      nombre: [''],
      apellidos: [''],
      fechanac: [''],
      checked: [false]
    });
  }

  iniciarSesion(){

    this.servicio.signIn(this.usu).subscribe(
      (data) => {
        console.log(data);
        localStorage.setItem('token', data.token);
        this.router.navigate(['admin']);
      },
      (error) => {
        console.log(error.data)
      }
    );
  }
}
