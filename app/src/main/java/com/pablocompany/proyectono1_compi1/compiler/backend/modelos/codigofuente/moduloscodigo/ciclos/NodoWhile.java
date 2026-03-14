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
        TipoVariable tipoCond = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCond != TipoVariable.NUMBER && tipoCond != TipoVariable.COMODIN && tipoCond != TipoVariable.ERROR) {
            listaErrores.add(new ErrorAnalisis("WHILE", "Semantico",
                    "La condición del ciclo WHILE debe ser una expresion numerica o logica.",
                    getLinea(), getColumna()));
        }

        TablaSimbolos tablaHija = new TablaSimbolos(tabla);

        if (codigo != null) {
            for (Nodo nodo : codigo) {
                if (nodo != null) {
                    nodo.validarSemantica(tablaHija, listaErrores);
                }
            }
        }

        return TipoVariable.VOID;

    }

    //Pendiente definir el metodo ejecutar

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
