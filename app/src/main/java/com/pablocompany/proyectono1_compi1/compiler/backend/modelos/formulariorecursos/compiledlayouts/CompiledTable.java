package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.colorescompiled.CompiledColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la tabla que es contenedora de codigo
public class CompiledTable extends CompiledContenedor {

    //Atributo que representa al contenido de la tabla
    private List<List<CompiledForm>> filas;

    public CompiledTable(Number width, Number height, Number pointX, Number pointY, List<List<CompiledForm>> filas, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY, estilos,fila,columna);
        this.filas = filas;
    }

    public void setFilas(List<List<CompiledForm>> filas) {
        this.filas = filas;
    }

    /*Metodos getters y setters que permiten obtener la lista de componentes*/
    public List<List<CompiledForm>> getElementos() {
        return this.filas;
    }

    /*Metodo que permite calcular el total de columas que tendra el layout*/
    public int getTotalColumnas(){

        int max = 0;
        for (List<CompiledForm> fila : filas) {
            if (fila.size() > max) {
                max = fila.size();
            }
        }

        return max;

    }

    /*Metodo delegado para poder validar los estilos para table*/
    @Override
    public void validarEstilos(List<ErrorAnalisis> listaErrores){

        if (this.estilos == null) {
            return;
        }

        int contadorBorder = 0;
        int contadorBackgroundColor = 0;
        int contadorColor = 0;
        int contadorTextSize = 0;
        int contadorFontFamily = 0;

        for(CompiledEstilos estilo : this.estilos){

            if(estilo instanceof CompiledBorder){
                contadorBorder++;
            }
            if(estilo instanceof CompiledBackground) {
                contadorBackgroundColor++;
            }

            if(estilo instanceof CompiledTextColor) {
                contadorColor++;
            }

            if(estilo instanceof CompiledTextSize){
                contadorTextSize++;
            }

            if(estilo instanceof CompiledFontFamily){
                contadorFontFamily++;
            }

        }

        validarDuplicado(contadorBorder,"<table>", "border", listaErrores);
        validarDuplicado(contadorBackgroundColor,"<table>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<table>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<table>", "text size", listaErrores);
        validarDuplicado(contadorColor, "<table>","color", listaErrores);


        for(List<CompiledForm> fila : this.filas){
            if (fila != null) {
                for (CompiledForm form : fila) {
                    if (form != null) {
                        form.validarEstilos(listaErrores);
                    }
                }
            }
        }
    }

    /*Metodo que permite validar la semantica del codigo interpretado*/
    public void validarSemantica(List<ErrorAnalisis> listaErrores){

        if(this.height.intValue() < -1 ){
            this.reportarErrores("<table>","El atributo \"height\" no debe ser menor a -1", listaErrores);
        }

        if(this.width.intValue() < -1 ){
            this.reportarErrores("<table>","El atributo \"width\" no debe ser menor a -1", listaErrores);
        }

        for(List<CompiledForm> fila : this.filas){
            if (fila != null) {
                for (CompiledForm form : fila) {
                    if (form != null) {
                        form.validarSemantica(listaErrores);
                    }
                }
            }
        }
    }

    /*Metodo que permite que cad clase empaquete sus estilos*/
    @Override
    public  void delegarEstilos(){

        super.empaquetaEstilos();

        for(List<CompiledForm> fila : this.filas){
            if (fila != null) {
                for (CompiledForm form : fila) {
                    if (form != null) {
                        form.delegarEstilos();
                    }
                }
            }
        }
    }

    /*Metodo que permite validar los estilos de los contenedores table*/
    @Override
    public void validarEstilosProcesados(List<ErrorAnalisis> listaErrores) {

        if(this.estilosProcesados == null){
            return;
        }


        if(this.estilosProcesados.getBackgroudColor() != null){
            if(this.estilosProcesados.getBackgroudColor()[0] < 0 || this.estilosProcesados.getBackgroudColor()[0] > 255){
                this.reportarErrores("<table>", "El atributo \"background color\" no puede ser menor a 0 o mayor a 255", listaErrores);
            }
        }
        if(this.estilosProcesados.getTextColor() != null){
            if(this.estilosProcesados.getTextColor()[0] < 0 || this.estilosProcesados.getTextColor()[0] > 255){
                this.reportarErrores("<table>", "El atributo \"color\" no puede ser menor a 0 o mayor a 255", listaErrores);
            }

        }

        if(this.estilosProcesados.getTextSize() != null ){
            if(this.estilosProcesados.getTextSize().intValue() < 0 && this.hayTextSize){
                this.reportarErrores("<table>", "El atributo \"text size\" no puede ser menor a 0", listaErrores);
            }
        }

        if(this.estilosProcesados.getBorder() != null){
            this.estilosProcesados.getBorder().validarConfiguracion(listaErrores, this.getFila(), this.getColumna());
        }


        //Propagacion
        for(List<CompiledForm> fila : this.filas){
            if (fila != null) {
                for (CompiledForm form : fila) {
                    if (form != null) {
                        form.validarEstilosProcesados(listaErrores);
                    }
                }
            }
        }

    }
}

/*Created by Pablo*/
