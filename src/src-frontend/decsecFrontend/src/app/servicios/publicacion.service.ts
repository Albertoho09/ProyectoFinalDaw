import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  private baseURL = 'http://localhost:8081/api/v1/publicaciones';
  constructor(private serviciotoken: AuthServiceService, private http: HttpClient) { }

  obtenerPublicaciones(){
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer '+accessToken
    })

    return this.http.get<any>(this.baseURL, {headers:headers});
  }
}
