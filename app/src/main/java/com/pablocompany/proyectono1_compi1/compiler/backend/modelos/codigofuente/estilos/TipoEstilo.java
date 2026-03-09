package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos;

//Enums que representan los tipos de estilos que puede tener en general componentes del formulario
public enum TipoEstilo {

    BACKGROUND_COLOR("color"),
    COLOR_TEXTO("background color"),
    FONT_FAMILY("font family"),
    TEXT_SIZE("text size"),
    BORDER("border");

    private String valor;

    TipoEstilo(String valor) {
        this.valor = valor;
    }

    //Permite retornar el valor
    public String getString() {
        return valor;
    }
}
