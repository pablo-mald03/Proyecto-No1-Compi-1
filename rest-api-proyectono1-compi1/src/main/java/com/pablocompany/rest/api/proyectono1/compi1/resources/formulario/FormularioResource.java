package com.pablocompany.rest.api.proyectono1.compi1.resources.formulario;

import com.pablocompany.rest.api.proyectono1.compi1.exceptions.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.Formulario;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.services.FormularioCrudService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Map;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author pablo03
 */
@Path("forms")
public class FormularioResource {

    //Endpoint para subir un formulario
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirFormulario(
            @FormDataParam("autor") String autor,
            @FormDataParam("foto") InputStream archivo,
            @FormDataParam("foto") FormDataContentDisposition archivoDetalles) {

        Formulario formularioNuevo = new Formulario(autor, archivo, archivoDetalles);

        FormularioCrudService service = new FormularioCrudService();

        try {

            if (service.publicarFormulario(formularioNuevo)) {
                
                return Response.status(Response.Status.CREATED).build();
            } else {

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", "No se pudo crear el usuario")).build();
            }

        } catch (FormatoInvalidoException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("mensaje", ex.getMessage())).build();
        } catch (ErrorInesperadoException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("mensaje", ex.getMessage())).build();
        }

    }

    //Endpoint para obtener el contenido del formulario (util para solo contestar)
    @GET
    @Path("content/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response formularioContent(@PathParam("id") String id) {

        return Response.ok("Hola mundo").build();

    }

    //Metodo que permite listar todos lod formularios
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response formulariosSubidos() {

        return Response.ok("Hola mundo").build();

    }

}
