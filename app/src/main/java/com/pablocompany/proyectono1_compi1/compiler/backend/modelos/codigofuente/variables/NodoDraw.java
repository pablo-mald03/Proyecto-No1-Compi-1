package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables;

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

        if (simbolo.getTipo() != TipoVariable.SPECIAL) {

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

        if (variable != null) {
            Object pregunta = variable.getValor();

            if (pregunta instanceof NodoQuestion) {

                NodoQuestion nodoQuestion = (NodoQuestion) pregunta;

                int comodines = nodoQuestion.contarComodines();

                if (comodines > this.parametros.size()) {
                    listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                            "La pregunta definida como: \"" + id + "\" tiene mas \"comodines\" que los parametros pasados en \".draw()\".",
                            getLinea(), getColumna()));
                    return null;
                }

                if(comodines == 0 && !this.parametros.isEmpty()){
                    listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                            "La pregunta definida como: \"" + id + "\" no tiene comodines para sustituir con los valores pasados por parametro.",
                            getLinea(), getColumna()));
                    return null;

                }

                if (comodines < this.parametros.size()) {
                    listaErrores.add(new ErrorAnalisis(this.getString(), "Semantico",
                            "La pregunta definida como: \"" + id + "\" tiene mas parametros pasados en \".draw()\" que \"comodines\".",
                            getLinea(), getColumna()));
                    return null;
                }

                nodoQuestion.inyectarParametros(this.parametros,listaErrores);
                return null;
            }
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
