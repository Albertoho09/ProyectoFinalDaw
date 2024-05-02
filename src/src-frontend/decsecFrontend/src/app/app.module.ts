import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RatingModule } from 'primeng/rating';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmationService } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { PasswordModule } from 'primeng/password';
import { CalendarModule } from 'primeng/calendar';
import { AvatarModule } from 'primeng/avatar';
import { FileUploadModule } from 'primeng/fileupload';
import { InputSwitchModule } from 'primeng/inputswitch';

import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AdminComponent } from './admin/admin.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { MessageService } from 'primeng/api';
import { CardloginComponent } from './login/cardlogin/cardlogin.component';
import { AuthServiceService } from './servicios/auth-service.service';
import { CascadeSelectModule } from 'primeng/cascadeselect';
import { SelectButtonModule } from 'primeng/selectbutton';
import { RippleModule } from 'primeng/ripple';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { TemasServiceService } from './servicios/temas-service.service';
import { BotonTemaComponent } from './componentes/boton-tema/boton-tema.component';
import { MenuadminComponent } from './admin/menuadmin/menuadmin.component';
import { UsuarioService } from './servicios/usuario.service';
import { TablausuarioComponent } from './admin/tablausuario/tablausuario.component';
import { HttpClientModule } from '@angular/common/http';
import { TablapublicacionComponent } from './admin/tablapublicacion/tablapublicacion.component';
import { PublicacionService } from './servicios/publicacion.service';
import { PublicacionformComponent } from './componentes/publicacionform/publicacionform.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminComponent,
    LoginComponent,
    CardloginComponent,
    BotonTemaComponent,
    MenuadminComponent,
    TablausuarioComponent,
    TablapublicacionComponent,
    PublicacionformComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    InputSwitchModule,
    CardModule,
    TagModule,
    HttpClientModule,
    PasswordModule,
    CalendarModule,
    FileUploadModule,
    SelectButtonModule,
    CascadeSelectModule,
    BrowserAnimationsModule,
    FormsModule,
    AvatarModule,
    TableModule,
    InputTextModule,
    RippleModule,
    DialogModule,
    CheckboxModule,
    ToolbarModule,
    ConfirmDialogModule,
    RatingModule,
    InputNumberModule,
    ToastModule,
    InputTextareaModule,
    RadioButtonModule,
    DropdownModule,
    ButtonModule,
    MenubarModule,
    AppRoutingModule
  ],
  providers: [ConfirmationService, MessageService, AuthServiceService, ConfirmDialogModule, TemasServiceService, UsuarioService, PublicacionService],
  bootstrap: [AppComponent]
})
export class AppModule { }
