package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al ciclo do while dentro del codigo fuente
public class NodoDoWhile extends Nodo {

    //Atributos
    private Nodo condicion;
    private List<Nodo> codigo;

    public NodoDoWhile( Nodo condicion, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.codigo = codigo;
    }

    //Metodo que permite validar semantica del ciclo do while (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        /*Tabla que permite validar variables locales*/
        TablaSimbolos tablaHija = new TablaSimbolos(tabla);

        if (codigo != null) {
            for (Nodo n : codigo) {
                if (n != null) {
                    n.validarSemantica(tablaHija, listaErrores);
                }
            }
        }

        TipoVariable tipoCond = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCond != TipoVariable.NUMBER
                && tipoCond != TipoVariable.COMODIN
                && tipoCond != TipoVariable.ERROR
                && tipoCond != TipoVariable.BOOLEAN_AND
                && tipoCond != TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("DO-WHILE", "Semantico",
                    "La condición del ciclo DO-WHILE debe ser numerica o logica.",
                    getLinea(), getColumna()));
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
