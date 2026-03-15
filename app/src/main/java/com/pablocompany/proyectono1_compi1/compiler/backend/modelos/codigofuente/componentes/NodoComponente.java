package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoOptions;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.funcionesespeciales.NodoFuncionPokemon;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Superclase que define a los componentse visuales que vengan en el codigo fuente
public abstract class NodoComponente extends Nodo {

    protected NodoWidth width;
    protected NodoHeight height;

    //Atibuto especial que trae toda su configuracion de estilos
    protected Estilos estilos;

    public NodoComponente( NodoWidth width, NodoHeight height, Estilos estilos, int linea, int columna) {
        super(linea, columna);
        this.width = width;
        this.height = height;
        this.estilos = estilos;
    }

    //Metodo abstracto para poder implementar los estilos en su demas jerarquia
    protected abstract Estilos procesarEstilos(List<NodoEstilos> lista);



}
/*Created by Pablo*/