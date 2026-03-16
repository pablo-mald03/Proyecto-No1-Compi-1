package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al caso contrario absoluto else dentro de el codigo de programacion fuente
/*
 * ES IMPORTANTE DESTACAR QUE EL NodoElseIf no existe
 * Por el simple hecho que en tiempo de compilacion nada mas es una condicion mas como if
 * ES DECIR QUE ES COMO OTRO IF ABAJO PERO COMO OTRA OPCION DEL PROPIO IF PADRE
 *
 *
 * */
/*Created by Pablo*/
public class NodoElse extends Nodo {

    //Atributos
    private List<Nodo> cuerpo;

    public NodoElse(List<Nodo> cuerpo, int linea, int columna) {
        super(linea, columna);
        this.cuerpo = cuerpo;
    }

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        /*Tabla que permite validar variables locales*/
        TablaSimbolos tablaHija = new TablaSimbolos(tabla);

        if (cuerpo != null) {
            for (Nodo nodo : cuerpo) {

                if (nodo == null) {
                    continue;
                }

                nodo.validarSemantica(tablaHija, listaErrores);

            }
        }
        return TipoVariable.VOID;
    }

    /*--MetodoGetter que permite obtener el cuerpo del else para poder ejecutar la primera pasada--*/
    public List<Nodo> getCuerpo(){
        return this.cuerpo;
    }

    //Clase que permite ejecutar por completo el codigo que tiene dentro el caso contrario else
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        //Solo invoca
        for (Nodo nodo : cuerpo) {
            nodo.ejecutar(tabla, listaErrores);
        }

        //PENDIENTE DEFINIR RETORNO
        return null;
    }

    //Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)
    @Override
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if(this.cuerpo != null){
            for (Nodo nodo : this.cuerpo) {
                nodo.ejecutarDraws(tabla, errores);
            }
        }
    }

    /*-----Metodo que permite ejecutar los requests hacia la pokeAPI en las preguntas (SEGUNDA PASADA)-----*/
    @Override
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores) {
        if (this.cuerpo != null) {
            for (Nodo nodo : this.cuerpo) {
                nodo.ejecutarRequests(tabla, errores);
            }
        }
    }

    //Pendiente definir
    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
