package repositories;

import entities.FacturaProducto;
import factories.MySqlConnectionFactory;
import repositories.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// DAO encargado de acceder a la tabla Factura_Producto.
// Esta tabla relaciona Facturas con Productos y contiene una clave primaria compuesta.

public class FacturaProductoDAO implements DAO<FacturaProducto> {
    private static FacturaProductoDAO unicaInstancia;

    private FacturaProductoDAO() {
    }

    public static FacturaProductoDAO getInstance() {
        if (unicaInstancia == null) {
            unicaInstancia = new FacturaProductoDAO();
        }
        return unicaInstancia;
    }

    @Override
    public void dropTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DROP TABLE IF EXISTS Factura_Producto")) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al eliminar la tabla Factura_Producto.", e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        Connection conn = MySqlConnectionFactory.getInstance().getConnection();
        // FK dobles: hacia Factura y hacia Producto. Por eso esta tabla se crea AL FINAL.
        // PK compuesta: la combinación (idFactura, idProducto) identifica una fila única.
        String sql = "CREATE TABLE IF NOT EXISTS Factura_Producto (" +
                "idFactura INT," +
                "idProducto INT," +
                "cantidad INT," +
                "PRIMARY KEY (idFactura, idProducto)," +
                "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura)," +
                "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error al crear la tabla Factura_Producto.", e);
        }
    }

}