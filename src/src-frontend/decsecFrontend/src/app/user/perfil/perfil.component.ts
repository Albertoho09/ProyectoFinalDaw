import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SignUpRequest, Usuario, usuarioPerfil, usuarioSesion } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';
import { Publicacion } from '../../interfaces/Publicacion';
import { PublicacionService } from '../../servicios/publicacion.service';
import { PeticionService } from '../../servicios/peticion.service';
import { MenuItem, MessageService } from 'primeng/api';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router ,private formBuilder: FormBuilder, private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private peticionesService: PeticionService, private messageService: MessageService) { }

  imagenes: string[] = ['assets/fondo/guts.webp', 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ];
  usuarioPerfil!: usuarioPerfil;
  usuarioSesion!: usuarioSesion;
  estadoMenu: String = 'publicaciones';
  estadoPerfil: String = 'PUBLICO';
  Publicaciones: Publicacion[] | undefined;

  visible: boolean = false;
  file!: File;
  filebanner!: File;
  imagenURL: string = '';
  imagenBanner: string = '';
  usuarioForm!: FormGroup;
  usuarioSecurityForm!: FormGroup;
  emailRepetido = false;
  items: MenuItem[] | undefined;
  activeItem!: MenuItem;

  ngOnInit() {
    this.route.params.subscribe(params => {
      const userNick = params['nick'];
      const userJson = sessionStorage.getItem('currentUser');
      this.usuarioSesion = userJson ? JSON.parse(userJson) : null;

      this.usuarioService.ObtenerUsuarioNick(userNick).subscribe(
        (data) => {
          this.usuarioPerfil = data;
          this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe(
            response => {
              this.Publicaciones = response;
            },
            error => {
              if (error.error == "Publicaciones no disponibles, usuario privado.") {
                this.estadoPerfil = 'PRIVADO';
              }
            }
          );
        }
      )
      this.items = [
        { label: 'Datos', icon: 'pi pi-user-edit' },
        { label: 'Seguridad', icon: 'pi pi-shield' },
        { label: 'Multimedia', icon: 'pi pi-image' }
      ]

      this.activeItem = this.items[0];

      this.usuarioForm = this.formBuilder.group({
        nick: [this.usuarioSesion.nick, Validators.required],
        nombre: [this.usuarioSesion.nombre, Validators.required],
        apellidos: [this.usuarioSesion.apellidos, Validators.required],
        email: [this.usuarioSesion.email, [Validators.required, Validators.email]],
        fechaNac: [new Date(this.usuarioSesion.fechaNac.toString())]
      });
      console.log('==========')
      console.log(this.usuarioSesion.privado)
      this.usuarioSecurityForm = this.formBuilder.group({
        password: ['', Validators.minLength(6)],
        passwordNew: ['', Validators.minLength(6)],
        passwordRepeat: ['', Validators.minLength(6)],
        privado: [this.usuarioSesion.privado],
      },
      {
        validator: this.passwordsMatchValidator
      });
    });
  }

  passwordsMatchValidator(form: AbstractControl) {
    const passwordNew = form.get('passwordNew')?.value;
    const passwordRepeat = form.get('passwordRepeat')?.value;
    const password = form.get('password')?.value;
    
    if (!password || !passwordNew || !passwordRepeat) {
      return null; // Si todos los campos están vacíos, no hay error
    }
    if (passwordNew !== passwordRepeat) {
      return { passwordsMismatch: true }; // Si no coinciden, devuelve un error
    }
    return null; // Si coinciden, no hay error
  }

  showDialog() {
    this.visible = true;
    console.log(this.visible)
  }

  get f() { return this.usuarioForm.controls; }
  get f1() { return this.usuarioSecurityForm.controls; }

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

  registrarUsuario() {
    if (this.usuarioForm.valid) {
      const formValues = this.usuarioForm.value; 
      this.usuarioService.actualizarParcialmente(formValues).subscribe(
        (response) => {
              sessionStorage.setItem('token', response.token);
              sessionStorage.setItem('currentUser', JSON.stringify(response.usuario));
              this.usuarioPerfil = response.usuario;
              const newUrl = `/user/perfil/${response.usuario.nick}`; // Ajusta según los datos que obtengas
              this.router.navigateByUrl(newUrl);
              this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados', life: 3000 });
        },
        (err) => {
          if (err.error === "Error: El nick ya está en uso.") {
            this.usuarioForm.get('nick')?.setErrors({ nickExists: true });
          }
          if (err.error === "Error: El Email ya está en uso.") {
            this.usuarioForm.get('email')?.setErrors({ emailExists: true });
          }
        }
      );
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Formulario no valido.', life: 3000 });
    }
  }

  registrarUsuarioSecurity(){
    if (this.usuarioSecurityForm.valid) {
      const formValues = Object.keys(this.usuarioSecurityForm.value).reduce((obj, key) => {
        if (this.usuarioSecurityForm.value[key] !== "") {
          obj[key] = this.usuarioSecurityForm.value[key];
        }
        return obj;
      }, {} as Record<string, unknown>);
      console.log(formValues) 
      this.usuarioService.actualizarParcialmente(formValues).subscribe(
        (response) => {
              sessionStorage.setItem('token', response.token);
              sessionStorage.setItem('currentUser', JSON.stringify(response.usuario));
              this.usuarioPerfil = response.usuario;
              this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados', life: 3000 });
        },
        (err) => {
          if (err.error === "Error: La contraseña no es correcta.") {
            this.usuarioSecurityForm.get('password')?.setErrors({ passwordConflict: true });
          }
        }
      );
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'La nueva contraseña no coincide.', life: 3000 });
    }
  }

  editarMedia(){
    this.usuarioService.editarUsuarioMedia(this.file, this.filebanner).subscribe(
      (response) => {
        sessionStorage.setItem('currentUser', JSON.stringify(response));
        this.usuarioPerfil = response;
        this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados', life: 3000 });
      },
      (err) => {
        console.log(err)
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err, life: 3000 });
      }
    );
  }

  manejarEliminacionPerfil(id: number) {
    this.Publicaciones = this.Publicaciones?.filter(pub => pub.id !== id);
    this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Publicación borrada', life: 3000 });
  }

  cambiarVista(vista: string) {
    // Lógica para cambiar la vista
    console.log('Cambiando a la vista:', vista);

    if (vista == 'publicaciones') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    } else if (vista == 'media') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.Publicaciones = this.Publicaciones?.filter(publicacion => publicacion.imagenes && publicacion.imagenes.length > 0);
        this.estadoMenu = vista
      });
    } else if (vista == 'meGustan') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email, true).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    }
  }
}
