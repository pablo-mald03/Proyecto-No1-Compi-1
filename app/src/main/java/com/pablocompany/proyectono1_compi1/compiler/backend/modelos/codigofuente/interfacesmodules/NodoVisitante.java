package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Interface que permite implementar el patron Visitor
/*Esta Interface permite generar las pasadas */
public interface NodoVisitante {

    //Metodo que permite ejecutar los draws en las funciones (PRIMERA PASADA)
    void ejecutarDraws(TablaSimbolos tabla, List<ErrorAnalisis> errores);
}
