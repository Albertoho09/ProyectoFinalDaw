import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class ServicioSocioService {
  private baseURL = 'http://localhost:8080/socio';
  private colums = new BehaviorSubject<any[]>([
    { field: 'id', header: 'ID' },
    { field: 'nombre', header: 'Nombre' },
    { field: 'apellido', header: 'Apellido' },
    { field: 'edad', header: 'Edad' },
    { field: 'numeromatricula', header: 'Numero Matricula' },
    { field: 'nbarcos', header: 'Numero Barcos' }

  ]);
  dataColumn$ = this.colums.asObservable();
  private noeditable = new BehaviorSubject<any[]>(['nbarcos', 'id']);
  dataNoEditable$ = this.noeditable.asObservable();
  private datos = new BehaviorSubject<any[]>(this.obtenerSocios());
  data$ = this.datos.asObservable();
  private nombreTabla = new BehaviorSubject<any>('Socio');
  datanombreTabla$ = this.nombreTabla.asObservable();

  constructor() { }

  obtenerSocios(): any {
    const accessToken = localStorage.getItem("token");

    axios.get(this.baseURL, {
      headers: {
        'Authorization': `Bearer ${accessToken}`
      }
    })
      .then(response => {
        this.datos.next(response.data);
        return response.data;
      })
      .catch(error => {
        return error.data;
      });
  }
  actualizar(id: number, data: any): Observable<any> {
    const accessToken = localStorage.getItem("token");

    return new Observable(observer => {
      axios.patch(this.baseURL + '?id=' + id, data, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      } ).then(response => {
        observer.next(response.data);
        observer.complete();
      })
        .catch(error => {
          observer.error(error);
        });
    });
  }
  borrar(id: number): Observable<any> {
    const accessToken = localStorage.getItem("token");

    return new Observable(observer => {
      axios.delete(this.baseURL + '?id=' + id, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      } ).then(response => {
        observer.next(response.data);
        observer.complete();
      })
        .catch(error => {
          observer.error(error);
        });
    });
  }

  cambiarTipo(tipo: string): void {
    if (tipo == "barco") {
      this.baseURL = "http://localhost:8080/barco";
      this.colums.next([
        { field: 'id', header: 'ID' },
        { field: 'nombre', header: 'Nombre' },
        { field: 'numeroamarre', header: 'Numero Amarre' },
        { field: 'cuota', header: 'Cuota' },
        { field: 'nombrepropietario', header: 'Nombre Propietario' }

      ]);
      this.noeditable.next(['nombrepropietario', 'id']);
      this.nombreTabla.next('Barcos');
      this.obtenerSocios();
    }
    if (tipo == "salida") {
      this.baseURL = "http://localhost:8080/salida";
      this.colums.next([
        { field: 'id', header: 'ID' },
        { field: 'fechahorasalida', header: 'Fecha y Hora' },
        { field: 'destino', header: 'Destino' },
        { field: 'nombrepatron', header: 'Nombre Patron' },
        { field: 'nombrebarco', header: 'Nombre Barco' }

      ]);
      this.noeditable.next(['nombrepatron','nombrebarco', 'id']);
      this.nombreTabla.next('Salidas');
      this.obtenerSocios();
    }
    if (tipo == "socio") {
      this.baseURL = "http://localhost:8080/socio";
      this.colums.next([
        { field: 'id', header: 'ID' },
        { field: 'nombre', header: 'Nombre' },
        { field: 'apellido', header: 'Apellido' },
        { field: 'edad', header: 'Edad' },
        { field: 'numeromatricula', header: 'Numero Matricula' },
        { field: 'nbarcos', header: 'Numero Barcos' }

      ]);
      this.noeditable.next(['nbarcos', 'id']);
      this.nombreTabla.next('Socios');
      this.obtenerSocios();
    }
    if (tipo == "nosocio") {
      this.baseURL = "http://localhost:8080/nosocio";
      this.colums.next([
        { field: 'id', header: 'ID' },
        { field: 'nombre', header: 'Nombre' },
        { field: 'apellido', header: 'Apellido' },
        { field: 'edad', header: 'Edad' },
        { field: 'dni', header: 'DNI' },

      ]);
      this.noeditable.next(['dni', 'id']);
      this.nombreTabla.next('No Socios');
      this.obtenerSocios();
    }
  }
}
