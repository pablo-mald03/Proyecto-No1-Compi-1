package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

//Enums representan uno de los tipos de variables que existen en el lenguaje

public enum TipoVariable {

    NUMBER("number"),
    STRING("string"),
    SPECIAL("special"),
    COMODIN("?"),
    VOID("void"),
    ERROR("error");


    private final String tipo;

    TipoVariable(String tipo) {
        this.tipo = tipo;
    }

    //Permite obtener el valor en String de la variable
    public String getTipo() {
        return tipo;
    }
}
