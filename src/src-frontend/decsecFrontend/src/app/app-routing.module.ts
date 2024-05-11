import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { authGuard } from './auth.guard';
import { UserComponent } from './user/user.component';
import { PerfilComponent } from './user/perfil/perfil.component';
import { PrincipalComponent } from './user/principal/principal.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'admin', component: AdminComponent, canActivate: [authGuard] },
  {path: 'user', 
  component: UserComponent,
  canActivate: [authGuard], 
  children: [
    { path: '', redirectTo: 'principal', pathMatch: 'full' },
    { path: 'perfil', component: PerfilComponent, canActivate: [authGuard]},
    { path: 'principal', component: PrincipalComponent, canActivate: [authGuard]}
  ]},
  { path: '', redirectTo: '/login', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/login' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}