package com.indumentaria.services;
import com.indumentaria.Config.DataBaseConnectionPool; // Necesario para obtener la conexión en el servicio si la transacción se maneja aquí
import com.indumentaria.Config.TransactionManager;
import com.indumentaria.DAO.GenericDAO;
import com.indumentaria.Models.Domicilio;

import java.sql.Connection;
import java.util.List;
import java.sql.SQLException; // Importar SQLException

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

        Connection conn = null; // Declarar la conexión fuera del try-catch
        TransactionManager tx = new TransactionManager();

        try{
            conn = DataBaseConnectionPool.getConnection(); // Obtener la conexión
            tx.setConn(conn); // Establecer la conexión en el TransactionManager
            tx.starTransaction();
            domicilioDAO.insertTx(domicilio, conn); // Pasar la conexión al DAO
            tx.commit();
            System.out.println("Inserción de domicilio con éxito (Tx)"); // Mensaje de éxito
        } catch (Exception e) {
            System.err.println("ERROR en la transacción de Domicilio: " + e.getMessage()); // Mensaje de error
            if (conn != null) { // Asegurarse de que la conexión no sea nula antes de intentar el rollback
                tx.rollback();
            }
            throw e; // Relanzar la excepción para que sea manejada por la capa superior
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Cerrar la conexión en el finally
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void actualizar(Domicilio entidad) throws Exception {
        // Implementación de actualizar si es necesario en el servicio
        domicilioDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        // Implementación de eliminar si es necesario en el servicio
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