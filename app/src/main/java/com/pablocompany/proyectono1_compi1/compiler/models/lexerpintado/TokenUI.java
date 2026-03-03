package com.pablocompany.proyectono1_compi1.compiler.models.lexerpintado;

//Clase especializada para poder manejar el coloreado de la UI de los tokens
public class TokenUI {

    //Atributos de la clase
    private final String lexema;
    private final int tipo;
    private final int linea;
    private final int columna;

    public TokenUI(String lexema, int tipo, int linea, int columna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }
    //Getters del token de la UI para poderlo colorear
    public int getColumna() {
        return columna;
    }

    public int getLinea() {
        return linea;
    }

    public int getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }
}
