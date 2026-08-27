package factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Factory concreta encargada de crear y administrar la conexión a MySQL.


// Patrón Singleton: una única instancia de esta clase en toda la aplicación
public class MySqlConnectionFactory extends DatabaseFactory {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URI = "jdbc:mysql://localhost:3306/db_integrador_01";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private static MySqlConnectionFactory unicaInstancia = null;
    private static Connection conn;

    private MySqlConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static MySqlConnectionFactory getInstance() throws SQLException {
        if (unicaInstancia == null) {
            unicaInstancia = new MySqlConnectionFactory();
        }
        return unicaInstancia;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);
        }
        return conn;
    }
}