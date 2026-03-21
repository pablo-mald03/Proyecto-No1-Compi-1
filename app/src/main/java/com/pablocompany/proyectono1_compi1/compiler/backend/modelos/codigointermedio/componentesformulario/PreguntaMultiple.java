package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

/*Clase delegada que representa a una pregunta multiple*/
public class PreguntaMultiple extends Formulario {

    //Atributos caracteristicos de la pregunta multiple
    private Number height;
    private Number width;

    //Atributo que representa la lista de opciones que tiene la pregunta multiple
    private List<String> opciones;

    private EstilosComponent estilos;

    private List<Integer> respuestasCorrectas;

    private String label;

    public PreguntaMultiple(Number height, Number width,String label, List<String> opciones, List<Integer> respuestas, EstilosComponent estilos, int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.opciones = opciones;
        this.estilos = estilos;
        this.respuestasCorrectas = respuestas;
        this.label = label;
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

    public List<Integer> getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public void setRespuestasCorrectas(List<Integer> respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
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

        estilosEtiquetaBasicos.append("<multiple=");
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

        if(this.respuestasCorrectas != null){
            estilosEtiquetaBasicos.append("{");
            for (int i = 0; i < this.respuestasCorrectas.size(); i++) {
                estilosEtiquetaBasicos.append(this.respuestasCorrectas.get(i));
                if(i < this.respuestasCorrectas.size() - 1){
                    estilosEtiquetaBasicos.append(",");
                }
            }
            estilosEtiquetaBasicos.append("}");
        }else{
            estilosEtiquetaBasicos.append("{}");
        }


        if (this.estilos != null && this.estilos.tieneEstilos()) {
            estilosEtiquetaBasicos.append(">");
            estilosEtiquetaBasicos.append("\n\n");
            estilosEtiquetaBasicos.append(this.estilos.crearEstilosBasicos());
            estilosEtiquetaBasicos.append("\n\n");
            estilosEtiquetaBasicos.append("</multiple>\n\n");
        } else {
            estilosEtiquetaBasicos.append("/>\n\n");
        }

        return estilosEtiquetaBasicos.toString();
    }

    /*Metodo utilizado para contabilizar la cantidad de componentes del formulario*/
    /*
     * position 0 -> Secciones
     * position 1 -> Preguntas
     * position 2 -> Abiertas
     * position 3 -> Drop
     * position 4 -> Select
     * position 5 -> Multiple
     * */
    @Override
    public void contarComponentes(Integer [] contadoresReporte){

        contadoresReporte[1]++;
        contadoresReporte[5]++;
    }

    //Metodo utilizado para heredar las configuraciones
    @Override
    public void heredarConfiguraciones(Formulario componente){

    }
}
