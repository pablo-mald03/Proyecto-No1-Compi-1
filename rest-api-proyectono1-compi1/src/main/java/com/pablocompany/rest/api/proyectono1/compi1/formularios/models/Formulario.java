/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono1.compi1.formularios.models;

import com.pablocompany.rest.api.proyectono1.compi1.exceptions.FormatoInvalidoException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo03
 */
//Objeto principal que representa el formulario base
public class Formulario {

    //Atributos
    private String autor;
    private InputStream archivo;
    private FormDataContentDisposition archivoDetalles;

    public Formulario(String autor, InputStream archivo, FormDataContentDisposition archivoDetalles) {
        this.autor = autor;
        this.archivo = archivo;
        this.archivoDetalles = archivoDetalles;
    }

    //Getters y setters
    public String getAutor() {
        return autor;
    }

    public InputStream getArchivo() {
        return archivo;
    }

    public FormDataContentDisposition getArchivoDetalles() {
        return archivoDetalles;
    }

    public void setArchivo(InputStream archivo) {
        this.archivo = archivo;
    }

    public void setArchivoDetalles(FormDataContentDisposition archivoDetalles) {
        this.archivoDetalles = archivoDetalles;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    //METODOS DE LA CLASE DELEGADOS AL PROPIO OBJETO (PATRON EXPERTO)
    public String getNombreArchivo() {
        if (archivoDetalles != null) {
            return archivoDetalles.getFileName();
        }
        return "desconocido.pkm";
    }

    //Metodo delegado al propio objeto que permite validar los datos recibidos
    public boolean validarDatos() throws FormatoInvalidoException {

        if (StringUtils.isBlank(this.autor)) {
            throw new FormatoInvalidoException("El nombre del autor esta vacio");
        }

        if (this.archivo == null) {
            throw new FormatoInvalidoException("El archivo enviado esta vacio");
        }

        return true;
    }

}
