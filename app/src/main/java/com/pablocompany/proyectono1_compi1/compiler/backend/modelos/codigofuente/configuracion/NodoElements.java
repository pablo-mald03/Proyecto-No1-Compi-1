package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa todos los elementos que pueden ir dentro de los dos tipos de layouts (o incluso mismos layouts dentro de otros)
public class NodoElements extends Nodo {

    //Atributos
    private List<NodoComponente> expresion;

    public NodoElements(List<NodoComponente> expresion, int linea, int columna) {
        super(linea, columna);
        this.expresion = expresion;
    }

    //Metodo que permite validar semantica de los elementos (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        if (this.expresion != null) {

            for (NodoComponente nodo : this.expresion) {
                if (nodo != null) {

                    nodo.validarSemantica(tabla, listaErrores);
                }
            }
        }

        return TipoVariable.VOID;
    }

    /*---Metodo que permite ejecutar los draws en las preguntas (PRIMERA PASADA)---*/
    @Override
    public void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores) {

        if (this.expresion != null) {
            for (Nodo n : this.expresion) {
                n.ejecutarDraws(tabla, errores);
            }
        }
    }

    //Metodo que permite ejecutar y retornar el listado de todas las preguntas o elementos que tiene dentro
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "height: "; //+ this.expresion.getString();
    }
}

/*Created by Pablo*/
