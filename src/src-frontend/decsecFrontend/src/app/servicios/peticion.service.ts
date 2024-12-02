import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PeticionService {
  private baseURL = 'http://localhost:8081/api/v1/peticiones';
  constructor(private serviciotoken: AuthServiceService, private http: HttpClient) { }

  obtenerPeticionPerfilUsuario(emailReceptor: String) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(this.baseURL + `/?emailReceptor=${emailReceptor}`, { headers: headers });
  }
}
