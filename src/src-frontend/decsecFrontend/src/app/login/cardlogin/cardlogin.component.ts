import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Usuario, SignUpRequest, usuarioDTO } from '../../interfaces/Usuario';
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

  // Variable para controlar la visibilidad del dialogo
  visible: boolean = false;

  // Método para mostrar el dialogo
  showDialog() {
    this.visible = true;
  }

  // Variables para almacenar el usuario y los archivos cargados
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
    // Inicialización del usuario
    this.usu = {
      "email": '',
      "password": ''
    };
    // Creación del formulario de usuario
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


  // Método para obtener los controles del formulario
  get f() { return this.usuarioForm.controls; }

  // Método para manejar el evento de carga de archivo de banner
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


  // Método para manejar el evento de carga de archivo de perfil
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

  // Método para validar el email
  validarEmail() {

    this.usuariService.validarEmail(this.usuarioForm.get('email')?.value).then((observable) => {
      observable.subscribe((response) => {
        this.emailRepetido = response;
        console.log(this.emailRepetido);
      });
    });
  }

  // Método para registrar un usuario
  registrarUsuario() {
    const socio = this.usuarioForm.value as unknown as SignUpRequest;
    this.usuariService.crearUsuario(socio, this.file, this.filebanner).then((observable) => {
      observable.subscribe((response) => {
        sessionStorage.setItem('token', response.token);
        this.usuariService.obtenerUsuarioToken().then((observable) => {
          observable.subscribe((data) => {
            sessionStorage.setItem('currentUser', JSON.stringify(data));
            this.router.navigate(['user']);
          })
        })
      }, (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear el usuario' });
      });
    });
  }

  // Método para iniciar sesión
  iniciarSesion() {

    this.usuariService.registroUsuario(this.usu).then((observable) => {
      observable.subscribe((response) => {
        console.log(response);
        sessionStorage.setItem('token', response.token);
        this.usuariService.obtenerUsuarioToken().then((observable) => {
          observable.subscribe((data) => {
            sessionStorage.setItem('currentUser', JSON.stringify(data));
            if (data.roles[0] == 'ROLE_ADMIN') {
              this.router.navigate(['admin']);
            }
            else {
              this.router.navigate(['user']);
            }          
          })
        })
      }, (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Credenciales incorrectas' });
      });
    });
  }
}
