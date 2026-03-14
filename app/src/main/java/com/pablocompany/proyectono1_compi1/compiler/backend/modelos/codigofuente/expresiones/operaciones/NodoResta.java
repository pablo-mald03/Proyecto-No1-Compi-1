package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.operaciones;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa la resta de dos expresiones
public class NodoResta extends NodoExpresion {
    //Atributos
    private NodoExpresion izquierda;
    private NodoExpresion derecha;

    public NodoResta(NodoExpresion izquierda, NodoExpresion derecha, int linea, int columna) {
        super(linea, columna);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    //Metodo que permite validar semantica de la operacion resta (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TipoVariable tipoIzquierda = (izquierda != null) ? izquierda.validarSemantica(tabla, listaErrores) : TipoVariable.ERROR;
        TipoVariable tipoDerecha = (derecha != null) ? derecha.validarSemantica(tabla, listaErrores) : TipoVariable.ERROR;

        if (tipoIzquierda == TipoVariable.NUMBER && tipoDerecha == TipoVariable.NUMBER) {
            return TipoVariable.NUMBER;
        }

        if (tipoIzquierda == TipoVariable.COMODIN || tipoDerecha == TipoVariable.COMODIN) {
            return TipoVariable.COMODIN;
        }

        if (tipoIzquierda != TipoVariable.ERROR && tipoDerecha != TipoVariable.ERROR) {
            listaErrores.add(new ErrorAnalisis(
                    "Resta", "Semántico",
                    "La resta no admite tipos \"" + tipoIzquierda.getTipo() + "\" y \"" + tipoDerecha.getTipo() + "\"",
                    getLinea(), getColumna()
            ));
        }

        return TipoVariable.ERROR;
    }

    //Metodo propio que permite contar la cantidad de comodines que tiene la expresion
    @Override
    public int contarComodines() {
        return izquierda.contarComodines() + derecha.contarComodines();
    }

    //Metodo que permite ejecutar la expresion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object valorIzquierdo = izquierda.ejecutar(tabla, listaErrores);
        Object valorDerecho = derecha.ejecutar(tabla, listaErrores);

        if (valorIzquierdo instanceof Double && valorDerecho instanceof Double) {
            return (Double) valorIzquierdo - (Double) valorDerecho;
        }
        else if (valorIzquierdo instanceof Integer && valorDerecho instanceof Integer) {
            return (Integer) valorIzquierdo - (Integer) valorDerecho;
        }

        listaErrores.add(new ErrorAnalisis(this.getString(), "Semántico", "Solo se pueden restar valores numericos", super.getLinea(), super.getColumna()));
        return null;
    }

    //Metodo que permite obtener el string de la expresion
    @Override
    public String getString() {
        return izquierda.getString() + " - " + derecha.getString();
    }
}