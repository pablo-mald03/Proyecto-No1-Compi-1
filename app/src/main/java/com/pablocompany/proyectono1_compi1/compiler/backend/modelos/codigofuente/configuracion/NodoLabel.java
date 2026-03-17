package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;


import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa el label de configuracion de una pregunta o layout
public class NodoLabel extends Nodo {

    //Atributos
    private NodoExpresion expresion;

    public NodoLabel(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica del label asignado (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        TipoVariable tipoVariable = expresion.validarSemantica(tabla, listaErrores);

        if (tipoVariable != TipoVariable.STRING && tipoVariable != TipoVariable.COMODIN && tipoVariable != TipoVariable.ERROR) {
            listaErrores.add(new ErrorAnalisis("label", "Semantico", "El \"label\" solo debe contener cadenas de texto", getLinea(), getColumna()));
        }

        return tipoVariable;
    }

    /*---Metodo getter para obtener el valor de la expresion dentro de la configuracion--*/
    public NodoExpresion getExpresion() {
        return this.expresion;
    }

    /*---Metodo setter para obtener el valor de la expresion dentro de la configuracion--*/
    public int setExpresion(List<NodoComodin> comodines, int iterador) {

        if (iterador < comodines.size() && this.expresion instanceof NodoComodin) {
            NodoComodin comodin = (NodoComodin) this.expresion;

            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object resultado = (expresion != null) ? expresion.ejecutar(tabla, listaErrores) : "";
        if (resultado instanceof OnCompilacionError) return resultado;

        if (!(resultado instanceof String)) {

            OnCompilacionError errorCompilacion = new OnCompilacionError(
                    "El valor del label debe ser un texto \"string\"",
                    getLinea(), getColumna(), false
            );
            errorCompilacion.reportar(listaErrores, this.getString());
            return errorCompilacion;
        }

        return resultado;
    }

    //Metodo que permite clonar a la instancia de label
    public NodoLabel clonar() {
        return new NodoLabel(this.expresion.clonar(), getLinea(), getColumna());
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "label: " + this.expresion.getString();
    }

    /*---Metodo propio de la clase que permite contar los comodines que tienen en el label---*/
    public int contarComodines() {
        return this.expresion.contarComodines();
    }
}
