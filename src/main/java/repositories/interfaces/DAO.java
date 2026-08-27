package repositories.interfaces;

import java.sql.SQLException;

// Interfaz genérica que define las operaciones básicas
// de acceso a datos que deben implementar los distintos DAO.

//DAO puede trabajar con diferentes tipos de objetos
public interface DAO<T> {
    void dropTable() throws SQLException;

    void createTable() throws SQLException;

}