package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

//Clase que representa a las secciones que pueden almacenar mas cosas dentro de si mismas
public class CompiledSection extends CompiledContenedor {

    public String orientation;

    public CompiledSection(double width, double height,double pointX, double pointY, String orientation) {
        super(width, height, pointX, pointY);
        this.orientation = orientation;
    }


}
