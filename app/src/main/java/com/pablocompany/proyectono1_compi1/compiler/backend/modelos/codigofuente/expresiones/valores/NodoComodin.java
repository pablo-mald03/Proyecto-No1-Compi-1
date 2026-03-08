package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que permite representar un comodin en el lenguaje que es sustituible por otro valor
public class NodoComodin extends NodoExpresion {

    //Constructor
    public NodoComodin(int linea, int columna) {
        super(linea, columna);
    }

    //Metodo que permite ejecutar el valor que este adopta
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Retorna su valor string (comodin)
    @Override
    public String getString() {
        return "?";
    }

    //Retorna uno porque si es un comodin
    @Override
    public int contarComodines() {
        return 1;
    }


}
