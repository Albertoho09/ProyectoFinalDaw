export interface Peticion {
    id: number;

    usuarioEmisor: number;

    usuarioReceptor: number;

    estado: Estado;

}

enum Estado{
    ACEPTADO,
    DENEGADA,
    PENDIENTE
}