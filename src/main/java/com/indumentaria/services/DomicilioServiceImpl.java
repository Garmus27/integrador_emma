package com.indumentaria.services;
import com.indumentaria.Config.TransactionManager;
import com.indumentaria.DAO.GenericDAO;
import com.indumentaria.Models.Domicilio;


import java.util.List;

public class DomicilioServiceImpl implements GenericService<Domicilio>{
    private final GenericDAO<Domicilio> domicilioDAO;

    public DomicilioServiceImpl(GenericDAO<Domicilio> domicilioDAO) {
        this.domicilioDAO = domicilioDAO;
    }

    @Override
    public void insertar(Domicilio domicilio) throws Exception {
        if(domicilio.getCalle() == null||domicilio.getCalle().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }
        if(domicilio.getNumero() == null||domicilio.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El numero de la callse no puede ser nulo");
        }
        domicilioDAO.insertar(domicilio);
    }

    @Override
    public void insertarTx(Domicilio domicilio) throws Exception {
        if(domicilio.getCalle() == null||domicilio.getCalle().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }
        if(domicilio.getNumero() == null||domicilio.getNumero().trim().isEmpty()){
            throw new IllegalArgumentException("El numero de la callse no puede ser nulo");
        }
        TransactionManager tx = new TransactionManager();

        try{
            tx.starTransaction();
            domicilioDAO.insertar(domicilio);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }



    }

    @Override
    public void actualizar(Domicilio entidad) throws Exception {

    }

    @Override
    public void eliminar(int id) throws Exception {

    }

    @Override
    public Domicilio getById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Domicilio> getAll() throws Exception {
        return List.of();
    }
}