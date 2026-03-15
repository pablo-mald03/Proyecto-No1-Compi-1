package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.TipoVariable;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Interface delegada para poder implementar el comportamiento de validar datos de los formularios sin obligar a que lo tengan todos
public interface ValidarDatosForms {

    TipoVariable validarSemantica(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores, boolean esLayout);
}
