package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;

import java.util.List;

/*Created by Pablo*/
//Superclase que representa toda la jeraquia de preguntas que puede haber en el formulario
public abstract class NodoQuestion extends Nodo {

    protected TipoVariable tipoVariable;

    protected String id;

    protected NodoExpresion width;
    protected NodoExpresion height;

    //Atibuto especial que trae toda su configuracion de estilos
    protected Estilos estilos;

    public NodoQuestion(TipoVariable tipo, String id,NodoExpresion width, NodoExpresion height,Estilos estilos, int linea, int columna) {
        super(linea, columna);
        this.tipoVariable = tipo;
        this.height = height;
        this.width = width;
        this.id = id;
        this.estilos = estilos;
    }


}
