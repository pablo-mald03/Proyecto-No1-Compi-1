package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

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
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.condicionales.NodoElse;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.NodoDraw;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa por completo la tabla de los formularios. Otro tipo de layout que existe
public class NodoTable extends NodoComponente implements ValidarDatosForms {

    private List<List<NodoComponente>> filas;

    private NodoPointX pointX;
    private NodoPointY pointY;


    /*Variable que representa los tipos de vorden que hay*/
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


    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
