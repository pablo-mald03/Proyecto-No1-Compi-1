package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.expresiones.NodoExpresion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase padre que define a los estilos que puede tener en general componentes del formulario
public class NodoEstilos extends Nodo {

    private TipoEstilo tipoEstilo;

    private NodoExpresion valor;

    public NodoEstilos(TipoEstilo tipoEstilo, NodoExpresion valor,int linea, int columna) {
        super(linea, columna);
        this.tipoEstilo = tipoEstilo;
        this.valor = valor;
    }

    // Getters para que el NodoOpenQuestion pueda leerlos
    public TipoEstilo getTipo() { return tipoEstilo; }
    public NodoExpresion getValor() { return valor; }


    //Metodo que permite ejecutar el valor que debe tener el estilo (pendiente validar el analisis sintactico)
    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return valor.ejecutar(tabla, listaErrores);
    }

    //Metodo que permite retornar el valor en String
    @Override
    public String getString() {
        return ": " + valor.getString();
    }
}
