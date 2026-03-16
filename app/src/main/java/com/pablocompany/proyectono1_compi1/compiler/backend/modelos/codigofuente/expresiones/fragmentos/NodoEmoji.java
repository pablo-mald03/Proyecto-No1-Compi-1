package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa un emoji que puede ir encadenado a una cadena de texto
public class NodoEmoji extends NodoFragmento {

    //Atributos
    private TipoEmoji tipo;
    private String valorTexto;
    private int cantidad = 1;

    public NodoEmoji(TipoEmoji tipo, String valorTexto, int linea, int columna) {
        super(linea, columna);
        this.valorTexto = valorTexto;
        this.tipo = tipo;
        this.reconocerEmoji(tipo);

    }

    //Metodo que permite validar semantica del valor que tiene la cadena
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return TipoVariable.STRING;
    }

    //Metodo propio de la clase polimorfica (La ER utilizada no tiene ningun efecto en tiempo de PARSEO)
    private void reconocerEmoji(TipoEmoji tipo){

        if (tipo != TipoEmoji.MULTI_STAR) {
            return;
        }
        try {
            //ER UTILIZADA SOLO PARA JALAR EL VALOR (NO TIENE NINGUN OTRO CONTEXTO EN TIEMPO DE EJCUCION)

            String numerico = valorTexto.replaceAll("[^0-9]", "");

            if (!numerico.isEmpty()) {
                this.cantidad = Integer.parseInt(numerico);
            }

        } catch (NumberFormatException e) {
            this.cantidad = 1; //Prevencion de excepcion
        }

    }
    /*Created by P*/
    //Metodo que retorna el tipo de emoji
    public TipoEmoji getTipo() {

        return this.tipo;
    }
    //Metodo que retorna la cantidad que se repetira el emoji
    public int getCantidad() {
        return cantidad;
    }

    //Se retorna a si mismo para poderlo reconocerlo por su superClase NodoCadena (PENDIENTE)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        return this;
    }

    //Metodo que retorna el valor del emoji (como cadena de texto. Tambien contiempla la cantidad de veces que retornara el emoji multiEstrella)
    @Override
    public String getString() {

        if (tipo == TipoEmoji.MULTI_STAR) {
            return "@[:star:]".repeat(this.cantidad);
        }

        return this.valorTexto;

    }
}
/*Created by Pablo*/