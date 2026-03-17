package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que permite evaluar los valores literales (numericos y string)
public class NodoLiteral extends NodoExpresion {

    //Atributos
    private Object valor;

    public NodoLiteral(Object valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        if (this.valor instanceof Number) {
            return TipoVariable.NUMBER;
        }

        if (this.valor instanceof String) {
            return TipoVariable.STRING;
        }
        return TipoVariable.ERROR;
    }

    //Metodo que permite contar los comodines que tiene la expresion
    @Override
    public int contarComodines() {
        return 0;
    }

    //Metodo que permite retornar el valor (no es necesario acceder a la tabla de valores ya que son valores puros)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return this.valor;
    }

    //Metodo que permite clonar la expresion
    @Override
    public NodoExpresion clonar() {
        return new NodoLiteral(this.valor, getLinea(), getColumna());
    }

    //Metodo que permite buscar comodines de forma recursiva en las expresiones
    @Override
    public void buscarComodines(List<NodoComodin> listaComodines){
    }

    //Metodo que permite obtener el valor como cadena de texto
    @Override
    public String getString() {
        return this.valor.toString();
    }
}
