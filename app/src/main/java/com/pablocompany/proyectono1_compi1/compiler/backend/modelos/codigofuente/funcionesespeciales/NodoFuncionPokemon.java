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

    //Metodo que permite validar semantica de la funcion pokemon para poder hacer el request (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        if (this.offset != null) {
            TipoVariable tipoOffset = offset.validarSemantica(tabla, listaErrores);
            if (tipoOffset != TipoVariable.NUMBER && tipoOffset != TipoVariable.COMODIN && tipoOffset != TipoVariable.ERROR) {
                listaErrores.add(new ErrorAnalisis("who_is_that_pokemon", "Semántico",
                        "El parámetro \"offset\" debe ser una expresion numerica.", getLinea(), getColumna()));
            }
        }

        if (this.limit != null) {
            TipoVariable tipoLimit = limit.validarSemantica(tabla, listaErrores);
            if (tipoLimit != TipoVariable.NUMBER && tipoLimit != TipoVariable.COMODIN && tipoLimit != TipoVariable.ERROR) {
                listaErrores.add(new ErrorAnalisis("who_is_that_pokemon", "Semántico",
                        "El parámetro \"limit\" debe ser una expresion numerica.", getLinea(), getColumna()));
            }
        }

        return TipoVariable.VOID;
    }

    /*--Metodos getter para retornar los atributos de la funcion pokemon---*/
    public NodoExpresion getOffset() {
        return this.offset;
    }
    public NodoExpresion getLimit() {
        return this.limit;
    }

    //Metodo que permite ejecutar el request de a la API para poder obtener los pokemon
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "wh";
    }

    /*--Metodo delegado para poder contar los comodines de la funcion--*/
    public int contarComodines() {
        int contador = 0;
        if (this.offset != null) {
            contador += this.offset.contarComodines();
        }
        if (this.limit != null) {
            contador += this.limit.contarComodines();
        }
        return contador;
    }
}
