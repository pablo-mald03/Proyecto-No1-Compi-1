package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que permite generar solo las asignaciones a la variable
public class NodoAsignacion extends Nodo {

    //Atributos
    private String id;
    private Nodo expresion;

    public NodoAsignacion(String id, Nodo expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
        this.id = id;

    }

    //Metodo que permite validar semantica de una asignacion de algun valor a una variable
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Simbolo variable = tabla.buscar(id);

        if (variable == null) {
            listaErrores.add(new ErrorAnalisis(
                    this.id,
                    "Semantico",
                    "La variable \"" + this.id + "\" no ha sido declarada. No se le puede asignar un valor.",
                    getLinea(),
                    getColumna()
            ));
            return TipoVariable.ERROR;
        }

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        if (tipoExpresion != TipoVariable.ERROR) {
            if (variable.getTipo() != tipoExpresion) {
                listaErrores.add(new ErrorAnalisis(id, "Semantico",
                        "Tipos incompatibles. No se puede asignar " + tipoExpresion + " a " + variable.getTipo(),
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        return variable.getTipo();
    }

    //Metodo que permite ejecutar el nodo (Validacion de variables y declaraciones)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo variable = tabla.buscar(id);

        if (variable == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "La variable '" + id + "' no ha sido declarada", super.getLinea(), super.getColumna()));
            return null;
        }

        Object nuevoValor = expresion.ejecutar(tabla, listaErrores);

        if (variable.getTipo() == TipoVariable.NUMBER && !(nuevoValor instanceof Double || nuevoValor instanceof Integer)) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "Tipo incompatible: '" + id + "' es number", super.getLinea(), super.getColumna()));
            return null;
        } else if (variable.getTipo() == TipoVariable.STRING && !(nuevoValor instanceof String)) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "Tipo incompatible: '" + id + "' es string", super.getLinea(), super.getColumna()));
            return null;
        }

        variable.setValor(nuevoValor);
        return null;
    }

    //Retorna el valor literal escrito
    @Override
    public String getString() {
        return this.id + " = " + (expresion != null ? expresion.getString() : "null");
    }

    /*Created by Pablo*/
}
