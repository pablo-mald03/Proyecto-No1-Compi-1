package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.operaciones;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa la division de dos expresiones
public class NodoDivision extends NodoExpresion {
    //Atributos
    private NodoExpresion izquierda;
    private NodoExpresion derecha;

    public NodoDivision(NodoExpresion izquierda, NodoExpresion derecha, int linea, int columna) {
        super(linea, columna);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    //Metodo que permite validar semantica de la operacion division (PATRON EXPERTO)
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
                    "Division", "Semántico",
                    "La división no admite tipos \"" + tipoIzquierda.getTipo() + "\" y \"" + tipoDerecha.getTipo() + "\"",
                    getLinea(), getColumna()
            ));
        }

        return TipoVariable.ERROR;
    }

    //Metodo que permite clonar la expresion
    @Override
    public NodoExpresion clonar() {
        return new NodoDivision(this.izquierda.clonar(), this.derecha.clonar(), getLinea(), getColumna());
    }

    //Metodo que permite buscar comodines de forma recursiva en las expresiones
    @Override
    public void buscarComodines(List<NodoComodin> listaComodines){
        if (izquierda != null) {
            izquierda.buscarComodines(listaComodines);
        }
        if (derecha != null) {
            derecha.buscarComodines(listaComodines);
        }
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

        if (valorIzquierdo instanceof OnCompilacionError) return valorIzquierdo;

        Object valorDerecho = derecha.ejecutar(tabla, listaErrores);

        if (valorDerecho instanceof OnCompilacionError) return valorDerecho;

        if (valorDerecho instanceof Number && ((Number) valorDerecho).doubleValue() == 0) {
            listaErrores.add(new ErrorAnalisis(this.getString(), "OnCompilacionError", "Division de un valor numerico por cero.", super.getLinea(), super.getColumna()));
            return new OnCompilacionError("Division por cero", getLinea(), getColumna(), true);
        }


        if (valorIzquierdo instanceof Double && valorDerecho instanceof Double) {
            return (Double) valorIzquierdo / (Double) valorDerecho;
        }
        else if (valorIzquierdo instanceof Integer && valorDerecho instanceof Integer) {
            return (Integer) valorIzquierdo / (Integer) valorDerecho;
        }

        listaErrores.add(new ErrorAnalisis(this.getString(), "OnCompilacionError", "Solo se pueden dividir valores numericos", super.getLinea(), super.getColumna()));
        return new OnCompilacionError("Tipo incompatible", getLinea(), getColumna(), true);
    }

    //Metodo que permite obtener el string de la expresion
    @Override
    public String getString() {
        return izquierda.getString() + " / " + derecha.getString();
    }
}