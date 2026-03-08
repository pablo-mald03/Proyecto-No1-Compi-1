package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa un emoji que puede ir encadenado a una cadena de texto
public class NodoEmoji extends NodoFragmento {

    //Atributos
    private TipoEmoji tipo;
    private String valorTexto;

    public NodoEmoji(TipoEmoji tipo, String valorTexto, int linea, int columna) {
        super(linea, columna);
        this.valorTexto = valorTexto;
        this.tipo = tipo;
    }

    //Metodo que retorna el tipo de emoji
    public TipoEmoji getTipo() {

        return this.tipo;
    }

    //Se retorna a si mismo para poderlo reconocerlo por su superClase NodoCadena
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        return this;
    }

    //Metodo que retorna el valor del emoji (como cadena de texto)
    @Override
    public String getString() {
        return this.valorTexto;
    }
}
