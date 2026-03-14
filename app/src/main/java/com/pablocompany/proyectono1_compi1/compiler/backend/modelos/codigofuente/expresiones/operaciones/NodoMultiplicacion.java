package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.operaciones;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa la multiplicacion de dos expresiones
public class NodoMultiplicacion extends NodoExpresion {
    //Atributos
    private NodoExpresion izquierda;
    private NodoExpresion derecha;

    public NodoMultiplicacion(NodoExpresion izquierda, NodoExpresion derecha, int linea, int columna) {
        super(linea, columna);
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
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
            return (Double) valorIzquierdo * (Double) valorDerecho;
        }
        else if (valorIzquierdo instanceof Integer && valorDerecho instanceof Integer) {
            return (Integer) valorIzquierdo * (Integer) valorDerecho;
        }

        listaErrores.add(new ErrorAnalisis(this.getString(), "Semántico", "Solo se pueden multiplicar valores numericos", super.getLinea(), super.getColumna()));
        return null;
    }

    //Metodo que permite obtener el string de la expresion
    @Override
    public String getString() {
        return izquierda.getString() + " * " + derecha.getString();
    }
}
