import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Usuario, SignUpRequest } from '../interfaces/Usuario';
import { AuthServiceService } from './auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private baseURLAUTH = 'http://localhost:8081/api/v1/auth';
  private baseURL = 'http://localhost:8081/api/v1/users';

  constructor(private http: HttpClient, private serviciotoken: AuthServiceService) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'multipart/form-data'
    })
  };

  registroUsuario(usuario:Usuario){
    return this.http.post<any>(this.baseURLAUTH+'/signin', usuario);
  }

  crearUsuario(datos: SignUpRequest, fotoPerfil:File) {
    const formData = new FormData();
    formData.append('usuario', JSON.stringify(datos));
    formData.append('imagen', fotoPerfil);
    return this.http.post<any>(this.baseURLAUTH+'/signup', formData);
  }

  obtenerUsuarios(){
    const accessToken = localStorage.getItem("token");

    let headers = new HttpHeaders({
      'Authorization': 'Bearer '+accessToken
    })

    return this.http.get<any>(this.baseURL, {headers:headers});
  }
}
