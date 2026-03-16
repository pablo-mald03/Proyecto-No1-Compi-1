package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
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

    //Metodo que permite validar las expresiones dentro de un color
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {
        if (color == null || color.isEmpty()) {
            listaErrores.add(new ErrorAnalisis("HEX", "Semantico",
                    "El valor del color \"HEX\" no debe estar vacio.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.COLOR;
    }

    //Metodo que permite ejecutar y retornar el valor del color
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores){
        return String.format("%s", color != null ? color : "0");
    }

    //Metodo que permite obtener el color especificado en la expresion
    @Override
    public int[] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        int red = Integer.parseInt(this.color.substring(1,3), 16);
        int green = Integer.parseInt(this.color.substring(3,5), 16);
        int blue = Integer.parseInt(this.color.substring(5,7), 16);

        return new int[]{red, green, blue};
    }

    //Metodo que permite retornar los valores del color en formato String
    @Override
    public  String getString(){
        return this.color;
    }

    /*--Metodo propio de la clase que permite contar los comodines que tienen en el color PRESET--*/
    public  int contarComodines(){
        return 0;
    }
}
/*Created by P*/