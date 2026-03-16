package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.analizadorsemantico;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.NodoDraw;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase delegada para ocuparse por completo del analisis semantico (PATRON EXPERTO)
public class AnalizadorSemantico {

    //Atributo que representa los errores encontrados en el analisis semantico
    private List<ErrorAnalisis> listadoErroresTotal;

    //Atributo que representa al AST que formo el analizador sintactico
    private List<Nodo> astParser;

    //Constructor que recibe el ast y la lista de errores actuales
    public AnalizadorSemantico(List<ErrorAnalisis> listadoErroresTotal, List<Nodo> astParser) {
        this.listadoErroresTotal = listadoErroresTotal;
        this.astParser = astParser;
    }

    //Metodo que permite retornar el codigo procesado y ya analizado por el analizador semantico
    public String codigoCompilado() {

        if (this.astParser == null) {
            return "";
        }

        TablaSimbolos tablaSimbolos = ejecutarPasadasAnalisis();

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        //Metodo que agrega los comodines en su lugar
        this.agregarComodines(tablaSimbolos);

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        tablaSimbolos = ejecutarPasadasAnalisis();

        //Metodo que ejecuta los requests a la API
        this.ejecutarRequests(tablaSimbolos);

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        tablaSimbolos = ejecutarPasadasAnalisis();

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        /*Metodo de compilacion del codigo*/


        return stringBuilder.toString();
    }

    /*Metodo utilizado para ejecutar pasadas*/
    private TablaSimbolos ejecutarPasadasAnalisis() {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        for (Nodo nodo : astParser) {
            if (nodo != null) {
                nodo.validarSemantica(tablaSimbolos, this.listadoErroresTotal);
            }
        }

        return  tablaSimbolos;
    }

    /*---Metodo delegado para poner los comodines que estaban en la funcion draw----*/
    private void agregarComodines(TablaSimbolos tablaSimbolos) {

        for (Nodo nodo : astParser) {
            if (nodo == null) {
                continue;
            }
            nodo.ejecutarDraws(tablaSimbolos, this.listadoErroresTotal);
        }
    }

    /*Metodo delegado para ejecutar la segunda pasada que es para mandar los requests a la POKEAPI*/
    private void ejecutarRequests(TablaSimbolos tablaSimbolos) {

        for (Nodo nodo : astParser) {
            if (nodo == null) {
                continue;
            }
            nodo.ejecutarRequests(tablaSimbolos, this.listadoErroresTotal);
        }
    }

    //Retorna la lista de errores semanticos
    public List<ErrorAnalisis> getListadoErroresTotal() {
        return listadoErroresTotal;
    }
}
/*Created by Pablo*/
