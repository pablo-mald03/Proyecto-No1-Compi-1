package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;

import java.util.List;

/*Created by Pablo*/
//Superclase que representa toda la jeraquia de preguntas que puede haber en el formulario
public abstract class NodoQuestion extends Nodo {

    protected TipoVariable tipoVariable;

    protected String id;

    protected NodoWidth width;
    protected NodoHeight height;

    //Atibuto especial que trae toda su configuracion de estilos
    protected Estilos estilos;

    public NodoQuestion(TipoVariable tipo, String id,NodoWidth width, NodoHeight height,Estilos estilos, int linea, int columna) {
        super(linea, columna);
        this.tipoVariable = tipo;
        this.height = height;
        this.width = width;
        this.id = id;
        this.estilos = estilos;
    }

    //Metodo que permite procesar los estilos que vienen en la configuracion
    protected Estilos procesarEstilos(List<NodoEstilos> lista) {

        if(lista.isEmpty()){
            return null;
        }

        NodoColor backgroundColor = null;
        NodoColor color = null;
        TipoLetra fontFamily = TipoLetra.MONO;
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
                    fontFamily = TipoLetra.valueOf((String) valorNodo);
                    break;
                case TEXT_SIZE:
                    textSize = (NodoExpresion) valorNodo;
                    break;
            }
        }
        return new Estilos(backgroundColor, color, fontFamily, textSize);

    }


}
