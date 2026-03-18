package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

import java.util.List;

//Clase que representa como contenedor principal al codigo interpretado que retorna el AST del interprete
public class CodigoInterpretado {

    private List<CompiledForm> codigo;

    public CodigoInterpretado(List<CompiledForm> codigo) {
        this.codigo = codigo;
    }

    //Metodo que retorna la lista de componentes interpretados
    public List<CompiledForm> getCodigo() {
        return codigo;
    }
}
