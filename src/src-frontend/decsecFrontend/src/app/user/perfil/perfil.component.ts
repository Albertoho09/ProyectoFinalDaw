import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SignUpRequest, usuarioDTO } from '../../interfaces/Usuario';
import { UsuarioService } from '../../servicios/usuario.service';
import { Publicacion } from '../../interfaces/Publicacion';
import { PublicacionService } from '../../servicios/publicacion.service';
import { PeticionService } from '../../servicios/peticion.service';
import { MenuItem, MessageService } from 'primeng/api';
import { Estado, Peticion } from '../../interfaces/Peticion';

// Definición del componente
@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss'
})
export class PerfilComponent {

  // Variables de estado
  isLoading: Boolean = true;
  imagenes: string[] = ['assets/fondo/guts.webp', 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
    , 'assets/fondo/guts.webp', 'assets/fondo/guts.webp'
  ];
  usuarioPerfil: usuarioDTO | null = null;
  usuarioSesion: usuarioDTO | null = null;
  estadoMenu: String = 'publicaciones';
  estadoPerfil: String = 'PUBLICO';
  Publicaciones: Publicacion[] | undefined;

  // Variables de multimedia
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

  // Variables de peticiones
  botonPeticionesTexto : String = 'Enviar Petición';
  botonActivo : Boolean = true;

  // Constructor del componente
  constructor(private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder, private usuarioService: UsuarioService, private publicacionesService: PublicacionService, private peticionesService: PeticionService, private messageService: MessageService) {
    this.route.params.subscribe(params => {
      const userNick = params['nick'];
      const userJson = sessionStorage.getItem('currentUser');
      this.usuarioSesion = userJson ? JSON.parse(userJson) : null;

      this.usuarioService.ObtenerUsuarioNick(userNick).then((observable) => {
        observable.subscribe({
          next: (user: usuarioDTO) => {
            this.usuarioPerfil = user;
            this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil.email).subscribe(
              response => {
                this.Publicaciones = response;
                this.peticionesService.obtenerPeticionPerfilUsuario(this.usuarioPerfil!.email).then((observable) => {
                  observable.subscribe({
                    next: (peticion: Peticion) => {
                      if(peticion != null){
                        console.log(peticion)
                        if(peticion.estado == Estado.ACEPTADO){
                          this.botonPeticionesTexto = 'Peticion Aceptada';
                          this.botonActivo = false;
                        }else if(peticion.estado == Estado.PENDIENTE){
                          this.botonPeticionesTexto = 'Peticion Pendiente';
                          this.botonActivo = false;
                        }
                      }else{
                        console.log("no hay peticion disponible");
                      }
                      this.isLoading = false
                    },
                    error: (error) => {
                      console.log(error)
                      this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error || 'Error desconocido' });
                    }
                  });
                });
              },
              error => {
                if (error.error == "Publicaciones no disponibles, usuario privado.") {
                  this.estadoPerfil = 'PRIVADO';
                }
              }
            );
          },
          error: (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al obtener el usuario' });
          }
        });
      });

      this.items = [
        { label: 'Datos', icon: 'pi pi-user-edit' },
        { label: 'Seguridad', icon: 'pi pi-shield' },
        { label: 'Multimedia', icon: 'pi pi-image' }
      ]

      this.activeItem = this.items[0];

      this.usuarioForm = this.formBuilder.group({
        nick: [this.usuarioSesion!.nick, Validators.required],
        nombre: [this.usuarioSesion!.nombre, Validators.required],
        apellidos: [this.usuarioSesion!.apellidos, Validators.required],
        email: [this.usuarioSesion!.email, [Validators.required, Validators.email]],
        fechaNac: [new Date(this.usuarioSesion!.fechaNac.toString())]
      });

