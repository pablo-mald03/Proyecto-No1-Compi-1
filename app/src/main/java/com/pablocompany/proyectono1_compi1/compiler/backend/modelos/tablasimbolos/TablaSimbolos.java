package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos;

import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.HashMap;
import java.util.List;

/*Created by Pablo*/
//Clase que permite manejar las tablas de simbolos (Patron experto)
/*
 * Esta tabla de simbolos es una clase que se maneja a si misma para ubicar todas las variables que hay tanto dentro de
 * un bloque de codigo como por fuera (es decir las variables generales del programa)
 * */
public class TablaSimbolos {

    //Atributos
    private HashMap<String, Simbolo> tabla;

    /*Contiene una propia tabla de simbolos que permite manejar las variables dentro*/
    private TablaSimbolos padre;


    //Constructor de tabla de simbolos generales
    public TablaSimbolos() {
        this.tabla = new HashMap<>();
        this.padre = null;
    }

    //Tabla de simbolos para variables dentro de un bloque de codigo
    public TablaSimbolos(TablaSimbolos padre) {
        this.tabla = new HashMap<>();
        this.padre = padre;
    }


    //Metodo que permite insertar a la tabla de simbolos la variable
    //false -> Si el simbolo ya existe
    //true -> Si el simbolo no existe
    public boolean insertar(Simbolo simbolo) {
        if (tabla.containsKey(simbolo.getId())) {
            return false;
        }
        tabla.put(simbolo.getId(), simbolo);
        return true;
    }

    //Metodo que permite buscar el simbolo en la tabla de simbolos por el ID
    public Simbolo buscar(String id) {

        Simbolo simboloEncontrado = tabla.get(id);

        if (simboloEncontrado == null && this.padre != null) {
            return this.padre.buscar(id);
        }

        return simboloEncontrado;

    }

    //Metodo que permite reasignar un valor a la variable
    public void asignar(String id, Object nuevoValor, List<ErrorAnalisis> errores) {

        if (tabla.containsKey(id)) {
            Simbolo simbolo = tabla.get(id);
            simbolo.setValor(nuevoValor);
            return;
        }
        if (this.padre != null) {
            this.padre.asignar(id, nuevoValor, errores);
        }
        else {
            //Preventivo (imposible)
            errores.add(new ErrorAnalisis("Asignacion", "Semantico",
                    "No se encontro la variable \"" + id + "\" en el programa.",
                    -1, -1));

        }
    }



    /*Created by Pablo*/

}
