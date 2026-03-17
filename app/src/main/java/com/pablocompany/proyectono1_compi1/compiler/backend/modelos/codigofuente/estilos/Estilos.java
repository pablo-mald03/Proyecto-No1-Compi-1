package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores.NodoHslColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores.NodoRgbColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoTipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa los estilos que puede tener en general componentes del formulario
public class Estilos {

    //Atributos
    private NodoColor backgroundColor;
    private NodoColor color;
    private NodoTipoLetra fontFamily;
    private NodoExpresion textSize;

    public Estilos(NodoColor backgroundColor, NodoColor color, NodoTipoLetra fontFamily, NodoExpresion textSize) {
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.fontFamily = fontFamily;
        this.textSize = textSize;
    }

    public Estilos() {

    }

    //Metodo que permite generar la bifurcacion de codigo que permite validar la aceptacion de comodines en los estilos de questions
    /*Rechaza comodines en SECTIONS Y TABLES*/
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {

        this.validarFuente(tabla,listaErrores);

        if(esLayout){
            return this.validarSemantica(tabla, listaErrores);
        }

        if (this.textSize != null) {
            TipoVariable tipoSize = this.textSize.validarSemantica(tabla, listaErrores);
            if (tipoSize != TipoVariable.NUMBER && tipoSize != TipoVariable.COMODIN && tipoSize != TipoVariable.ERROR) {
                listaErrores.add(new ErrorAnalisis(textSize.getString(), "Semantico",
                        "En preguntas, \"text size\" debe ser \"numerico\" o \"comodin\".",
                        this.textSize.getLinea(), this.textSize.getColumna()));
            }
        }

        if(this.color != null){
            this.color.validarSemantica(tabla, listaErrores,false);

        }

        if(this.backgroundColor != null){
            this.backgroundColor.validarSemantica(tabla, listaErrores,false);
        }

        return TipoVariable.VOID;
    }


    //Metodo que sirve para anlizar la expresion que esta dentro de los estilos
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        this.validarFuente(tabla,listaErrores);

        if (this.textSize != null) {
            TipoVariable tipoSize = this.textSize.validarSemantica(tabla, listaErrores);
            if (tipoSize != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis(textSize.getString(), "Semantico",
                        "El valor de \"text size\" debe ser de tipo numerico dentro de \"SECTION\", \"TABLE\" o \"TEXT\".", this.textSize.getLinea(), this.textSize.getColumna()));
            }
        }

        if(this.color != null){
           this.color.validarSemantica(tabla, listaErrores,true);
        }

        if(this.backgroundColor != null){
            this.backgroundColor.validarSemantica(tabla, listaErrores,true);
        }

        return TipoVariable.VOID;
    }

    //Metodo que permite validar la fuente de los estilos (Delegacion de metodo)
    private void validarFuente(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.fontFamily != null) {
            this.fontFamily.validarSemantica(tabla,listaErrores);
        }
    }

    //Getters y setters
    public NodoColor getBackgroundColor() {
        return backgroundColor;
    }

    /*Metodo que permite setear el nuevo valor de background color*/
    public int setBackgroundColor(List<NodoComodin> comodines, int iterador) {

        if(this.backgroundColor instanceof NodoRgbColor){
            NodoRgbColor rgbColor = (NodoRgbColor) this.backgroundColor;
            iterador = rgbColor.setRed(comodines, iterador);
            iterador = rgbColor.setGreen(comodines, iterador);
            iterador = rgbColor.setBlue(comodines, iterador);
            return iterador;
        }

        if(this.backgroundColor instanceof NodoHslColor){
            NodoHslColor rgbColor = (NodoHslColor) this.backgroundColor;
            iterador = rgbColor.setRed(comodines, iterador);
            iterador = rgbColor.setGreen(comodines, iterador);
            iterador = rgbColor.setBlue(comodines, iterador);
            return iterador;
        }
        return iterador;
    }

    public NodoColor getColor() {
        return color;
    }

    public int setColor(List<NodoComodin> comodines, int iterador) {

        if(this.color instanceof NodoRgbColor){
            NodoRgbColor rgbColor = (NodoRgbColor) this.color;
            iterador = rgbColor.setRed(comodines, iterador);
            iterador = rgbColor.setGreen(comodines, iterador);
            iterador = rgbColor.setBlue(comodines, iterador);
            return iterador;
        }

        if(this.color instanceof NodoHslColor){
            NodoHslColor rgbColor = (NodoHslColor) this.color;
            iterador = rgbColor.setRed(comodines, iterador);
            iterador = rgbColor.setGreen(comodines, iterador);
            iterador = rgbColor.setBlue(comodines, iterador);
            return iterador;
        }
        return iterador;
    }

    public NodoTipoLetra getFontFamily() {
        return fontFamily;
    }

    /*--Metodo que permite obtener la expresion del text size---*/
    public NodoExpresion getTextSize() {
        return textSize;
    }

    public int setTextSize(List<NodoComodin> comodines, int iterador) {

        if (iterador < comodines.size() && this.textSize instanceof NodoComodin) {
            NodoComodin comodin = (NodoComodin) this.textSize;

            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    public void setTextSize(NodoExpresion textSize) {
        this.textSize = textSize;
    }

    /*--Metodo propio de la clase que permite contar los comodines que tienen en los estilos--*/
    public  int contarComodines(){
        int contador = 0;

        if(this.color != null){
            contador += this.color.contarComodines();
        }
        if(this.backgroundColor != null){
            contador += this.backgroundColor.contarComodines();
        }
        if(this.fontFamily != null){
            contador += this.fontFamily.contarComodines();
        }
        if(this.textSize != null){
            contador += this.textSize.contarComodines();
        }
        return contador;
    }

    //Metodo que permite clonar
    public Estilos clonar() {
        Estilos clon = new Estilos();

        clon.backgroundColor = (this.backgroundColor != null) ? this.backgroundColor.clonar() : null;
        clon.color = (this.color != null) ? this.color.clonar() : null;
        clon.fontFamily = (this.fontFamily != null) ? this.fontFamily.clonar() : null;
        clon.textSize = (this.textSize != null) ? this.textSize.clonar() : null;
        return clon;
    }


}

/*Created by Pablo*/
