/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono1.compi1.formularios.models;

import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author pablo03
 */
//Objeto principal que representa el formulario base
public class Formulario {
    
    private String autor;
    private InputStream archivo;
    private FormDataContentDisposition archivoDetalles;

    public Formulario(String autor, InputStream archivo, FormDataContentDisposition archivoDetalles) {
        this.autor = autor;
        this.archivo = archivo;
        this.archivoDetalles = archivoDetalles;
    }

    //Getters y setters
    public InputStream getArchivo() {
        return archivo;
    }

    public void setArchivo(InputStream archivo) {
        this.archivo = archivo;
    }

    public FormDataContentDisposition getArchivoDetalles() {
        return archivoDetalles;
    }

    public void setArchivoDetalles(FormDataContentDisposition archivoDetalles) {
        this.archivoDetalles = archivoDetalles;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
    
}
