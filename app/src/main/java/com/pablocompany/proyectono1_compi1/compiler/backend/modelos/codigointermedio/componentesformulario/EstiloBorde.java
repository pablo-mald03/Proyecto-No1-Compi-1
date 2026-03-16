package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoBorde;

//Clase que represetna a los estilos de los bordes de los componentes del formulario
public class EstiloBorde {

    private TipoBorde tipoBorde;
    private Number grosor;
    private String color;

    public EstiloBorde(TipoBorde tipoBorde, Number grosor, String color) {
        this.tipoBorde = tipoBorde;
        this.grosor = grosor;
        this.color = color;
    }

    /*--Metodos getters y setters que se tienen para poder acceder a los atributos privados--*/
    public TipoBorde getTipoBorde() {
        return tipoBorde;
    }

    public Number getGrosor() {
        return grosor;
    }

    public String getColor() {
        return color;
    }
}
