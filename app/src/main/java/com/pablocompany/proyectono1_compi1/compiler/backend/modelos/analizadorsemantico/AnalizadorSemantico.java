package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.analizadorsemantico;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
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
    public String codigoCompilado(){

        StringBuilder stringBuilder = new StringBuilder();
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        if (this.astParser == null) {
            return "";
        }

        for (Nodo nodo : astParser) {

            if (nodo == null) {
                continue;
            }
            nodo.validarSemantica(tablaSimbolos, this.listadoErroresTotal);
        }

        if(!this.listadoErroresTotal.isEmpty()){
            return "";
        }

        //Metodo que agrega los comodines en su lugar
        this.agregarComodines(tablaSimbolos);

        if(!this.listadoErroresTotal.isEmpty()){
            return "";
        }


        return stringBuilder.toString();
    }

    /*---Metodo delegado para poner los comodines que estaban en la funcion draw----*/
    private void agregarComodines(TablaSimbolos tablaSimbolos){

        for (Nodo nodo : astParser) {
            if (nodo == null) {
                continue;
            }
            if(nodo instanceof NodoDraw){
                NodoDraw nodoDraw = (NodoDraw) nodo;
                nodoDraw.ejecutar(tablaSimbolos,this.listadoErroresTotal);
            }
        }
    }


    //Retorna la lista de errores semanticos
    public List<ErrorAnalisis> getListadoErroresTotal() {
        return listadoErroresTotal;
    }
}
/*Created by Pablo*/
