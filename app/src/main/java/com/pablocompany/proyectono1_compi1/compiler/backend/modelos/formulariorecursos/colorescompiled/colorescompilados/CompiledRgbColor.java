package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.colorescompilados;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;

//Clase delegada que representa a un color en formato RGB
public class CompiledRgbColor extends CompiledColor {
    private Number red;
    private Number green;
    private Number blue;

    public CompiledRgbColor(Number red, Number green, Number blue, TablaSimbolos tabla) {
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
