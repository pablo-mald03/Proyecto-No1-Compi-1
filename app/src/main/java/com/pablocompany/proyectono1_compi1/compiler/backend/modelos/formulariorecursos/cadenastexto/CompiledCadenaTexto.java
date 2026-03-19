package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto;

import java.util.List;

//Clase que representa a una cadena de texto compuesta por los fragmentos de texto y emojis
public class CompiledCadenaTexto {

    //Atributos
    private List<CompiledTexto> texto;

    //Consutructor
    public CompiledCadenaTexto(List<CompiledTexto> texto) {
        this.texto = texto;
    }

    //Metodo que permite retornar la lista de texto
    public List<CompiledTexto> getTexto(){
        return this.texto;
    }

}
/*Created by Pablo*/
