package com.pablocompany.proyectono1_compi1.compiler.backend.exceptions;

//Clase delegada como excepcion que permite interrumpir el flujo para poder notificar errores ya en tiempo de compilacion
public class TiempoEjecucionException extends  Exception {
    public TiempoEjecucionException(String mensaje) {
        super(mensaje);
    }
}
