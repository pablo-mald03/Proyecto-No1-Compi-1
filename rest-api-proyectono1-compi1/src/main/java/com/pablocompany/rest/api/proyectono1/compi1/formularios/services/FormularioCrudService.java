package com.pablocompany.rest.api.proyectono1.compi1.formularios.services;

import com.pablocompany.rest.api.proyectono1.compi1.exceptions.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.database.FormulariosDB;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.Formulario;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.FormularioDTO;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.FormularioDescargaDTO;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pablo03
 */
//Service que representa por completo toda la logica de negocio para poder operar con los formularios
public class FormularioCrudService {

    //Metodo que permite subir el formulario al servidor
    public boolean publicarFormulario(Formulario formNuevo) throws ErrorInesperadoException, FormatoInvalidoException {

        if (formNuevo.validarDatos()) {

            FormulariosDB formulariosDB = new FormulariosDB();
            return formulariosDB.insertarFormulario(formNuevo);

        }

        throw new ErrorInesperadoException("No se ha tenido respuesta del servidor");

    }

    //Metodo que permite retornar el listado de formularios paginados
    public List<FormularioDTO> obtenerFormulariosListado(String limite, String inicio) throws ErrorInesperadoException, DatosNoEncontradosException, FormatoInvalidoException {

        if (!StringUtils.isNumeric(limite)) {
            throw new FormatoInvalidoException("El limite del paginado no es numerico");
        }

        if (!StringUtils.isNumeric(inicio)) {
            throw new FormatoInvalidoException("El inicio del paginado no es numerico");
        }

        int limitInt = Integer.parseInt(limite);
        int offsetInt = Integer.parseInt(inicio);

        FormulariosDB formulariosDB = new FormulariosDB();

        return formulariosDB.formulariosRegistrados(limitInt, offsetInt);
    }

    //Metodo que permite retornar el contenido de texto del formulario
    public String obtenerContenidoFormulario(String id) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (!StringUtils.isNumeric(id)) {
            throw new FormatoInvalidoException("El id del formulario no es numerico o esta vacio");
        }

        FormulariosDB formulariosDB = new FormulariosDB();

        int idBuscar = Integer.parseInt(id);

        return formulariosDB.obtenerContenidoComoString(idBuscar);

    }

    //Metodo que retornar el contenido del formulario para poderlo descargar
    public FormularioDescargaDTO obtenerFormularioDescarga(String id) throws FormatoInvalidoException, ErrorInesperadoException, DatosNoEncontradosException {

        if (!StringUtils.isNumeric(id)) {
            throw new FormatoInvalidoException("El id del formulario no es numerico o esta vacio");
        }

        FormulariosDB formulariosDB = new FormulariosDB();

        int idBuscar = Integer.parseInt(id);

        return formulariosDB.obtenerArchivoBinario(idBuscar);

    }
}
