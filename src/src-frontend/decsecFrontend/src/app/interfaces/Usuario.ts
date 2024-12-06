import { publicacionAdmin } from "./Publicacion";

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

export interface usuarioDTO {
    nick: string;
    nombre: string;
    apellidos: string;
    email: string;
    fechaNac: Date;
    privado: Boolean;
    npublicaciones: number;
    roles: Array<String>;
    fotoperfil: Imagen;
    banner: Imagen;
}


export interface usuarioSearch {
    nick: string;
    foto: Imagen;
}

interface Imagen {
    id: number;
    nombre: string;
    tipo: string;
    datos: any;
}