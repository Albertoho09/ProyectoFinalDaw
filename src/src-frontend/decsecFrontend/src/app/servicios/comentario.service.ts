import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';
import { map } from 'rxjs';
import { ComentarioAdmin, ComentarioRequest } from '../interfaces/Comentario';

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

    return this.http.get<any>(this.baseURL, { headers: headers })
    .pipe(
      map(response => {
        return response.map((item: ComentarioAdmin) => {
          const fechaOriginal = item.hora;
          const fechaLegible = new Date(fechaOriginal).toLocaleString(); // Transforma la fecha a un formato legible
          return { ...item, hora: fechaLegible };
        });
      })
    );
  }

  ObtenerComentariosPublicacion(idPublicacion: number) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(`${this.baseURL}/${idPublicacion}`, { headers: headers });
  }

  CrearComentario(idPublicacion: number, comentario: String) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    const comentarioRequest: ComentarioRequest = { comentario: comentario };

    return this.http.post<any>(`${this.baseURL}/${idPublicacion}`,comentarioRequest, { headers: headers });
  }

  borrarComentario(id: number) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.delete<any>(this.baseURL + "/" + id.toString(), { headers: headers });

  }


}
