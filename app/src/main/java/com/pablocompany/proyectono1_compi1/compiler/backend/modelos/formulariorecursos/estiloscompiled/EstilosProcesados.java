package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;

/*Clase delegada para poder generar la intercomunicacion directa con la UI*/
/*Esta clase esta delegada para ya tener empaquetados todos los estilos listos para la UI*/
public class EstilosProcesados {

    //Atributos
    private int [] backgroudColor;
    private int [] textColor;
    private TipoLetra fontFamilly;
    private Number textSize;

    //Nuleable
    private CompiledBorder border;

    /*Constructor para questions y textos*/
    public EstilosProcesados(int[] backgroudColor, int[] textColor, TipoLetra fontFamilly, Number textSize, CompiledBorder border) {
        this.backgroudColor = backgroudColor;
        this.textColor = textColor;
        this.fontFamilly = fontFamilly;
        this.textSize = textSize;
        this.border = border;
    }

    /*Constructor para sections y tables*/
    public EstilosProcesados(int[] backgroudColor, int[] textColor, TipoLetra fontFamilly, Number textSize) {
        this.backgroudColor = backgroudColor;
        this.textColor = textColor;
        this.fontFamilly = fontFamilly;
        this.textSize = textSize;
        this.border = null;
    }

    /*Getters directos*/

    public int[] getBackgroudColor() {
        return backgroudColor;
    }

    public int[] getTextColor() {
        return textColor;
    }

    public TipoLetra getFontFamilly() {
        System.out.println("Tipo de letra enviada: " + this.fontFamilly);
        return fontFamilly;
    }

    public Number getTextSize() {
        return textSize;
    }

    public CompiledBorder getBorder() {
        return border;
    }
}
/*Created by Pablo*/
