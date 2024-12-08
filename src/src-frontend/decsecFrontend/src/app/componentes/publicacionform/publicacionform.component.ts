import { Component, Input, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { FileUploadEvent } from 'primeng/fileupload';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PublicacionService } from '../../servicios/publicacion.service';
import { publicacionForm } from '../../interfaces/Publicacion';
import { usuarioDTO } from '../../interfaces/Usuario';

// Decorador del componente
@Component({
  selector: 'app-publicacionform',
  templateUrl: './publicacionform.component.html',
  styleUrl: './publicacionform.component.scss'
})
// Clase del componente
export class PublicacionformComponent implements OnInit {

  // Array para almacenar los archivos cargados
  uploadedFiles: File[] = [];
  // Formulario de publicación
  publicacionForm!: FormGroup;
  // Variable para controlar la visibilidad del dialogo
  visible: boolean = false;
  // Entrada para el usuario
  @Input()
  usuario!: usuarioDTO;
  // Constructor del componente
  constructor(private messageService: MessageService, private formBuilder: FormBuilder, private publicacionService: PublicacionService) { }

  // Método que se ejecuta al inicializar el componente
  ngOnInit(): void {
    // Creación del formulario de publicación
    this.publicacionForm = this.formBuilder.group({
      comentarioUsuario: ['', Validators.required],
    })
  }

  // Método para mostrar el dialogo
  showDialog() {
    this.visible = true;
  }

  // Método para manejar el evento de carga de archivos
  onUpload(event: FileUploadEvent) {
    // Iteración sobre los archivos cargados y agregarlos al array
    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }
  }

  // Método para registrar una publicación
  registrarPublicacion() {
    // Obtener los valores del formulario como un objeto de tipo publicacionForm
    const publi = this.publicacionForm.value as unknown as publicacionForm;
    // Servicio para crear una publicación
    this.publicacionService.crearPublicacion(publi, this.uploadedFiles).subscribe(
      () => {
        // Resetear el formulario
        this.publicacionForm.reset();
        // Ocultar el dialogo
        this.visible = false;
        // Limpiar el array de archivos cargados
        this.uploadedFiles = [];
        // Mostrar mensaje de éxito
        this.messageService.add({ severity: 'success', summary: 'Exito', detail: 'Publicación Creada' });
      },
      () => {
        // Mostrar mensaje de error
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear la publicación' });
      }
    );
  }
}
