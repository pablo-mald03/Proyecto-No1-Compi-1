package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa a la funcion especial para poder generar un requesta  la API DE POKEMON
public class NodoFuncionPokemon extends Nodo {

    //Atributos
    private NodoExpresion limit;
    private NodoExpresion offset;

    public NodoFuncionPokemon(NodoExpresion offset,NodoExpresion limit,int linea, int columna) {
        super(linea, columna);
        this.limit = limit;
        this.offset = offset;
    }

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Metodo que permite ejecutar el request de a la API para poder obtener los pokemon
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
