package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoElements;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoOrientation;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointX;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointY;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoTipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstiloBorde;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstilosComponent;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.Seccion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que define la seccion de un formulario y puede contener a mas componentes
public class NodoSection extends NodoComponente implements ValidarDatosForms {

    private NodoElements elementos;
    private NodoOrientation orientation;
    private NodoPointX pointX;
    private NodoPointY pointY;

    /*Variable que representa los tipos de borde que hay*/
    private NodoBorder borde;

    /*Atributos contador que permiten validar si slo viene una configuracion de cada*/
    private int countElementos = 0;
    private int countOrientation = 0;
    private int countPointX = 0;
    private int countPointY = 0;
    private int countWidth = 0;
    private int countHeight = 0;
    private int countStyles = 0;


    public NodoSection(List<AtributoConfig> cuerpo, int linea, int columna) {
        super(null,null,null,linea, columna);
        this.elementos = null;
        this.orientation = null;
        this.pointX = null;
        this.pointY = null;
        this.borde = null;
        this.estilos = null;
        this.setConfiguraciones(cuerpo);

    }

    //Metodo que permite bifurcar la logica para poder validar la seccion que no tenga comodines y
    // no interrumpir el metodo heredado de la clase padre
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {

        validarObligatorio(countWidth, "width", listaErrores);
        validarObligatorio(countHeight, "height", listaErrores);
        validarObligatorio(countPointX, "pointX", listaErrores);
        validarObligatorio(countPointY, "pointY", listaErrores);
        validarObligatorio(countElementos, "elements", listaErrores);

        validarDuplicado(countElementos, "elements", listaErrores);
        validarDuplicado(countOrientation, "orientation", listaErrores);
        validarDuplicado(countPointX, "pointX", listaErrores);
        validarDuplicado(countPointY, "pointY", listaErrores);
        validarDuplicado(countWidth, "width", listaErrores);
        validarDuplicado(countHeight, "height", listaErrores);
        validarDuplicado(countStyles, "styles", listaErrores);

        if (this.elementos != null) {
            this.elementos.validarSemantica(tabla, listaErrores);
        }

        if (this.pointX != null) {
            TipoVariable tipoX = this.pointX.validarSemantica(tabla, listaErrores);
            if (tipoX != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("SECTION", "Semantico",
                        "El valor de PointX debe ser tipo \"number\".", getLinea(), getColumna()));
            }
        }

