package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoLabel;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstilosComponent;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.PreguntaAbierta;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa a una pregunta abierta dentro del contexo del lenguaje de programacion
public class NodoOpenQuestion extends NodoQuestion {

    //Atributos
    private NodoLabel label;

    //Atributos de validacion
    private int countWidth = 0;
    private int countHeight = 0;
    private int countLabel = 0;
    private int countStyles = 0;

    /*SI EL ID ES NULO TIENE SIGNIFICADO TAMBIEN*/

    public NodoOpenQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
        super(tipo, id, null, null, null, linea, columna);
        this.setConfiguraciones(config);
        this.ejecucion = false;
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
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    this.countStyles++;
                    break;
            }

        }

    }

    //Metodo que permite validar semantica de la pregunta tipo Open (PATRON EXPERTO)
    /*
     * Comentarios de validacion detallados porque es importante saber en que momento se valida cada cosa
     *
     * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*--Validacion de duplicidad---*/

        validarObligatorio(this.countLabel,"OPEN_QUESTION", "label", listaErrores);

        validarDuplicado(this.countWidth,"OPEN_QUESTION", "width", listaErrores);
        validarDuplicado(this.countHeight,"OPEN_QUESTION", "height", listaErrores);
        validarDuplicado(this.countLabel,"OPEN_QUESTION", "label", listaErrores);
        validarDuplicado(this.countStyles,"OPEN_QUESTION", "styles", listaErrores);

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

        if(this.label != null){
            this.label.validarSemantica(tabla,listaErrores);
        }

        /*--Validacion de comodines--*/
        if (this.id == null) {
            int totalComodines = this.contarComodines();
            if (totalComodines > 0) {
                listaErrores.add(new ErrorAnalisis("OPEN_QUESTION", "Semantico",
                        "La pregunta contiene: " + totalComodines +
                                " comodines. Las preguntas sin estar asignadas a una variable no pueden ser dinamicas.",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        if (id != null && !this.ejecucion) {
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

    /*Metodo que permite generar el set para el tiempo de ejecucion y validacion de existencia*/
    public void setEjecucion(boolean ejecucion) {
        this.ejecucion = ejecucion;
    }

    //Metodo que permite validar si tiene comodines la open question
    @Override
    public int contarComodines() {
        return contarComodinesPregunta();
    }

    /*--Metodo delegado para poder contar los comodines de la pregunta--*/
    private int contarComodinesPregunta() {
        int contador = 0;

        if (this.label != null) {
            contador += this.label.contarComodines();
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

        if (this.width != null && this.width.getExpresion() != null ) {
            this.width.getExpresion().buscarComodines(parametrosPregunta);
        }

        if (this.height != null && this.height.getExpresion() != null) {
            this.height.getExpresion().buscarComodines(parametrosPregunta);
        }

        if (this.label != null && this.label.getExpresion() != null) {
            this.label.getExpresion().buscarComodines(parametrosPregunta);
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

    //Metodo que permite ejecutar las acciones que tenga la pregunta
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object labelTexto = this.label.ejecutar(tabla, listaErrores);

        if (labelTexto instanceof OnCompilacionError) return labelTexto;

        if (labelTexto == null) {
            return this.reporteError("OPEN_QUESTION", "El atributo \"label\" es obligatorio.", listaErrores);
        }

        if ( !(labelTexto instanceof String)) {
            return this.reporteError("OPEN_QUESTION","El \"label\" de la \"OPEN_QUESTION\" debe ser una cadena de texto.",listaErrores);
        }

        Object widthResultado = (this.width != null) ? this.width.ejecutar(tabla, listaErrores) : null;

        if (widthResultado instanceof OnCompilacionError) return widthResultado;

        Object heightResultado = (this.height != null) ? this.height.ejecutar(tabla, listaErrores) : null;

        if (heightResultado instanceof OnCompilacionError) return heightResultado;


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
            return this.reporteError("OPEN_QUESTION","El \"width\" de la \"OPEN_QUESTION\" no puede ser negativo.",listaErrores);
        }

        if (alto != null && alto.doubleValue() < 0) {
            return this.reporteError("OPEN_QUESTION","El \"height\" de la \"OPEN_QUESTION\" no puede ser negativo.",listaErrores);
        }

        return new PreguntaAbierta(alto, ancho, ( labelTexto != null)? labelTexto.toString():null, estilosObjeto, getLinea(), getColumna());
    }



    /*Metodo propio que permite clonar a una intancia de clase nodo open question*/
    @Override
    public  NodoQuestion clonar(){
        NodoOpenQuestion clon = new NodoOpenQuestion(this.tipoVariable, this.id, new ArrayList<>(), getLinea(), getColumna());

        clon.setEjecucion(true);
        clon.label = (this.label != null) ? this.label.clonar():null;
        clon.width = (this.width != null) ? this.width.clonar() : null;
        clon.height = (this.height != null) ? this.height.clonar() : null;
        clon.estilos = (this.estilos != null) ? this.estilos.clonar() : null;

        clon.countWidth = this.countWidth;
        clon.countHeight = this.countHeight;
        clon.countLabel = this.countLabel;
        clon.countStyles = this.countStyles;

        return clon;
    }

    //Metodo que retorna la representacion base de la variable
    @Override
    public String getString() {
        return this.tipoVariable.getTipo() + " " + this.id;
    }
}
