package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.componentes.layouts.TipoOrientacion;
import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.util.List;

//Clase que representa a una seccion de un formulario (Puede tener a secciones anidadas tambien)
public class Seccion extends Formulario {

    //Atributos caracteristicos de la seccion
    private Number height;
    private Number width;
    private Number pointX;
    private Number pointY;

    //Atributo que representa a la orientacion de la seccion
    private TipoOrientacion orientacion;


    //Atributo que representa la lista de elementos que tiene la seccion
    private List<Formulario> elementos;
    private EstilosComponent estilos;
    private EstiloBorde borde;

    public Seccion(Number height, Number width, Number pointX, Number pointY, TipoOrientacion orientacion, List<Formulario> elementos, EstilosComponent estilos, EstiloBorde borde, int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.pointX = pointX;
        this.orientacion = orientacion;
        this.pointY = pointY;
        this.elementos = elementos;
        this.estilos = estilos;
        this.borde = borde;
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

    public Number getPointY() {
        return pointY;
    }

    public void setPointY(Number pointY) {
        this.pointY = pointY;
    }

    public Number getPointX() {
        return pointX;
    }

    public void setPointX(Number pointX) {
        this.pointX = pointX;
    }

    public TipoOrientacion getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(TipoOrientacion orientacion) {
        this.orientacion = orientacion;
    }

    public List<Formulario> getElementos() {
        return elementos;
    }

    public void setElementos(List<Formulario> elementos) {
        this.elementos = elementos;
    }

    public EstilosComponent getEstilos() {
        return estilos;
    }

    public void setEstilos(EstilosComponent estilos) {
        this.estilos = estilos;
    }

    public EstiloBorde getBorde() {
        return borde;
    }

    public void setBorde(EstiloBorde borde) {
        this.borde = borde;
    }

    //Metodo que permite retornar el codigo compilado de la seccion
    @Override
    public String compilar() {


        StringBuilder estilosEtiquetaBasicos = new StringBuilder();

        estilosEtiquetaBasicos.append("<section=");
        estilosEtiquetaBasicos.append(this.width != null ? this.width.toString() : "-1").append(",");
        estilosEtiquetaBasicos.append(this.height != null ? this.height : "-1").append(",");
        estilosEtiquetaBasicos.append(this.pointX != null ? this.pointX : "-1").append(",");
        estilosEtiquetaBasicos.append(this.pointY != null ? this.pointY : "-1").append(",");
        estilosEtiquetaBasicos.append(this.orientacion != null ? this.orientacion : "VERTICAL");

        estilosEtiquetaBasicos.append(">");

        if ((this.estilos != null && this.estilos.tieneEstilos()) || this.borde != null) {
            estilosEtiquetaBasicos.append("\n\n").append(this.estilos.crearEstilosLayout());

            if (this.borde != null) {
                estilosEtiquetaBasicos.append("\n   ").append(this.borde.crearBorde());
            }

            estilosEtiquetaBasicos.append("\n\n    </style>\n\n");
        }

        this.heredarEstilosComponentes();

        estilosEtiquetaBasicos.append("\n");

        estilosEtiquetaBasicos.append("     <content>\n\n");

        for (Formulario formulario : this.elementos) {
            estilosEtiquetaBasicos.append("        ").append(formulario.compilar()).append("\n");
        }

        estilosEtiquetaBasicos.append("     </content>\n\n");

        estilosEtiquetaBasicos.append("</section>\n\n");
        return estilosEtiquetaBasicos.toString();
    }

    /*Metodo utilizado para contabilizar la cantidad de componentes del formulario*/
    /*
     * position 0 -> Secciones
     * position 1 -> Preguntas
     * position 2 -> Abiertas
     * position 3 -> Drop
     * position 4 -> Multiple
     * position 5 -> Text
     * */
    @Override
    public void contarComponentes(Integer [] contadoresReporte){

        contadoresReporte[0]++;

        for (Formulario formulario : this.elementos) {
            formulario.contarComponentes(contadoresReporte);
        }

    }

    //Metodo que permite heredar los estilos
    @Override
    public void heredarEstilos(EstilosComponent estilosPadre,Formulario componente) {

        if (this.estilos == null) {
            this.estilos = new EstilosComponent();
        }

        if (estilosPadre != null) {

            if (this.estilos.getColor() == null) {
                this.estilos.setColor(estilosPadre.getColor());
            }
            if (this.estilos.getBackgroundColor() == null) {
                this.estilos.setBackgroundColor(estilosPadre.getBackgroundColor());
            }
            if (this.estilos.getFontFamily() == null) {
                this.estilos.setFontFamily(estilosPadre.getFontFamily());
            }
            if (this.estilos.getTextSize() == null) {
                this.estilos.setTextSize(estilosPadre.getTextSize());
            }
        }

        if (estilosPadre != null) {

            if (this.borde == null && componente instanceof Seccion) {
                Seccion seccion = (Seccion) componente;
                this.setBorde(seccion.getBorde());
            }

            if (this.borde == null && componente instanceof Tablero) {
                Tablero tabla = (Tablero) componente;
                this.setBorde(tabla.getBorde());
            }

        }
    }


    //Metodo que permite ejecutar herencia de estilos
    private void heredarEstilosComponentes() {
        for (Formulario formulario : this.elementos) {
            pasarHerencia(formulario);
            pasarHerenciaConfiguraciones(formulario);
        }
    }

    //Metodo auxilar que permite heredar los estilos normales
    private void pasarHerencia(Formulario formulario) {
        if (formulario instanceof Seccion && ((Seccion) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof Tablero && ((Tablero) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof TextoPlano && ((TextoPlano) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof PreguntaAbierta && ((PreguntaAbierta) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof PreguntaMultiple && ((PreguntaMultiple) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof PreguntaDrop && ((PreguntaDrop) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
        if (formulario instanceof PreguntaSelect && ((PreguntaSelect) formulario).getEstilos() != null) {
            formulario.heredarEstilos(this.estilos,this);
        }
    }

    //Metodo que permite heredar las configuraciones
    private void pasarHerenciaConfiguraciones(Formulario formulario){

        if (formulario instanceof Seccion) {
            formulario.heredarConfiguraciones(this);
        }
        if (formulario instanceof Tablero) {
            formulario.heredarConfiguraciones(this);
        }
    }

    //Metodo utilizado para heredar las configuraciones
    @Override
    public void heredarConfiguraciones(Formulario componente){

        if(componente instanceof Seccion){
            Seccion seccion = (Seccion) componente;

            if(this.pointX== null){
                this.pointX = seccion.getPointX();
            }
            if(this.pointY== null){
                this.pointY = seccion.getPointY();
            }
            if(this.orientacion== null){
                this.orientacion = seccion.getOrientacion();
            }
        }

        if(componente instanceof Tablero){
            Tablero tablero = (Tablero) componente;

            if(this.pointX== null){
                this.pointX = tablero.getPointX();
            }
            if(this.pointY== null){
                this.pointY = tablero.getPointY();
            }
        }

    }


}