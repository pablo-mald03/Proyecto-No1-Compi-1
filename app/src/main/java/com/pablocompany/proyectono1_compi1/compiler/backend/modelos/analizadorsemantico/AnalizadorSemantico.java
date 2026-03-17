package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.analizadorsemantico;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.TiempoEjecucionException;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.NodoDraw;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
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

        this.validarComodinesResueltos();

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        //Metodo que ejecuta los requests a la API
        this.ejecutarRequests(tablaSimbolos);

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        /*Metodo de compilacion del codigo*/
        try {
            return codigoIntermedio(tablaSimbolos);

        } catch (TiempoEjecucionException e) {
            return "";
        }
    }

    /*Metodo que permite retornar el codigo intermedio listo para la ultima fase de compilacion*/
    private String codigoIntermedio(TablaSimbolos tablaSimbolos) throws TiempoEjecucionException {

        System.out.println("llega despues de codigo intermedio");

        List<Formulario> codigoIntermedio = new ArrayList<>();
        for (Nodo nodo : astParser) {
            if (nodo != null) {

                Object resultado = nodo.ejecutar(tablaSimbolos, this.listadoErroresTotal);

                if (resultado instanceof OnCompilacionError) {
                    throw new TiempoEjecucionException("Error en tiempo de ejecucion");
                }

                if (resultado instanceof Formulario) {
                    codigoIntermedio.add((Formulario) resultado);
                } else if (resultado instanceof List) {
                    codigoIntermedio.addAll((List<Formulario>) resultado);
                }
            }
        }

        System.out.println("llega despues de codigo intermedio");

        StringBuilder codigoIntermedioBuilder = new StringBuilder();
        for (Formulario formulario : codigoIntermedio){
            codigoIntermedioBuilder.append(formulario.compilar());
        }

        return codigoIntermedioBuilder.toString();
    }

    //Pendiente
    private void validarComodinesResueltos() {

        for (Nodo nodo : astParser) {

            if (nodo instanceof NodoQuestion) {

                NodoQuestion question = (NodoQuestion) nodo;

                int pendientes = question.contarComodines();

                if (pendientes > 0) {
                    this.listadoErroresTotal.add(
                            new ErrorAnalisis("Comodines","Semantico",
                                    "La pregunta tiene comodines sin sustituir: " + pendientes,
                                    question.getLinea(),
                                    question.getColumna()
                            )
                    );
                }
            }
        }
    }

    /*Metodo utilizado para ejecutar pasadas*/
    private TablaSimbolos ejecutarPasadasAnalisis() {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        for (Nodo nodo : astParser) {
            if (nodo != null) {
                nodo.validarSemantica(tablaSimbolos, this.listadoErroresTotal);
            }
        }

        return tablaSimbolos;
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
