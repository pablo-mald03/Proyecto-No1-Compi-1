package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoLabel;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoOptions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales.NodoFuncionPokemon;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa una pregunta de seleccion
public class NodoSelectQuestion extends NodoQuestion {

    //Atributos

    //Representa a la opciones que ofrece
    private NodoOptions opciones;

    //Atributo que permite declarar la funcion de la API
    private Nodo funcionPokemon;

    //Atributo que permite definir la respuesta correcta de la pregunta
    private Nodo respuestaCorrecta;

    /*SI EL ID ES NULO TIENE SIGNIFICADO TAMBIEN*/

    public NodoSelectQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
        super(tipo, id, null, null, null, linea, columna);
        this.setConfiguraciones(config);
    }

    //Metodo que permite setear los valores que vienen en la configuracion
    private void setConfiguraciones(List<AtributoConfig> configuracion) {

        if(configuracion.isEmpty()){
            return;
        }

        for (AtributoConfig config : configuracion) {

            if(config ==null){
                continue;
            }

            switch (config.getTipo()) {

                case WIDTH:
                    this.width = (NodoWidth) config.getNodoValor();
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    break;
                case OPTIONS:
                    this.opciones = (NodoOptions) config.getNodoValor();
                    break;
                case POKEMON:
                    this.funcionPokemon = (NodoFuncionPokemon) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    break;
                case CORRECT:
                    this.respuestaCorrecta = (Nodo) config.getNodoValor();
                    break;
            }

        }

    }

    //Metodo que permite validar semantica de la pregunta tipo select (PATRON EXPERTO)
    /*
    * Comentarios de validacion detallados porque es importante saber en que momento se valida cada cosa
    *
    * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*--Validacion de config---*/
        if(this.width != null){
            width.validarSemantica(tabla,listaErrores,false);
        }
        if(this.height != null){
            height.validarSemantica(tabla,listaErrores,false);
        }
        if(this.estilos != null){
            this.estilos.validarSemantica(tabla, listaErrores, false);
        }

        /*--Validacion de opciones--*/
        if (this.opciones == null && this.funcionPokemon == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico",
                    "La pregunta SELECT_QUESTION debe tener opciones definidas.", getLinea(), getColumna()));
        }

        if (this.opciones != null && this.funcionPokemon != null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico",
                    "La pregunta SELECT_QUESTION debe tener solo la lista de opciones o solo la funcion who_is_that_pokemon.", getLinea(), getColumna()));
        }

        if (this.opciones != null) {
            this.opciones.validarSemantica(tabla, listaErrores);
        }

        /*---Validacion de la respuesta correcta---*/

        if(respuestaCorrecta != null){
            respuestaCorrecta.validarSemantica(tabla,listaErrores);
        }

        /*---Validacion de la funcion para el request a la api de pokemon---*/

        if (this.funcionPokemon != null) {
            this.funcionPokemon.validarSemantica(tabla, listaErrores);
        }

        if (this.id == null) {
            int totalComodines = this.contarComodines();
            if (totalComodines > 0) {
                listaErrores.add(new ErrorAnalisis("SELECT_QUESTION", "Semantico",
                        "La pregunta contiene: " + totalComodines +
                                " comodines. Las preguntas sin estar asignadas a una variable no pueden ser dinamicas.",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        /*---Registrar y validar existencia---*/
        if (id != null) {
            Simbolo simbolo = new Simbolo(id, TipoVariable.SPECIAL, this, getLinea(), getColumna());

            if (!tabla.insertar(simbolo)) {
                listaErrores.add(new ErrorAnalisis(id, "Semántico",
                        "La variable \"" + id + "\" ya ha sido definida.", getLinea(), getColumna()));
            }
        }
        return TipoVariable.SPECIAL;
    }

    //Metodo que permite validar si tiene comodines la question (PENDIENTE)
    //Metodo que permite validar si tiene comodines la multiple question
    @Override
    public int contarComodines() {
        return contarComodinesPregunta();
    }

    /*--Metodo delegado para poder contar los comodines de la pregunta--*/
    private int contarComodinesPregunta() {
        int contador = 0;

        if (this.opciones != null) {
            contador += this.opciones.contarComodines();
        }

        if (this.respuestaCorrecta != null) {
            if (this.respuestaCorrecta instanceof NodoExpresion) {
                NodoExpresion expresion = (NodoExpresion) this.respuestaCorrecta;
                contador += expresion.contarComodines();
            }
        }
        if (this.width != null) {
            contador += this.width.contarComodines();
        }
        if (this.height != null) {
            contador += this.height.contarComodines();
        }

        if (this.estilos != null) {
            contador += this.estilos.contarComodines();
        }

        if (this.funcionPokemon != null) {

            if(this.funcionPokemon instanceof NodoFuncionPokemon){
                NodoFuncionPokemon funcionPokemon = (NodoFuncionPokemon) this.funcionPokemon;
                contador += funcionPokemon.contarComodines();
            }
        }

        return contador;
    }

    /*----APARTADO DE METODOS GETTERS Y SETTERS (PENDIENTE)----*/

    //Metodo que permite ejecutar laa acciones dentro de la question
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "SELECT_QUESTION";
    }
}
