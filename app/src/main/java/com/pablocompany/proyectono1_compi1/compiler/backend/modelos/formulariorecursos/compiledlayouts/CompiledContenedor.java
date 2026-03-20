package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a toda la jerarquia de layouts. Es decir a estos componentes que almacenan dentro de si mismos mas cosas
public abstract class CompiledContenedor extends CompiledForm {

    /*Constructor*/
    public CompiledContenedor(Number width, Number height, Number pointX, Number pointY, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY,estilos,fila,columna);
    }

}
