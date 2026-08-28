package dao;

import entity.Producto;

import java.util.List;

public interface ProductoDAO {
    Producto findById(int id);
    List<Producto> findAll();
    void create(Producto producto);
    void update(Producto producto);
    void delete(Producto producto);

}