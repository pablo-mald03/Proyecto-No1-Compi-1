
package com.pablocompany.rest.api.proyectono1.compi1.formularios.models;

/**
 *
 * @author pablo03
 */

//Clase que permite obtener la informacion del formulario ya subido
public class FormularioDTO {
    
    private String id;
    private String autor;
    private String nombreArchivo;
    private String fechaPublicacion;
    private String horaPublicacion;

    public FormularioDTO(String id, String autor, String nombreArchivo, String fechaPublicacion, String horaPublicacion) {
        this.id = id;
        this.autor = autor;
        this.nombreArchivo = nombreArchivo;
        this.fechaPublicacion = fechaPublicacion;
        this.horaPublicacion = horaPublicacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getHoraPublicacion() {
        return horaPublicacion;
    }

    public void setHoraPublicacion(String horaPublicacion) {
        this.horaPublicacion = horaPublicacion;
    }
  
}
