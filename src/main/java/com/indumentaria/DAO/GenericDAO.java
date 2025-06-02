package com.indumentaria.DAO;

import java.sql.Connection;
import java.util.List;

public interface GenericDAO<T> {

    void insertar(T entidad) throws Exception;
    void insertTx(T entidad, Connection conn) throws Exception;
    void actualizar(T entidad)throws Exception;
    void eliminar(int id)throws Exception;
    T getById(int id)throws Exception;
    List<T> getAll()throws Exception;




}