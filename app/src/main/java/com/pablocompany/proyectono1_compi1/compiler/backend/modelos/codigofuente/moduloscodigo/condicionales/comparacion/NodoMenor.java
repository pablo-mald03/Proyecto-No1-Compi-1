package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales.comparacion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al operador de comparacion menor que
public class NodoMenor extends Nodo {
    //Atributos
    Nodo expresionA;
    Nodo expresionB;

    public NodoMenor( Nodo expA, Nodo expB,int linea, int columna) {
        super(linea, columna);
        this.expresionA = expA;
        this.expresionB = expB;
    }

    //Metodo que permite validar semantica de Menor que  (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TipoVariable tipoA = (expresionA != null) ? expresionA.validarSemantica(tabla, listaErrores) : TipoVariable.ERROR;
        TipoVariable tipoB = (expresionB != null) ? expresionB.validarSemantica(tabla, listaErrores) : TipoVariable.ERROR;

        if (tipoA == TipoVariable.ERROR || tipoB == TipoVariable.ERROR) {
            return TipoVariable.ERROR;
        }

        if (tipoA == TipoVariable.COMODIN || tipoB == TipoVariable.COMODIN) {
            return TipoVariable.COMODIN;
        }
        if (tipoA == tipoB) {
            return TipoVariable.NUMBER;
        }

        listaErrores.add(new ErrorAnalisis("Menor que", "Semantico",
                "No se puede comparar el tipo: \"" + tipoA.getTipo() + "\" con el tipo: \"" + tipoB.getTipo()+"\"",
                getLinea(), getColumna()));

        return TipoVariable.ERROR;

    }


    //Expresion que permite evaluar el operador logico menor
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object valorA = expresionA.ejecutar(tabla, listaErrores);
        Object valorB = expresionB.ejecutar(tabla, listaErrores);

        if (valorA instanceof Number && valorB instanceof Number) {

            double numeroA = ((Number) valorA).doubleValue();
            double numeroB = ((Number) valorB).doubleValue();

            return (numeroA < numeroB)?1.0:0.0;
        }

        if (valorA instanceof String && valorB instanceof String) {
            return (((String) valorA).compareTo((String) valorB) < 0) ? 1.0 : 0.0;
        }

        listaErrores.add(new ErrorAnalisis(
                this.getString(),
                "Semántico",
                "Los operandos deben ser ambos numéricos o ambos strings para la comparación \"menor\".",
                this.getLinea(),
                this.getColumna()));

        return 0.0;
    }

    @Override
    public String getString() {
        return "<";
    }
}
