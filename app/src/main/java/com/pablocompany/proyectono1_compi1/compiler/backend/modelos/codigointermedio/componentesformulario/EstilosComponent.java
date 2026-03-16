package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;

//Clase que representa a los estilos de los componentes del formulario
public class EstilosComponent {

    //Atributos de estilos
    private String backgroundColor;
    private String color;
    private TipoLetra fontFamily;
    private Number textSize;

    public EstilosComponent() {
        this.backgroundColor = null;
        this.color = null;
        this.fontFamily = null;
        this.textSize = null;
    }

    //Metodos getter y setter
    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TipoLetra getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(TipoLetra fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Number getTextSize() {
        return textSize;
    }

    public void setTextSize(Number textSize) {
        this.textSize = textSize;
    }
}
