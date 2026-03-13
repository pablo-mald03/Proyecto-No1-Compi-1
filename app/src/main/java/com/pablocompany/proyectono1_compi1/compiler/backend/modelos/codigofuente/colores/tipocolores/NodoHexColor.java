package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a un color en formato hexadecimal
public class NodoHexColor extends NodoColor {

    private String color;

    public NodoHexColor(String color, int linea, int columna) {
        super(linea, columna);
        this.color = color;
    }

    //Metodo que permite obtener el color especificado en la expresion
    @Override
    public int[] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        int red = Integer.parseInt(this.color.substring(1,3), 16);
        int green = Integer.parseInt(this.color.substring(3,5), 16);
        int blue = Integer.parseInt(this.color.substring(5,7), 16);

        return new int[]{red, green, blue};
    }
}
/*Created by P*/