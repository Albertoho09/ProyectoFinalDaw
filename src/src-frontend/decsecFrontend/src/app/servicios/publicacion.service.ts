import { Injectable } from '@angular/core';
import { AuthServiceService } from './auth-service.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { publicacionForm } from '../interfaces/Publicacion';

@Injectable({
  providedIn: 'root'
})
export class PublicacionService {
  private baseURL = 'http://localhost:8081/api/v1/publicaciones';
  constructor(private serviciotoken: AuthServiceService, private http: HttpClient) { }

  obtenerPublicaciones(email?: string, megusta?: boolean) {
    const accessToken = this.serviciotoken.getToken();
  
    // Configuración de encabezados con el token de autorización
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });
  
    // Configuración de parámetros opcionales
    let params = new HttpParams();
    if (email) {
      params = params.append('email', email);
    }
    if (megusta !== undefined) {
      params = params.append('megusta', megusta.toString());
    }
  
    // Llamada HTTP GET
    return this.http.get<any>(this.baseURL, { headers: headers, params: params });
  }

  obtenerPublicacionesFeed(dias: number) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(this.baseURL + `/publicacionesFeed?dias=${dias}`, { headers: headers });
  }

  crearPublicacion(datos: publicacionForm, fotoPerfil: File[]) {
    const accessToken = this.serviciotoken.getToken();
    const usuario = sessionStorage.getItem('currentUser');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    console.log(fotoPerfil);
    const formData = new FormData();
    formData.append('publicacion', JSON.stringify(datos));
    fotoPerfil.forEach((foto, index) => {
      formData.append(`imagen_${index}`, foto);
    });
    return this.http.post<any>(this.baseURL, formData, { headers: headers });
  }

  darMegusta(id: number) {
    const accessToken = this.serviciotoken.getToken();
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });

    return this.http.post<any>(this.baseURL + `/darmegusta/${id}`, {}, { headers: headers });
  }

  quitarMegusta(id: number) {
    const accessToken = this.serviciotoken.getToken();
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });

    return this.http.post<any>(this.baseURL + `/quitarmegusta/${id}`, {}, { headers: headers });
  }
}
