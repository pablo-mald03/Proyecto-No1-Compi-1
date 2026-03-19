package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;

//Clase que representa al background compilado (BACKGROUND REPRESENTA AL COLOR DE TEXTO)
public class CompiledBackground extends CompiledEstilos {

    //Atributos
    private CompiledColor backgroundColor;

    //Constructor para inicializar los atributos de layouts
    public CompiledBackground(CompiledColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    //Metodos getters de los atributos de la clase

    public CompiledColor getBackgroundColor() {
        return backgroundColor;
    }
}
