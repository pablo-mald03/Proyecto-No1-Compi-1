package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;
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
    private List<CompiledForm> getElementos() {
        return elementos;
    }

    private void setElementos(List<CompiledForm> elementos) {
        this.elementos = elementos;
    }

}
