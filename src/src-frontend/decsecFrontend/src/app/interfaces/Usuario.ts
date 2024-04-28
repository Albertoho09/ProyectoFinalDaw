export interface Usuario {
    email: string;
    password: string;
}

export interface SignUpRequest {
    nick: string;
    nombre: string;
    apellidos: string;
    email: string;
    fechaNac: Date;
    password: string;
    privado: Boolean;
}

export interface usuarioAdmin {
    nick: string;
    nombre: string;
    apellidos: string;
    email: string;
    fechaNac: Date;
    password: string;
    privado: Boolean;
    roles: Array<String>;
    foto: File;
}