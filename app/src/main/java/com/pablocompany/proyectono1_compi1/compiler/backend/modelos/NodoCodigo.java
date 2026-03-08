package com.pablocompany.proyectono1_compi1.compiler.backend.modelos;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;

import java.util.List;

//Clase padre que permite manejar el AST que retorna el Parser al interactuar
public class NodoCodigo {

    //Atributos
    private List<Nodo> lista;

    public NodoCodigo(List<Nodo> lista) {
        this.lista = lista;
    }

    //Metodo que permite retornar la lista de nodos
    public List<Nodo> getLista() {
        return lista;
    }
    /*Created by Pablo*/
}
