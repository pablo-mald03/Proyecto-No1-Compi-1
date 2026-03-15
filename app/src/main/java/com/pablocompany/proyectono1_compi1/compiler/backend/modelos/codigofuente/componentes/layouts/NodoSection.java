package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

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
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que define la seccion de un formulario y puede contener a mas componentes
public class NodoSection extends NodoComponente implements ValidarDatosForms{

    private NodoElements elementos;
    private NodoOrientation orientation;
    private NodoPointX pointX;
    private NodoPointY pointY;

    /*Variable que representa los tipos de vorden que hay*/
    private NodoBorder borde;

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
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    break;
                case POINT_X:
                    this.pointX = (NodoPointX) config.getNodoValor();
                    break;
                case POINT_Y:
                    this.pointY = (NodoPointY) config.getNodoValor();
                    break;
                case ELEMENTS:
                    this.elementos = (NodoElements) config.getNodoValor();
                    break;
                case ORIENTATION:
                    this.orientation = (NodoOrientation) config.getNodoValor();
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

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }


}
/*created by Pablo*/