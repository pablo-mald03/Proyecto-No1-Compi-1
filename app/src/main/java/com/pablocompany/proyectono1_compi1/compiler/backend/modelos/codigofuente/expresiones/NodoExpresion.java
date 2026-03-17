package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

public abstract class NodoExpresion extends Nodo {

    //Constructor de la clase padre
    public NodoExpresion(int linea, int columna) {
        super(linea, columna);
    }

    //Metodo que permite validar si tiene comodines
    public abstract int contarComodines();

    public abstract void buscarComodines(List<NodoComodin> listaComodines);
}
