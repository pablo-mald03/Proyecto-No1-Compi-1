package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a un color en formato RGB contenmplando sus expresiones dentro
public class NodoRgbColor extends NodoColor{

    //Atributos
    private NodoExpresion red;
    private NodoExpresion green;
    private NodoExpresion blue;


    public NodoRgbColor(NodoExpresion red, NodoExpresion green, NodoExpresion blue, int linea, int columna) {
        super(linea, columna);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    //Metodo que permite validar las expresiones dentro de un color RGB (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {

        TipoVariable tipoRed = red.validarSemantica(tabla, listaErrores);
        TipoVariable tipoGreen = green.validarSemantica(tabla, listaErrores);
        TipoVariable tipoBlue = blue.validarSemantica(tabla, listaErrores);

        if (esLayout) {
            if (tipoRed == TipoVariable.COMODIN || tipoGreen == TipoVariable.COMODIN || tipoBlue == TipoVariable.COMODIN) {
                listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                        "Los colores dentro de \"SECTION\", \"TABLE\" o \"TEXT\" no permiten tipos COMODIN.",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        if ((tipoRed != TipoVariable.NUMBER && tipoRed != TipoVariable.COMODIN) ||
                (tipoGreen != TipoVariable.NUMBER && tipoGreen != TipoVariable.COMODIN) ||
                (tipoBlue != TipoVariable.NUMBER && tipoBlue != TipoVariable.COMODIN)) {

            listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                    "Los valores de color \"RGB\" deben ser \"numericos\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.COLOR;
    }

    /*Metodos getter de las expresiones*/
    public NodoExpresion getRed() {
        return red;
    }

    public NodoExpresion getGreen() {
        return green;
    }

    public NodoExpresion getBlue() {
        return blue;
    }

    /*Metodos que permiten setear de nuevo el valor de las expresiones*/

    public int setRed(List<NodoComodin> comodines, int iterador) {
        if(iterador < comodines.size() && this.red instanceof NodoComodin){
            NodoComodin comodin = (NodoComodin) this.red;
            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    public int setGreen(List<NodoComodin> comodines, int iterador) {
        if(iterador < comodines.size() && this.green instanceof NodoComodin){
            NodoComodin comodin = (NodoComodin) this.green;
            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    public int setBlue(List<NodoComodin> comodines, int iterador) {
        if(iterador < comodines.size() && this.blue instanceof NodoComodin){
            NodoComodin comodin = (NodoComodin) this.blue;
            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    //Metodo que permite retornar el color en formato RGB (util en frontend)
    @Override
    public int[] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valorRed = (this.red != null) ? this.red.ejecutar(tabla, listaErrores) : null;
        Object valorGreen = (this.green != null) ? this.green.ejecutar(tabla, listaErrores) : null;
        Object valorBlue = (this.blue != null) ? this.blue.ejecutar(tabla, listaErrores) : null;

        if (!(valorRed instanceof Number)) {
            listaErrores.add(new ErrorAnalisis(red != null ? red.getString() : "null", "Semántico", "Valor RED debe ser numerico", red.getLinea(), red.getColumna()));
            return null;
        }
        if (!(valorGreen instanceof Number)) {
            listaErrores.add(new ErrorAnalisis(green != null ? green.getString() : "null", "Semántico", "Valor GREEN debe ser numerico", green.getLinea(), green.getColumna()));
            return null;
        }
        if (!(valorBlue instanceof Number)) {
            listaErrores.add(new ErrorAnalisis(blue != null ? blue.getString() : "null", "Semántico", "Valor BLUE debe ser numerico", blue.getLinea(), blue.getColumna()));
            return null;
        }

        return new int[]{
                ((Number) valorRed).intValue(),
                ((Number) valorGreen).intValue(),
                ((Number) valorBlue).intValue()
        };

    }

    //Metodo que permite retornar los valores del color en formato String
    @Override
    public  String getString(){
        return "(" + this.red.getString() + ", " + this.green.getString() + ", " + this.blue.getString() + ")";
    }

    /*--Metodo propio de la clase que permite contar los comodines que tienen en el color RGB--*/
    public  int contarComodines(){
        int contador = 0;

        if(this.red != null){
            contador += this.red.contarComodines();
        }
        if(this.green != null){
            contador += this.green.contarComodines();
        }
        if(this.blue != null){
            contador += this.blue.contarComodines();
        }
        return contador;
    }

}
/*Created by P*/