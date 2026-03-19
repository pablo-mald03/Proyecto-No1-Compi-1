package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;

//Clase que representa al size de la letra
public class CompiledTextSize extends CompiledEstilos {

    //Atributo
    private Number textSize;

    //Constructor para inicializar los atributos de layouts
    public CompiledTextSize( Number textSize) {
        this.textSize = textSize;
    }

    //Metodo getter para obtener el size
    public Number getTextSize() {
        return textSize;
    }

}
