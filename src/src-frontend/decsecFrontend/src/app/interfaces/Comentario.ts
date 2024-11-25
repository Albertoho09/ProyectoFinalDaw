export interface ComentarioAdmin {
    id: number;
    comentario: string;
    hora: Date;
    nickUsuario: string;
}

export interface Comentario {
    id: number;
    comentario: string;
    hora: Date;
    nickUsuario: string;
    fotoPerfil: Imagen;
}

export interface ComentarioRequest{
    comentario: String;
}

interface Imagen {
    id: number;
    nombre: string;
    tipo: string;
    datos: any;
}