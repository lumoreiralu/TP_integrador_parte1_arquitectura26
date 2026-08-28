import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            crearEsquema();
            System.out.println("Punto 1: esquema creado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Punto 1: crea el esquema de la base de datos mediante JDBC.
    // Primero se eliminan las tablas existentes respetando las dependencias
    // y luego se crean nuevamente en el orden correcto.

    // ---------- PUNTO 1: crear el esquema ----------
    private static void crearEsquema() throws SQLException {
        ClienteDAO clienteDAO = ClienteDAO.getInstance();
        ProductoDAO productoDAO = ProductoDAO.getInstance();
        FacturaDAO facturaDAO = FacturaDAO.getInstance();
        FacturaProductoDAO facturaProductoDAO = FacturaProductoDAO.getInstance();

        // DROP en orden inverso a las dependencias: primero las "hijas" (con FK),
        // al final las "padres". Al revés, MySQL rechaza el DROP.
        facturaProductoDAO.dropTable();
        facturaDAO.dropTable();
        productoDAO.dropTable();
        clienteDAO.dropTable();

        // CREATE en orden de dependencias: primero las "padres", al final las "hijas".
        clienteDAO.createTable();
        productoDAO.createTable();
        facturaDAO.createTable();
        facturaProductoDAO.createTable();
    }
}