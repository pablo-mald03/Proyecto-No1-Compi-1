package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.operadoreslogicos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la operacion de comparacion logica de AND
public class NodoAnd extends Nodo {
    //Atributos
    private Nodo condicionA;
    private Nodo condicionB;

    public NodoAnd(Nodo condicionA, Nodo condicionB, int linea, int columna) {
        super(linea, columna);
        this.condicionA = condicionA;
        this.condicionB = condicionB;
    }

    //Metodo que permite validar semantica del operador logico AND (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TipoVariable tipoA = condicionA.validarSemantica(tabla, listaErrores);
        TipoVariable tipoB = condicionB.validarSemantica(tabla, listaErrores);

        if (tipoA == TipoVariable.ERROR || tipoB == TipoVariable.ERROR) {
            return TipoVariable.ERROR;
        }

        if (tipoA == TipoVariable.COMODIN || tipoB == TipoVariable.COMODIN) {
            listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                    "Los operadores logicos \"AND\" no permiten tipos \"comodin\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (tipoA == TipoVariable.BOOLEAN_OR || tipoB == TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("AND", "Semantico",
                    "No se permite mezclar operadores AND y OR en una condicion.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (tipoA == TipoVariable.BOOLEAN_NOT || tipoB == TipoVariable.BOOLEAN_NOT) {
            listaErrores.add(new ErrorAnalisis("AND", "Semantico",
                    "No se permite el uso de \"NOT\" dentro de una operacion AND.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (tipoA != TipoVariable.BOOLEAN_AND && tipoA != TipoVariable.NUMBER &&
                tipoB != TipoVariable.BOOLEAN_AND && tipoB != TipoVariable.NUMBER) {

            listaErrores.add(new ErrorAnalisis("AND", "Semantico",
                    "El operador AND solo puede operar sobre condiciones lógicas o comparaciones.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.BOOLEAN_AND;
    }

    //Metodo que permite retornar la condicion logica que se maneja dentro
    /*
     *
     *  Comparacion tal como se pide en el enunciado (comparacion mayor que 0)
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object valorA = condicionA.ejecutar(tabla, listaErrores);

        if (valorA instanceof OnCompilacionError) return valorA;

        double numA = (valorA instanceof Number) ? ((Number) valorA).doubleValue() : 0.0;

        if (numA <= 0) {
            return 0.0;
        }

        Object valorB = condicionB.ejecutar(tabla, listaErrores);

        if (valorB instanceof OnCompilacionError) return valorB;

        double numB = (valorB instanceof Number) ? ((Number) valorB).doubleValue() : 0.0;

        return (numB > 0) ? 1.0 : 0.0;
    }

    //Metodo que permite retornar el valor de la expresion
    @Override
    public String getString() {
        return this.condicionA.getString() + " && " + this.condicionB.getString();
    }
}
/*Created by Pablo*/