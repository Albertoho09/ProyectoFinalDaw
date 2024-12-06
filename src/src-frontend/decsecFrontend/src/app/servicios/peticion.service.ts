import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Peticion } from '../interfaces/Peticion';

@Injectable({
  providedIn: 'root'
})
export class PeticionService {
  private baseURL = 'http://localhost:8081/api/v1/peticiones';
  constructor(private serviciotoken: AuthServiceService, private http: HttpClient) { }

  async obtenerPeticionPerfilUsuario(emailReceptor: String) {
    const accessToken = this.serviciotoken.getToken();
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<Peticion>(this.baseURL + `?emailReceptor=${emailReceptor}`, { headers: headers });
  }

  async obtenerMisPeticiones() {
    const accessToken = this.serviciotoken.getToken();
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<Peticion[]>(this.baseURL, { headers: headers });
  }

  async obtenerMisAmigos() {
    const accessToken = this.serviciotoken.getToken();
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<Peticion[]>(this.baseURL+'/amigos', { headers: headers });
  }

  async cambiarEstado(id?: number, estado?: string){
    const accessToken = this.serviciotoken.getToken();
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });
    const params = new HttpParams()
      .set('id', id || '')
      .set('estado', estado || '');

    return this.http.put<Peticion>(`${this.baseURL}/cambiarEstado`, null, { headers, params });
  }

  async enviarPeticionPerfilUsuario(emailReceptor: String) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });
    let params = new HttpParams()
      .set('emailReceptor', emailReceptor.toString());
    
    return this.http.post<Peticion>(this.baseURL, null, { headers, params });
  }
}
