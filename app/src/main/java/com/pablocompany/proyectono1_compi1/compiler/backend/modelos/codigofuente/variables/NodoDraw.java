package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
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

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
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
