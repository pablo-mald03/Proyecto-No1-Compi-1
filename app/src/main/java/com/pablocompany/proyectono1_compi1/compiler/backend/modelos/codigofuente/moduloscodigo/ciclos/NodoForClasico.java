package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
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


    //Pendiente definir el metodo ejecutar

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        //tabla.insertar(id1, exp1.ejecutar(tabla, errores));
/*
        while (condicion.ejecutar(tabla, errores) > 0) {

            for (Nodo n : cuerpo) {
                n.ejecutar(tabla, errores);
            }

            tabla.asignar(id2, exp2.ejecutar(tabla, errores));
        }*/
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
