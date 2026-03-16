package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores.NodoHslColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores.NodoRgbColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoCorrect;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoLabel;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoOptions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales.NodoFuncionPokemon;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstilosComponent;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.PreguntaAbierta;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
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
                    if (tieneFuncionPokemon(this.opciones)) {
                        getFuncionPokemon();
                    }
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

    /*Metodo que permite ejecutar los requests a la API SI HAY FUNCION POKEMON DECLARADA*/
    @Override
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if (this.funcionPokemon != null) {
            Object respuesta = this.funcionPokemon.ejecutar(tabla, errores);

            if (respuesta instanceof List) {

                List<Nodo> listaNodos = (List<Nodo>) respuesta;

                this.opciones = new NodoOptions(listaNodos, getLinea(), getColumna());
                this.funcionPokemon = null;
            }
        }
    }

    /*------Metodo interno que permite determinar si viene la funcion pokemon------*/
    private boolean tieneFuncionPokemon(NodoOptions options) {

        if (options != null && options.getOpciones() != null && options.getOpciones().size() == 1) {
            return options.getOpciones().get(0) instanceof NodoFuncionPokemon;
        }
        return false;
    }

    /*Metodo que permite instanciar la funcion pokemon*/
    private void getFuncionPokemon() {

        if (this.opciones == null || this.opciones.getOpciones().isEmpty()) {
            return;
        }

        Nodo nodo = this.opciones.getOpciones().get(0);

        if (nodo instanceof NodoFuncionPokemon) {
            this.funcionPokemon = nodo;
            this.opciones = null;
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
        if (this.width != null) {
            width.validarSemantica(tabla, listaErrores, false);
        }
        if (this.height != null) {
            height.validarSemantica(tabla, listaErrores, false);
        }
        if (this.estilos != null) {
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

        if (respuestaCorrecta != null) {
            respuestaCorrecta.validarSemantica(tabla, listaErrores);
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


    //Metodo que permite validar si tiene comodines la select question
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

        if (this.respuestaCorrecta != null && this.respuestaCorrecta instanceof NodoCorrect) {

            NodoCorrect expresion = (NodoCorrect) this.respuestaCorrecta;
            contador += expresion.contarComodines();
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

            if (this.funcionPokemon instanceof NodoFuncionPokemon) {
                NodoFuncionPokemon funcionPokemon = (NodoFuncionPokemon) this.funcionPokemon;
                contador += funcionPokemon.contarComodines();
            }
        }

        return contador;
    }

    /*----APARTADO DE METODOS DELEGADOS A LA CLASE (PATRON EXPERTO)----*/

    /*Metodo que permite listar los parametros que se van a inyectar dentro de la pregunta*/
    public void inyectarParametros(List<Nodo> parametros, List<ErrorAnalisis> listaErrores) {

        List<NodoComodin> parametrosPregunta = obtenerParametrosComodines();

        if (parametrosPregunta.isEmpty()) {
            return;
        }

        for (int i = 0; i < parametrosPregunta.size(); i++) {

            NodoComodin comodin = parametrosPregunta.get(i);
            Nodo parametro = parametros.get(i);

            if (parametro instanceof NodoExpresion) {
                NodoExpresion comodinParametro = (NodoExpresion) parametro;
                comodin.darValorIncognita(comodinParametro);
            }
        }
        setearParametros(parametrosPregunta);

    }


    /*---Metodo utilizado para retornar la lista de parametros comodin en la pregunta----*/
    private void setearParametros(List<NodoComodin> parametrosPregunta) {
        int iterador = 0;

        if (this.width != null) {
            NodoComodin comodin = extraerValor(this.width.getExpresion());
            if (comodin != null && comodin.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = this.width.setExpresion(parametrosPregunta, iterador);
            }
        }

        if (this.height != null) {
            NodoComodin comodin = extraerValor(this.height.getExpresion());
            if (comodin != null && comodin.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = this.height.setExpresion(parametrosPregunta, iterador);
            }
        }


        if (this.funcionPokemon != null && this.funcionPokemon instanceof NodoFuncionPokemon) {
            NodoFuncionPokemon fp = (NodoFuncionPokemon) this.funcionPokemon;

            NodoComodin cOffset = extraerValor(fp.getOffset());
            if (cOffset != null && cOffset.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = fp.setOffset(parametrosPregunta, iterador);
            }

            NodoComodin cLimit = extraerValor(fp.getLimit());
            if (cLimit != null && cLimit.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = fp.setLimit(parametrosPregunta, iterador);
            }
        }

        if (this.opciones != null && iterador < parametrosPregunta.size()) {
            iterador = this.opciones.setOpciones(parametrosPregunta, iterador);
        }

        if (this.respuestaCorrecta != null && this.respuestaCorrecta instanceof NodoCorrect) {
            NodoCorrect rc = (NodoCorrect) this.respuestaCorrecta;
            NodoComodin comodin = extraerValor(rc.getExpresion());
            if (comodin != null && comodin.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = rc.setExpresion(parametrosPregunta, iterador);
            }
        }

        if (this.estilos != null) {
            if (this.estilos.getBackgroundColor() != null && iterador < parametrosPregunta.size()) {
                iterador = this.estilos.setBackgroundColor(parametrosPregunta, iterador);
            }
            if (this.estilos.getColor() != null && iterador < parametrosPregunta.size()) {
                iterador = this.estilos.setColor(parametrosPregunta, iterador);
            }
            if (this.estilos.getTextSize() != null) {
                NodoComodin comodin = extraerValor(this.estilos.getTextSize());
                if (comodin != null && comodin.getExpresion() == null && iterador < parametrosPregunta.size()) {
                    iterador = this.estilos.setTextSize(parametrosPregunta, iterador);
                }
            }
        }
    }

    /*---Metodo utilizado para retornar la lista de parametros comodin en la pregunta----*/
    private List<NodoComodin> obtenerParametrosComodines() {

        List<NodoComodin> parametrosPregunta = new ArrayList<>();

        if (this.width != null) {
            NodoComodin comodin = extraerValor(this.width.getExpresion());
            if (comodin != null && comodin.getExpresion() == null) {
                parametrosPregunta.add(comodin);
            }
        }

        if (this.height != null) {
            NodoComodin comodin = extraerValor(this.height.getExpresion());
            if (comodin != null && comodin.getExpresion() == null) {
                parametrosPregunta.add(comodin);
            }
        }


        if (this.funcionPokemon != null && this.funcionPokemon instanceof NodoFuncionPokemon) {
            NodoFuncionPokemon funcionPokemon = (NodoFuncionPokemon) this.funcionPokemon;

            NodoExpresion offset = funcionPokemon.getOffset();
            NodoExpresion limit = funcionPokemon.getLimit();

            NodoComodin comodinOffset = extraerValor(offset);
            NodoComodin comodinLimit = extraerValor(limit);

            if (comodinOffset != null && comodinOffset.getExpresion() == null) {
                parametrosPregunta.add(comodinOffset);
            }

            if (comodinLimit != null && comodinLimit.getExpresion() == null) {
                parametrosPregunta.add(comodinLimit);
            }
        }

        if (this.opciones != null) {
            List<Nodo> parametrosOpciones = this.opciones.getOpciones();

            for (Nodo parametro : parametrosOpciones) {
                NodoComodin comodin = extraerValor(parametro);
                if (comodin != null && comodin.getExpresion() == null) {
                    parametrosPregunta.add(comodin);
                }
            }
        }

        if (this.respuestaCorrecta != null && this.respuestaCorrecta instanceof NodoCorrect) {
            NodoCorrect respuesta = (NodoCorrect) this.respuestaCorrecta;
            NodoExpresion expresion = respuesta.getExpresion();
            NodoComodin comodin = extraerValor(expresion);
            if (comodin != null && comodin.getExpresion() == null) {
                parametrosPregunta.add(comodin);
            }
        }

        if (this.estilos != null) {

            if (this.estilos.getBackgroundColor() != null) {
                NodoColor color = this.estilos.getBackgroundColor();

                List<NodoComodin> comodinesColor = obtenerParametrosComodinesColor(color);

                if (!comodinesColor.isEmpty()) {
                    parametrosPregunta.addAll(comodinesColor);
                }
            }

            if (this.estilos.getColor() != null) {
                NodoColor color = this.estilos.getColor();
                List<NodoComodin> comodinesColor = obtenerParametrosComodinesColor(color);
                if (!comodinesColor.isEmpty()) {
                    parametrosPregunta.addAll(comodinesColor);
                }
            }

            if (this.estilos.getTextSize() != null) {
                NodoExpresion expresion = this.estilos.getTextSize();
                NodoComodin comodin = extraerValor(expresion);
                if (comodin != null && comodin.getExpresion() == null) {
                    parametrosPregunta.add(comodin);
                }
            }
        }
        return parametrosPregunta;
    }

    //Metodo que permite ejecutar laa acciones dentro de la question
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        /*Object contenido = (this.width != null) ? this.label.ejecutar(tabla, listaErrores):null;

        Object width = (this.width != null) ? this.width.ejecutar(tabla, listaErrores) : null;
        Object height = (this.height != null) ? this.height.ejecutar(tabla, listaErrores) : null;

        EstilosComponent estilos = new EstilosComponent();

        if (this.estilos != null) {

            Object textSize = (this.estilos.getTextSize() != null) ? this.estilos.getTextSize().ejecutar(tabla, listaErrores) : null;
            Object letra = (this.estilos.getFontFamily() != null) ? this.estilos.getFontFamily().ejecutar(tabla, listaErrores) : null;

            Object backgroundColor = (this.estilos.getBackgroundColor() != null) ? this.estilos.getBackgroundColor().ejecutar(tabla, listaErrores) : null;
            Object color = (this.estilos.getColor() != null) ? this.estilos.getColor().ejecutar(tabla, listaErrores) : null;


            if (textSize != null) {
                estilos.setTextSize(Double.parseDouble(textSize.toString()));
            }

            if (letra != null) {
                estilos.setFontFamily(TipoLetra.valueOf(letra.toString()));
            }

            if (backgroundColor != null) {
                estilos.setBackgroundColor(backgroundColor.toString());
            }

            if (color != null) {
                estilos.setColor(color.toString());
            }
        }
        Double alto = (height != null) ? Double.parseDouble(height.toString()) : null;
        Double ancho = (width != null) ? Double.parseDouble(width.toString()) : null;

        return new PreguntaAbierta(alto, ancho, ( contenido != null)? contenido.toString():null, estilos, getLinea(), getColumna());*/
        return null;
    }

    @Override
    public String getString() {
        return "SELECT_QUESTION";
    }
}
