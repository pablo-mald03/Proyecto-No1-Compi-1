package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

import java.util.List;

//Clase que representa a una pregunta abierta que puede tener un formulario
public class CompiledOpenQuest extends CompiledQuestions {

    //Atributo
    public List<CompiledCadenaTexto> texto;

    public CompiledOpenQuest(Number width, Number height, List<CompiledCadenaTexto> texto, CompiledEstilos estilos) {
        super(width, height, estilos);
        this.texto = texto;
    }

    /*Metodo getter del texto*/
    public List<CompiledCadenaTexto> getTexto() {
        return this.texto;
    }

}/*Created by Pablo*/
