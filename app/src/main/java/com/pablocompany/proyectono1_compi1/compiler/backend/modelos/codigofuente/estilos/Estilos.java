package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;

//Clase que representa los estilos que puede tener en general componentes del formulario
public class Estilos {

    //Atributos
    private NodoColor backgroundColor;
    private NodoColor color;
    private TipoLetra fontFamily;
    private NodoExpresion textSize;

    public Estilos(NodoColor backgroundColor, NodoColor color, TipoLetra fontFamily, NodoExpresion textSize) {
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.fontFamily = fontFamily;
        this.textSize = textSize;
    }

    //Getters y setters
    public NodoColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(NodoColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public NodoColor getColor() {
        return color;
    }

    public void setColor(NodoColor color) {
        this.color = color;
    }

    public TipoLetra getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(TipoLetra fontFamily) {
        this.fontFamily = fontFamily;
    }

    public NodoExpresion getTextSize() {
        return textSize;
    }

    public void setTextSize(NodoExpresion textSize) {
        this.textSize = textSize;
    }


}

/*Created by Pablo*/
