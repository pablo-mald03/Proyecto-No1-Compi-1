package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;

//Representa los simbolos generados por el programa, es decir las variables y permitira evaluar SINTACTICAMENTE
//(Es decir que permitira verificar que no existan errores de sintaxis)
public class Simbolo {

    //Atributos
    private String id;
    private Object valor;
    private TipoVariable tipo;
    private int linea;
    private int columna;

    //Constructor
    public Simbolo( String id, TipoVariable tipo, Object valor, int linea, int columna) {
        this.columna = columna;
        this.linea = linea;
        this.tipo = tipo;
        this.valor = valor;
        this.id = id;
    }

    //Getters y Setters
    public int getColumna() {
        return columna;
    }
    /*P*/
    public int getLinea() {
        return linea;
    }

    public TipoVariable getTipo() {
        return tipo;
    }

    public Object getValor() {
        return valor;
    }

    public String getId() {
        return id;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public void setTipo(TipoVariable tipo) {
        this.tipo = tipo;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*Created by Pablo*/
}
