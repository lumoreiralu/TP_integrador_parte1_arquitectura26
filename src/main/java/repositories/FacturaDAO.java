package repositories;

import entities.Factura;
import factories.MySqlConnectionFactory;
import repositories.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// DAO encargado de acceder a la tabla Factura mediante JDBC.
// La tabla posee una clave foránea que referencia a Cliente.

public class FacturaDAO implements DAO<Factura> {
    private static FacturaDAO unicaInstancia;

    private FacturaDAO() {
    }

    public static FacturaDAO getInstance() {
        if (unicaInstancia == null) {
            unicaInstancia = new FacturaDAO();
        }
        return unicaInstancia;
    }

    @Override
    public void dropTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS Factura")) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al eliminar la tabla Factura.", e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        // FOREIGN KEY hacia Cliente: por eso Factura se crea DESPUÉS de Cliente
        String sql = "CREATE TABLE IF NOT EXISTS Factura (" +
                "idFactura INT PRIMARY KEY," +
                "idCliente INT," +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al crear la tabla Factura.", e);
        }
    }

}