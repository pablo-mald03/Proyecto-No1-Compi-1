package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores.NodoColor;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.TipoBorde;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Nodo que permite definir la configuracion de estilos del borde de un layout
public class NodoBorder extends Nodo {

    //Atributos
    private TipoBorde tipoBorde;
    private NodoColor color;
    private NodoExpresion grosor;


    public NodoBorder( NodoExpresion grosor, String tipoBorde, NodoColor color, int linea, int columna) {
        super(linea, columna);

        try {
            this.tipoBorde = TipoBorde.valueOf(tipoBorde.toUpperCase());
        }catch (Exception ex){
            this.tipoBorde = TipoBorde.NOT_FOUND;
        }
        this.color = color;
        this.grosor = grosor;
    }

    //Metodo que permite validar semantica del borde (PATRON EXPERTO)
    @Override
    public TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {

        if (this.tipoBorde == TipoBorde.NOT_FOUND) {
            listaErrores.add(new ErrorAnalisis(
                    "border", "Semantico",
                    "El estilo del borde proporcionado no es valido.",
                    getLinea(), getColumna()
            ));
        }

        if (grosor != null) {
            TipoVariable tipoGrosor = grosor.validarSemantica(tabla, listaErrores);
            if (tipoGrosor != TipoVariable.NUMBER && tipoGrosor != TipoVariable.ERROR) {
                listaErrores.add(new ErrorAnalisis(
                        "border", "Semantico",
                        "El grosor del borde debe ser numerico. Se encontro con una expresion tipo: \"" + tipoGrosor.getTipo()+ "\"",
                        getLinea(), getColumna()
                ));
            }
        }

        if (color == null) {
            listaErrores.add(new ErrorAnalisis(
                    "color", "Semantico",
                    "El color del borde no debe estar vacio." ,
                    getLinea(), getColumna()
            ));
        }

        return TipoVariable.VOID;
    }

    //Metodo que permite ejecutar la expresion que esta dentro del nodo de configuracion (PENDIENTE)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    //Metodo que retorna la configuracion que es
    @Override
    public String getString() {
        return "border: ";// + this.expresion.getString();
    }

}
