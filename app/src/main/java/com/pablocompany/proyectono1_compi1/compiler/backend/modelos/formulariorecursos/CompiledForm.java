package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase de maxima jerarquia que permite almacenar o representar al codigo compilado que se lee de etiquetas
public abstract class CompiledForm {

    //Atributos de la clase
    protected Number width;
    protected Number height;

    protected Number pointX;
    protected Number pointY;

    /*Atributo que representa todos los estilos de un componente*/
    protected List<CompiledEstilos> estilos;

    //Coordenadas para reportar errores
    protected int fila;
    protected int columna;

    /*Constructor*/
    public CompiledForm (Number width, Number height, Number pointX, Number pointY, List<CompiledEstilos> estilos,int fila, int columna){
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
        this.estilos = estilos;
        this.fila = fila;
        this.columna = columna;
    }

    /*Metodos getters*/

    public Number getWidth() {
        return this.width;
    }

    public Number getHeight() {
        return this.height;
    }

    public Number getPointX() {
        return this.pointX;
    }

    public Number getPointY() {
        return this.pointY;
    }

    public List<CompiledEstilos> getEstilos() {
        return this.estilos;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    /*Metodo delegado para poder validar los estilos de cada clase*/
    public abstract void ValidarEstilos(List<ErrorAnalisis> listaErrores);

}
