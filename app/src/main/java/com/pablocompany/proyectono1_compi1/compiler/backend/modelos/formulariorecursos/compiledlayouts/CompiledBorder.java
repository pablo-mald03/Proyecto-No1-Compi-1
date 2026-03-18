package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoBorde;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;

//Clase delegada que representa a los estilos de borde de un componente
public class CompiledBorder {

    //Atributos
    private Number width;
    private CompiledColor color;
    private TipoBorde tipo;

    public CompiledBorder(Number width, String tipo,CompiledColor color) {
        this.width = width;
        this.color = color;
        this.tipo = TipoBorde.valueOf(tipo);
    }

    public Number getWidth() {
        return width;
    }

    public CompiledColor getColor() {
        return color;
    }

    public TipoBorde getTipo() {
        return tipo;
    }
}
