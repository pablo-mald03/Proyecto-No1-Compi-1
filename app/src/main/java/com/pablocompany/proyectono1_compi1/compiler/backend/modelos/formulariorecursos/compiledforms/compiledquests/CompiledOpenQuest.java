package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

//Clase que representa a una pregunta abierta que puede tener un formulario
public class CompiledOpenQuest extends CompiledQuestions {

    //Atributo
    public String texto;

    public CompiledOpenQuest(Number width, Number height, String texto, CompiledEstilos estilos) {
        super(width, height, estilos);
        this.texto = texto;
    }

    /*Metodo getter del texto*/
    public String getTexto() {
        return this.texto;
    }

}/*Created by Pablo*/
