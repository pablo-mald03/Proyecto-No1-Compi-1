package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.EstilosProcesados;
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

    /*Estilos que permiten que cada clase empaquete su propio estilo*/
    protected EstilosProcesados estilosProcesados;


    /*Constructor*/
    public CompiledForm (Number width, Number height, Number pointX, Number pointY, List<CompiledEstilos> estilos,int fila, int columna){
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
        this.estilos = estilos;
        this.fila = fila;
        this.columna = columna;
        this.estilosProcesados = null;
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

    public EstilosProcesados getEstilosProcesados() {
        return estilosProcesados;
    }


    /*Metodo delegado para poder validar los estilos de cada clase*/
    public abstract void validarEstilos(List<ErrorAnalisis> listaErrores);

    // Metodo que permite validar la duplicadad de instrucciones en el cuerpo de un componente
    protected void validarDuplicado(int contador,String componente, String nombreAtributo, List<ErrorAnalisis> listaErrores) {
        if (contador > 1) {
            listaErrores.add(new ErrorAnalisis(componente, "Semantico",
                    "El atributo \"" + nombreAtributo + "\" ha sido definido mas de una vez en la \"etiqueta\".",
                    getFila(), getColumna()));
        }
    }

    /*Metodo delegado para poder validar la semantica del interprete*/
    public void reportarErrores(String componente, String mensaje, List<ErrorAnalisis> listaErrores){
        listaErrores.add(new ErrorAnalisis(componente, "Semantico", mensaje, getFila(), getColumna()));
    }

    /*Metodo que permite validar la semantica de los estilos procesados*/
    public abstract void validarEstilosProcesados(List<ErrorAnalisis> listaErrores);

    /*Metodo que permite validar la semantica del codigo interpretado*/
    public abstract void validarSemantica(List<ErrorAnalisis> listaErrores);

    //Metodo de maxima jerarquia que permite que cada una de las clases que heredan de esta puedan empaquetar sus estilos o adquieran predeterminados
    protected abstract void empaquetaEstilos();

    /*Metodo utilizado para que todos los componentes empqueten sus estilos*/
    public abstract void delegarEstilos();

}
