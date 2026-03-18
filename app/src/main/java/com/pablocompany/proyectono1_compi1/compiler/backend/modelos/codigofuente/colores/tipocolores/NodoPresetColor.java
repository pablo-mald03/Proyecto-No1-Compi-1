package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.tipocolores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa los colores que estan como preset del lenguaje
public class NodoPresetColor extends NodoColor {

    //Atributos
    private TipoColor colorPreset;

    public NodoPresetColor(String colorPreset, int linea, int columna) {
        super(linea, columna);
        try {
            this.colorPreset = TipoColor.valueOf(colorPreset.toUpperCase());
        }catch (Exception e){
            this.colorPreset = TipoColor.NOT_FOUND;
        }

    }

    //Metodo que permite validar las expresiones dentro de un color
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout) {
        if (this.colorPreset == TipoColor.NOT_FOUND) {
            listaErrores.add(new ErrorAnalisis("COLOR PRESET", "Semantico",
                    "El color \"PRESET\" proporcionado no es valido.",
                    getLinea(), getColumna()));
            return TipoVariable.ERROR;
        }
        return TipoVariable.COLOR;
    }

    //Metodo que permite ejecutar y retornar el valor del color
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores){
        return this.colorPreset.toString();
    }

    /*Metodos getter de las expresiones*/



    //Metodo que permite clonar el color
    @Override
    public NodoColor clonar() {
        return new NodoPresetColor(this.colorPreset.toString(), getLinea(), getColumna());
    }


    //Metodo que permite retornar los valores del color en formato String
    @Override
    public  String getString(){
        return this.colorPreset.toString();
    }

    /*--Metodo propio de la clase que permite contar los comodines que tienen en el color PRESET--*/
    public  int contarComodines(){
        return 0;
    }
}
