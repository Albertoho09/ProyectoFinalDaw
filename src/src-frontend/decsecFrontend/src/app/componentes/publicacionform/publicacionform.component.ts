import { Component, Input, OnInit } from '@angular/core';
import { usuarioAdmin } from '../../interfaces/Usuario';
import { MessageService } from 'primeng/api';
import { FileUploadEvent } from 'primeng/fileupload';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PublicacionService } from '../../servicios/publicacion.service';
import { publicacionForm } from '../../interfaces/Publicacion';

@Component({
  selector: 'app-publicacionform',
  templateUrl: './publicacionform.component.html',
  styleUrl: './publicacionform.component.scss'
})
export class PublicacionformComponent implements OnInit{

  uploadedFiles: File[] = [];
  publicacionForm!: FormGroup;
  visible: boolean = false;
  @Input()
  usuario!: usuarioAdmin;
  constructor(private messageService: MessageService, private formBuilder: FormBuilder, private publicacionService: PublicacionService) { }

  ngOnInit(): void {
    this.publicacionForm = this.formBuilder.group({
      comentarioUsuario: ['', Validators.required],
    })
  }

  showDialog() {
    this.visible = true;
  }

  onUpload(event: FileUploadEvent) {
    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }
  }

  registrarPublicacion() {
    const publi = this.publicacionForm.value as unknown as publicacionForm;
    this.publicacionService.crearPublicacion(publi, this.uploadedFiles).subscribe(
      () =>{
        this.publicacionForm.reset();
        this.visible = false;
        this.messageService.add({ severity: 'success', summary: 'Exito', detail: 'Publicación Creada' });
      },
      () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear la publicación' });
      }
    );
  }
}
