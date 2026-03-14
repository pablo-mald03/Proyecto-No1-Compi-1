package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

/*Created by Pablo*/
//Clase que representa la declaracion de una variable
public class NodoDeclaracion extends Nodo {

    //Atributos
    TipoVariable tipo;
    String id;
    Nodo expresion;

    public NodoDeclaracion(TipoVariable tipo, String id, Nodo expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
        this.id = id;
        this.tipo = tipo;
    }

    //Metodo que permite validar semantica al declarar una variable (Se agrega al contexto)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        //Valores iniciales de la variable (Solicitado)

        Object valorInicial = null;
        if (this.tipo == TipoVariable.NUMBER) {
            valorInicial = 0.0;
        } else if (this.tipo == TipoVariable.STRING) {
            valorInicial = "";
        }

        Simbolo nuevoSimbolo = new Simbolo(id, this.tipo, valorInicial, getLinea(), getColumna());

        boolean insertado = tabla.insertar(nuevoSimbolo);

        if (!insertado) {
            listaErrores.add(new ErrorAnalisis(
                    id,
                    "Semántico",
                    "La variable \"" + id + "\" ya ha sido definida en este ámbito.",
                    getLinea(),
                    getColumna()
            ));
        }

        if (expresion != null) {

            TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

            if (tipoExpresion != TipoVariable.ERROR && tipoExpresion != TipoVariable.COMODIN) {
                if (this.tipo != tipoExpresion) {
                    listaErrores.add(new ErrorAnalisis(
                            id,
                            "Semántico",
                            "Tipo incompatible en la inicialización: se esperaba: " + this.tipo.getTipo() +
                                    " pero se es \"" + tipoExpresion.getTipo() + "\"",
                            getLinea(),
                            getColumna()
                    ));
                }
            }
        }

        return this.tipo != null ? this.tipo : TipoVariable.ERROR;
    }

    //Metodo que permite ejecutar el nodo (Validacion de variables y declaraciones)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object valorResuelto = (expresion != null) ? expresion.ejecutar(tabla, listaErrores) : null;


        if (valorResuelto == null) {

            if (this.tipo == TipoVariable.NUMBER && !(valorResuelto instanceof Double || valorResuelto instanceof Integer)) {
                listaErrores.add(new ErrorAnalisis(id, "Semántico", "Tipo incompatible: inicialización de '" + id + "' requiere un number", super.getLinea(), super.getColumna()));
                valorResuelto = 0.0;
            } else if (this.tipo == TipoVariable.STRING && !(valorResuelto instanceof String)) {
                listaErrores.add(new ErrorAnalisis(id, "Semántico", "Tipo incompatible: inicialización de '" + id + "' requiere un string", super.getLinea(), super.getColumna()));
                valorResuelto = "";
            }
        } else {
            if (this.tipo == TipoVariable.NUMBER) {
                valorResuelto = 0.0;
            } else if (this.tipo == TipoVariable.STRING) {
                valorResuelto = "";
            }
        }

        Simbolo nuevo = new Simbolo(id, tipo, valorResuelto, super.getLinea(), super.getColumna());

        if (!tabla.insertar(nuevo)) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "La variable '" + id + "' ya ha sido definida", super.getLinea(), super.getColumna()));
        }

        return null;
    }

    //Retorna el valor literal escrito
    @Override
    public String getString() {
        return this.tipo.getTipo() + " " + id + " = " + (expresion != null ? expresion.getString() : "null");
    }
}
