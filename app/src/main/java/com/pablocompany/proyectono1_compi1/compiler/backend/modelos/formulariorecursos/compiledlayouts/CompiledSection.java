package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBackground;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledFontFamily;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.estilosmodels.CompiledTextSize;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a las secciones que pueden almacenar mas cosas dentro de si mismas
public class CompiledSection extends CompiledContenedor {

    //Representa la orientacion de la seccion
    public TipoOrientacion orientation;
    private List<CompiledForm> elementos;

    public CompiledSection(Number width, Number height, Number pointX, Number pointY, String orientation,List<CompiledForm> elementos, List<CompiledEstilos> estilos,int fila, int columna) {
        super(width, height, pointX, pointY,estilos,fila,columna);
        try{
            this.orientation = TipoOrientacion.valueOf(orientation.toUpperCase());
        }catch(Exception e){
            this.orientation = TipoOrientacion.VERTICAL;
        }
        this.elementos = elementos;
    }


    /*Metodos getters y setters que permiten obtener la lista de componentes*/
    public List<CompiledForm> getElementos() {
        return elementos;
    }

    private void setElementos(List<CompiledForm> elementos) {
        this.elementos = elementos;
    }

    /*Metodo que permite validar la semantica del codigo interpretado*/
    public void validarSemantica(List<ErrorAnalisis> listaErrores){

        if(this.height.intValue() < -1 ){
            this.reportarErrores("<section>","El atributo \"height\" no debe ser menor a -1", listaErrores);
        }

        if(this.width.intValue() < -1 ){
            this.reportarErrores("<section>","El atributo \"width\" no debe ser menor a -1", listaErrores);
        }

        for(CompiledForm form: this.elementos){
            if(form != null) {
                form.validarSemantica(listaErrores);
            }
        }
    }

    /*Metodo que permite validar los estilos de los contenedores section*/
    @Override
    public void validarEstilosProcesados(List<ErrorAnalisis> listaErrores) {

        if(this.estilosProcesados == null){
            return;
        }


        if(this.estilosProcesados.getBackgroudColor() != null){
            if(this.estilosProcesados.getBackgroudColor()[0] < 0 || this.estilosProcesados.getBackgroudColor()[0] > 255){
                this.reportarErrores("<section>", "El atributo \"background color\" no puede ser menor a 0 o mayor a 255", listaErrores);
            }
        }
        if(this.estilosProcesados.getTextColor() != null){
            if(this.estilosProcesados.getTextColor()[0] < 0 || this.estilosProcesados.getTextColor()[0] > 255){
                this.reportarErrores("<section>", "El atributo \"color\" no puede ser menor a 0 o mayor a 255", listaErrores);
            }

        }

        if(this.estilosProcesados.getTextSize() != null){
            if(this.estilosProcesados.getTextSize().intValue() < 0 && this.hayTextSize){
                this.reportarErrores("<section>", "El atributo \"text size\" no puede ser menor a 0", listaErrores);
            }
        }

        if(this.estilosProcesados.getBorder() != null){
            this.estilosProcesados.getBorder().validarConfiguracion(listaErrores, this.getFila(), this.getColumna());
        }


        //Propagacion
       for(CompiledForm form: this.elementos){
           if(form != null) {
               form.validarEstilosProcesados(listaErrores);
           }
       }

    }


    /*Metodo delegado para poder validar los estilos para cada section*/
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

        validarDuplicado(contadorBorder,"<section>", "border", listaErrores);
        validarDuplicado(contadorBackgroundColor,"<section>", "background color", listaErrores);
        validarDuplicado(contadorFontFamily,"<section>", "font family", listaErrores);
        validarDuplicado(contadorTextSize,"<section>", "text size", listaErrores);
        validarDuplicado(contadorColor, "<section>","color", listaErrores);

        for(CompiledForm form: this.elementos){
            if(form != null) {
                form.validarEstilos(listaErrores);
            }
        }
    }
    //Metodo de maxima jerarquia que permite que cada una de las clases que heredan de esta puedan empaquetar sus estilos o adquieran predeterminados
    @Override
    public  void delegarEstilos(){
        this.empaquetaEstilos();

        for(CompiledForm form: this.elementos){
            if(form != null) {
                form.delegarEstilos();
            }
        }
    }

}
