package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a variables que estan en la declaracion o asignacion de las demas variables
public class NodoAccesoVariable extends NodoExpresion {

    //Atributos
    private String id;

    public NodoAccesoVariable(String id,int linea, int columna) {
        super(linea, columna);
        this.id = id;
    }

    /*Nodo que permite validar la semantica del lenguaje generado*/
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo simbolo = tabla.buscar(this.id);
        if (simbolo == null) {
            listaErrores.add(new ErrorAnalisis(id, "Semántico", "Variable \""+this.id+"\" no declarada", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        return simbolo.getTipo();
    }


    //Metodo que permite contar los comodines que tiene la expresion
    @Override
    public int contarComodines() {
        return 0;
    }

    //Metodo que permite retornar el valor (busca en la tabla de simbolos el valor)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        Simbolo simbolo = tabla.buscar(this.id);
        if (simbolo == null) {
            listaErrores.add(new ErrorAnalisis(this.id, "Semántico", "Variable no definida", super.getLinea(), super.getColumna()));
            return null;
        }

        return simbolo.getValor();
    }

    //Metodo que permite obtener el valor como cadena de texto
    @Override
    public String getString() {
        return this.id;
    }
}
