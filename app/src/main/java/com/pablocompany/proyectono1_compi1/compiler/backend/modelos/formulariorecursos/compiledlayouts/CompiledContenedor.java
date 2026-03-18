package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;

import java.util.ArrayList;
import java.util.List;

//Clase que representa a toda la jerarquia de layouts. Es decir a estos componentes que almacenan dentro de si mismos mas cosas
public abstract class CompiledContenedor extends CompiledForm {

    protected List<CompiledForm> elementos = new ArrayList<>();

    public CompiledContenedor(double width, double height, double pointX, double pointY) {
        super(width, height, pointX, pointY);
    }

    /*Metodos getters y setters que permiten obtener la lista de componentes*/
    protected List<CompiledForm> getElementos() {
        return elementos;
    }

    protected void setElementos(List<CompiledForm> elementos) {
        this.elementos = elementos;
    }
}
