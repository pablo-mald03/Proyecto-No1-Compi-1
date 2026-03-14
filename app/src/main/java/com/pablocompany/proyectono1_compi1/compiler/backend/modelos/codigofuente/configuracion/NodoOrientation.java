package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la orientacion de una section o table
public class NodoOrientation extends Nodo {

    //Atributos
    private TipoOrientacion orientacion;

    public NodoOrientation(String expresion, int linea, int columna) {
        super(linea, columna);
        this.orientacion = TipoOrientacion.valueOf(expresion.toUpperCase());
    }

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.orientacion == null) {
            listaErrores.add(new ErrorAnalisis("orientation", "Semántico",
                    "La orientacion es invalida o no ha sido definida correctamente.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return TipoVariable.VOID;
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion (PENDIENTE)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "orientation: " ;
    }
}
