package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa el operador unario para poder reconocer negativos en expresiones
public class NodoMenosUnario extends NodoExpresion {
    private NodoExpresion expresion;

    public NodoMenosUnario(NodoExpresion exp, int linea, int columna) {
        super(linea, columna);
        this.expresion = exp;
    }


    //Metodo que permite validar semantica del operador unario (Solo permite a los numericos)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        if (tipoExpresion == TipoVariable.NUMBER || tipoExpresion == TipoVariable.COMODIN) {
            return tipoExpresion;
        }

        if (tipoExpresion != TipoVariable.ERROR) {
            listaErrores.add(new ErrorAnalisis(
                    this.getString(),
                    "Semántico",
                    "No se puede aplicar el operador unario '-' a un tipo: \"" + tipoExpresion.getTipo() + "\"",
                    getLinea(),
                    getColumna()
            ));
        }

        return TipoVariable.ERROR;
    }

    //Metodo que permite retornar el valor del operador UNARIO
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object valor = expresion.ejecutar(tabla, listaErrores);

        if (valor instanceof Integer) {
            return (Integer) valor * -1;
        } else if (valor instanceof Double) {
            return (Double) valor * -1.0;
        }

        OnCompilacionError error = new OnCompilacionError("El operador unario solo se puede aplicar a valores tipo \"number\"", getLinea(), getColumna(), false);
        error.reportar(listaErrores, (this.expresion != null) ? this.expresion.getString() : "UNARIO");
        return error;
    }

    @Override
    public String getString() {
        return "-" + this.expresion.getString();
    }

    @Override
    public int contarComodines() {
        return this.expresion.contarComodines();
    }
}
