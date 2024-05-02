export interface publicacionAdmin {
    id: number;
    imagenes: Imagen[];
    comentarioUsuario: string;
    megusta: number;
    fechaPublicacion: Date;
}

export interface publicacionForm {
    comentarioUsuario: string;
}

interface Imagen {
    id: number;
    nombre: string;
    tipo: string;
    datos: any;
}