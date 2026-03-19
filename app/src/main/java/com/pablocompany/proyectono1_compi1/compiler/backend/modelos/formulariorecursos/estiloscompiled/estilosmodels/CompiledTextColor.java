
package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;

//Clase que representa al color de fondo compilado (COLOR REPRESENTA AL COLOR DE FONDO)
public class CompiledTextColor extends CompiledEstilos {

    //Atributos
    private CompiledColor colorTexto;

    public CompiledTextColor(CompiledColor colorTexto) {
        this.colorTexto = colorTexto;
    }

    //Metodos getters de los atributos de la clase
    public CompiledColor getColorTexto() {
        return colorTexto;
    }

}
