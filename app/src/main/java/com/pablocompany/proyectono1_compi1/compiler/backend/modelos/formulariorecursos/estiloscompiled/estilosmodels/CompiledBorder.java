package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoBorde;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;

//Clase delegada que representa a los estilos de borde de un componente
public class CompiledBorder extends CompiledEstilos {

    //Atributos
    private Number width;
    private CompiledColor color;
    private TipoBorde tipo;

    public CompiledBorder(Number width, String tipos, CompiledColor color) {
        this.width = width;
        this.color = color;

        try {
            this.tipo = TipoBorde.valueOf(tipos.toUpperCase());

        } catch (Exception e) {
            this.tipo = TipoBorde.DOTTED;
        }
    }

    //Metodos que permiten obtener los atributos de la clase
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
