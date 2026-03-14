package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

public class NodoCorrect extends Nodo {

    //Atributos
    private NodoExpresion expresion;

    public NodoCorrect(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica de las respuestas correctas (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (expresion == null) {
            listaErrores.add(new ErrorAnalisis("CORRECT", "Semantico",
                    "La respuesta correcta no puede estar vacía.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        if (tipoExpresion != TipoVariable.NUMBER &&
                tipoExpresion != TipoVariable.COMODIN &&
                tipoExpresion != TipoVariable.ERROR) {

            listaErrores.add(new ErrorAnalisis("correct", "Semantico",
                    "La respuesta correcta debe ser un numero. Se encontro con una expreion tipo: \"" + tipoExpresion.getTipo() +"\"",
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
        return "correct: " + this.expresion.getString();
    }
}
