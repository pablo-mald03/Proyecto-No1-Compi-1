package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la funcion draw de preguntas dentro de una section
public class NodoDraw extends NodoComponente {

    //Atributos
    private String id;

    //Representa a los parametros de la funcion
    private List<Nodo> parametros;

    /*Que la lista no tenga parametros TAMBIEN TIENE UN SIGNIFICADO*/

    public NodoDraw(String id, List<Nodo> parametros, int linea, int columna) {
        super(null, null, null, linea, columna);
        this.parametros = parametros;
        this.id = id;
    }

    //Metodo que permite validar semantica de que solo se puedan agregar comodines a funciones special
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo simbolo = tabla.buscar(id);

        if (simbolo == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La pregunta tipo special \"" + id + "\" no ha sido declarada.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        if (!(simbolo.getValor() instanceof NodoQuestion)) {

            listaErrores.add(new ErrorAnalisis(id, "Semantico",
                    "La función \"draw\" solo puede usarse con tipos special. \"" + id + "\" es: " + simbolo.getTipo().toString(),
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        //Se validan los parametros dentro
        if (parametros != null) {
            for (Nodo param : parametros) {
                TipoVariable tipoRetornado = param.validarSemantica(tabla, listaErrores);

                if (tipoRetornado == TipoVariable.COMODIN) {
                    listaErrores.add(new ErrorAnalisis(param.getString(), "Semantico",
                            "La funcion \"draw\" solo puede contener expresiones como parametros . \"" + param.getString() + "\" es: \"comodin\"",
                            param.getLinea(), param.getColumna()));
                }
            }
        }

        //PENDIENTE
/*
        NodoQuestion q = (NodoQuestion) s.getValor();

        List<TipoVariable> tiposEsperados = q.obtenerTiposEsperados();
        List<Nodo> params = this.parametros;

        for (int i = 0; i < params.size(); i++) {

            Nodo param = params.get(i);

            TipoVariable tipoParam = param.validarSemantica(tabla, listaErrores);

            if (i < tiposEsperados.size()) {

                TipoVariable esperado = tiposEsperados.get(i);

                if (tipoParam != esperado) {
                    listaErrores.add(new ErrorAnalisis(
                            this.id,
                            "Semantico",
                            "Parametro " + (i+1) + " no coincide. Esperado: "
                                    + esperado + " pero vino: " + tipoParam,
                            getLinea(),
                            getColumna()
                    ));
                }
            }
        }
*/


        return TipoVariable.SPECIAL;
    }

    /*---Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)---*/
    @Override
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {
        this.ejecutar(tabla, errores);
    }

    /*METODO EJECUTADO PPARA INYECTAR LOS PARAMETROS A LOS COMODINES QUE ESTAN DENTRO DE LA FUNCION*/

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo variable = tabla.buscar(id);

        if (variable == null || !(variable.getValor() instanceof NodoQuestion)) {
            /*Caso imposible casi*/
            listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                    "La variable: \"" + id + "\" no ha sido definida aun \".draw()\".",
                    getLinea(), getColumna()));
            return new OnCompilacionError("Draw invalido", getLinea(), getColumna(), true);
        }

        NodoQuestion nodoQuestion = (NodoQuestion) variable.getValor();
        int comodines = nodoQuestion.contarComodines();
        int totalParametros = this.parametros.size();

        if (comodines != totalParametros) {
            String mensaje;

            if (comodines == 0) {
                mensaje = "La pregunta \"" + id + "\" no tiene comodines para sustituir con los valores pasados por parametro.";
            } else if (totalParametros < comodines) {
                mensaje = "Faltan parametros para la pregunta \"" + id + "\". Se esperan " + comodines + " y se pasaron \"" + totalParametros + "\" parametros.";
            } else {
                mensaje = "Demasiados parametros para la pregunta \"" + id + "\". Se esperan " + comodines + " y se pasaron \"" + totalParametros + "\".";
            }

            listaErrores.add(new ErrorAnalisis(this.getString(), "Semántico", mensaje, getLinea(), getColumna()));
            return new OnCompilacionError("Mismatch de parametros", getLinea(), getColumna(), true);
        }

        if (comodines > 0) {
            nodoQuestion.inyectarParametros(this.parametros, listaErrores);
        }

        return null;
    }


    //Metodo que permite obtener el string de la funcion
    @Override
    public String getString() {

        if (this.parametros != null) {
            return this.id + ".draw()";
        }

        return this.id + ".draw(" + this.getStringParametros() + ")";
    }

    /*Metodo auxilar para obtener el string de los prametros de la funcion*/
    private String getStringParametros() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.parametros != null && !this.parametros.isEmpty()) {

            for (int i = 0; i < this.parametros.size(); i++) {
                stringBuilder.append(this.parametros.get(i).getString());

                if (i < this.parametros.size() - 1) {
                    stringBuilder.append(", ");
                }

            }
        }

        return stringBuilder.toString();
    }

    /*Parte de la logica de validacion de datos para la funcion draw*/
    @Override
    protected Estilos procesarEstilos(List<NodoEstilos> lista) {
        return null;
    }
}
/*Created by Pablo*/
