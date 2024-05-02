import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import axios from 'axios';
import { Usuario } from '../interfaces/Usuario';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private url = 'http://localhost:8080/api/v1/auth';

  constructor(private router: Router) {}

  signIn(user: Usuario): Observable<any> {
    return new Observable(observer => {
      axios.post(this.url + '/signin', user).then(response => {
        observer.next(response.data);
        observer.complete();
      })
        .catch(error => {
          observer.error(error);
        });
    });
  }

  loggedIn(): boolean {
    if (typeof sessionStorage !== 'undefined') {
      return !!sessionStorage.getItem('token');
    }
    return false;
  }

  getToken() {
    if (typeof sessionStorage !== 'undefined') {
      return sessionStorage.getItem('token');
    }
    return '';
  }

  logout() {
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.removeItem('token');
      this.router.navigate(['/login']);
    }
  }
}
