package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.ValidarDatosForms;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa el ancho de configuracion de una pregunta o layout
public class NodoWidth extends Nodo implements ValidarDatosForms {

    //Atributos
    private NodoExpresion expresion;

    public NodoWidth(NodoExpresion expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica del ancho (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {

        TipoVariable tipoResult = this.validarSemantica(tabla, listaErrores);

        if (esLayout) {
            if (tipoResult == TipoVariable.COMODIN) {
                listaErrores.add(new ErrorAnalisis("width", "Semantico",
                        "La propiedad \"width\" en un layout \"SECTION\", \"TABLE\" o \"TEXT\" no permiten el uso de \"comodines\".",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        return tipoResult;
    }

    //Metodo que permite validar semantica del ancho (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.expresion == null) {
            listaErrores.add(new ErrorAnalisis("width", "Semantico",
                    "El valor del ancho es obligatorio.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        TipoVariable tipoExpresion = expresion.validarSemantica(tabla, listaErrores);

        if (tipoExpresion != TipoVariable.NUMBER &&
                tipoExpresion != TipoVariable.COMODIN &&
                tipoExpresion != TipoVariable.ERROR) {

            listaErrores.add(new ErrorAnalisis("width", "Semantico",
                    "La propiedad \"width\" debe ser \"numerica\". Pero se encontro con una expreion tipo: \"" + tipoExpresion.getTipo()+"\"",
                    getLinea(), getColumna()));

            return TipoVariable.ERROR;
        }

        return tipoExpresion;
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Object resultado = (expresion != null) ? expresion.ejecutar(tabla, listaErrores) : 0.0;

        if (resultado instanceof OnCompilacionError) return resultado;

        if (!(resultado instanceof Number)) {
            OnCompilacionError errorCompilacion = new OnCompilacionError("El valor de width debe ser \"numerico\"", getLinea(), getColumna(), false);
            errorCompilacion.reportar(listaErrores, this.getString());
            return errorCompilacion;
        }
        return resultado;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "width: " + this.expresion.getString();
    }

    /*--Metodo propio de la clase que permite contar los comodines que tienen en el ancho--*/
    public int contarComodines() {
        return this.expresion.contarComodines();
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
}
