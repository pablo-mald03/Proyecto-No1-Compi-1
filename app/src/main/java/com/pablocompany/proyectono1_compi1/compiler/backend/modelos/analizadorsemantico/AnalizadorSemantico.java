package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.analizadorsemantico;

import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.OnCompilacionError;
import com.pablocompany.proyectono1_compi1.compiler.backend.exceptions.TiempoEjecucionException;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.Nodo;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.interfacesmodules.NodoVisitante;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.questions.NodoQuestion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.variables.NodoDraw;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.ArrayList;
import java.util.List;

//Clase delegada para ocuparse por completo del analisis semantico (PATRON EXPERTO)
public class AnalizadorSemantico {

    //Atributo que representa los errores encontrados en el analisis semantico
    private List<ErrorAnalisis> listadoErroresTotal;

    //Atributo que representa al AST que formo el analizador sintactico
    private List<Nodo> astParser;

    //Constructor que recibe el ast y la lista de errores actuales
    public AnalizadorSemantico(List<ErrorAnalisis> listadoErroresTotal, List<Nodo> astParser) {
        this.listadoErroresTotal = listadoErroresTotal;
        this.astParser = astParser;
    }

    //Metodo que permite retornar el codigo procesado y ya analizado por el analizador semantico
    public String codigoCompilado() {

        if (this.astParser == null) {
            return "";
        }

        TablaSimbolos tablaSimbolos = ejecutarPasadasAnalisis();

        if (!this.listadoErroresTotal.isEmpty()) {
            return "";
        }

        /*Metodo de compilacion del codigo*/
        try {
            return codigoIntermedio(tablaSimbolos);

        } catch (TiempoEjecucionException e) {
            return "";
        }
    }

    /*Metodo que permite retornar el codigo intermedio listo para la ultima fase de compilacion*/
    private String codigoIntermedio(TablaSimbolos tablaSimbolos) throws TiempoEjecucionException {


        List<Formulario> codigoIntermedio = new ArrayList<>();
        for (Nodo nodo : astParser) {
            if (nodo != null) {

                if (nodo instanceof NodoQuestion) {
                    if (((NodoQuestion) nodo).getId() != null) {
                        continue;
                    }
                }

                Object resultado = nodo.ejecutar(tablaSimbolos, this.listadoErroresTotal);

                if (resultado instanceof OnCompilacionError) {
                    throw new TiempoEjecucionException("Error en tiempo de ejecucion");
                }

                if (resultado instanceof Formulario) {
                    codigoIntermedio.add((Formulario) resultado);
                } else if (resultado instanceof List) {
                    codigoIntermedio.addAll((List<Formulario>) resultado);
                }
            }
        }

        return retornarCodigo(codigoIntermedio);
    }


    //Metodo delegado para correr el codigo intermedio y retornar el codigo compilado
    private String retornarCodigo(List<Formulario> codigoIntermedio){

        Integer [] contadoresReporte  = {0, 0, 0, 0, 0, 0,0};
        /*
        * position 0 -> Secciones
        * position 1 -> Preguntas
        * position 2 -> Abiertas
        * position 3 -> Drop
        * position 4 -> Seleccion
        * position 5 -> Multiple
        * position 6 -> Textos
        * */


        StringBuilder codigoIntermedioBuilder = new StringBuilder();
        for (Formulario formulario : codigoIntermedio){
            codigoIntermedioBuilder.append(formulario.compilar());
            codigoIntermedioBuilder.append("\n");
        }


        for (Formulario formulario : codigoIntermedio){
            formulario.contarComponentes(contadoresReporte);
        }


        if(contadoresReporte[0] == 0 && contadoresReporte[1] == 0 &&
                contadoresReporte[2] == 0 && contadoresReporte[3] == 0 &&
                contadoresReporte[4] == 0 && contadoresReporte[5] == 0 &&
                contadoresReporte[6] == 0){
            return "";
        }

        StringBuilder reporte = new StringBuilder();

        reporte.append("    Total de Secciones: ").append(contadoresReporte[0]).append("\n");
        reporte.append("    Total de Preguntas: ").append(contadoresReporte[1]).append("\n");
        reporte.append("        Abiertas: ").append(contadoresReporte[2]).append("\n");
        reporte.append("        Desplegables: ").append(contadoresReporte[3]).append("\n");
        reporte.append("        Seleccion: ").append(contadoresReporte[4]).append("\n");
        reporte.append("        Multiples: ").append(contadoresReporte[5]).append("\n");
        reporte.append("        Textos (Abiertos): ").append(contadoresReporte[6]).append("\n");
        reporte.append("###").append("\n\n");

        String codigoCompilado = codigoIntermedioBuilder.toString();
        reporte.append(codigoCompilado);

        return reporte.toString();

    }

    /*Metodo utilizado para ejecutar pasadas*/
    private TablaSimbolos ejecutarPasadasAnalisis() {
        TablaSimbolos tablaSimbolos = new TablaSimbolos();

        for (Nodo nodo : astParser) {
            if (nodo != null) {
                nodo.validarSemantica(tablaSimbolos, this.listadoErroresTotal);
            }
        }

        return tablaSimbolos;
    }

    //Retorna la lista de errores semanticos
    public List<ErrorAnalisis> getListadoErroresTotal() {
        return listadoErroresTotal;
    }
}
/*Created by Pablo*/
