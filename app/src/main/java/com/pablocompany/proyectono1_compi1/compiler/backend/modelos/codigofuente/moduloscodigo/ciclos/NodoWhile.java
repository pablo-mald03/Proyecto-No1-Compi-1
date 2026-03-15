package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al ciclo while dentro del codigo fuente
public class NodoWhile extends Nodo {

    //Atributos
    private Nodo condicion;
    private List<Nodo> codigo;

    public NodoWhile(Nodo condicion, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.codigo = codigo;
    }

    //Metodo que permite validar semantica del ciclo while (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        /*Tabla que permite validar variables locales*/
        TipoVariable tipoCondicion = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCondicion != TipoVariable.NUMBER
                && tipoCondicion != TipoVariable.COMODIN
                && tipoCondicion != TipoVariable.ERROR
                && tipoCondicion != TipoVariable.BOOLEAN_AND
                && tipoCondicion != TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("WHILE", "Semantico",
                    "La condición del ciclo WHILE debe ser una expresion numerica o logica.",
                    getLinea(), getColumna()));
        }

        if (codigo != null) {
            for (Nodo nodo : codigo) {
                if (nodo != null) {
                    nodo.validarSemantica(tabla, listaErrores);
                }
            }
        }

        return TipoVariable.VOID;

    }

    //Metodo que permite ejecutar el ciclo while (PATRON EXPERTO)
    /*
     *Solo se convierte a un ciclo while normal (PENDIENTE DEFNIR RETORNO)
     */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {


        while (true) {
            Object valorCondicion = condicion.ejecutar(tabla, listaErrores);

            if (!(valorCondicion instanceof Number) || ((Number) valorCondicion).doubleValue() <= 0.0) {
                break;
            }

            if (codigo != null) {
                for (Nodo nodo : codigo) {
                    if (nodo != null) {
                        nodo.ejecutar(tabla, listaErrores);
                    }
                }
            }
        }

        return null;
    }

    /*---Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)---*/
    @Override
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if (this.codigo != null) {
            for (Nodo nodo : this.codigo) {
                nodo.ejecutarDraws(tabla, errores);
            }
        }

    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
