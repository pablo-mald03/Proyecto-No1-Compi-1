package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa a los condicionales if dentro de el codigo de programacion fuente
/*
 * ES IMPORTANTE DESTACAR QUE EL NodoElseIf no existe
 * Por el simple hecho que en tiempo de compilacion nada mas es una condicion mas como if
 * ES DECIR QUE ES COMO OTRO IF ABAJO PERO COMO OTRA OPCION DEL PROPIO IF PADRE
 *
 *
 * */
/*Created by Pablo*/
public class NodoIf extends Nodo {

    //Atributos
    private Nodo condicion;
    private List<Nodo> codigo;
    //Atributo muy importante porque puede ser else if o else
    private Nodo nodoElse;

    public NodoIf(Nodo condicion, List<Nodo> codigo, Nodo nodoElse, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.codigo = codigo;
        this.nodoElse = nodoElse;
    }

    //Metodo que permite validar semantica del condicional if (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TipoVariable tipoCond = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCond != TipoVariable.NUMBER
                && tipoCond != TipoVariable.COMODIN
                && tipoCond != TipoVariable.ERROR
                && tipoCond != TipoVariable.BOOLEAN_AND
                && tipoCond != TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("IF", "Semantico",
                    "La condicion del IF debe ser una expresion logica o numerica.", getLinea(), getColumna()));
        }
        /*Tabla que permite validar variables locales*/
        TablaSimbolos tablaHija = new TablaSimbolos(tabla);

        if (codigo != null) {

            for (Nodo nodo : codigo) {

                if (nodo == null) {
                    continue;
                }
                nodo.validarSemantica(tablaHija, listaErrores);
            }

        }


        if (nodoElse != null) {
            nodoElse.validarSemantica(tabla, listaErrores);
        }

        return TipoVariable.VOID;
    }

    //Clase que permite ejecutar por completo el codigo que tiene dentro el condicional
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object result = condicion.ejecutar(tabla, listaErrores);

        if(result instanceof  OnCompilacionError){
            return result;
        }

        double valorCondicion = (result instanceof Number) ? ((Number) result).doubleValue() : 0.0;

        List<Formulario> componentesFinales = new ArrayList<>();

        if (valorCondicion > 0) {

            //Cuerpo de if
            for (Nodo nodo : codigo) {

                Object resultado = nodo.ejecutar(tabla, listaErrores);

                if (resultado instanceof OnCompilacionError) return resultado;

                if (resultado instanceof Formulario) {
                    componentesFinales.add((Formulario) resultado);
                } else if (resultado instanceof List) {
                    componentesFinales.addAll((List<Formulario>) resultado);
                }


            }
        } else if (nodoElse != null) {
            //Cuerpo de else o if
            Object resElse = nodoElse.ejecutar(tabla, listaErrores);

            if (resElse instanceof OnCompilacionError) return resElse;

            if (resElse instanceof Formulario) {
                componentesFinales.add((Formulario) resElse);
            } else if (resElse instanceof List) {
                componentesFinales.addAll((List<Formulario>) resElse);
            }
        }

        return componentesFinales;
    }

    //Pendiente definir
    @Override
    public String getString() {
        return "";
    }

    /*-----Metodo que permite ejecutar los requests hacia la pokeAPI en las preguntas (SEGUNDA PASADA)-----*/
    @Override
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if (this.codigo != null) {
            for (Nodo nodo : this.codigo) {
                nodo.ejecutarRequests(tabla, errores);
            }
        }

        if (this.nodoElse != null) {
            this.nodoElse.ejecutarRequests(tabla, errores);
        }
    }
}
/*Created by Pablo*/
