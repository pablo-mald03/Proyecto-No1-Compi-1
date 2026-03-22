package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa al ciclo while dentro del codigo fuente
public class NodoWhile extends Nodo {

    //Atributos
    private Nodo condicion;
    private List<Nodo> codigo;

    public NodoWhile(Nodo condicion, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.codigo = codigo;
    }

    //Metodo que permite validar semantica del ciclo while (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        /*Tabla que permite validar variables locales*/
        TipoVariable tipoCondicion = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCondicion != TipoVariable.NUMBER
                && tipoCondicion != TipoVariable.COMODIN
                && tipoCondicion != TipoVariable.ERROR
                && tipoCondicion != TipoVariable.BOOLEAN_AND
                && tipoCondicion != TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("WHILE", "Semantico",
                    "La condición del ciclo WHILE debe ser una expresion numerica o logica.",
                    getLinea(), getColumna()));
        }

        if (codigo != null) {
            for (Nodo nodo : codigo) {
                if (nodo != null) {
                    nodo.validarSemantica(tabla, listaErrores);
                }
            }
        }

        return TipoVariable.VOID;

    }

    //Metodo que permite ejecutar el ciclo while (PATRON EXPERTO)
    /*
     *Solo se convierte a un ciclo while normal
     */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        List<Formulario> componentesAcumulados = new ArrayList<>();

        while (true) {
            Object valorCondicion = condicion.ejecutar(tabla, listaErrores);

            if (valorCondicion instanceof OnCompilacionError) return valorCondicion;

            if (!(valorCondicion instanceof Number) || ((Number) valorCondicion).doubleValue() <= 0.0) {
                break;
            }

            if (codigo != null) {
                for (Nodo nodo : codigo) {
                    if (nodo != null) {
                        Object resultado = nodo.ejecutar(tabla, listaErrores);

                        if (resultado instanceof OnCompilacionError) {
                            return resultado;
                        }

                        if (resultado instanceof Formulario) {
                            componentesAcumulados.add((Formulario) resultado);
                        } else if (resultado instanceof List) {
                            componentesAcumulados.addAll((List<Formulario>) resultado);
                        }
                    }
                }
            }
        }

        return componentesAcumulados;
    }

    /*-----Metodo que permite ejecutar los requests hacia la pokeAPI en las preguntas (SEGUNDA PASADA)-----*/
    @Override
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores) {
        if (this.codigo != null) {
            for (Nodo nodo : this.codigo) {
                nodo.ejecutarRequests(tabla, errores);
            }
        }
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
