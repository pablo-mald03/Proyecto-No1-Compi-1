package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.formulariorecursos;

//Clase de maxima jerarquia que permite almacenar o representar al codigo compilado que se lee de etiquetas
public abstract class CompiledForm {

    //Atributos de la clase
    private Number width;
    private Number height;

    private Number pointX;
    private Number pointY;

    /*Atributo que representa todos los estilos de un componente*/
    private CompiledEstilos estilos;

    /*Constructor*/
    public CompiledForm (Number width, Number height, Number pointX, Number pointY, CompiledEstilos estilos){
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
        this.estilos = estilos;
    }

    /*Metodos getters*/

    public Number getWidth() {
        return this.width;
    }

    public Number getHeight() {
        return this.height;
    }

    public Number getPointX() {
        return this.pointX;
    }

    public Number getPointY() {
        return this.pointY;
    }

    public CompiledEstilos getEstilos() {
        return this.estilos;
    }

}
