package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.NodoComponente;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.AtributoConfig;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoBorder;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoHeight;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointX;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoPointY;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion.NodoWidth;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.Estilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.estilos.NodoEstilos;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.tablasimbolos.TablaSimbolos;
import com.pablocompany.proyectono1_compi1.compiler.models.errores.ErrorAnalisis;

import java.util.List;

//Clase que representa por completo la tabla de los formularios. Otro tipo de layout que existe
public class NodoTable extends NodoComponente {

    private List<List<NodoComponente>> filas;

    private NodoPointX pointX;
    private NodoPointY pointY;

    private List<AtributoConfig> configuracion;

    /*Variable que representa los tipos de vorden que hay*/
    private NodoBorder borde;

    public NodoTable(List<AtributoConfig> configs, int linea, int columna) {
        // Inicializamos con nulls por ahora, luego el procesado los llena
        super(null, null, null, linea, columna);
        this.filas = null;
        this.pointX = null;
        this.pointY = null;
        this.borde = null;
        this.estilos = null;
        this.configuracion = configs;
        procesarConfiguracion();
    }

    private void procesarConfiguracion() {

        if (configuracion.isEmpty()) {
            return;
        }

        for (AtributoConfig config : configuracion) {

            if (config == null) {
                continue;
            }

            switch (config.getTipo()) {
                case WIDTH:
                    this.width = (NodoWidth) config.getNodoValor();
                    break;
                case HEIGHT:
                    this.height = (NodoHeight) config.getNodoValor();
                    break;
                case STYLES:
                    this.estilos = procesarEstilos((List<NodoEstilos>) config.getNodoValor());
                    break;
                case POINT_X:
                    this.pointX = (NodoPointX) config.getNodoValor();
                    break;
                case POINT_Y:
                    this.pointY = (NodoPointY) config.getNodoValor();
                    break;
                case ELEMENTS:
                    this.filas = (List<List<NodoComponente>>) config.getNodoValor();
                    break;
            }
        }
            /*Conteo de columnas y filas*/
            /*int maxCols = 0;
            for(List<NodoComponente> fila : filas) {
            maxCols = Math.max(maxCols, fila.size());
            }*/
    }

    @Override
    protected Estilos procesarEstilos(List<NodoEstilos> lista) {
        // Lógica de herencia de estilos
        return null; // Implementación real aquí
    }

    @Override
    public Object ejecutar(TablaSimbolos tabla, List<ErrorAnalisis> listaErrores) {
        return null;
    }

    @Override
    public String getString() {
        return "";
    }
}
