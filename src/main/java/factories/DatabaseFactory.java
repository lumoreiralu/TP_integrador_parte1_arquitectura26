package factories;

import java.sql.Connection;
import java.sql.SQLException;

// Factory abstracta encargada de proporcionar conexiones
// según el tipo de base de datos utilizado.

public abstract class DatabaseFactory {
    public static final int MYSQL_JDBC = 1;

    public abstract Connection getConnection() throws SQLException;

    public static DatabaseFactory getInstance(int whichFactory) throws SQLException {
        switch (whichFactory) {
            case MYSQL_JDBC:
                return MySqlConnectionFactory.getInstance();
            default:
                return null;
        }
    }
}