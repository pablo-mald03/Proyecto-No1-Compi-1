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

    /*SI EL ID ES NULO TIENE SIGNIFICADO TAMBIEN*/

    public NodoOpenQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
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
                case LABEL:
                    this.label = (NodoLabel) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
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
        }else{

            //Solo es preventivo
            listaErrores.add(new ErrorAnalisis(id, "Semántico",
                    "El atributo \"label\" es obligatorio en la pregunta OPEN_QUESTION.", getLinea(), getColumna()));

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

        if (id != null) {
            Simbolo simbolo = new Simbolo(id, TipoVariable.SPECIAL, this, getLinea(), getColumna());
            if (!tabla.insertar(simbolo)) {
                listaErrores.add(new ErrorAnalisis(id, "Semántico",
                        "La variable \"" + id + "\" ya ha sido definida.", getLinea(), getColumna()));
            }
        }

        return TipoVariable.SPECIAL;
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

        if (this.label != null) {
            NodoComodin comodin = extraerValor(this.label.getExpresion());
            if (comodin != null && comodin.getExpresion() == null && iterador < parametrosPregunta.size()) {
                iterador = this.label.setExpresion(parametrosPregunta, iterador);
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

        if (this.label != null) {
            NodoExpresion expresion = this.label.getExpresion();
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

    //Metodo que permite ejecutar las acciones que tenga la pregunta
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object labelTexto = this.label.ejecutar(tabla, listaErrores);

        if (labelTexto instanceof OnCompilacionError) return labelTexto;

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

        return new PreguntaAbierta(alto, ancho, ( labelTexto != null)? labelTexto.toString():null, estilosObjeto, getLinea(), getColumna());
    }

    //Metodo que retorna la representacion base de la variable
    @Override
    public String getString() {
        return this.tipoVariable.getTipo() + " " + this.id;
    }
}
