import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { Usuario, SignUpRequest, usuarioAdmin } from '../../interfaces/Usuario';
import { AuthServiceService } from '../../servicios/auth-service.service';
import { UsuarioService } from '../../servicios/usuario.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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
  usuarioForm!: FormGroup;
  emailRepetido = false;
  constructor(private router: Router, private formBuilder: FormBuilder, private servicio: AuthServiceService, private usuariService: UsuarioService) {}

  ngOnInit(): void {
    this.usu = {
      "email": '',
      "password": ''
    };
    this.usuarioForm = this.formBuilder.group({
      nick: ['', Validators.required],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      privado: [false],
      fechaNac: [new Date()]
    });
  }


  get f() { return this.usuarioForm.controls; }

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

  validarEmail(){
    this.usuariService.validarEmail(this.usuarioForm.get('email')?.value)
    .subscribe(
      response => {this.emailRepetido = response;
        console.log(this.emailRepetido);
      }
    )
  }

  registrarUsuario(){
    const socio = this.usuarioForm.value as unknown as SignUpRequest;
    this.usuariService.crearUsuario(socio, this.file).subscribe(
      response => {
        console.log('Respuesta del servidor:', response);
      },
      error => {
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