      this.usuarioSecurityForm = this.formBuilder.group({
        password: ['', Validators.minLength(6)],
        passwordNew: ['', Validators.minLength(6)],
        passwordRepeat: ['', Validators.minLength(6)],
        privado: [this.usuarioSesion!.privado],
      },
        {
          validator: this.passwordsMatchValidator
        });
    });
  }

  // Validador de contraseñas
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

  // Método para mostrar el diálogo de multimedia
  showDialog() {
    this.visible = true;
    console.log(this.visible)
  }

  // Getters para los controles del formulario
  get f() { return this.usuarioForm.controls; }
  get f1() { return this.usuarioSecurityForm.controls; }

  // Método para subir el banner
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

  // Método para subir la imagen de perfil
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

  // Método para registrar un usuario
  registrarUsuario() {
    if (this.usuarioForm.valid) {
      const formValues = this.usuarioForm.value;
      this.usuarioService.actualizarParcialmente(formValues).then((observable) => {
        observable.subscribe((response) => {
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('currentUser', JSON.stringify(response.usuario));
          this.usuarioPerfil = response.usuario;
          const newUrl = `/user/perfil/${response.usuario.nick}`; // Ajusta según los datos que obtengas
          this.router.navigateByUrl(newUrl);
          this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados', life: 3000 });
        }, (err) => {
          if (err.error === "Error: El nick ya está en uso.") {
            this.usuarioForm.get('nick')?.setErrors({ nickExists: true });
          }
          if (err.error === "Error: El Email ya está en uso.") {
            this.usuarioForm.get('email')?.setErrors({ emailExists: true });
          }
        });
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Formulario no valido.', life: 3000 });
    }
  }

  // Método para registrar la seguridad del usuario
  registrarUsuarioSecurity() {
    if (this.usuarioSecurityForm.valid) {
      const formValues = Object.keys(this.usuarioSecurityForm.value).reduce((obj, key) => {
        if (this.usuarioSecurityForm.value[key] !== "") {
          obj[key] = this.usuarioSecurityForm.value[key];
        }
        return obj;
      }, {} as Record<string, unknown>);

      this.usuarioService.actualizarParcialmente(formValues).then((observable) => {
        observable.subscribe((response) => {
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('currentUser', JSON.stringify(response.usuario));
          this.usuarioPerfil = response.usuario;
          this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados'});
        }, (err) => {
          if (err.error === "Error: La contraseña no es correcta.") {
            this.usuarioSecurityForm.get('password')?.setErrors({ passwordConflict: true });
          }
        });
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'La nueva contraseña no coincide.'});
    }
  }

  // Método para enviar una petición
  enviarPeticion(){
    this.peticionesService.enviarPeticionPerfilUsuario(this.usuarioPerfil!.email).then((observable) => {
      observable.subscribe({
        next: (peti: Peticion) => {
          console.log(peti);
          this.botonPeticionesTexto = 'Peticion Pendiente';
          this.botonActivo = false;
          this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Petición Enviada'});
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al enviar Petición' });
        }
      });
    });
  }

  // Método para editar la multimedia del usuario
  editarMedia() {
    this.usuarioService.editarUsuarioMedia(this.file, this.filebanner).then((observable) => {
      observable.subscribe((response) => {
        sessionStorage.setItem('currentUser', JSON.stringify(response));
        this.usuarioPerfil = response;
        this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Datos Actualizados'});
      }, (err) => {
        console.log(err)
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err});
      });
    });
  }

  // Método para manejar la eliminación de un perfil
  manejarEliminacionPerfil(id: number) {
    this.Publicaciones = this.Publicaciones?.filter(pub => pub.id !== id);
    this.messageService.add({ severity: 'success', summary: 'Confirmado', detail: 'Publicación borrada'});
  }

  // Método para cambiar la vista
  cambiarVista(vista: string) {
    // Lógica para cambiar la vista
    console.log('Cambiando a la vista:', vista);

    if (vista == 'publicaciones') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil!.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    } else if (vista == 'media') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil!.email).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.Publicaciones = this.Publicaciones?.filter(publicacion => publicacion.imagenes && publicacion.imagenes.length > 0);
        this.estadoMenu = vista
      });
    } else if (vista == 'meGustan') {
      this.publicacionesService.obtenerPublicaciones(this.usuarioPerfil!.email, true).subscribe((publicaciones) => {
        this.Publicaciones = publicaciones;
        this.estadoMenu = vista
      });
    }
  }
}
