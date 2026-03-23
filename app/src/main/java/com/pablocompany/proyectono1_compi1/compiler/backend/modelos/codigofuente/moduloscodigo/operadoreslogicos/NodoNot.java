package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.operadoreslogicos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la operacion de comparacion logica de AND
public class NodoNot extends Nodo {

    //Atributos
    private Nodo condicion;

    public NodoNot(Nodo condicion, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
    }

    //Metodo que permite validar semantica del operador logico NOT (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TipoVariable tipoCondicion = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCondicion == TipoVariable.ERROR) {
            return TipoVariable.ERROR;
        }

        if (tipoCondicion == TipoVariable.COMODIN) {
            listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                    "El operador \"NOT\" no permite tipos \"comodin\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (tipoCondicion == TipoVariable.BOOLEAN_AND || tipoCondicion == TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("NOT", "Semantico",
                    "No se permite aplicar \"NOT\" directamente sobre operadores AND o OR.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (tipoCondicion != TipoVariable.NUMBER && tipoCondicion != TipoVariable.BOOLEAN_NOT) {

            listaErrores.add(new ErrorAnalisis("NOT", "Semantico",
                    "El operador NOT solo puede operar sobre condiciones lógicas.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.BOOLEAN_NOT;
    }

    //Metodo que permite retornar la condicion logica que se maneja dentro del OR
    /*
     *
     *  Comparacion tal como se pide en el enunciado (comparacion mayor que 0)
     *
     * Sin embargo este NIEGA LA SALIDA
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valor = condicion.ejecutar(tabla, listaErrores);

        if (valor instanceof OnCompilacionError) {
            return valor;
        }

        double num = (valor instanceof Number) ? ((Number) valor).doubleValue() : 0.0;

        if (num > 0) {

            return 1.0;
        } else {

            return 0.0;
        }
    }

    @Override
    public String getString() {
        return "~" + this.condicion.getString();
    }
}
/*Created by Pablo*/
