package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa la forma de poder representar un color en formato HSL
public class NodoHslColor extends NodoColor {

    //Atributos
    private NodoExpresion red;
    private NodoExpresion green;
    private NodoExpresion blue;


    public NodoHslColor(NodoExpresion red, NodoExpresion green, NodoExpresion blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public NodoExpresion getRed() {
        return red;
    }

    public NodoExpresion getGreen() {
        return green;
    }

    public NodoExpresion getBlue() {
        return blue;
    }

    //Metodo que permite retornar el color en formato RGB (util en frontend)
    @Override
    public int[] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valorRed = (this.red != null) ? this.red.ejecutar(tabla, listaErrores) : null;
        Object valorGreen = (this.green != null) ? this.green.ejecutar(tabla, listaErrores) : null;
        Object valorBlue = (this.blue != null) ? this.blue.ejecutar(tabla, listaErrores) : null;

        /*if (valorRed.getTipo() == TipoVariable.NUMBER && !(valorRed instanceof Double || valorRed instanceof Integer)) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "Tipo incompatible: '" + id + "' es number", super.getLinea(), super.getColumna()));
            return null;
        }*/



        return new int[0];
    }
}
/*Created by P*/

