package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.ValidadorDatosForms;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa los estilos que puede tener en general componentes del formulario
public class Estilos implements ValidadorDatosForms {

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

    //Metodo que permite generar la bifurcacion de codigo que permite validar la aceptacion de comodines en los estilos de questions
    /*Rechaza comodines en SECTIONS Y TABLES*/
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {
        if(esLayout){
            return validarSemantica(tabla, listaErrores);
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

        if (this.textSize != null) {
            TipoVariable tipoSize = this.textSize.validarSemantica(tabla, listaErrores);
            if (tipoSize != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis(textSize.getString(), "Semantico",
                        "El valor de \"text size\" debe ser de tipo numerico.", this.textSize.getLinea(), this.textSize.getColumna()));
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
    private void validarFuente(List<ErrorAnalisis> listaErrores) {
        if (this.fontFamily == TipoLetra.NOT_FOUND) {
            listaErrores.add(new ErrorAnalisis("font-family", "Semántico",
                    "La tipografía proporcionada no es válida o no está soportada por el sistema.",
                    0, 0));
        }
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
