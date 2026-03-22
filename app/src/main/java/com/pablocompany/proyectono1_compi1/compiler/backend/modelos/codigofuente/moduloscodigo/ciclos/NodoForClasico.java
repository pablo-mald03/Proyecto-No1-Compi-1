package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa el ciclo for pero de la manera estandar en la que se encuentra en el lenguaje de programacion
public class NodoForClasico extends Nodo {

    //Atributos
    private String idInicial;
    private String idIterador;
    private NodoExpresion expresionIgualacion;
    private NodoExpresion expresionIterador;
    private Nodo condicion;

    private List<Nodo> codigo;

    public NodoForClasico( String idInicial, NodoExpresion expresionIgualacion, Nodo condicion,String idIterador, NodoExpresion expresionIterador, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.idInicial = idInicial;
        this.idIterador = idIterador;
        this.expresionIgualacion = expresionIgualacion;
        this.expresionIterador = expresionIterador;
        this.condicion = condicion;
        this.codigo = codigo;
    }

    //Metodo que permite validar semantica del ciclo for (PATRON EXPERTO)
    /*
    *
    * Es importante destacar que esto lo unico que hace es verificar si el iterador existe fuera del ciclo
    *
    * Lo que me da como resultado que la variable local (A no ser de que este en ambito global) QUE PERMANECE Y SERA SOBREESCRITA SIEMPRE QUE SE LLAME
    * PERO ESTO SOLO PASA CON LA CONDICION DEL FOR.
    * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*Tabla que permite validar variables*/
        Simbolo simboloExistente = tabla.buscar(idInicial);

        if (simboloExistente != null) {
            if (simboloExistente.getTipo() != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                        "La variable \"" + idInicial + "\" ya existe y no es de tipo \"number\".",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        } else {
            tabla.insertar(new Simbolo(idInicial, TipoVariable.NUMBER, 0.0, getLinea(), getColumna()));
        }

        TipoVariable tipoAsig = expresionIgualacion.validarSemantica(tabla, listaErrores);
        TipoVariable tipoIter = expresionIterador.validarSemantica(tabla, listaErrores);

        if (tipoAsig != TipoVariable.NUMBER || tipoIter != TipoVariable.NUMBER) {
            listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                    "La inicializacion y el iterador deben ser de tipo \"number\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        condicion.validarSemantica(tabla, listaErrores);
        for (Nodo nodo : codigo) {
            if (nodo == null) {
                continue;
            }

            nodo.validarSemantica(tabla, listaErrores);
        }

        return TipoVariable.VOID;

    }


    //Metodo que permite ejecutar el ciclo for (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que se aplica la misma tecnica (debido a que esto es una ejecucion despues de haber hecho el analisis semantico)
     * La garantia es que el iterador ya existe o ya fue declarado anteriormente en la tabla de simbolos ya solo se utiliza y queda
     * para siempre en el ambito general (no se manejan ambitos internos)
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valorInicial = expresionIgualacion.ejecutar(tabla, listaErrores);

        if (valorInicial instanceof OnCompilacionError) return valorInicial;

        tabla.asignar(idInicial, valorInicial,listaErrores);

        List<Formulario> componentesAcumulados = new ArrayList<>();

        while(true){

            Object condicionFor = condicion.ejecutar(tabla, listaErrores);

            if (condicionFor instanceof OnCompilacionError) return condicionFor;

            if(!(condicionFor instanceof  Number) || ((Number) condicionFor).doubleValue() <= 0.0){
                break;
            }

            for(Nodo nodo : codigo) {
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

            Object valorIterador = expresionIterador.ejecutar(tabla, listaErrores);

            if (valorIterador instanceof OnCompilacionError) {
                return valorIterador;
            }

            tabla.asignar(idIterador, valorIterador, listaErrores);

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
