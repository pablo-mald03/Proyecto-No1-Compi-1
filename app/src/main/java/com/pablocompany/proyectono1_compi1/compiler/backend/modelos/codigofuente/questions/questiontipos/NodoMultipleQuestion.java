package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoLabel;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoOptions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales.NodoFuncionPokemon;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que resperesenta a un a multiple question que permite definir la estructura
public class NodoMultipleQuestion extends NodoQuestion {

    //Atributos

    //Representa a la opciones que ofrece
    private NodoOptions opciones;

    //Atributo que permite declarar la funcion de la API
    private Nodo funcionPokemon;

    //Atributo que permite definir la respuesta correcta de la pregunta
    private List<Nodo> respuestasCorrectas;

    /*SI EL ID ES NULO TIENE SIGNIFICADO TAMBIEN*/

    public NodoMultipleQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
        super(tipo, id, null, null, null, linea, columna);
        this.funcionPokemon = null;
        this.respuestasCorrectas = null;
        this.setConfiguraciones(config);
    }

    //Metodo que permite validar semantica de la pregunta tipo multiple (PATRON EXPERTO)
    /*
     * Comentarios de validacion detallados porque es importante saber en que momento se valida cada cosa
     *
     * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*--Validacion de config---*/
        if (this.width != null) {
            width.validarSemantica(tabla, listaErrores,false);
        }
        if (this.height != null) {
            height.validarSemantica(tabla, listaErrores,false);
        }
        if(this.estilos != null){
            this.estilos.validarSemantica(tabla, listaErrores, false);
        }

        /*--Validacion de opciones--*/
        if (this.opciones == null && this.funcionPokemon == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico",
                    "La pregunta MULTIPLE_QUESTION debe tener opciones definidas.", getLinea(), getColumna()));
        }

        if (this.opciones != null && this.funcionPokemon != null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico",
                    "La pregunta MULTIPLE_QUESTION debe tener solo la lista de opciones o solo la funcion who_is_that_pokemon.", getLinea(), getColumna()));
        }

        if (this.opciones != null) {
            this.opciones.validarSemantica(tabla, listaErrores);
        }

        /*---Validacion de las respuestas correctas---*/

        if (this.respuestasCorrectas != null) {

            for (Nodo nodo : this.respuestasCorrectas) {
                nodo.validarSemantica(tabla, listaErrores);
            }
        }

        /*---Validacion de la funcion para el request a la api de pokemon---*/

        if (this.funcionPokemon != null) {
            this.funcionPokemon.validarSemantica(tabla, listaErrores);
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

    //Metodo que permite setear los valores que vienen en la configuracion
    private void setConfiguraciones(List<AtributoConfig> configuracion) {

        if (configuracion.isEmpty()) {
            return;
        }

        for (AtributoConfig config : configuracion) {

            if (config == null) {
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
                    this.respuestasCorrectas = (List<Nodo>) config.getNodoValor();
                    break;
            }
        }
    }

    //Metodo que permite validar si tiene comodines la question (PENDIENTE)
    @Override
    public int contarComodines(){
        return 0;
    }

    /*----APARTADO DE METODOS GETTERS Y SETTERS (PENDIENTE)----*/

    //Metodo que permite ejecutar laa acciones dentro de la question
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
