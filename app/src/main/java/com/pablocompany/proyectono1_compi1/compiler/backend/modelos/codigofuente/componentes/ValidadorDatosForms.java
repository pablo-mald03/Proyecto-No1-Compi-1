package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Interface creada para poder aplicar el comportamiento directo de validar los datos de los nodos que representan una definicion en la UI
public interface ValidadorDatosForms {

        TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout);
}
