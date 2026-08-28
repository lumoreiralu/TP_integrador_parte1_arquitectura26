package repository;
import entity.Cliente;
import dao.ClienteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySQLClienteDAO implements ClienteDAO {
    private final Connection cn;

    public MySQLClienteDAO(Connection cn) {
        this.cn = cn;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        final String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                "idCliente BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                ")";
        try (Statement st = cn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando tabla 'clientes'", e);
        }
    }


    @Override
    public Cliente findById(Integer id) {
        final String sql = "SELECT * FROM clientes WHERE idCliente = ?";
        try(PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                    return rs.next() ? map(rs) : null;
            }
        } catch(SQLException e){
            throw new RuntimeException("Error en la consulta del registro", e);
        }
    }

    @Override
    public Cliente findByEmail(String email) {
        final String sql = "SELECT * FROM clientes WHERE email = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en la consulta por email", e);
        }
    }

    @Override
    public Cliente findByName(String name) {
        final String sql = "SELECT * FROM clientes WHERE nombre = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en la consulta por nombre", e);
        }
    }

    @Override
    public List<Cliente> findAll() {
        final String sql = "SELECT id, nombre, edad FROM clientes";
        List<Cliente> out = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error en findAll", e);
        }
        return out;
    }

    @Override
    public void create(Cliente c) {
        final String sql = "INSERT INTO clientes (nombre, email) VALUES (?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getEmail());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en create", e);
        }

    }

    @Override
    public void update(Cliente c) {
        final String sql = "UPDATE clientes SET nombre = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getEmail());
            ps.setInt(3, c.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en update", e);
        }

    }

    @Override
    public void delete(Integer id) {
        final String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en delete", e);
        }

    }

    // ---- mapper privado ----
    private Cliente map(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();

        // 1. Asignar el ID (int)
        c.setId(rs.getInt("idCliente"));

        // 2. Asignar el Nombre (String)
        c.setNombre(rs.getString("nombre"));

        // 3. Asignar el Email / Valor (según los campos que tenga tu clase Cliente)
        c.setEmail(rs.getString("email"));

        return c;
    }
}
