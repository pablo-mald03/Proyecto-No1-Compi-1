package com.pablocompany.proyectono1_compi1.compiler.backend.gestores;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.NodoCodigo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.analizadorsemantico.AnalizadorSemantico;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase delegada para poder retornar el codigo compilado que genera el programa (PATRON EXPERTO)
public class GestorCodigoCompilado {

    //Atributos
    private NodoCodigo codigoParser;
    private List<ErrorAnalisis> listadoErrores;
    private AnalizadorSemantico analizadorSemantico;

    //Constructor
    public GestorCodigoCompilado(NodoCodigo codigoParser, List<ErrorAnalisis> listadoErrores) {

        this.codigoParser = codigoParser;
        this.listadoErrores = listadoErrores;

        if(this.listadoErrores == null){
            this.listadoErrores = new ArrayList<>();
        }

    }

    //Metodo delegado por completo para poder retornar el texto de codigo compilado
    public String obtenerCodigoCompilado(){

        if(codigoParser == null){
            return "";
        }
        this.analizadorSemantico = new AnalizadorSemantico(this.listadoErrores,this.codigoParser.getLista()) ;
        return this.analizadorSemantico.codigoCompilado();
    }

    //Metodo que permite retornar la lista de errores encontrados
    public List<ErrorAnalisis> getListadoErrores() {

        if(this.analizadorSemantico == null){
            return this.listadoErrores;
        }

        return this.analizadorSemantico.getListadoErroresTotal();
    }


}
/*Created by Pablo*/
