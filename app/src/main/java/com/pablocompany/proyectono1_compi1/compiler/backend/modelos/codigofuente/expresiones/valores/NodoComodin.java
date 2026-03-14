package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que permite representar un comodin en el lenguaje que es sustituible por otro valor
public class NodoComodin extends NodoExpresion {

    //Atributo que representa la incongita de la expresion
    private NodoExpresion expresion;

    //Constructor
    public NodoComodin(int linea, int columna) {
        super(linea, columna);
        this.expresion = null;
    }

    //Metodo que permite validar semantica del lenguaje generado cuando se encuentra un comodin
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return TipoVariable.COMODIN;
    }

    //Metodo que permite ejecutar el valor que este adopta (Pendiente)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        if(this.expresion == null){
            return  null;
        }
        //Codigo pendiente

        return null;
    }

    //Metodo set utilizado para reemplazar el valor de la expresion incognita
    public void darValorIncognita(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    //Retorna su valor string (comodin)
    @Override
    public String getString() {
        return "?";
    }

    //Retorna uno porque si es un comodin
    @Override
    public int contarComodines() {
        return 1;
    }


}
