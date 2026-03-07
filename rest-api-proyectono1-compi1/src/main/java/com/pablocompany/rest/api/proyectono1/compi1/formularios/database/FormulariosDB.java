
package com.pablocompany.rest.api.proyectono1.compi1.formularios.database;

import com.pablocompany.rest.api.proyectono1.compi1.exceptions.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.Formulario;
import com.pablocompany.rest.api.proyectono1.compi1.resources.connection.DBConnectionSingleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author pablo03
 */
//Clase delegada para poder comunicarse con la base de datos
public class FormulariosDB {
    
    //Constante que permite insertar un formulario a la base de datos
    private static final String INSERT_FORM = "INSERT INTO formulario (autor, filename, archivo) VALUES (?, ?, ?)";
    
    //Constante que permite consultar todos los formularios (sin paginacion)
    private static final String OBTENER_TODOS = "SELECT id, autor, filename, fecha_publicacion, hora_publicacion FROM formulario";
    
    //Constante que permite obtener el contenido dentro del formulario 
    private static final String OBTENER_FORMULARIO = "SELECT archivo FROM formulario WHERE id = ?";
    
    
    //Metodo que sirve para poder registrar un usuario en el sistema
    public boolean insertarFormulario(Formulario referenciaFormulario) throws ErrorInesperadoException, FormatoInvalidoException {

        Connection conexion = DBConnectionSingleton.getInstance().getConnection();

        try {

            conexion.setAutoCommit(false);


            int filasAfectadasBilletera = subirFormulario(referenciaFormulario, conexion);

            if ( filasAfectadasBilletera > 0) {

                conexion.commit();
                return true;

            } else {
                conexion.rollback();
                throw new ErrorInesperadoException("No se ha podido registrar al usuario. Contactar al administrador de Sistema. ");
            }

        } catch (SQLException ex) {

            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                throw new ErrorInesperadoException("Error al hacer Rollback. Contactar a soporte tecnico al insertar el usuario");
            }

            throw new ErrorInesperadoException("No se permiten inyecciones sql o partones diferentes a los que se piden al insertar el usuario.");
        } finally {

            try {

                conexion.setAutoCommit(true);

            } catch (SQLException ex) {
                throw new ErrorInesperadoException("Error al reactivar la autoconfirmacion al insertar el usuario. Contactar Soporte tecnico.");
            }
        }

    }
    
     //Metodo que sirve para poder generar la transaccion para insertar al usuario y crearle su registro a su bileltera digital
    public int subirFormulario(Formulario referenciaFormulario, Connection conexion) throws SQLException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(INSERT_FORM);) {

            conexion.setAutoCommit(false);

            preparedStmt.setString(1, referenciaFormulario.getAutor().trim());
            //preparedStmt.setString(2, referenciaFormulario.getArchivoDetalles().toString());
            //preparedStmt.setBytes(3, referenciaFormulario);


            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        }

    }
    
    
}
