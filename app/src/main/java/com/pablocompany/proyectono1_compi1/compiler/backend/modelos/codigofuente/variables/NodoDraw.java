package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la funcion draw de preguntas dentro de una section
public class NodoDraw extends NodoComponente {

    //Atributos
    private String id;

    //Representa a los parametros de la funcion
    private List<Nodo> parametros;

    /*Que la lista no tenga parametros TAMBIEN TIENE UN SIGNIFICADO*/

    public NodoDraw(String id, List<Nodo> parametros, int linea, int columna) {
        super(null,null,null,linea, columna);
        this.parametros = parametros;
        this.id = id;
    }

    //Metodo que permite validar semantica de que solo se puedan agregar comodines a funciones special
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo simbolo = tabla.buscar(id);

        if (simbolo == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La pregunta tipo special \"" + id + "\" no ha sido declarada.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if(simbolo.getTipo() != TipoVariable.SPECIAL){

            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La función \"draw\" solo puede usarse con tipos special. \"" + id + "\" es: " + simbolo.getTipo().toString(),
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        //Se validan los parametros dentro
        if (parametros != null) {
            for (Nodo param : parametros) {
                TipoVariable tipoRetornado = param.validarSemantica(tabla, listaErrores);

                if (tipoRetornado == TipoVariable.COMODIN) {
                    listaErrores.add(new ErrorAnalisis(param.getString(), "Semantico",
                            "La funcion \"draw\" solo puede contener expresiones como parametros . \"" + param.getString() + "\" es: \"comodin\"",
                            param.getLinea(), param.getColumna()));
                }
            }
        }

        return TipoVariable.SPECIAL;
    }

    /*PENDIENTE CREAR LA LOGICA DE VALIDACION DE DATOS*/
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }

    /*Parte de la logica de validacion de datos para la funcion draw*/
    @Override
    protected Estilos procesarEstilos(List<NodoEstilos> lista) {
        return null;
    }
}
/*Created by Pablo*/
