package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores.NodoComodin;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase que representa las opciones de configuracion de una pregunta
public class NodoOptions extends Nodo {

    //Atributos
    private List<Nodo> opciones;

    public NodoOptions(List<Nodo> opciones, int linea, int columna) {
        super(linea, columna);
        this.opciones = opciones;
    }

    //Metodo que permite validar semantica del lenguaje generado (PENDIENTE)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.opciones == null || this.opciones.isEmpty()) {
            listaErrores.add(new ErrorAnalisis("options", "Semantico",
                    "El atributo de opciones no puede estar vacío.", getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        for (Nodo nodo : this.opciones) {
            if (nodo == null) continue;

            TipoVariable tipoActual = nodo.validarSemantica(tabla, listaErrores);

            if (tipoActual == null || tipoActual == TipoVariable.ERROR || tipoActual == TipoVariable.COMODIN) {
                continue;
            }

            if (tipoActual != TipoVariable.STRING && tipoActual != TipoVariable.POKEMON) {
                listaErrores.add(new ErrorAnalisis((this.opciones!=null)? this.getString():"options", "Semantico",
                        "Las opciones solo aceptan tipo \"string\". Se encontro con una expresion tipo: \"" +
                                tipoActual + "\"", getLinea(), getColumna()));
            }
        }

        return TipoVariable.STRING;
    }

    /*Metodo getter que retorna la lista de opciones */
    public List<Nodo> getOpciones() {
        return this.opciones;
    }

    /*---Metodo delegado para poder pasar los parametros a las funciones dentro de las preguntas y asignarlos a las opciones----*/
    public int setOpciones(List<NodoComodin> comodines, int iterador) {
        for (int i = 0; i < this.opciones.size(); i++) {
            Nodo nodo = this.opciones.get(i);

            if (nodo instanceof NodoComodin) {
                NodoComodin comodin = (NodoComodin) nodo;
                if (comodin.getExpresion() == null && iterador < comodines.size()) {
                    comodin.darValorIncognita(comodines.get(iterador).getExpresion());

                    iterador++;
                }
            }
        }
        return iterador;
    }
    /*--Metodo que permite obtener la lista de opciones--*/
    public List<Nodo> getListaOpciones(){
        return this.opciones;
    }

    /*Metodo que permite setear la la lista de opciones */
    public  void setLista(List<Nodo> lista){
        this.opciones = lista;
    }

    //Metodo que permite ejecutar la lista de opciones de expresion que esta dentro del nodo de configuracion
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        List<String> listaResultados = new ArrayList<>();

        for (Nodo objeto : this.opciones) {

            Object resultado = objeto.ejecutar(tabla, listaErrores);

            if (resultado instanceof OnCompilacionError) return resultado;

            if (resultado != null) {
                listaResultados.add(resultado.toString());
            }
        }

        return listaResultados;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {

        if (this.opciones == null || this.opciones.isEmpty()) {
            return "options: {}";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.opciones.size(); i++) {
            builder.append(this.opciones.get(i).getString());

            if (i < this.opciones.size() - 1) {
                builder.append(", ");
            }
        }

        return "options: {" + builder + "}";
    }

    /*---Metodo propio de la clase que permite contar los comodines que tienen en las opciones---*/
    public int contarComodines() {

        int contadorComodines = 0;

        for (Nodo nodo : this.opciones) {
            if (nodo == null) {
                continue;
            }
            if (nodo instanceof NodoExpresion) {
                NodoExpresion expresion = (NodoExpresion) nodo;
                contadorComodines += expresion.contarComodines();
            }
        }
        return contadorComodines;
    }

   //Metodo que permite clonar a una instancia de clase nodo options
    public NodoOptions clonar() {
        List<Nodo> nuevasOpciones = new ArrayList<>();

        if (this.opciones != null) {
            for (Nodo opcion : this.opciones) {
                if (opcion instanceof NodoExpresion) {
                    nuevasOpciones.add(((NodoExpresion) opcion).clonar());
                } else {
                    nuevasOpciones.add(opcion);
                }
            }
        }

        return new NodoOptions(nuevasOpciones, getLinea(), getColumna());
    }

    //Metodo que permite buscar los comodines dentro de la expresion
    @Override
    public void buscarComodines(List<NodoComodin> listaComodines) {

        if (this.opciones != null) {
            for (Nodo opcion : opciones) {
                if (opcion != null) {
                    opcion.buscarComodines(listaComodines);
                }
            }
        }
    }

}

