package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.colorescompilados;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores.TipoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;


//Clase delegada para representar al color preset para poderlo retornar a front
public class CompiledPresetColor extends CompiledColor {

    //Atributos
    private TipoColor colorPreset;

    public CompiledPresetColor(TipoColor colorPreset) {
        this.colorPreset = colorPreset;
    }

    //Metodo que permite retornar el color en formato RGB (util en frontend)
    @Override
    public int[] evaluarColor() {
        switch (colorPreset) {
            case RED:    return new int[]{255, 0, 0};
            case BLUE:   return new int[]{0, 0, 255};
            case GREEN:  return new int[]{0, 128, 0};
            case PURPLE: return new int[]{128, 0, 128};
            case SKY:    return new int[]{135, 206, 235};
            case YELLOW: return new int[]{255, 255, 0};
            case BLACK:  return new int[]{0, 0, 0};
            case WHITE:  return new int[]{255, 255, 255};
            default:
                return new int[]{10, 10, 10};
        }
    }
}
