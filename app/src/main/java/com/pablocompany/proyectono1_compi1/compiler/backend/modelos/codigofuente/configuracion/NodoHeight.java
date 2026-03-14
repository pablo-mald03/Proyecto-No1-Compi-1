package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa el alto de configuracion de una pregunta o layout
public class NodoHeight extends Nodo {

    //Atributos
    private NodoExpresion expresion;

    public NodoHeight(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica de la altura (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.expresion == null) {
            listaErrores.add(new ErrorAnalisis("height", "Semantico",
                    "El valor de la altura es obligatorio.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        if (tipoExpresion != TipoVariable.NUMBER &&
                tipoExpresion != TipoVariable.COMODIN &&
                tipoExpresion != TipoVariable.ERROR) {

            listaErrores.add(new ErrorAnalisis("height", "Semantico",
                    "La propiedad \"height\" debe ser numerica. Pero se encontro con una expreion tipo: \"" + tipoExpresion.getTipo()+"\"",
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
        return "height: " + this.expresion.getString();
    }
}

