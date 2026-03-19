package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;

import java.util.ArrayList;
import java.util.List;

//Clase que representa a toda la jerarquia de layouts. Es decir a estos componentes que almacenan dentro de si mismos mas cosas
public abstract class CompiledContenedor extends CompiledForm {

    /*COnstructor*/
    public CompiledContenedor(Number width, Number height, Number pointX, Number pointY, CompiledEstilos estilos) {
        super(width, height, pointX, pointY,estilos);
    }

}
