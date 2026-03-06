
package com.pablocompany.rest.api.proyectono1.compi1.resources.formulario;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author pablo03
 */
@Path("forms")
public class FormularioResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response formulariosSubidos(){
        
        return Response.ok("Hola mundo").build();
        
    }
        
}
