package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.cadenastexto;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.TipoEmoji;

//Clase delegada que representa a un emoji
public class CompiledEmoji extends CompiledTexto {

    //Atributo
    private TipoEmoji tipoEmoji;

    //Atributo que reconoce las veces que se repetira el emoji
    private int veces = 1;

    //Constructor par emojis simples
    public CompiledEmoji(TipoEmoji tipoEmoji) {
        this.tipoEmoji = tipoEmoji;
    }

    //Constructor que sirve para reconocer el emoji de multiStar
    public CompiledEmoji(TipoEmoji tipoEmoji,String valor) {
        this.tipoEmoji = tipoEmoji;
        this.reconocerEmoji(tipoEmoji,valor);
    }

    //Metodo propio de la clase polimorfica (La ER utilizada no tiene ningun efecto en tiempo de PARSEO)
    private void reconocerEmoji(TipoEmoji tipo,String valor){

        if (tipo != TipoEmoji.MULTI_STAR) {
            return;
        }
        try {
            //ER UTILIZADA SOLO PARA JALAR EL VALOR (NO TIENE NINGUN OTRO CONTEXTO EN TIEMPO DE INTERPRETACION)
            String numerico = valor.replaceAll("[^0-9]", "");

            if (!numerico.isEmpty()) {
                this.veces = Integer.parseInt(numerico);
            }

        } catch (NumberFormatException e) {
            this.veces = 1; //Prevencion de excepcion
        }

    }

    //Metodo que retorna las veces que se va a repetir el emoji
    public int getVeces() {
        return this.veces;
    }

    //Metodo que retorna el valor del emoji
    @Override
    public String getStringCompiled() {

        if(this.tipoEmoji == TipoEmoji.MULTI_STAR){
            return TipoEmoji.STAR.toString();
        }

        return tipoEmoji.toString();
    }
}
