import { Component, Input, OnInit } from '@angular/core';
import { usuarioAdmin } from '../../interfaces/Usuario';
import { MessageService } from 'primeng/api';
import { FileUploadEvent } from 'primeng/fileupload';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-publicacionform',
  templateUrl: './publicacionform.component.html',
  styleUrl: './publicacionform.component.scss'
})
export class PublicacionformComponent implements OnInit{
registrarPublicacion() {
throw new Error('Method not implemented.');
}
  uploadedFiles: any[] = [];
  publicacionForm!: FormGroup;
  visible: boolean = false;
  @Input()
  usuario!: usuarioAdmin;
  constructor(private messageService: MessageService, private formBuilder: FormBuilder) { }
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

    this.messageService.add({ severity: 'info', summary: 'File Uploaded', detail: '' });
  }
}
