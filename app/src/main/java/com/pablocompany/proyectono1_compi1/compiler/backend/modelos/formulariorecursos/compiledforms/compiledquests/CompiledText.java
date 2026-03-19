package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.compiledquests;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto.CompiledCadenaTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.estiloscompiled.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledforms.CompiledQuestions;

import java.util.List;

//Clase que representa a un texto plano
public class CompiledText extends CompiledQuestions {

    private CompiledCadenaTexto texto;

    public CompiledText(Number width, Number height, CompiledCadenaTexto texto, List<CompiledEstilos> estilos, int fila, int columna) {
        super(width, height, estilos, fila, columna);
        this.texto = texto;
    }

    /*Metodo getter del texto*/
    public CompiledCadenaTexto getTexto() {
        return this.texto;
    }

}/*Created by Pablo*/
