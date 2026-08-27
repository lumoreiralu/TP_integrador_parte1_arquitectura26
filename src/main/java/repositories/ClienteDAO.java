package repositories;

import entities.Cliente;
import factories.MySqlConnectionFactory;
import repositories.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// DAO encargado de acceder a la tabla Cliente mediante JDBC.
// DAO encargado de realizar todas las operaciones de acceso
// a la tabla Cliente de la base de datos.
// Implementa DAO<Cliente>, por lo que trabaja específicamente
// con objetos de tipo Cliente.

public class ClienteDAO implements DAO<Cliente> {
    //Guarda la única instancia de ClienteDAO.
    // Se utiliza el patrón Singleton para evitar crear
    // varias instancias de esta clase.
    private static ClienteDAO unicaInstancia;

    // La instancia se obtiene mediante getInstance().
    private ClienteDAO() {
    }

    public static ClienteDAO getInstance() {
        if (unicaInstancia == null) {
            unicaInstancia = new ClienteDAO();
        }
        return unicaInstancia;
    }

    // Elimina la tabla Cliente de la base de datos.
    // PreparedStatement permite preparar y ejecutar una sentencia SQL.

    @Override
    public void dropTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS Cliente")) {
            ps.executeUpdate();
            conn.commit(); //Confirmamos la operación. Como el autocommit está desactivado,
            // debemos hacer commit manualmente.
        } catch (SQLException e) {
            conn.rollback(); // Si ocurrió un error, deshacemos la transacción.
            throw new SQLException("Error al eliminar la tabla Cliente.", e);
        }
    }

    // Crea la tabla Cliente en la base de datos.
    @Override
    public void createTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "idCliente INT PRIMARY KEY," +
                "nombre VARCHAR(500)," +
                "email VARCHAR(150))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            conn.commit();     // Confirmamos la creación de la tabla.
        } catch (SQLException e) {
            conn.rollback();   // Si algo falla, deshacemos la operación.
            throw new SQLException("Error al crear la tabla Cliente.", e);
        }
    }

}