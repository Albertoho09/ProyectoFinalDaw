import { Component, OnInit } from '@angular/core';
import { ServicioSocioService } from '../../servicios/servicio-socio.service';
import { Column } from '../../interfaces/Column';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';

@Component({
  selector: 'app-tabla',
  templateUrl: './tabla.component.html',
  styleUrl: './tabla.component.scss'
})
export class TablaComponent implements OnInit {

  datos!: any[];
  cols!: Column[];
  clonedDatos: { [s: string]: any } = {};
  nonEditableFields!: string[];
  nombreTabla!: string[];


  constructor(private servicio: ServicioSocioService, private messageService: MessageService, private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
    this.servicio.data$.subscribe(datos => {
      this.datos = datos;
    });

    this.servicio.dataColumn$.subscribe(columnas => {
      this.cols = columnas;
    });
    this.servicio.dataNoEditable$.subscribe(noeditable => {
      this.nonEditableFields = noeditable;
    });
    this.servicio.datanombreTabla$.subscribe(nombreTabla => {
      this.nombreTabla = nombreTabla;
    });
  }

  onRowEditInit(data: any) {
    this.clonedDatos[data.id as number] = { ...data };
  }

  onRowEditSave(data: any) {
    this.servicio.actualizar(data.id, data).subscribe(
      (data) => {
        delete this.clonedDatos[data.id as number];
        this.messageService.add({ severity: 'success', summary: 'Exito', detail: 'Registro actualizado' });
      },
      (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al actualizar' });
        console.log(error);
      }
    );
  }

  onRowEditCancel(data: any, index: number) {
    this.datos[index] = this.clonedDatos[data.id as number];
    delete this.clonedDatos[data.id as number];
  }

  isNonEditableField(field: string): boolean {
    return this.nonEditableFields.includes(field);
  }

  deleteProduct(objt: any) {
    this.confirmationService.confirm({
      message: 'Estas seguro de que quieres borrar este registro?',
      header: 'Confirmar',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
          this.servicio.borrar(objt.id).subscribe(
            (data) => {
              this.datos = this.datos.filter((val) => val.id !== objt.id);
              this.messageService.add({ severity: 'success', summary: 'Exito', detail: 'Registro borrado' });
            },
            (error) => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al borrar' });
            }
          );
      }
  });
  }
}
