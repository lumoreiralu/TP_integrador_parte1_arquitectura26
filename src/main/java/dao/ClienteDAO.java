package dao;

import entity.Cliente;

import java.util.List;


public interface ClienteDAO {
    Cliente findById(Integer id);
    Cliente findByEmail(String email);
    Cliente findByName(String name);
    List<Cliente> findAll();
    void create(Cliente cliente);
    void update(Cliente cliente);
    void delete(Integer id);

}