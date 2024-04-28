import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { Usuario, SignUpRequest } from '../../interfaces/Usuario';
import { AuthServiceService } from '../../servicios/auth-service.service';
import { UsuarioService } from '../../servicios/usuario.service';

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

  usu!:Usuario;
  usuario!: SignUpRequest;
  file!: File;
  imagenURL: string = "https://primefaces.org/cdn/primeng/images/demo/avatar/amyelsner.png";
  constructor(private router: Router, private servicio: AuthServiceService, private usuariService: UsuarioService) {}

  ngOnInit(): void {
    this.usu = {
      "email": '',
      "password": ''
    };
    
    this.usuario ={
      "nick": "",
      "nombre": "",
      "apellidos": "",
      "email": "",
      "password": "",
      "privado": false,
      "fechaNac": new Date()
    };
  }

  onUpload(event:any) {
    this.file = event.files[0];
    if (this.file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagenURL = reader.result as string;
      };
      reader.readAsDataURL(this.file);
    }
    console.log(this.file);
    console.log(this.imagenURL);
  }

  registrarUsuario(){
    this.usuariService.crearUsuario(this.usuario, this.file).subscribe(
      response => {
        // Manejar la respuesta del servidor
        console.log('Respuesta del servidor:', response);
      },
      error => {
        // Manejar errores
        console.error('Error al enviar datos:', error);
      }
    )
  }

  iniciarSesion(){
    this.usuariService.registroUsuario(this.usu).subscribe(
      response => {
        console.log(response);
        localStorage.setItem('token', response.token);
        this.router.navigate(['admin']);
      },
      error => {
        console.log(error.data)
      }
    );
  }
}
