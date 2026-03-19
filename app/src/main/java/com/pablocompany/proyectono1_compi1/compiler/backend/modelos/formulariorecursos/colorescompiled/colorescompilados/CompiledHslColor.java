package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.colorescompilados;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;


//Clase delegada para representar al tipo de color HSL
public class CompiledHslColor extends CompiledColor {


    //Atributos
    private Number red;
    private Number blue;
    private Number green;

    public CompiledHslColor(Number red, Number green, Number blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    //Metodo que permite retornar el color en formato RGB (util en frontend)
    @Override
    public int[] evaluarColor() {

        return new int[]{
                (red).intValue(),
                (blue).intValue(),
                (green).intValue()
        };

    }
}
