package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase que representa al objeto de la pregunta drop
public class PreguntaDrop extends Formulario {

    //Atributos caracteristicos de la pregunta drop
    private Number height;
    private Number width;

    //Atributo que representa la lista de opciones que tiene la pregunta drop
    private List<String> opciones;

    private EstilosComponent estilos;

    private Integer respuestaCorrecta;
    private String label;


    public PreguntaDrop( Number height, Number width,String label, List<String> opciones, Integer respuestaCorrecta, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.label = label;
        this.opciones = opciones;
        this.estilos = estilos;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    /*Metodos getters y setters*/

    public Number getHeight() {
        return height;
    }

    public void setHeight(Number height) {
        this.height = height;
    }

    public Number getWidth() {
        return width;
    }

    public void setWidth(Number width) {
        this.width = width;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public EstilosComponent getEstilos() {
        return estilos;
    }

    public void setEstilos(EstilosComponent estilos) {
        this.estilos = estilos;
    }

    public Integer getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(Integer respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    //Metodo que permite heredar los estilos
    @Override
    public void heredarEstilos(EstilosComponent estilos,Formulario componente) {

        if (this.estilos == null) {
            this.estilos = new EstilosComponent();
        }

        if(this.estilos.getColor()==null) {
            this.estilos.setColor(estilos.getColor());
        }
        if(this.estilos.getBackgroundColor()==null) {
            this.estilos.setBackgroundColor(estilos.getBackgroundColor());
        }
        if(this.estilos.getFontFamily()==null) {
            this.estilos.setFontFamily(estilos.getFontFamily());
        }
        if(this.estilos.getTextSize()==null) {
            this.estilos.setTextSize(estilos.getTextSize());
        }

    }

    //Metodo que permite retornar el codigo compilado
    @Override
    public String compilar() {


        StringBuilder estilosEtiquetaBasicos = new StringBuilder();

        estilosEtiquetaBasicos.append("<drop=");
        estilosEtiquetaBasicos.append(this.width != null ? this.width.toString() : "-1").append(",");
        estilosEtiquetaBasicos.append(this.height != null ? this.height : "-1").append(",");
        estilosEtiquetaBasicos.append("\"").append(this.label).append("\",");

        if(this.opciones != null){
            estilosEtiquetaBasicos.append("{");
            for (int i = 0; i < this.opciones.size(); i++) {
                estilosEtiquetaBasicos.append("\"").append(this.opciones.get(i)).append("\"");
                if(i < this.opciones.size() - 1){
                    estilosEtiquetaBasicos.append(",");
                }
            }
            estilosEtiquetaBasicos.append("}");
            estilosEtiquetaBasicos.append(",");
        }

        estilosEtiquetaBasicos.append(this.respuestaCorrecta != null ? this.respuestaCorrecta : "-1");


        if (this.estilos != null && this.estilos.tieneEstilos()) {
            estilosEtiquetaBasicos.append(">");
            estilosEtiquetaBasicos.append(this.estilos.crearEstilosBasicos());
            estilosEtiquetaBasicos.append("\n");
            estilosEtiquetaBasicos.append("</drop>");
        } else {
            estilosEtiquetaBasicos.append("/>");
        }

        return estilosEtiquetaBasicos.toString();
    }

    //Metodo utilizado para heredar las configuraciones
    @Override
    public void heredarConfiguraciones(Formulario componente){

        if(componente instanceof Seccion){
            Seccion seccion = (Seccion) componente;

            if(this.width== null){
                this.width = seccion.getWidth();
            }
            if(this.height== null){
                this.height = seccion.getHeight();
            }

        }


        if(componente instanceof Tablero){
            Tablero tablero = (Tablero) componente;

            if(this.width== null){
                this.width = tablero.getWidth();
            }
            if(this.height== null){
                this.height = tablero.getHeight();
            }
        }

    }
}
