package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

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
public class NodoIf extends Nodo{

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

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Clase que permite ejecutar por completo el codigo que tiene dentro el condicional
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object result = condicion.ejecutar(tabla, listaErrores);

        double valorCondicion = (result instanceof Number) ? ((Number) result).doubleValue() : 0.0;

        //Pendiente definir la forma de condicionar
        if (valorCondicion > 0) {

            //Cuerpo de if
            for (Nodo nodo : codigo) {
                nodo.ejecutar(tabla, listaErrores);
            }
        }
        else if(nodoElse != null){
            //Cuerpo de else o if
            nodoElse.ejecutar(tabla, listaErrores);
        }

        //PENDIENTE DEFINIR RETORNO
        return null;
    }

    //Pendiente definir
    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
