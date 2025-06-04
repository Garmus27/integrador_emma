package com.indumentaria.services;
import com.indumentaria.Config.DataBaseConnectionPool;
import com.indumentaria.Config.TransactionManager;
import com.indumentaria.DAO.GenericDAO;
import com.indumentaria.Models.Domicilio;

import java.sql.Connection;
import java.util.List;
import java.sql.SQLException;

public class DomicilioServiceImpl implements GenericService<Domicilio>{
    private final GenericDAO<Domicilio> domicilioDAO;

    public DomicilioServiceImpl(GenericDAO<Domicilio> domicilioDAO) {
        this.domicilioDAO = domicilioDAO;
    }

    @Override
    public void insertar(Domicilio domicilio) throws Exception {
        if(domicilio.getCalle() == null || domicilio.getCalle().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }
        if(domicilio.getNumero() == null || domicilio.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El numero de la calle no puede ser nulo");
        }
        domicilioDAO.insertar(domicilio);
    }

    @Override
    public void insertarTx(Domicilio domicilio) throws Exception {
        if(domicilio.getCalle() == null || domicilio.getCalle().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }
        if(domicilio.getNumero() == null || domicilio.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El numero de la calle no puede ser nulo");
        }

        Connection conn = null; // Declarar la conexión fuera del try-catch sino queda anidada y no sirve
        TransactionManager tx = new TransactionManager();

        try{
            conn = DataBaseConnectionPool.getConnection(); // Obtener la conexión
            tx.setConn(conn); // Establecer la conexión en el TransactionManager
            tx.starTransaction();
            domicilioDAO.insertTx(domicilio, conn);
            tx.commit();
            System.out.println("Inserción de domicilio con éxito (Tx)");
        } catch (Exception e) {
            System.err.println("ERROR en la transacción de Domicilio: " + e.getMessage());
            e.printStackTrace(); // Mejora el tema de ver donde nos la echamos
            if (conn != null) { // Asegurarse de que la conexión no sea nula antes de intentar el rollback
                try {
                    tx.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error durante el rollback de Domicilio: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Cerramos conexión en el finally
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void actualizar(Domicilio entidad) throws Exception {
        domicilioDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        domicilioDAO.eliminar(id);
    }

    @Override
    public Domicilio getById(int id) throws Exception {
        return domicilioDAO.getById(id);
    }

    @Override
    public List<Domicilio> getAll() throws Exception {
        return domicilioDAO.getAll();
    }
}