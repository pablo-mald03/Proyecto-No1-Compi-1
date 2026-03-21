package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
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
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.PreguntaMultiple;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
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

    private NodoLabel label;

    //Atributos de validacion
    private int countWidth = 0;
    private int countHeight = 0;
    private int countLabel = 0;
    private int countStyles = 0;
    private int countOptions = 0;
    private int countCorrect = 0;
    private int countPokemon = 0;

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

        /*--Validacion de duplicidad---*/
        validarObligatorio(this.countLabel,"MULTIPLE_QUESTION", "label", listaErrores);

        if(this.countOptions == 0){
            validarObligatorio(this.countPokemon,"MULTIPLE_QUESTION", "who_is_that_pokemon()", listaErrores);
        }else{
            validarObligatorio(this.countOptions,"MULTIPLE_QUESTION", "options", listaErrores);
        }

        validarDuplicado(this.countWidth,"MULTIPLE_QUESTION", "width", listaErrores);
        validarDuplicado(this.countCorrect,"MULTIPLE_QUESTION", "correct", listaErrores);
        validarDuplicado(this.countPokemon,"MULTIPLE_QUESTION", "who_is_that_pokemon()", listaErrores);
        validarDuplicado(this.countOptions,"MULTIPLE_QUESTION", "options", listaErrores);
        validarDuplicado(this.countHeight,"MULTIPLE_QUESTION", "height", listaErrores);
        validarDuplicado(this.countLabel,"MULTIPLE_QUESTION", "label", listaErrores);
        validarDuplicado(this.countStyles,"MULTIPLE_QUESTION", "styles", listaErrores);


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

        if (this.opciones != null && this.respuestasCorrectas != null) {

            int cantidadOpciones = this.opciones.getListaOpciones().size();
            int cantidadRespuestas = this.respuestasCorrectas.size();

            if (cantidadRespuestas > cantidadOpciones) {

                int lineaError = (cantidadRespuestas > 0)
                        ? this.respuestasCorrectas.get(cantidadRespuestas - 1).getLinea()
                        : getLinea();

                listaErrores.add(new ErrorAnalisis(
                        (id != null) ? id : "MULTIPLE_QUESTION",
                        "Semántico",
                        "La cantidad de respuestas correctas es: { " + cantidadRespuestas +
                                " }. No puede ser mayor a la cantidad de opciones definidas: { " + cantidadOpciones + " }.",
                        lineaError,
                        this.respuestasCorrectas.get(0).getColumna()

                ));
            }
        }


        /*---Validacion de la funcion para el request a la api de pokemon---*/

        if (this.funcionPokemon != null) {
            this.funcionPokemon.validarSemantica(tabla, listaErrores);
        }

        if(this.label != null){
            this.label.validarSemantica(tabla,listaErrores);
        }

        if (this.id == null) {
            int totalComodines = this.contarComodines();
            if (totalComodines > 0) {
                listaErrores.add(new ErrorAnalisis("MULTIPLE_QUESTION", "Semantico",
                        "La pregunta contiene: " + totalComodines +
                                " comodines. Las preguntas sin estar asignadas a una variable no pueden ser dinamicas.",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        /*---Registrar y validar existencia---*/
        if (id != null) {
            Simbolo existente = tabla.buscar(id);
            if (existente == null) {
                Simbolo simbolo = new Simbolo(id, TipoVariable.SPECIAL, this, getLinea(), getColumna());
                if (!tabla.insertar(simbolo)) {
                    listaErrores.add(new ErrorAnalisis(id, "Semantico",
                            "Error al definir la variable \"" + id + "\".", getLinea(), getColumna()));
                }
            } else {
                if (existente.getValor() != this) {
                    listaErrores.add(new ErrorAnalisis(id, "Semantico",
                            "La variable \"" + id + "\" ya ha sido definida en este ambito.", getLinea(), getColumna()));
                }
            }
        }
        return TipoVariable.SPECIAL;
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
                    this.countWidth++;
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    this.countHeight++;
                    break;
                case LABEL:
                    this.label = (NodoLabel) config.getNodoValor();
                    this.countLabel++;
                    break;
                case OPTIONS:
                    this.opciones = (NodoOptions) config.getNodoValor();
                    if (tieneFuncionPokemon(this.opciones)) {
                        getFuncionPokemon();
                    } else {
                        this.countOptions++;
                    }
                    break;
                case POKEMON:
                    this.funcionPokemon = (NodoFuncionPokemon) config.getNodoValor();
                    this.countPokemon++;
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    this.countStyles++;
                    break;
                case CORRECT:
                    this.respuestasCorrectas = (List<Nodo>) config.getNodoValor();
                    this.countCorrect++;
                    break;
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
            this.countPokemon++;
        } else {
            this.countOptions++;
        }
    }

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

        if (this.respuestasCorrectas != null) {

            for (Nodo nodo : this.respuestasCorrectas) {
                if (nodo instanceof NodoExpresion) {
                    NodoExpresion expresion = (NodoExpresion) nodo;
                    contador += expresion.contarComodines();
                }
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

        List<NodoComodin> comodinesEncontrados = obtenerParametrosComodines();

        if (comodinesEncontrados.isEmpty()) {
            return;
        }

        for (int i = 0; i < comodinesEncontrados.size() && i < parametros.size(); i++) {

            NodoComodin comodinReal = comodinesEncontrados.get(i);
            Nodo valorEntrante = parametros.get(i);

            if (valorEntrante instanceof NodoExpresion) {
                comodinReal.darValorIncognita((NodoExpresion) valorEntrante);
            }
        }
    }


    /*---Metodo utilizado para retornar la lista de parametros comodin en la pregunta----*/
    private List<NodoComodin> obtenerParametrosComodines() {

        List<NodoComodin> parametrosPregunta = new ArrayList<>();

        if (this.width != null && this.width.getExpresion() != null) {
            this.width.getExpresion().buscarComodines(parametrosPregunta);
        }

        if (this.height != null && this.height.getExpresion() != null) {
            this.height.getExpresion().buscarComodines(parametrosPregunta);
        }

        if (this.label != null && this.label.getExpresion() != null) {
            this.label.getExpresion().buscarComodines(parametrosPregunta);
        }

        if (this.funcionPokemon != null) {
            this.funcionPokemon.buscarComodines(parametrosPregunta);
        }

        if (this.opciones != null) {
            this.opciones.buscarComodines(parametrosPregunta);
        }

        if (this.respuestasCorrectas != null) {
            for (Nodo nodo : this.respuestasCorrectas) {
                if (nodo != null) {
                    nodo.buscarComodines(parametrosPregunta);
                }
            }
        }

        if (this.estilos != null) {
            recolectarComodinesColor(this.estilos.getBackgroundColor(), parametrosPregunta);
            recolectarComodinesColor(this.estilos.getColor(), parametrosPregunta);

            if (this.estilos.getTextSize() != null) {
                this.estilos.getTextSize().buscarComodines(parametrosPregunta);
            }
        }

        return parametrosPregunta;
    }


    //Metodo que permite ejecutar laa acciones dentro de la question
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object widthResultado = (this.width != null) ? this.width.ejecutar(tabla, listaErrores) : null;

        if (widthResultado instanceof OnCompilacionError) return widthResultado;

        Object heightResultado = (this.height != null) ? this.height.ejecutar(tabla, listaErrores) : null;

        if (heightResultado instanceof OnCompilacionError) return heightResultado;

        if (this.funcionPokemon != null) {
            Object resultadoPokemon = this.funcionPokemon.ejecutar(tabla, listaErrores);
            if (resultadoPokemon instanceof OnCompilacionError) return resultadoPokemon;

            if (resultadoPokemon instanceof List) {
                this.opciones = new NodoOptions((List<Nodo>) resultadoPokemon, getLinea(), getColumna());
            }
        }

        Object opcionesResultado = (this.opciones != null) ? this.opciones.ejecutar(tabla, listaErrores) : new ArrayList<String>();

        if (opcionesResultado instanceof OnCompilacionError) return opcionesResultado;

        Object labelResultado = (this.label != null) ? this.label.ejecutar(tabla, listaErrores) : null;

        if (labelResultado instanceof OnCompilacionError) return labelResultado;

        if (labelResultado == null) {
            return this.reporteError("MULTIPLE_QUESTION", "El atributo \"label\" de la \"MULTIPLE_QUESTION\" es obligatorio.", listaErrores);
        }

        if (!(labelResultado instanceof String)) {
            return this.reporteError("MULTIPLE_QUESTION", "El \"label\" de la \"MULTIPLE_QUESTION\" debe ser una cadena de texto.", listaErrores);
        }

        List<String> listaOpciones = (List<String>) opcionesResultado;

        Object indicesCalculados = this.obtenerListaindices(tabla, listaErrores, listaOpciones);

        if (indicesCalculados instanceof OnCompilacionError) return indicesCalculados;


        List<Integer> indicesCorrectos = new ArrayList<>();

        if (indicesCalculados instanceof List) {
            indicesCorrectos = (List<Integer>) indicesCalculados;

            if (indicesCorrectos.isEmpty()) {
                indicesCorrectos = null;
            }
        }


        EstilosComponent estilosObjeto = new EstilosComponent();

        if (this.estilos != null) {

            Object textSize = (this.estilos.getTextSize() != null) ? this.estilos.getTextSize().ejecutar(tabla, listaErrores) : null;

            if (textSize instanceof OnCompilacionError) return textSize;

            Object letra = (this.estilos.getFontFamily() != null) ? this.estilos.getFontFamily().ejecutar(tabla, listaErrores) : null;

            if (letra instanceof OnCompilacionError) return letra;

            Object backgroundColor = (this.estilos.getBackgroundColor() != null) ? this.estilos.getBackgroundColor().ejecutar(tabla, listaErrores) : null;

            if (backgroundColor instanceof OnCompilacionError) return backgroundColor;

            Object color = (this.estilos.getColor() != null) ? this.estilos.getColor().ejecutar(tabla, listaErrores) : null;

            if (color instanceof OnCompilacionError) return color;

            if (textSize instanceof Number) {
                estilosObjeto.setTextSize((Number) textSize);
            }

            if (letra != null) {
                estilosObjeto.setFontFamily(TipoLetra.valueOf(letra.toString()));
            }

            if (backgroundColor != null) {
                estilosObjeto.setBackgroundColor(backgroundColor.toString());
            }

            if (color != null) {
                estilosObjeto.setColor(color.toString());
            }
        }

        Number alto = (heightResultado instanceof Number) ? (Number) heightResultado : null;
        Number ancho = (widthResultado instanceof Number) ? (Number) widthResultado : null;

        if (ancho != null && ancho.doubleValue() < 0) {
            return this.reporteError("MULTIPLE_QUESTION", "El \"width\" de la \"MULTIPLE_QUESTION\" no puede ser negativo.", listaErrores);
        }

        if (alto != null && alto.doubleValue() < 0) {
            return this.reporteError("MULTIPLE_QUESTION", "El \"height\" de la \"MULTIPLE_QUESTION\" no puede ser negativo.", listaErrores);
        }


        return new PreguntaMultiple(alto, ancho, ( labelResultado != null)? labelResultado.toString():null, listaOpciones, indicesCorrectos, estilosObjeto, getLinea(), getColumna());
    }

    /*Metodo auxiliar que permite retornar el listado de indices correctos*/
    private Object obtenerListaindices(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, List<String> listaOpciones) {

        List<Integer> indicesCorrectos = new ArrayList<>();

        if (this.respuestasCorrectas != null) {
            for (Nodo nodo : this.respuestasCorrectas) {
                Object resultadoValor = nodo.ejecutar(tabla, listaErrores);

                if (resultadoValor instanceof OnCompilacionError) return resultadoValor;

                Integer indiceActual = -1;

                if (resultadoValor instanceof Integer || resultadoValor instanceof Long) {
                    indiceActual = ((Number) resultadoValor).intValue();
                } else if (resultadoValor instanceof Double) {
                    double val = (Double) resultadoValor;
                    if (val == (int) val) {
                        indiceActual = (int) val;
                    } else {
                        return reportarError(listaErrores, "El indice de \"MULTIPLE_QUESTION\" debe ser entero sin decimales", getLinea(), getColumna());
                    }
                } else {
                    return reportarError(listaErrores, "El indice de \"MULTIPLE_QUESTION\" debe ser un valor numerico", getLinea(), getColumna());
                }

                if (!listaOpciones.isEmpty()) {

                    if (indiceActual < 0 || indiceActual >= listaOpciones.size()) {
                        return reportarError(listaErrores, "Índice de respuesta (" + indiceActual + ") fuera de rango", getLinea(), getColumna());
                    }
                }

                indicesCorrectos.add(indiceActual);
            }
        }

        return indicesCorrectos;
    }

    /*Metodo propio que permite clonar a una intancia de clase nodo multiple question*/
    @Override
    public NodoQuestion clonar() {

        NodoMultipleQuestion clon = new NodoMultipleQuestion(this.tipoVariable, this.id, new ArrayList<>(), getLinea(), getColumna());

        clon.width = (this.width != null) ? this.width.clonar() : null;
        clon.height = (this.height != null) ? this.height.clonar() : null;
        clon.estilos = (this.estilos != null) ? this.estilos.clonar() : null;
        clon.opciones = (this.opciones != null) ? this.opciones.clonar() : null;
        clon.label = (this.label != null) ? this.label.clonar() : null;

        clon.countWidth = this.countWidth;
        clon.countHeight = this.countHeight;
        clon.countLabel = this.countLabel;
        clon.countStyles = this.countStyles;
        clon.countOptions = this.countOptions;
        clon.countCorrect = this.countCorrect;
        clon.countPokemon = this.countPokemon;

        if (this.funcionPokemon instanceof NodoFuncionPokemon) {
            clon.funcionPokemon = ((NodoFuncionPokemon) this.funcionPokemon).clonar();
        } else {
            clon.funcionPokemon = this.funcionPokemon;
        }

        if (this.respuestasCorrectas != null) {
            clon.respuestasCorrectas = new ArrayList<>();
            for (Nodo response : this.respuestasCorrectas) {
                if (response == null) continue;

                if (response instanceof NodoExpresion) {
                    clon.respuestasCorrectas.add(((NodoExpresion) response).clonar());
                } else if (response instanceof NodoCorrect) {
                    NodoExpresion expClonada = ((NodoCorrect) response).getExpresion().clonar();
                    clon.respuestasCorrectas.add(new NodoCorrect(expClonada, response.getLinea(), response.getColumna()));
                }
            }
        }

        return clon;
    }


    /*--Metodo utilizado para Reportar error--*/
    private OnCompilacionError reportarError(List<ErrorAnalisis> listaErrores, String mensaje, int linea, int columna) {
        listaErrores.add(new ErrorAnalisis("MULTIPLE_QUESTION", "Semantico",
                mensaje, linea, columna));
        return new OnCompilacionError("Error tiempo de compilacion", getLinea(), getColumna(), true);
    }


    @Override
    public String getString() {
        return "";
    }
}
