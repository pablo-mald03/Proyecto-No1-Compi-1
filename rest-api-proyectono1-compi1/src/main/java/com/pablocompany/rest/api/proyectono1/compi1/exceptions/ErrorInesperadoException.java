/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pablocompany.rest.api.proyectono1.compi1.exceptions;

/**
 *
 * @author pablo03
 */
//Excepcion que representa cualquier error con el servidor
public class ErrorInesperadoException extends Exception{

    public ErrorInesperadoException(String message) {
        super(message);
    }
    
}
