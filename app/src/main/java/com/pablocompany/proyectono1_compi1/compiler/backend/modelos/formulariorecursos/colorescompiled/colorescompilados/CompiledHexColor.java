package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.colorescompilados;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;


//Clase delegada para representar al tipo de color HEX
public class CompiledHexColor extends CompiledColor {

    private String color;

    public CompiledHexColor(String color) {
        this.color = color;
    }

    //Metodo que permite obtener el color especificado en la expresion
    @Override
    public int[] evaluarColor() {

        int red = Integer.parseInt(this.color.substring(1,3), 16);
        int green = Integer.parseInt(this.color.substring(3,5), 16);
        int blue = Integer.parseInt(this.color.substring(5,7), 16);

        return new int[]{red, green, blue};
    }
}
