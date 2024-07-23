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

  registroUsuario(usuario: Usuario) {
    return this.http.post<any>(this.baseURLAUTH + '/signin', usuario);
  }

  obtenerUsuarioToken() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    return this.http.get<any>(this.baseURL + "/token", { headers: headers });
  }

  crearUsuario(datos: SignUpRequest, fotoPerfil: File, banner: File) {
    const formData = new FormData();
    formData.append('usuario', JSON.stringify(datos));
    formData.append('imagen', fotoPerfil);
    formData.append('banner', banner);
    return this.http.post<any>(this.baseURLAUTH + '/signup', formData);
  }

  obtenerUsuarios() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(this.baseURL, { headers: headers });
  }

  ObtenerUsuarioNick(nick: string) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<any>(`${this.baseURL}/nick/${nick}`, { headers: headers });
  }

  obtenerUsuariosSearch() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    return this.http.get<any>(this.baseURL + '/search', { headers: headers });
  }

  validarEmail(email: String) {
    return this.http.get<boolean>(this.baseURLAUTH + '/validar-email?email=' + email);
  }
}
