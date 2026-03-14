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

        if (this.tipo == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La variable \"" + id + "\" no tiene un tipo definido.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }


        //Valores iniciales de la variable (Solicitado)
        Object valorInicial = (this.tipo == TipoVariable.NUMBER) ? 0.0 : "";

        Simbolo nuevoSimbolo = new Simbolo(id, this.tipo, valorInicial, getLinea(), getColumna());
        boolean insertado = tabla.insertar(nuevoSimbolo);

        if (!insertado) {
            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La variable \"" + id + "\" ya ha sido definida en el ambito.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }


        if (expresion != null) {
            TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

            if (tipoExpresion == TipoVariable.COMODIN) {
                listaErrores.add(new ErrorAnalisis(id, "Semantico",
                        "No se puede asignar un valor \"comodin\" a una variable \"number\" o \"string\".",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }

            if (tipoExpresion != TipoVariable.ERROR) {
                if (this.tipo != tipoExpresion) {
                    listaErrores.add(new ErrorAnalisis(id, "Semantico",
                            "Tipo incompatible: Se esperaba " +  tipoExpresion + " pero se declaro como \"" + this.tipo+"\"",
                            getLinea(), getColumna()));
                    return TipoVariable.ERROR;
                }
            }
        }

        return this.tipo;
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
