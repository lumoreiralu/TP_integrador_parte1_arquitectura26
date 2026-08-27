package repositories;

import entities.Producto;
import factories.MySqlConnectionFactory;
import repositories.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// DAO encargado de acceder a la tabla Producto mediante JDBC.

public class ProductoDAO implements DAO<Producto> {
    private static ProductoDAO unicaInstancia;

    private ProductoDAO() {
    }

    public static ProductoDAO getInstance() {
        if (unicaInstancia == null) {
            unicaInstancia = new ProductoDAO();
        }
        return unicaInstancia;
    }

    @Override
    public void dropTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS Producto")) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al eliminar la tabla Producto.", e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Producto (" +
                "idProducto INT PRIMARY KEY," +
                "nombre VARCHAR(45)," +
                "valor FLOAT)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al crear la tabla Producto.", e);
        }
    }

}