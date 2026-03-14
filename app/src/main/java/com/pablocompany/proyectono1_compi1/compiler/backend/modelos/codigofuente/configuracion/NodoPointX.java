package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la configuracion de pointX de configuracion de la section
public class NodoPointX extends Nodo {

    //Atributos
    private NodoExpresion expresion;

    public NodoPointX(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica de la posicion en x (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.expresion == null) {
            listaErrores.add(new ErrorAnalisis("pointX", "Semantico",
                    "El valor de la coordenada \"X\" es obligatorio.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        // Validamos que el resultado sea un número
        if (tipoExpresion != TipoVariable.NUMBER &&
                tipoExpresion != TipoVariable.COMODIN &&
                tipoExpresion != TipoVariable.ERROR) {

            listaErrores.add(new ErrorAnalisis("pointX", "Semantico",
                    "La coordenada \"X\" debe ser numerica. Se encontro con una expresion tipo: \"" + tipoExpresion.getTipo() + "\"",
                    getLinea(), getColumna()));

            return TipoVariable.ERROR;
        }

        return TipoVariable.NUMBER;
    }


    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return expresion.ejecutar(tabla,listaErrores);
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "pointY: " + this.expresion.getString();
    }
}

