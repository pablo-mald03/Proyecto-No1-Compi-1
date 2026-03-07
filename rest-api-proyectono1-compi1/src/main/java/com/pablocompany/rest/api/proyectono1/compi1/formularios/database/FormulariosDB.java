package com.pablocompany.rest.api.proyectono1.compi1.formularios.database;

import com.pablocompany.rest.api.proyectono1.compi1.exceptions.DatosNoEncontradosException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.ErrorInesperadoException;
import com.pablocompany.rest.api.proyectono1.compi1.exceptions.FormatoInvalidoException;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.Formulario;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.FormularioDTO;
import com.pablocompany.rest.api.proyectono1.compi1.formularios.models.FormularioDescargaDTO;
import com.pablocompany.rest.api.proyectono1.compi1.resources.connection.DBConnectionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo03
 */
//Clase delegada para poder comunicarse con la base de datos
public class FormulariosDB {

    //Constante que permite insertar un formulario a la base de datos
    private static final String INSERT_FORM = "INSERT INTO formulario (autor, filename, archivo) VALUES (?, ?, ?)";

    //Constante que permite consultar todos los formularios (sin paginacion)
    private static final String OBTENER_TODOS = "SELECT id, autor, filename, fecha_publicacion, hora_publicacion FROM formulario LIMIT ? OFFSET ?";

    //Constante que permite obtener el contenido dentro del formulario 
    private static final String OBTENER_FORMULARIO = "SELECT filename, archivo FROM formulario WHERE id = ?";

    //Metodo que sirve para poder registrar un usuario en el sistema
    public boolean insertarFormulario(Formulario referenciaFormulario) throws ErrorInesperadoException, FormatoInvalidoException {

        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection()) {

            try {
                conexion.setAutoCommit(false);

                int filasAfectadas = subirFormulario(referenciaFormulario, conexion);

                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    throw new ErrorInesperadoException("No se ha podido registrar el formulario.");
                }

            } catch (SQLException | FormatoInvalidoException ex) {
                try {
                    if (conexion != null) {
                        conexion.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    throw new ErrorInesperadoException("Error crítico al hacer Rollback.");
                }
                throw new ErrorInesperadoException("Error al subir el nuevo formulario: " + ex.getMessage());
            } finally {
            }

        } catch (SQLException ex) {
            throw new ErrorInesperadoException("Error de conexión con la base de datos.");
        }

    }

    //Metodo que sirve para poder generar la transaccion para insertar al usuario y crearle su registro a su bileltera digital
    public int subirFormulario(Formulario referenciaFormulario, Connection conexion) throws SQLException, FormatoInvalidoException {

        try (PreparedStatement preparedStmt = conexion.prepareStatement(INSERT_FORM);) {

            conexion.setAutoCommit(false);

            preparedStmt.setString(1, referenciaFormulario.getAutor().trim());
            preparedStmt.setString(2, referenciaFormulario.getNombreArchivo());
            preparedStmt.setBinaryStream(3, referenciaFormulario.getArchivo());

            int filasAfectadas = preparedStmt.executeUpdate();

            return filasAfectadas;

        }
    }

    //Metodo que permite obtener toda la informacion de los formularios almacenados
    public List<FormularioDTO> formulariosRegistrados(int limite, int inicio) throws ErrorInesperadoException {
        List<FormularioDTO> listadoFormularios = new ArrayList<>();
        SimpleDateFormat fmtFecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm:ss");

        try (Connection connection = DBConnectionSingleton.getInstance().getConnection(); PreparedStatement query = connection.prepareStatement(OBTENER_TODOS)) {

            query.setInt(1, limite);
            query.setInt(2, inicio);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id"));
                String autor = resultSet.getString("autor");
                String filename = resultSet.getString("filename");
                java.sql.Date sqlFecha = resultSet.getDate("fecha_publicacion");
                java.sql.Time sqlHora = resultSet.getTime("hora_publicacion");

                String fechaStr = (sqlFecha != null) ? fmtFecha.format(sqlFecha) : "0000-00-00";
                String horaStr = (sqlHora != null) ? fmtHora.format(sqlHora) : "00:00:00";

                listadoFormularios.add(new FormularioDTO(id, autor, filename, fechaStr, horaStr));
            }
        } catch (SQLException e) {
            throw new ErrorInesperadoException("Error al obtener el listado paginado: " + e.getMessage());
        }
        return listadoFormularios;
    }

    //Metodo que permite obtener el archivo y poderlo mandar a la aplicacion
    public FormularioDescargaDTO obtenerArchivoBinario(int id) throws ErrorInesperadoException, DatosNoEncontradosException {
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection(); PreparedStatement query = conexion.prepareStatement(OBTENER_FORMULARIO)) {

            query.setInt(1, id);

            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    return new FormularioDescargaDTO(
                            rs.getString("filename"),
                            rs.getBytes("archivo")
                    );
                }
            }

        } catch (SQLException e) {
            throw new ErrorInesperadoException("Error al recuperar el archivo del formulario binario");
        }

        throw new DatosNoEncontradosException("No se ha encontrado el formulario con el ID solicitado");
    }

    //Metodo que permite retornar SOLO EL TEXTO del formulario subido (MODO CONTESTAR DIRECTO)
    public String obtenerContenidoComoString(int id) throws ErrorInesperadoException, DatosNoEncontradosException {
        try (Connection conexion = DBConnectionSingleton.getInstance().getConnection(); PreparedStatement query = conexion.prepareStatement("SELECT archivo FROM formulario WHERE id = ?")) {

            query.setInt(1, id);
            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    byte[] bytes = rs.getBytes("archivo");
                    return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                }
            }
        } catch (SQLException e) {
            throw new ErrorInesperadoException("Error al procesar el contenido del formulario");
        }
        throw new DatosNoEncontradosException("Formulario no encontrado con el ID solicitado");
    }

}
