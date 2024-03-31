import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AdministradorComponent } from './pages/administrador/administrador.component';

export const routes: Routes = [
    { path: '', component:  LoginComponent},
    { path: 'adminConsole', component:  AdministradorComponent}
];
