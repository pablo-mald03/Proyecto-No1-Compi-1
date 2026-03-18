package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts.CompiledBorder;

//Clase delegada que representa a todos los estilos que puede tener un componente
public class CompiledEstilos {

    //Atributos de la clase que representa a los estilos
    private CompiledColor backgroundColor;
    private CompiledColor colorTexto;
    private TipoLetra fontFamily;
    private Number textSize;

    private CompiledBorder borde;

    //Constructor para inicializar los atributos de layouts
    public CompiledEstilos(CompiledColor colorTexto, CompiledColor backgroundColor, String fontFamily, Number textSize, CompiledBorder borde) {
        this.backgroundColor = backgroundColor;
        this.colorTexto = colorTexto;
        this.fontFamily = TipoLetra.valueOf(fontFamily);
        this.textSize = textSize;
        this.borde = borde;
    }

    //Constructor para inicializar los atributos de componentes
    public CompiledEstilos(CompiledColor colorTexto, CompiledColor backgroundColor, String fontFamily, Number textSize) {
        this.backgroundColor = backgroundColor;
        this.colorTexto = colorTexto;
        this.fontFamily = TipoLetra.valueOf(fontFamily);
        this.textSize = textSize;
        this.borde = null;
    }

    //Metodos getters de los atributos de la clase

    public CompiledColor getBackgroundColor() {
        return backgroundColor;
    }

    public CompiledColor getColorTexto() {
        return colorTexto;
    }

    public TipoLetra getFontFamily() {
        return fontFamily;
    }

    public Number getTextSize() {
        return textSize;
    }

    public CompiledBorder getBorde() {
        return borde;
    }
}
