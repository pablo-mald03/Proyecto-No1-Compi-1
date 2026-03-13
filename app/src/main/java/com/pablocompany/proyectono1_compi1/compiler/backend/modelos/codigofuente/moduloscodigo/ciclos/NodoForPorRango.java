package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
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


    //Pendiente definir el metodo ejecutar

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
