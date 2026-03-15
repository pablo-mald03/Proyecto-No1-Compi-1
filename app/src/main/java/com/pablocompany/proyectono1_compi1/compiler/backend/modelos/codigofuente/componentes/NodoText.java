package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointX;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointY;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoTipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.TipoConfiguracion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a los textos que se mostraran en el formulario para dar mensajes
public class NodoText extends NodoComponente {

    //Atributos
    private NodoExpresion contenido;

    public NodoText(List<AtributoConfig> configs, int linea, int columna) {
        super(null, null, null, linea, columna);
        this.contenido = null;
        this.estilos = null;
        procesarConfiguracion(configs);
    }

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.contenido != null) {
            TipoVariable tipoContenido = this.contenido.validarSemantica(tabla, listaErrores);

            if (tipoContenido != TipoVariable.STRING &&
                    tipoContenido != TipoVariable.ERROR) {

                listaErrores.add(new ErrorAnalisis("TEXT", "Semantico",
                        "El contenido de un \"TEXT\" debe ser de tipo \"string\". Se encontro: " + tipoContenido,
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
        }

        if(this.width != null){
            this.width.validarSemantica(tabla, listaErrores, true);
        }

        if(this.height != null) {
            this.height.validarSemantica(tabla, listaErrores, true);
        }

        if (this.estilos != null) {
            this.estilos.validarSemantica(tabla, listaErrores, true);
        }

        return TipoVariable.STRING;

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
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    break;
                case CONTENT:
                    this.contenido = (NodoExpresion) config.getNodoValor();
                    break;
            }
        }
    }

    //Pendiente procesar los estilos del texto
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
