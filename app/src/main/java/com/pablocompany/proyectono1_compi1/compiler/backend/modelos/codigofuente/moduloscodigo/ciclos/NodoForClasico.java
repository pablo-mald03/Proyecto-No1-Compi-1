package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

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
    * Es importante destacar que esto es un juego de referencias en el stack de las funciones. ya que media vez muere la funcion
    * en el respectivo stack. La tabla de simbolos muere tambien.
    *
    * Lo que me da como resultado que la variable local (A no ser de que este en ambito global) DESAPAREZCA AL MORIR EL STACK DE LA FUNCION
    * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*Inserta a la tabla de simbolos local*/
        TablaSimbolos tablaFor = new TablaSimbolos(tabla);

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
            tablaFor.insertar(new Simbolo(idInicial, TipoVariable.NUMBER, 0.0, getLinea(), getColumna()));
        }

        TipoVariable tipoAsig = expresionIgualacion.validarSemantica(tablaFor, listaErrores);
        TipoVariable tipoIter = expresionIterador.validarSemantica(tablaFor, listaErrores);

        if (tipoAsig != TipoVariable.NUMBER || tipoIter != TipoVariable.NUMBER) {
            listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                    "La inicializacion y el iterador deben ser de tipo \"number\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        condicion.validarSemantica(tablaFor, listaErrores);
        for (Nodo n : codigo) {
            n.validarSemantica(tablaFor, listaErrores);
        }

        return TipoVariable.VOID;

    }


    //Metodo que permite ejecutar el ciclo for (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que se aplica la misma tecnica (debido a que esto es una ejecucion despues de haber hecho el analisis semantico
     * La garantia es que SI EL ITERADOR SERA LOCAL. Morira al salir del stack tambien
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        //Tabla de simbolos local (variables dentro del for)
        TablaSimbolos tablaFor = new TablaSimbolos(tabla);

        Object valorInicial = expresionIgualacion.ejecutar(tabla, listaErrores);
        tablaFor.insertar(new Simbolo(idInicial, TipoVariable.NUMBER, valorInicial, getLinea(), getColumna()));

        while(true){

            Object condidicionFor = condicion.ejecutar(tablaFor, listaErrores);
            if(!(condidicionFor instanceof  Number) || ((Number) condidicionFor).doubleValue() <= 0.0){
                break;
            }

            for(Nodo nodo : codigo) {
                nodo.ejecutar(tablaFor, listaErrores);
            }

            Object valorIterador = expresionIterador.ejecutar(tablaFor, listaErrores);
            tablaFor.asignar(idIterador, valorIterador, listaErrores);

        }
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
