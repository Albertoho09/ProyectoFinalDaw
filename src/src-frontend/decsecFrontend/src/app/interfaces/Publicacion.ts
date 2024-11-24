export interface publicacionAdmin {
    id: number;
    imagenes: Imagen[];
    comentarioUsuario: string;
    megusta: number;
    fechaPublicacion: Date;
    displayBasic: Boolean;
}

export interface publicacionForm {
    comentarioUsuario: string;
}

export interface Publicacion {
    id: number;
    imagenes: Imagen[];
    comentarioUsuario: string;
    megusta: number;
    fechaPublicacion: Date;
    nick: String;
    fotoPerfil: Imagen;
}

interface Imagen {
    id: number;
    nombre: string;
    tipo: string;
    datos: any;
}

