package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoLetra;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questionrecursos.TipoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a una pregunta abierta dentro del contexo del lenguaje de programacion
public class NodoOpenQuestion extends NodoQuestion {

    //Atributos
    private NodoExpresion label;

    public NodoOpenQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
        super(tipo, id, null, null, null, linea, columna);
        this.setConfiguraciones(config);
    }

    //Metodo que permite setear los valores que vienen en la configuracion
    private void setConfiguraciones(List<AtributoConfig> configuracion) {

        for (AtributoConfig config : configuracion) {
            switch (config.getTipo()) {

                case WIDTH:
                    this.width = (NodoExpresion) config.getNodoValor();
                    break;
                case HEIGHT:
                    this.height = (NodoExpresion) config.getNodoValor();
                    break;
                case LABEL:
                    this.label = (NodoExpresion) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    break;

            }

        }

    }

    //Metodo que permite procesar los estilos que vienen en la configuracion
    private Estilos procesarEstilos(List<NodoEstilos> lista) {
        NodoColor backgroundColor = null;
        NodoColor color = null;
        TipoLetra fontFamily = TipoLetra.MONO;
        NodoExpresion textSize = null;

        for (NodoEstilos nodo : lista) {

            Object valorNodo = nodo.getValor();

            switch (nodo.getTipo()) {
                case BACKGROUND_COLOR:
                    backgroundColor = (NodoColor) valorNodo;
                    break;
                case COLOR_TEXTO:
                    color = (NodoColor) valorNodo;
                    break;
                case FONT_FAMILY:
                    fontFamily = TipoLetra.valueOf ((String)valorNodo);
                    break;
                case TEXT_SIZE:
                    textSize = (NodoExpresion) valorNodo;
                    break;
            }
        }
        return new Estilos(backgroundColor, color, fontFamily, textSize);

    }


    //Metodo que permite ejecutar las acciones que tenga la pregunta
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this;
    }

    //Metodo que retorna la representacion base de la variable
    @Override
    public String getString() {
        return this.tipoVariable.getTipo() + " " + this.id;
    }
}
