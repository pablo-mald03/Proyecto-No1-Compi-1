package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.componentesformulario;

import com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigointermedio.Formulario;

import java.text.Normalizer;
import java.util.List;

//Clase delegada que representa una tabla dentro de un formulario
public class Tablero extends Formulario {

    //Atributos caracteristicos de la table
    private Number height;
    private Number width;
    private Number pointX;
    private Number pointY;

    //Atributo que representa la lista de elementos que tiene la seccion
    private List<List<Formulario>> elementos;
    private EstilosComponent estilos;
    private EstiloBorde borde;

    public Tablero( Number height,  Number width,Number pointX, Number pointY,  List<List<Formulario>> elementos,  EstilosComponent estilos,  EstiloBorde borde ,int linea, int columna) {
        super(linea, columna);
        this.height = height;
        this.width = width;
        this.pointX = pointX;
        this.pointY = pointY;
        this.elementos = elementos;
        this.estilos = estilos;
        this.borde = borde;
    }

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

    public Number getPointX() {
        return pointX;
    }

    public void setPointX(Number pointX) {
        this.pointX = pointX;
    }

    public Number getPointY() {
        return pointY;
    }

    public void setPointY(Number pointY) {
        this.pointY = pointY;
    }

    public List<List<Formulario>> getElementos() {
        return elementos;
    }

    public void setElementos(List<List<Formulario>> elementos) {
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

        if(this.borde == null && componente instanceof Seccion){
            Seccion seccion = (Seccion) componente;
            this.setBorde(seccion.getBorde());
        }

        if(this.borde == null && componente instanceof Tablero){
            Tablero tabla = (Tablero) componente;
            this.setBorde(tabla.getBorde());
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
            if(this.width== null){
                this.width = seccion.getWidth();
            }
            if(this.height== null){
                this.height = seccion.getHeight();
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
            if(this.width== null){
                this.width = tablero.getWidth();
            }
            if(this.height== null){
                this.height = tablero.getHeight();
            }
        }

    }


    //Metodo que permite retornar la estructura de la tabla
    public String compilar() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<table>\n");
        if (this.estilos != null && this.estilos.tieneEstilos()) {

            stringBuilder.append("\n").append(this.estilos.crearEstilosLayout());

            if (this.borde != null) {
                stringBuilder.append("\n    ").append(this.borde.crearBorde());
            }
            stringBuilder.append("\n    </style>\n\n");
        }

        this.heredarEstilosAElementos();

        stringBuilder.append("\n    <content>");

        for (List<Formulario> fila : this.elementos) {
            stringBuilder.append("\n\n        <line>\n");
            for (Formulario componente : fila) {
                stringBuilder.append("\n\n            <element>\n\n");
                stringBuilder.append("                ").append(componente.compilar().replace("\n", "\n                "));
                stringBuilder.append("\n\n            </element>");
            }
            stringBuilder.append("\n\n        </line>\n\n");
        }

        stringBuilder.append("\n    </content>\n\n");
        stringBuilder.append("\n</table>\n\n");

        return stringBuilder.toString();
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
        for (List<Formulario> fila : this.elementos) {
            for (Formulario componente : fila) {
                componente.contarComponentes(contadoresReporte);
            }
        }
    }

    // Nueva versión del procesador de herencia para Tablero
    private void heredarEstilosAElementos() {
        if (this.elementos == null) return;

        for (List<Formulario> fila : this.elementos) {
            for (Formulario componente : fila) {
                componente.heredarEstilos(this.estilos, this);
                componente.heredarConfiguraciones(this);
            }
        }
    }
}
