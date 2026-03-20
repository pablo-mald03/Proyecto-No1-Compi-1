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
