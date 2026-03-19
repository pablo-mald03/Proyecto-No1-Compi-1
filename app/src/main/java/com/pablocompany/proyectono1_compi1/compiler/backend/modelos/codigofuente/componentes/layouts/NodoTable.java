package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointX;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointY;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoTipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales.NodoElse;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.NodoDraw;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstiloBorde;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.EstilosComponent;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.Seccion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario.Tablero;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa por completo la tabla de los formularios. Otro tipo de layout que existe
public class NodoTable extends NodoComponente implements ValidarDatosForms {

    private List<List<NodoComponente>> filas;

    private NodoPointX pointX;
    private NodoPointY pointY;


    /*Variable que representa los tipos de borde que hay*/
    private NodoBorder borde;

    /*Atributos contador que permiten validar si slo viene una configuracion de cada*/
    private int countWidth = 0;
    private int countHeight = 0;
    private int countPointX = 0;
    private int countPointY = 0;
    private int countElements = 0;
    private int countStyles = 0;

    public NodoTable(List<AtributoConfig> configs, int linea, int columna) {
        // Inicializamos con nulls por ahora, luego el procesado los llena
        super(null, null, null, linea, columna);
        this.filas = null;
        this.pointX = null;
        this.pointY = null;
        this.borde = null;
        this.estilos = null;
        procesarConfiguracion(configs);
    }

    //Metodo que permite bifurcar la logica para poder validar la seccion que no tenga comodines y
    // no interrumpir el metodo heredado de la clase padre
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {

        validarObligatorio(countWidth, "width", listaErrores);
        validarObligatorio(countHeight, "height", listaErrores);
        validarObligatorio(countPointX, "pointX", listaErrores);
        validarObligatorio(countPointY, "pointY", listaErrores);
        validarObligatorio(countElements, "elements", listaErrores);

        validarDuplicado(countWidth, "width", listaErrores);
        validarDuplicado(countHeight, "height", listaErrores);
        validarDuplicado(countPointX, "pointX", listaErrores);
        validarDuplicado(countPointY, "pointY", listaErrores);
        validarDuplicado(countElements, "elements", listaErrores);
        validarDuplicado(countStyles, "styles", listaErrores);


        if (this.pointX != null) {
            TipoVariable tipoX = this.pointX.validarSemantica(tabla, listaErrores);
            if (tipoX != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("TABLE", "Semantico",
                        "El valor de PointX debe ser tipo \"number\".", getLinea(), getColumna()));
            }
        }

        if (this.width != null) {
            this.width.validarSemantica(tabla, listaErrores, esLayout);
        }

        if (this.height != null) {
            this.height.validarSemantica(tabla, listaErrores, esLayout);
        }

        if (this.pointY != null) {
            TipoVariable tipoY = this.pointY.validarSemantica(tabla, listaErrores);
            if (tipoY != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("TABLE", "Semantico",
                        "El valor de PointY debe ser un \"number\".", getLinea(), getColumna()));
            }
        }

        //Bifurcacion de logica
        if (this.estilos != null) {
            this.estilos.validarSemantica(tabla, listaErrores, esLayout);
        }

        if (this.borde != null) {
            this.borde.validarSemantica(tabla, listaErrores);
        }

        if (filas != null) {

            for (List<NodoComponente> fila : filas) {
                if (fila == null) {
                    continue;
                }
                for (NodoComponente componente : fila) {
                    if (componente == null) {
                        continue;
                    }

                    componente.validarSemantica(tabla, listaErrores);
                }
            }

        }

        return TipoVariable.VOID;
    }

    // Metodo que valida que el atributo haya sido definido al menos una vez
    private void validarObligatorio(int contador, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador == 0) {
            listaErrores.add(new ErrorAnalisis("TABLE", "Semantico",
                    "El atributo \"" + nombreAtributo + "\" es obligatorio y no ha sido definido en la \"TABLE\".",
                    getLinea(), getColumna()));
        }
    }

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this.validarSemantica(tabla, listaErrores, true);
    }

    //Metodo que permite procesar la configuracion de los textos
    private void procesarConfiguracion(List<AtributoConfig> configs) {

        if (configs.isEmpty()) {
            return;
        }

        for (AtributoConfig config : configs) {

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
                    this.filas = (List<List<NodoComponente>>) config.getNodoValor();
                    this.countElements++;
                    break;
            }
        }
        /*Conteo de columnas y filas*/
            /*int maxCols = 0;
            for(List<NodoComponente> fila : filas) {
            maxCols = Math.max(maxCols, fila.size());
            }*/
    }

    //Metodo que permite validar la duplicidad de instrucciones en el cuerpo de la tabla
    private void validarDuplicado(int contador, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador > 1) {
            listaErrores.add(new ErrorAnalisis("TABLE", "Semantico",
                    "El atributo \"" + nombreAtributo + "\" ha sido definido más de una vez en la TABLE.",
                    getLinea(), getColumna()));
        }
    }


    //Metodo que permite procesar los estilos de la tabla
    @Override
    protected Estilos procesarEstilos(List<NodoEstilos> lista) {
        if (lista.isEmpty()) {
            return null;
        }

        NodoColor backgroundColor = null;
        NodoColor color = null;
        NodoTipoLetra fontFamily = null;
        NodoExpresion textSize = null;

        for (NodoEstilos nodo : lista) {

            if (nodo == null) {
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

        if (this.filas != null) {
            for (List<NodoComponente> fila : filas) {
                if (fila != null) {
                    for (NodoComponente celda : fila) {
                        if (celda != null) {
                            celda.ejecutarDraws(tabla, errores);
                        }
                    }
                }
            }
        }
    }

    /*Metodo que permite retornar todos los componentes que tiene dentro la tabla*/
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

        Object bordeResultado = (this.borde != null) ? this.borde.ejecutar(tabla, listaErrores) : null;

        if (bordeResultado instanceof OnCompilacionError) return bordeResultado;

        List<List<Formulario>> tablaFinal = new ArrayList<>();

        if (this.filas != null) {
            for (List<NodoComponente> filaActual : this.filas) {
                List<Formulario> filaProcesada = new ArrayList<>();

                for (NodoComponente componente : filaActual) {
                    Object resultado = componente.ejecutar(tabla, listaErrores);

                    if (resultado instanceof OnCompilacionError) return resultado;

                    if (resultado instanceof Formulario) {
                        filaProcesada.add((Formulario) resultado);
                    }
                }
                tablaFinal.add(filaProcesada);
            }
        }

        Object estilosObjetoProcesados = procesarEstilosLocales(tabla, listaErrores);

        if (estilosObjetoProcesados instanceof OnCompilacionError) return estilosObjetoProcesados;

        EstilosComponent estilosObjeto = (EstilosComponent) estilosObjetoProcesados;

        Number alto = (heightResultado instanceof Number) ? (Number) heightResultado : null;
        Number ancho = (widthResultado instanceof Number) ? (Number) widthResultado : null;
        Number x = (pointXConfig instanceof Number) ? (Number) pointXConfig : 0;
        Number y = (pointYConfig instanceof Number) ? (Number) pointYConfig : 0;

        EstiloBorde bordeFinal = (bordeResultado instanceof EstiloBorde) ? (EstiloBorde) bordeResultado : null;

        return new Tablero(alto, ancho, x, y, tablaFinal, estilosObjeto, bordeFinal, getLinea(), getColumna());

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
        return "";
    }
}
