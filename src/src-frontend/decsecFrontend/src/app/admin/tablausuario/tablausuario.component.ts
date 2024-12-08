// Importaciones necesarias
import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../servicios/usuario.service';
import { usuarioDTO } from '../../interfaces/Usuario';
import { MessageService } from 'primeng/api';

// Decorador del componente
@Component({
  selector: 'app-tablausuario',
  templateUrl: './tablausuario.component.html',
  styleUrl: './tablausuario.component.scss'
})
// Clase del componente
export class TablausuarioComponent{

  // Array para almacenar los usuarios
  usuarios: usuarioDTO[] = [];

  // Constructor del componente
  constructor(private servicio: UsuarioService, private messageService: MessageService) { 
    // Obtener los usuarios
    this.servicio.obtenerUsuarios().then((observable) => {
      observable.subscribe({
        next: (response: usuarioDTO[]) => {
          this.usuarios = response; // Asignación de los usuarios obtenidos al array
        },
        error: (error) => {
          console.error(error); // Manejo de errores
        }
      });
    });
  }

  // Método para obtener la severidad del estado privado
  getSeverityPrivado(status: boolean) {
    switch (status) {
      case false:
        return 'success';
      case true:
        return 'danger';
    }
  }
  // Método para obtener la severidad del rol
  getSeverityRol(status: string) {
    switch (status) {
      case 'ROLE_USER':
        return 'success';
      case 'ROLE_ADMIN':
        return 'warning';
    }
    return 'danger';
  }
  // Método para borrar un usuario
  deleteProduct(email: String) {
    const userJson = sessionStorage.getItem('currentUser');
    const usuarioSesion = userJson ? JSON.parse(userJson) : null;
    if (usuarioSesion.email !== email.toString()) {
      this.servicio.borrarPorEmail(email.toString()).then((observable) => {
        observable.subscribe({
          next: (response) => {
            this.usuarios = this.usuarios.filter(usuario => usuario.email !== email); // Filtrado del array para eliminar el usuario
          },
          error: (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al borrar el usuario' }); // Mensaje de error
          }
        });
      });
    }else{
      this.messageService.add({ severity: 'warn', summary: 'Cuidado', detail: 'No puedes borrar tu propio usuario' }); // Mensaje de advertencia
    }
  }
}
