package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

//Enums representan uno de los tipos de variables que existen en el lenguaje

public enum TipoVariable {
    NUMBER("number"),
    STRING("string"),
    SPECIAL("special"),
    COMODIN("?"),
    BOOLEAN_AND("AND"),
    BOOLEAN_OR("OR"),
    VOID("void"),
    COLOR("color"),

    ERROR("error");

    private final String tipo;

    TipoVariable(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    //Metodo sobreescrito
    @Override
    public String toString() {
        return this.tipo;
    }
}