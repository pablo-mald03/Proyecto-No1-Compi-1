package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.moduloscodigo.ciclos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa al ciclo do while dentro del codigo fuente
public class NodoDoWhile extends Nodo {

    //Atributos
    private Nodo condicion;
    private List<Nodo> codigo;

    public NodoDoWhile(Nodo condicion, List<Nodo> codigo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.codigo = codigo;
    }

    //Metodo que permite validar semantica del ciclo do while (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        if (codigo != null) {
            for (Nodo nodo : codigo) {
                if (nodo != null) {
                    nodo.validarSemantica(tabla, listaErrores);
                }
            }
        }

        TipoVariable tipoCondicion = condicion.validarSemantica(tabla, listaErrores);

        if (tipoCondicion != TipoVariable.NUMBER
                && tipoCondicion != TipoVariable.COMODIN
                && tipoCondicion != TipoVariable.ERROR
                && tipoCondicion != TipoVariable.BOOLEAN_AND
                && tipoCondicion != TipoVariable.BOOLEAN_OR) {
            listaErrores.add(new ErrorAnalisis("DO-WHILE", "Semantico",
                    "La condición del ciclo DO-WHILE debe ser numerica o logica.",
                    getLinea(), getColumna()));
        }

        return TipoVariable.VOID;
    }


    //Metodo que permite ejecutar el ciclo do while (PATRON EXPERTO)
    /*
    *Solo pasa a un ciclo do while normal  de paso se hace infinito si la condicion nunca se cumple (PENDIENTE DEFNIR RETORNO)
    * */
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        do {

            if (codigo != null) {
                for (Nodo nodo : codigo) {
                    if (nodo != null) {
                        nodo.ejecutar(tabla, listaErrores);
                    }
                }
            }

            Object valorCondicion = condicion.ejecutar(tabla, listaErrores);

            if(!(valorCondicion instanceof  Number) || ((Number) valorCondicion).doubleValue() <= 0.0){
                break;
            }
        } while (true);

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

    /*-----Metodo que permite ejecutar los requests hacia la pokeAPI en las preguntas (SEGUNDA PASADA)-----*/
    @Override
    public void ejecutarRequests(TablaSimbolos tabla, List<ErrorAnalisis> errores) {
        if (this.codigo != null) {
            for (Nodo nodo : this.codigo) {
                nodo.ejecutarRequests(tabla, errores);
            }
        }
    }

    @Override
    public String getString() {
        return "";
    }
}
/*Created by Pablo*/
