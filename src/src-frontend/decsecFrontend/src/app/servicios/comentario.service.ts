import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {

  private baseURL = 'http://localhost:8081/api/v1/comentarios';

  constructor(private http: HttpClient, private serviciotoken: AuthServiceService) { }

  obtenerComentarios() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(this.baseURL, { headers: headers });
  }

  borrarComentario(id: number) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.delete<any>(this.baseURL + "/" + id.toString(), { headers: headers });

  }


}
