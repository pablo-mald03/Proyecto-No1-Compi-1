package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

//Enum que representa los tipos de configuraciones que puede tener en general componentes del formulario
public enum TipoConfiguracion {

    WIDTH("width"),
    HEIGHT("height"),
    LABEL("label"),
    POKEMON("pokemon"),
    STYLES("styles"),
    CONTENT("content"),
    POINT_X("pointX"),
    POINT_Y("pointY"),
    ORIENTATION("orientation"),
    CORRECT("correct"),
    OPTIONS("options"),
    ELEMENTS("elements");


    private String valor;

    TipoConfiguracion(String valor) {
        this.valor = valor;
    }

    //Permite retornar el en string
    public String getString() {
        return valor;
    }


}
