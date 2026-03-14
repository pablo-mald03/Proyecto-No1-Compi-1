package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.valores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.fragmentos.NodoFragmento;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa una secuencia de cadenas de texto en conjungo a emojis o solo textos
public class NodoCadenaCompuesta extends NodoExpresion {

    //Atributos
    private List<NodoFragmento> fragmentos;

    public NodoCadenaCompuesta(List<NodoFragmento> fragmentos,int linea, int columna) {
        super(linea, columna);
        this.fragmentos = fragmentos;
    }

    //Metodo que permite validar semantica del lenguaje generado
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        for (NodoFragmento f : fragmentos) {
            TipoVariable tipo = f.validarSemantica(tabla, listaErrores);

            if (tipo == TipoVariable.ERROR) {
                return TipoVariable.ERROR;
            }
        }
        return null;
    }

    //No pueden contener comodines
    @Override
    public int contarComodines() {
        return 0;
    }

    //Metodo que permite retornar las cadenas que tienen dentro
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        StringBuilder stringBuilder = new StringBuilder();
        for (NodoFragmento f : fragmentos) {
            stringBuilder.append(f.getString());
        }
        return stringBuilder.toString();
    }

    //Permite retornar el valor del texto
    @Override
    public String getString() {
        return ejecutar(null,null).toString();
    }
}
