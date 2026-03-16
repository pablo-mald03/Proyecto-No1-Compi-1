package com.pablocompany.proyectono1_compi1.compiler.backend.exceptions;

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase excepcion delegada para poder generar una excepcion en tiempo de compilacion (Por prevencion)
public class OnCompilacionError {

    //Atributos que solo son utiles si aun no se ha reportado la excepcion
    private final int linea;
    private final int columna;
    private final String mensaje;

    /*Atributo que permite generar el comportamiento de la excepcion*/
    private boolean esReportada;


    public OnCompilacionError(String mensaje, int linea, int columna, boolean esReportada) {
        this.mensaje = mensaje;
        this.linea = linea;
        this.columna = columna;
        this.esReportada = esReportada;
    }

    //Metodos getters
    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getMensaje() {
        return mensaje;
    }

    // Metodo que permite reportar el error en tiempo de compilacion
    public void reportar(List<ErrorAnalisis> listaErrores, String contexto) {
        if (!esReportada) {
            listaErrores.add(new ErrorAnalisis(contexto, "OnCompilacionError", mensaje, linea, columna));
            esReportada = true;
        }
    }
}

/*Created by Pablo*/
