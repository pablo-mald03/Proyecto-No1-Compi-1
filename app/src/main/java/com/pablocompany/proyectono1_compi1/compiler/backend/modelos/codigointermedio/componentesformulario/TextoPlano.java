package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase que representa a la etiqueta texto dentro del formulario
public class TextoPlano extends Formulario {

    //Atributos caracteristicos del texto
    private Number height;
    private Number width;
    private String texto;
    private EstilosComponent estilos;

    public TextoPlano(Number height, Number width, String texto, EstilosComponent estilos, int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.texto = texto;
        this.estilos = estilos;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    /*Metodo utilizado para contabilizar la cantidad de componentes del formulario*/
    @Override
    public void contarComponentes(Integer [] contadoresReporte){
        contadoresReporte[6]++;
    }

    //Metodo que permite retornar el codigo compilado
    @Override
    public String compilar() {


        StringBuilder estilosEtiquetaBasicos = new StringBuilder();

        estilosEtiquetaBasicos.append("<text=");
        estilosEtiquetaBasicos.append(this.width != null ? this.width.toString() : "-1").append(",");
        estilosEtiquetaBasicos.append(this.height != null ? this.height : "-1").append(",");
        estilosEtiquetaBasicos.append("\"").append(this.texto).append("\"");

        if (this.estilos != null && this.estilos.tieneEstilos()) {
            estilosEtiquetaBasicos.append(">\n\n");
            estilosEtiquetaBasicos.append(this.estilos.crearEstilosBasicos());
            estilosEtiquetaBasicos.append("\n\n");
            estilosEtiquetaBasicos.append("</text>");
        } else {
            estilosEtiquetaBasicos.append("/>\n\n");
        }

        return estilosEtiquetaBasicos.toString();
    }

    //Metodo utilizado para heredar las configuraciones
    @Override
    public void heredarConfiguraciones(Formulario componente){

    }

}
