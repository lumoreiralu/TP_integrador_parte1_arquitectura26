package repository;
import entity.Factura;
import dao.ProductoDAO;
import entity.Producto;
import entity.ProductoRecaudacion;

import java.sql.*;
import java.util.*;


public class MySQLProductoDAO implements ProductoDAO {

    private final Connection cn;

    public MySQLProductoDAO(Connection cn) {
        this.cn = cn;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        final String sql = "CREATE TABLE IF NOT EXISTS productos (" +
                "idProducto INT PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "valor FLOAT NULL" +
                ")";
        try (Statement st = cn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando tabla 'productos'", e);
        }
    }
    @Override
    public Producto findById(int id) {
        final String sql = "SELECT * FROM productos WHERE idProducto = ?";
        try(PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next() ? map(rs) : null ;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto por id" + id, e);
        }
    }

    @Override
    public List<Producto> findAll() {
        final String sql = "SELECT * FROM productos";
        List<Producto> out = new ArrayList<>();
        try(PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()) out.add(map(rs));
        } catch(SQLException e) {
            throw new RuntimeException("Error al buscar todos los productos", e);
        }
        return out;
    }

    @Override
    public void create(Producto producto) {
        final String sql = "INSERT INTO productos (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try(PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setInt(1,producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            ps.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException("Error al insertar producto", e);
        }

    }

    @Override
    public void update(Producto producto) {
        final String sql = "UPDATE productos SET nombre = ?, valor = ? WHERE idProducto = ?";
        try(PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setString(1, producto.getNombre());
            ps.setFloat(2, producto.getValor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar el producto", e);
        }

    }

    @Override
    public void delete(Producto producto) {
        final String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, producto.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error en delete", e);
        }
    }

    @Override
    public void deleteAll() {
        final String sql = "DELETE FROM productos";
        try (Statement st = cn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error borrando 'productos'", e);
        }
    }  

    @Override
    public ProductoRecaudacion findTopRevenueProduct() {
        final String sql = "SELECT p.idProducto, p.nombre, p.valor, SUM(fp.cantidad * p.valor) as revenue " +
                "FROM productos p " +
                "JOIN factura_producto fp ON p.idProducto = fp.idProducto " +
                "GROUP BY p.idProducto, p.nombre, p.valor " +
                "ORDER BY revenue DESC " +
                "LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new ProductoRecaudacion(
                                rs.getInt("idProducto"),
                                rs.getString("nombre"),
                                rs.getFloat("valor"),
                                rs.getFloat("revenue")
                        );
                    } else {
                        return null; // No hay productos
                    }
                }
        catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto con mayor ingreso", e);
        }
    }
            
        

    // ---- mapper privado ----
    private Producto map(ResultSet rs) throws SQLException {
        Producto u = new Producto();
        u.setIdProducto(rs.getInt("idProducto"));
        u.setNombre(rs.getString("nombre"));
        float valor = rs.getInt("valor");
        return u;
    }
}