        if (this.pointY != null) {
            TipoVariable tipoY = this.pointY.validarSemantica(tabla, listaErrores);
            if (tipoY != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("SECTION", "Semantico",
                        "El valor de PointY debe ser un \"number\".", getLinea(), getColumna()));
            }
        }

        if(this.width != null){
            this.width.validarSemantica(tabla, listaErrores, esLayout);
        }

        if(this.height != null){
            this.height.validarSemantica(tabla, listaErrores, esLayout);
        }

        //Bifurcacion de logica
        if(this.estilos != null){
            this.estilos.validarSemantica(tabla, listaErrores,esLayout);
        }


        if (this.borde != null) {
            this.borde.validarSemantica(tabla, listaErrores);
        }

        return TipoVariable.VOID;
    }

    // Metodo que permite validar la duplicadad de instrucciones en el cuerpo de la seccion
    private void validarDuplicado(int contador, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador > 1) {
            listaErrores.add(new ErrorAnalisis("SECTION", "Semantico",
                    "El atributo \"" + nombreAtributo + "\" ha sido definido más de una vez en la SECTION.",
                    getLinea(), getColumna()));
        }
    }

    // Metodo que valida que el atributo haya sido definido al menos una vez
    private void validarObligatorio(int contador, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador == 0) {
            listaErrores.add(new ErrorAnalisis("SECTION", "Semantico",
                    "El atributo \"" + nombreAtributo + "\" es obligatorio y no ha sido definido en la \"SECTION\".",
                    getLinea(), getColumna()));
        }
    }

    //Metodo que permite validar semantica de la seccion (PATRON EXPERTO)
    /*Simplemente es un llamado a lo que ya esta creado*/
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

       return this.validarSemantica(tabla,listaErrores,true);
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
                    this.countWidth++;
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    this.countHeight++;
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    this.countStyles++;
                    break;
                case POINT_X:
                    this.pointX = (NodoPointX) config.getNodoValor();
                    this.countPointX++;
                    break;
                case POINT_Y:
                    this.pointY = (NodoPointY) config.getNodoValor();
                    this.countPointY++;
                    break;
                case ELEMENTS:
                    this.elementos = (NodoElements) config.getNodoValor();
                    this.countElementos++;
                    break;
                case ORIENTATION:
                    this.orientation = (NodoOrientation) config.getNodoValor();
                    this.countOrientation++;
                    break;
            }
        }

        //Predeterminado
        if(this.orientation == null){
            this.orientation = new NodoOrientation("VERTICAL",0,0);
        }

    }

    //Metodo utilizado para implementar estilos de la section
    @Override
    protected  Estilos procesarEstilos(List<NodoEstilos> lista) {

        if(lista.isEmpty()){
            return null;
        }

        NodoColor backgroundColor = null;
        NodoColor color = null;
        NodoTipoLetra fontFamily = null;
        NodoExpresion textSize = null;

        for (NodoEstilos nodo : lista) {

            if(nodo ==null){
                continue;
            }

            Object valorNodo = nodo.getValor();

            switch (nodo.getTipo()) {
                case BACKGROUND_COLOR:
                    backgroundColor = (NodoColor) valorNodo;
                    break;
                case COLOR_TEXTO:
                    color = (NodoColor) valorNodo;
                    break;
                case FONT_FAMILY:
                    fontFamily = (NodoTipoLetra) valorNodo;
                    break;
                case TEXT_SIZE:
                    textSize = (NodoExpresion) valorNodo;
                    break;
                case BORDER:
                    this.borde = (NodoBorder) valorNodo;
                    break;
            }
        }
        return new Estilos(backgroundColor, color, fontFamily, textSize);

    }

    /*---Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)---*/
    @Override
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if (this.elementos != null) {
            this.elementos.ejecutarDraws(tabla, errores);
        }
    }

    //--Metodo que permite ejecutar y retornar el listado de todas las preguntas o elementos que tiene dentro---
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Object widthResultado = (this.width != null) ? this.width.ejecutar(tabla, listaErrores) : null;

        if (widthResultado instanceof OnCompilacionError) return widthResultado;

        Object heightResultado = (this.height != null) ? this.height.ejecutar(tabla, listaErrores) : null;

        if (heightResultado instanceof OnCompilacionError) return heightResultado;

        Object pointXConfig = (this.pointX != null) ? this.pointX.ejecutar(tabla, listaErrores) : null;

        if (pointXConfig instanceof OnCompilacionError) return pointXConfig;

        Object pointYConfig = (this.pointY != null) ? this.pointY.ejecutar(tabla, listaErrores) : null;

        if (pointYConfig instanceof OnCompilacionError) return pointYConfig;

        Object elementosResultado = (this.elementos != null) ? this.elementos.ejecutar(tabla, listaErrores) : new ArrayList<Formulario>();

        if (elementosResultado instanceof OnCompilacionError) return elementosResultado;

        Object bordeResultado = (this.borde != null) ? this.borde.ejecutar(tabla, listaErrores) : null;

        if (bordeResultado instanceof OnCompilacionError) return bordeResultado;

        Object estilosObjetoProcesados = procesarEstilosLocales(tabla, listaErrores);


        if (estilosObjetoProcesados instanceof OnCompilacionError) return estilosObjetoProcesados;

        EstilosComponent estilosObjeto = (EstilosComponent) estilosObjetoProcesados;


        Object orientacionProcesada = (this.orientation != null) ? this.orientation.ejecutar(tabla, listaErrores) : null;

        if (orientacionProcesada instanceof OnCompilacionError) return orientacionProcesada;

        TipoOrientacion orientacion = (TipoOrientacion) orientacionProcesada;

        Number alto = (heightResultado instanceof Number) ? (Number) heightResultado : null;
        Number ancho = (widthResultado instanceof Number) ? (Number) widthResultado : null;
        Number x = (pointXConfig instanceof Number) ? (Number) pointXConfig : 0;
        Number y = (pointYConfig instanceof Number) ? (Number) pointYConfig : 0;

        List<Formulario> listaFinal = (List<Formulario>) elementosResultado;

        EstiloBorde bordeFinal = (bordeResultado instanceof EstiloBorde) ? (EstiloBorde) bordeResultado : null;

        return new Seccion(alto, ancho, x, y,orientacion, listaFinal, estilosObjeto, bordeFinal, getLinea(), getColumna());

    }

    /*Metodo que permite ejecutar el listado de estilos locales*/
    private Object procesarEstilosLocales(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores){
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
        return estilosObjeto;
    }

    @Override
    public String getString() {
        return "SECTION";
    }


}
/*created by Pablo*/