package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto;


//Clase delegada que representa a un valor de texto normal
public class CompiledCadena extends CompiledTexto {

    //Atributo del valor del texto
    private String valor;

    public CompiledCadena(String valor) {
        this.valor = valor;
    }

    //Metodo que retorna el valor string
    @Override
    public String getStringCompiled() {
        return valor;
    }
}
