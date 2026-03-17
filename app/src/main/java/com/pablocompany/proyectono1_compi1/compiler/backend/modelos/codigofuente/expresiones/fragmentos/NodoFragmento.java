package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;

//Clase padre que permite genera las cadenas con emojis dentro
public abstract class NodoFragmento extends Nodo {

    public NodoFragmento(int linea, int columna) {
        super(linea, columna);
    }


    //Metodo que permite clonar el nodo
    public abstract NodoFragmento clonar() ;

}
/*Created by P*/