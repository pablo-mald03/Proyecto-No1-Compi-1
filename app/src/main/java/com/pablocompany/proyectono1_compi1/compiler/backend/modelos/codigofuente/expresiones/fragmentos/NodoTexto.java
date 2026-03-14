package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a un texto dentro de una cadena de texto que puede irt en conjunto a emojis
public class NodoTexto extends NodoFragmento{

    //Atributos
    private String contenido;

    public NodoTexto(String contenido,int linea, int columna) {
        super(linea, columna);
        this.contenido = contenido;
    }

    //Metodo que permite validar semantica del texto contenido
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return TipoVariable.STRING;
    }

    //Metodo que permite ejecutarse propio de la jerarquia pero en este caso retorna solo su propio valor object
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this.contenido;
    }

    //Metodo que permite obtener como texto el contenido de la cadena
    @Override
    public String getString() {
        return this.contenido;
    }
}
/*Created by P*/