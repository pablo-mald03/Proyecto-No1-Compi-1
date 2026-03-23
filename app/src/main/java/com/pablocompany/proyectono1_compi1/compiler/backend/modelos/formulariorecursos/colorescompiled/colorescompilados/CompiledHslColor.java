package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.colorescompilados;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;


//Clase delegada para representar al tipo de color HSL
public class CompiledHslColor extends CompiledColor {


    //Atributos
    private Number hue;
    private Number saturation;
    private Number lightness;

    public CompiledHslColor(Number hue, Number saturation, Number lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    //Metodo que permite retornar el color en formato HSL (util en frontend)
    @Override
    public int[] evaluarColor() {
        return hslToRgb(
                hue.doubleValue(),
                saturation.doubleValue(),
                lightness.doubleValue());
    }

    /*Metodo que permite convertir el formato hsl a un equivalente en rgb*/
    private int[] hslToRgb(double h, double s, double l) {

        s /= 100.0;
        l /= 100.0;

        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = l - c / 2.0;

        double redPrimero;
        double greenPrimero;
        double bluePrimero;

        if (h < 60) {
            redPrimero = c;
            greenPrimero = x;
            bluePrimero = 0;
        } else if (h < 120) {
            redPrimero = x;
            greenPrimero = c;
            bluePrimero = 0;
        } else if (h < 180) {
            redPrimero = 0;
            greenPrimero = c;
            bluePrimero = x;
        } else if (h < 240) {
            redPrimero = 0;
            greenPrimero = x;
            bluePrimero = c;
        } else if (h < 300) {
            redPrimero = x;
            greenPrimero = 0;
            bluePrimero = c;
        } else {
            redPrimero = c;
            greenPrimero = 0;
            bluePrimero = x;
        }

        return new int[]{
                (int) Math.round((redPrimero + m) * 255),
                (int) Math.round((greenPrimero + m) * 255),
                (int) Math.round((bluePrimero + m) * 255)
        };

    }

}