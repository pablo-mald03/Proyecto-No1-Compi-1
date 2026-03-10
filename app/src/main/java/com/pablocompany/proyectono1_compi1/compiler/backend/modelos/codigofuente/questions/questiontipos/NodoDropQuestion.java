package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a una drop question
public class NodoDropQuestion extends NodoQuestion {

    //Atributos
    private NodoExpresion label;
    //Representa a la opciones que ofrece
    private List<NodoExpresion> opciones;

    //PENDIENTE REFACTORIZAR TODO
    public NodoDropQuestion(TipoVariable tipo, String id, List<AtributoConfig> config, int linea, int columna) {
        super(tipo, id, null, null, null, linea, columna);
        //this.setConfiguraciones(config);
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
