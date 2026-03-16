package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.NodoFragmento;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.NodoTexto;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoCadenaCompuesta;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
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

        return TipoVariable.POKEMON;
    }

    /*--Metodos getter para retornar los atributos de la funcion pokemon---*/
    public NodoExpresion getOffset() {
        return this.offset;
    }
    public NodoExpresion getLimit() {
        return this.limit;
    }

    /*--Metodos getter para setear los atributos de la funcion pokemon---*/
    public int setOffset(List<NodoComodin> comodines, int iterador) {
        if (iterador < comodines.size() && this.offset instanceof NodoComodin) {
            NodoComodin comodin = (NodoComodin) this.offset;

            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }
    public int setLimit(List<NodoComodin> comodines, int iterador) {
        if (iterador < comodines.size() && this.limit instanceof NodoComodin) {
            NodoComodin comodin = (NodoComodin) this.limit;

            if (comodin.getExpresion() == null) {
                comodin.darValorIncognita(comodines.get(iterador).getExpresion());
                iterador++;
            }
        }
        return iterador;
    }

    //Metodo que permite ejecutar el request de a la API para poder obtener los pokemon
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        int valOffset = 0;
        int valLimit = 1;

        Object off = (this.offset != null) ? this.offset.ejecutar(tabla, listaErrores) : 0;

        if (off instanceof OnCompilacionError) return off;

        Object lim = (this.limit != null) ? this.limit.ejecutar(tabla, listaErrores) : 1;

        if (lim instanceof OnCompilacionError) return lim;

        if (off instanceof Number) valOffset = ((Number) off).intValue();
        if (lim instanceof Number) valLimit = ((Number) lim).intValue();

        PokeApiService pokeApiService = new PokeApiService();

        List<String> nombresPokemon = pokeApiService.getPokemones(valOffset, valLimit, listaErrores, getLinea(), getColumna(), this.getString());

        if (nombresPokemon == null) {
            OnCompilacionError errorAPI = new OnCompilacionError("Fallo en la llamada a la API", getLinea(), getColumna(), false);
            errorAPI.reportar(listaErrores, this.getString());
            return errorAPI;
        }

        return  convertirNodos(nombresPokemon);
    }

    private List<Nodo> convertirNodos(List<String> nombres) {
        List<Nodo> listaNodos = new ArrayList<>();
        for (String nombre : nombres) {

            NodoTexto nodoTexto = new NodoTexto(nombre, getLinea(), getColumna());
            List<NodoFragmento> fragmentos = new ArrayList<>();
            fragmentos.add(nodoTexto);
            listaNodos.add(new NodoCadenaCompuesta(fragmentos, getLinea(), getColumna()));
        }

        if(nombres.isEmpty()){
            List<NodoFragmento> fragmentos = new ArrayList<>();
            fragmentos.add(new NodoTexto("", getLinea(), getColumna()));
            listaNodos.add(new NodoCadenaCompuesta(fragmentos, getLinea(), getColumna()));
        }

        return listaNodos;
    }

    @Override
    public String getString() {
        return "who_is_that_pokemon(NUMBER,"+ this.offset.getString() + "," + this.limit.getString() + ")";
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
