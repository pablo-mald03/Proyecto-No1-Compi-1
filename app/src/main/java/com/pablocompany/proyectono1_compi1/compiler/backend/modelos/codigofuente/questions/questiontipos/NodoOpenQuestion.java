package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.questiontipos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
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

    public NodoOpenQuestion(TipoVariable tipo, String id, NodoExpresion width, NodoExpresion height, NodoExpresion label, Estilos estilos, int linea, int columna) {
        super(tipo,id, width, height, estilos, linea, columna);
        this.label = label;
    }

    //Metodo que permite ejecutar las acciones que tenga la pregunta
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this;
    }

    //Metodo que retorna la representacion base de la variable
    @Override
    public String getString() {
        return this.tipoVariable.getTipo() + " " +this.id;
    }
}
