package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

//Clase que representa a las preguntas abiertas
public class PreguntaAbierta extends Formulario {

    //Atributos caracteristicos de la pregunta abierta
    private Number height;
    private Number width;
    private String label;
    private EstilosComponent estilos;

    public PreguntaAbierta( Number height, Number width, String label, EstilosComponent estilos,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.label = label;
        this.estilos = estilos;
    }

    //Metodos getters y setters


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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EstilosComponent getEstilos() {
        return estilos;
    }

    public void setEstilos(EstilosComponent estilos) {
        this.estilos = estilos;
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

        estilosEtiquetaBasicos.append("<open=");
        estilosEtiquetaBasicos.append(this.width != null ? this.width.toString() : "-1").append(",");
        estilosEtiquetaBasicos.append(this.height != null ? this.height : "-1").append(",");
        estilosEtiquetaBasicos.append("\"").append(this.label).append("\"");


        if (this.estilos != null && this.estilos.tieneEstilos()) {
            estilosEtiquetaBasicos.append(">");
            estilosEtiquetaBasicos.append("\n\n");
            estilosEtiquetaBasicos.append(this.estilos.crearEstilosBasicos());
            estilosEtiquetaBasicos.append("\n\n");
            estilosEtiquetaBasicos.append("</open>\n\n");
        } else {
            estilosEtiquetaBasicos.append("/>");
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
        contadoresReporte[2]++;
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
