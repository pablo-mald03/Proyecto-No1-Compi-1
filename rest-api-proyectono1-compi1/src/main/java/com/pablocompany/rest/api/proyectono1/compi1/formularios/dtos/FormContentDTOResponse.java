/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono1.compi1.formularios.dtos;

/**
 *
 * @author pablo03
 */
//Dto que representa el contenido que se retorna del formulario
public class FormContentDTOResponse {
    //Atributos
    private String contenido;

    public FormContentDTOResponse(String contenido) {
        this.contenido = contenido;
    }

    public FormContentDTOResponse() {
    }

    
    //getters y setters
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    
    
}
