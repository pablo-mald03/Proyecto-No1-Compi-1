package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al ciclo for pero modificado. Tal como el que tiene kotlin de un rango a hacia b
public class NodoForPorRango extends Nodo {

    //Atributos
    private String id;
    private NodoExpresion rangoInicial;
    private NodoExpresion rangoFinal;

    //Codigo dentro del ciclo
    private List<Nodo> codigo;

    public NodoForPorRango( String id, NodoExpresion rangoInicial,NodoExpresion rangoFinal, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.id = id;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.codigo = codigo;
    }

    //Metodo que permite ejecutar el ciclo for estilo kotlin (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que esto es un juego de referencias en el stack de las funciones. ya que media vez muere la funcion
     * en el respectivo stack. La tabla de simbolos muere tambien. (Tecnica utilizada)
     *
     * Lo que me da como resultado que la variable local (A no ser de que este en ambito global) DESAPAREZCA AL MORIR EL STACK DE LA FUNCION
     * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TablaSimbolos tablaFor = new TablaSimbolos(tabla);

        Simbolo simboloExistente = tabla.buscar(id);

        if (simboloExistente != null) {
            if(simboloExistente.getTipo() != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                        "La variable \"" + id + "\" no es de tipo \"number\".",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
            tablaFor.insertar(simboloExistente);
        }else{
            tablaFor.insertar(new Simbolo(id, TipoVariable.NUMBER, 0.0, getLinea(), getColumna()));

        }

        TipoVariable TipoInicio = rangoInicial.validarSemantica(tablaFor, listaErrores);
        TipoVariable TipoFinal = rangoFinal.validarSemantica(tablaFor, listaErrores);

        if (TipoInicio != TipoVariable.NUMBER || TipoFinal != TipoVariable.NUMBER) {
            listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                    "Los valores del rango deben ser de tipo \"number\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        for(Nodo nodo : codigo) {
            nodo.validarSemantica(tablaFor, listaErrores);
        }

        return TipoVariable.VOID;
    }


    //Metodo que permite ejecutar el ciclo for (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que se aplica la misma tecnica que el for clasico
     * (debido a que esto es una ejecucion despues de haber hecho el analisis semantico)
     * La garantia es que SI EL ITERADOR SERA LOCAL. Morira al salir del stack tambien
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        TablaSimbolos tablaFor = new TablaSimbolos(tabla);

        double inicio = ((Number) rangoInicial.ejecutar(tabla, listaErrores)).doubleValue();
        double fin = ((Number) rangoFinal.ejecutar(tabla, listaErrores)).doubleValue();

        tablaFor.insertar(new Simbolo(id, TipoVariable.NUMBER, inicio, getLinea(), getColumna()));

        double i = inicio;

        while (i <= fin) {
            tablaFor.asignar(id, i, listaErrores);
            for(Nodo nodo : codigo) {
                nodo.ejecutar(tablaFor, listaErrores);
            }
            i = ((Number) tablaFor.buscar(id).getValor()).doubleValue() + 1;
        }
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
