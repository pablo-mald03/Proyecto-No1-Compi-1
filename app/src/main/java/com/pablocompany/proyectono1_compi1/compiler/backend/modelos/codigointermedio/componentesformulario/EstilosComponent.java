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






    /*Metodo que permite contar si el codigo tiene estilos (basicos)*/
    public boolean tieneEstilos(){

        if(this.backgroundColor != null){
            return true;
        }

        if(this.textSize != null){
            return true;
        }

        if(this.color != null){
            return true;
        }

        if(this.fontFamily != null){
            return true;
        }

        return false;
    }


    //---Metodo que permite crear los estilos basicos de las preguntas o textos------
    public String crearEstilosBasicos(){

        StringBuilder estilos = new StringBuilder();

        estilos.append("    <style>");
        estilos.append("\n\n");

        if(this.color != null){
            estilos.append("        <color=");
            estilos.append(this.color );
            estilos.append("/>");
            estilos.append("\n");
        }

        if(this.backgroundColor != null){
            estilos.append("        <background color=");
            estilos.append(this.backgroundColor);
            estilos.append("/>");
            estilos.append("\n");
        }


        if(this.fontFamily != null){
            estilos.append("        <font family=");
            estilos.append(this.fontFamily.toString());
            estilos.append("/>");
            estilos.append("\n");
        }

        if(this.textSize != null){
            estilos.append("        <text size=");
            estilos.append(this.textSize.toString());
            estilos.append(">");
            estilos.append("\n");
        }

        estilos.append("\n\n    </style>");

        return estilos.toString();
    }


    //---Metodo que permite crear los estilos basicos de las secciones o tablas------
    public String crearEstilosLayout() {
        StringBuilder estilos = new StringBuilder();
        estilos.append("\n\n    <style>\n");

        if (this.color != null) {
            estilos.append("\n        <color=").append(this.color).append("/>");
        }
        if (this.backgroundColor != null) {
            estilos.append("\n        <background color=").append(this.backgroundColor).append("/>");
        }
        if (this.fontFamily != null) {
            estilos.append("\n        <font family=").append(this.fontFamily).append("/>");
        }
        if (this.textSize != null) {
            estilos.append("\n        <text size=").append(this.textSize).append(">");
        }

        return estilos.toString();
    }

}
