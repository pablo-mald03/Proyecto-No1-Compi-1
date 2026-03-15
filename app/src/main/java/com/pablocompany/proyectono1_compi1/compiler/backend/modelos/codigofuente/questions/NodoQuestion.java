package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoTipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;

import java.util.List;

/*Created by Pablo*/
//Superclase que representa toda la jeraquia de preguntas que puede haber en el formulario
public abstract class NodoQuestion extends NodoComponente {

    //Atributos que definen la pregunta
    protected TipoVariable tipoVariable;

    //Atributo que representa el nombre de la variable (Puede ser nulleable)
    protected String id;

    public NodoQuestion(TipoVariable tipo, String id,NodoWidth width, NodoHeight height,Estilos estilos, int linea, int columna) {
        super(width,height,estilos,linea, columna);
        this.tipoVariable = tipo;
        this.id = id;
    }

    //Metodo que permite procesar los estilos que vienen en la configuracion
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
            }
        }
        return new Estilos(backgroundColor, color, fontFamily, textSize);

    }

    //Metodo que permite validar si tiene comodines la question
    public abstract int contarComodines();

    /*Created by Pablo*/
}
