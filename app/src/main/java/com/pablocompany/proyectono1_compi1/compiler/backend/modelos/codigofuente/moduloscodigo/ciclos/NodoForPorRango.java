package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.Simbolo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al ciclo for pero modificado. Tal como el que tiene kotlin de un rango a hacia b
public class NodoForPorRango extends Nodo {

    //Atributos
    private String id;
    private NodoExpresion rangoInicial;
    private NodoExpresion rangoFinal;

    //Codigo dentro del ciclo
    private List<Nodo> codigo;

    public NodoForPorRango( String id, NodoExpresion rangoInicial,NodoExpresion rangoFinal, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.id = id;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.codigo = codigo;
    }

    //Metodo que permite ejecutar el ciclo for estilo kotlin (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que esto lo unico que hace es verificar si el iterador existe fuera del ciclo
     *
     * Lo que me da como resultado que la variable local (A no ser de que este en ambito global) QUE PERMANECE Y SERA SOBREESCRITA SIEMPRE QUE SE LLAME
     * PERO ESTO SOLO PASA CON LA CONDICION DEL FOR.
     * */
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        Simbolo simboloExistente = tabla.buscar(id);

        if (simboloExistente != null) {
            if(simboloExistente.getTipo() != TipoVariable.NUMBER) {
                listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                        "La variable \"" + id + "\" no es de tipo \"number\".",
                        getLinea(), getColumna()));
                return TipoVariable.ERROR;
            }
            tabla.insertar(simboloExistente);
        }else{
            tabla.insertar(new Simbolo(id, TipoVariable.NUMBER, 0.0, getLinea(), getColumna()));

        }

        TipoVariable TipoInicio = rangoInicial.validarSemantica(tabla, listaErrores);
        TipoVariable TipoFinal = rangoFinal.validarSemantica(tabla, listaErrores);

        if (TipoInicio != TipoVariable.NUMBER || TipoFinal != TipoVariable.NUMBER) {
            listaErrores.add(new ErrorAnalisis("FOR", "Semantico",
                    "Los valores del rango deben ser de tipo \"number\".",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }

        for(Nodo nodo : codigo) {
            if (nodo == null) {
                continue;
            }
            nodo.validarSemantica(tabla, listaErrores);
        }

        return TipoVariable.VOID;
    }


    //Metodo que permite ejecutar el ciclo for (PATRON EXPERTO)
    /*
     *
     * Es importante destacar que se aplica la misma tecnica (debido a que esto es una ejecucion despues de haber hecho el analisis semantico)
     * La garantia es que el iterador ya existe o ya fue declarado anteriormente en la tabla de simbolos ya solo se utiliza y queda
     * para siempre en el ambito general (no se manejan ambitos internos)
     *
     * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        double inicio = ((Number) rangoInicial.ejecutar(tabla, listaErrores)).doubleValue();
        double fin = ((Number) rangoFinal.ejecutar(tabla, listaErrores)).doubleValue();

        tabla.insertar(new Simbolo(id, TipoVariable.NUMBER, inicio, getLinea(), getColumna()));

        double i = inicio;

        while (i <= fin) {
            tabla.asignar(id, i, listaErrores);
            for(Nodo nodo : codigo) {
                nodo.ejecutar(tabla, listaErrores);
            }
            i = ((Number) tabla.buscar(id).getValor()).doubleValue() + 1;
        }
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
