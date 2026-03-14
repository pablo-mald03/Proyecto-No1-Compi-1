package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa las opciones de configuracion de una pregunta
public class NodoOptions extends Nodo {

    //Atributos
    private List<Nodo> opciones;

    public NodoOptions(List<Nodo> opciones, int linea, int columna) {
        super(linea, columna);
        this.opciones = opciones;
    }

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.opciones == null || this.opciones.isEmpty()) {
            listaErrores.add(new ErrorAnalisis("options", "Semántico",
                    "El atributo de opciones no puede estar vacío.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        for (Nodo nodo : this.opciones) {
            if (nodo == null) continue;

            TipoVariable tipoActual = nodo.validarSemantica(tabla, listaErrores);

            if (tipoActual == null || tipoActual == TipoVariable.ERROR || tipoActual == TipoVariable.COMODIN) {
                continue;
            }

            if (tipoActual != TipoVariable.STRING) {
                listaErrores.add(new ErrorAnalisis("options", "Semantico",
                        "Las opciones solo aceptan tipo \"string\". Se encontro con una expresion tipo: \"" +
                                tipoActual + "\"", getLinea(), getColumna()));
            }
        }

        return TipoVariable.STRING;
    }

    //Metodo que permite ejecutar la lista de opciones de expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        //Metodo pendiente de implementar
        for (Nodo objeto: this.opciones) {
            objeto.ejecutar(tabla,listaErrores);
        }

        return null;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {

        if (this.opciones == null || this.opciones.isEmpty()) {
            return "options: {}";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.opciones.size(); i++) {
            builder.append(this.opciones.get(i).getString());

            if (i < this.opciones.size() - 1) {
                builder.append(", ");
            }
        }

        return "options: {" + builder.toString() +"}";
    }
}

