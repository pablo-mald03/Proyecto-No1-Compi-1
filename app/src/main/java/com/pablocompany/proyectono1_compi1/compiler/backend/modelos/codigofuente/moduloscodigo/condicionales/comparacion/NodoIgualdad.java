package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales.comparacion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

public class NodoIgualdad extends Nodo {
    //Atributos
    Nodo expA;
    Nodo expB;

    public NodoIgualdad(Nodo expA, Nodo expB, int linea, int columna) {
        super(linea, columna);
        this.expA = expA;
        this.expB = expB;
    }


    //Expresion que permite evaluar el operador logico igual
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object valorA = expA.ejecutar(tabla, listaErrores);
        Object valorB = expB.ejecutar(tabla, listaErrores);

        if (valorA instanceof Number && valorB instanceof Number) {

            double numeroA = ((Number) valorA).doubleValue();
            double numeroB = ((Number) valorB).doubleValue();

            return (numeroA == numeroB)?1.0:0.0;
        }

        if (valorA instanceof String && valorB instanceof String) {
            String valorStringA = (String) valorA;
            String valorStringB = (String) valorB;
            return (valorStringA.equals(valorStringB)) ? 1.0 : 0.0;
        }

        listaErrores.add(new ErrorAnalisis(
                this.getString(),
                "Semántico",
                "Los operandos deben ser ambos numéricos o ambos strings para la comparación \"igual\".",
                this.getLinea(),
                this.getColumna()));

        return 0.0;
    }

    @Override
    public String getString() {
        return "==";
    }
}
