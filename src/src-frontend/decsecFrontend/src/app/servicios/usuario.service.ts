import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Usuario, SignUpRequest, usuarioDTO, usuarioSearch } from '../interfaces/Usuario';
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

  async registroUsuario(usuario: Usuario) {
    return this.http.post<any>(this.baseURLAUTH + '/signin', usuario);
  }

  async actualizarParcialmente(updates: Partial<any>){
    const accessToken = this.serviciotoken.getToken();
    console.log(updates)
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    return this.http.patch<any>(this.baseURL, updates, { headers: headers });
  }

  async obtenerUsuarioToken() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    return this.http.get<usuarioDTO>(this.baseURL + "/token", { headers: headers });
  }

  async crearUsuario(datos: SignUpRequest, fotoPerfil: File, banner: File) {
    const formData = new FormData();
    formData.append('usuario', JSON.stringify(datos));
    formData.append('imagen', fotoPerfil);
    formData.append('banner', banner);
    return this.http.post<usuarioDTO>(this.baseURLAUTH + '/signup', formData);
  }

  async editarUsuarioMedia(fotoPerfil: File, banner: File) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    const formData = new FormData();
    formData.append('imagen', fotoPerfil);
    formData.append('banner', banner);
    return this.http.patch<any>(this.baseURL + '/actualizarMedia', formData, { headers: headers });
  }

  async obtenerUsuarios() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<usuarioDTO[]>(this.baseURL, { headers: headers });
  }

  async ObtenerUsuarioNick(nick: string) {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })

    return this.http.get<usuarioDTO>(`${this.baseURL}/nick/${nick}`, { headers: headers });
  }

  async obtenerUsuariosSearch() {
    const accessToken = this.serviciotoken.getToken();

    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    })
    return this.http.get<usuarioSearch[]>(this.baseURL + '/search', { headers: headers });
  }

  async validarEmail(email: String) {
    return this.http.get<boolean>(this.baseURLAUTH + '/validar-email?email=' + email);
  }
}
