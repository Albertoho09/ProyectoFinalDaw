import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario, SignUpRequest, usuarioSesion } from '../../interfaces/Usuario';
import { AuthServiceService } from '../../servicios/auth-service.service';
import { UsuarioService } from '../../servicios/usuario.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { FileUpload } from 'primeng/fileupload';

@Component({
  selector: 'app-cardlogin',
  templateUrl: './cardlogin.component.html',
  styleUrl: './cardlogin.component.scss',
})

export class CardloginComponent implements OnInit {

  visible: boolean = false;

  showDialog() {
    this.visible = true;
  }

  usu!: Usuario;
  usuario!: SignUpRequest;
  file!: File;
  filebanner!: File;
  imagenURL: string = "https://primefaces.org/cdn/primeng/images/demo/avatar/amyelsner.png";
  imagenBanner: string = "https://i.pinimg.com/736x/88/eb/a5/88eba554eb141ad1bc126daaab018594.jpg";
  usuarioForm!: FormGroup;
  emailRepetido = false;
  constructor(private router: Router, private formBuilder: FormBuilder, private messageService: MessageService, private usuariService: UsuarioService) { }

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

  onUploadBanner(event: any) {
    this.filebanner = event.files[0];
    if (this.filebanner) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagenBanner = reader.result as string;
      };
      reader.readAsDataURL(this.filebanner);
    }
    console.log(this.filebanner);
    console.log(this.imagenBanner);
  }


  onUpload(event: any) {
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

  validarEmail() {
    this.usuariService.validarEmail(this.usuarioForm.get('email')?.value)
      .subscribe(
        response => {
          this.emailRepetido = response;
          console.log(this.emailRepetido);
        }
      )
  }

  registrarUsuario() {
    const socio = this.usuarioForm.value as unknown as SignUpRequest;
    this.usuariService.crearUsuario(socio, this.file, this.filebanner).subscribe(
      response => {
        console.log('Respuesta del servidor:', response);
      },
      error => {
        console.error('Error al enviar datos:', error);
      }
    )
  }

  iniciarSesion() {
    this.usuariService.registroUsuario(this.usu).subscribe(
      response => {
        console.log(response);
        sessionStorage.setItem('token', response.token);
        this.usuariService.obtenerUsuarioToken().subscribe(
          (data) => {
            sessionStorage.setItem('currentUser', JSON.stringify(data));
            if (data.roles[0] == 'ROLE_ADMIN') {
              this.router.navigate(['admin']);
            }
            else {
              this.router.navigate(['user']);
            }
          }
        )
      },
      error => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Credenciales incorrectas' });
      }
    );
  }
}
