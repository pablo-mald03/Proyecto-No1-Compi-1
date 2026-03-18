package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

//Clase de maxima jerarquia que permite almacenar o representar al codigo compilado que se lee de etiquetas
public abstract class CompiledForm {

    //Atributos de la clase
    private double width;
    private double height;

    private double pointX;
    private double pointY;

    /*Atributo que representa todos los estilos de un componente*/
    private CompiledEstilos estilos;

    /*Constructor*/
    public CompiledForm (double width, double height, double pointX, double pointY, CompiledEstilos estilos){
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
        this.estilos = estilos;
    }

    /*Metodos getters*/

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getPointX() {
        return this.pointX;
    }

    public double getPointY() {
        return this.pointY;
    }

    public CompiledEstilos getEstilos() {
        return this.estilos;
    }

}
