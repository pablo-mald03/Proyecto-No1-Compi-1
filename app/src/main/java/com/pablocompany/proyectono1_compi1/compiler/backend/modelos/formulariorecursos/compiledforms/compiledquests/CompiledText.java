package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

//Clase que representa a un texto plano
public class CompiledText extends CompiledQuestions {

    public String texto;

    public CompiledText(Number width, Number height, String texto, CompiledEstilos estilos) {
        super(width, height, estilos);
        this.texto = texto;
    }

    /*Metodo getter del texto*/
    public String getTexto() {
        return this.texto;
    }

}/*Created by Pablo*/
