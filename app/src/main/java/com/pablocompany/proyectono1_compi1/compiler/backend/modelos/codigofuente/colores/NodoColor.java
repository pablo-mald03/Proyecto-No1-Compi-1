package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.colores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa un color definido por el usuario. Es decir que represena a la JERARQUIA PRINCIPAL DE COLORES
public abstract class NodoColor {

    //Metodo principal que retorna el color en formato RGB (util en frontend)
    public abstract int [] evaluarColor(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores);

}

/*Created by P*/

