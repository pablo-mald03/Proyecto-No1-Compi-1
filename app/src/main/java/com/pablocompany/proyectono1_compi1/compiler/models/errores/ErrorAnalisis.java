package com.pablocompany.proyectono1_compi1.compiler.models.errores;

//Clase que permite retornar los errores causados tanto por el analisis lexico como sintactico
public class ErrorAnalisis {

    //Atributos de la clase
    private String tipo;
    private String descripcion;
    private int linea;
    private int columna;
    private String cadena;


    //Constructor de la clase
    public ErrorAnalisis(String cadena,String tipo, String descripcion, int linea, int columna) {
        this.cadena = cadena;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }

    //Getters de la clase que permiten obtener todos los datos
    public int getColumna() {
        return columna;
    }

    public int getLinea() {
        return linea;
    }

    public String getCadena() {
        return cadena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }
}
