package com.pablocompany.proyectono1_compi1.compiler.backend.modelos.codigofuente.configuracion;

//Clase que define un atributo de configuracion en general (que tienen en comun las preguntas)
public class AtributoConfig {

    //Atributos de configuracion
    private TipoConfiguracion tipo;
    private Object nodoValor;

    public AtributoConfig(TipoConfiguracion tipo, Object nodoValor) {
        this.tipo = tipo;
        this.nodoValor = nodoValor;
    }

    //Getters y setters
    public Object getNodoValor() {
        return nodoValor;
    }

    public void setNodoValor(Object nodoValor) {
        this.nodoValor = nodoValor;
    }

    //Metodo que permite obtener el tipo de configuracion

    public TipoConfiguracion getTipo() {
        return tipo;
    }

    public void setTipo(TipoConfiguracion tipo) {
        this.tipo = tipo;
    }
}
