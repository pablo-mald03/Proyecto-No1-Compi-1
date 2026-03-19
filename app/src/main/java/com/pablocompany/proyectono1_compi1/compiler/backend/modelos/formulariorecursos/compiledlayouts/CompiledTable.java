package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.compiledlayouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos.CompiledForm;

import java.util.ArrayList;
import java.util.List;

//Clase que representa a la tabla que es contenedora de codigo
public class CompiledTable extends CompiledContenedor {

    private List<List<CompiledForm>> filas;


    public CompiledTable(Number width, Number height, Number pointX, Number pointY, List<List<CompiledForm>> filas, CompiledEstilos estilos) {
        super(width, height, pointX, pointY, estilos);
        this.filas = filas;
    }

    /*Metodos getters y setters que permiten obtener la lista de componentes*/
    private List<List<CompiledForm>> getElementos() {
        return this.filas;
    }

    /*Metodo que permite calcular el total de columas que tendra el layout*/
    public int getTotalColumnas(){

        int max = 0;
        for (List<CompiledForm> fila : filas) {
            if (fila.size() > max) {
                max = fila.size();
            }
        }
        return max;

    }


}
