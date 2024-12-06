export interface Peticion {
    id: number;
    usuarioReceptorNick: String;
    usuarioEmisorNick: String;
    estado: Estado;
    usuarioEmisorFoto: Imagen;
    usuarioReceptorFoto: Imagen;
}

interface Imagen {
    id: number;
    nombre: string;
    tipo: string;
    datos: any;
}

export enum Estado{
    ACEPTADO = "ACEPTADO",
    DENEGADA = "DENEGADA",
    PENDIENTE = "PENDIENTE"
}