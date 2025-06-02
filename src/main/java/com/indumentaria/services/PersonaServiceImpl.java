package com.indumentaria.services;

import com.indumentaria.Config.DataBaseConnectionPool;
import com.indumentaria.Config.TransactionManager;
import com.indumentaria.DAO.DomicilioDAO;
import com.indumentaria.DAO.PersonaDAO;
import com.indumentaria.Models.Persona;

import java.sql.Connection;
import java.util.List;

public class PersonaServiceImpl implements GenericService<Persona>{

    private final PersonaDAO personaDAO;
    private final DomicilioDAO domicilioDAO;

    public PersonaServiceImpl(PersonaDAO personaDAO, DomicilioDAO domicilioDAO) {
        this.personaDAO = personaDAO;
        this.domicilioDAO = domicilioDAO;
    }



    @Override
    public void insertar(Persona persona) throws Exception {
        if(persona.getNombre() == null || persona.getNombre().trim().isEmpty()){
            throw new Exception("El nombre del persona no puede ser vacio");
        }
        if(persona.getApellido() == null || persona.getApellido().trim().isEmpty() ){
            throw new Exception("El apellido del persona no puede ser vacio");
        }
        if(persona.getDni() == null || persona.getDni().trim().isEmpty()){
            throw new Exception("El dni del persona no puede ser vacio");
        }
        if(persona.getDomicilio() != null && persona.getDomicilio().getId() >0){
            domicilioDAO.insertar(persona.getDomicilio());
        }

        if(persona.getDomicilio() != null && persona.getDomicilio().getId() == 0){
            domicilioDAO.insertar(persona.getDomicilio()); // Inserta el domicilio y le asigna un ID
        } else if (persona.getDomicilio() != null && persona.getDomicilio().getId() > 0) {
            domicilioDAO.actualizar(persona.getDomicilio()); // Si ya tiene ID, lo actualiza
        }

        personaDAO.insertar(persona);

    }

    @Override
    public void insertarTx(Persona persona) throws Exception {
        Connection conn = null;
        TransactionManager tx = new TransactionManager();

        try {
            conn = DataBaseConnectionPool.getConnection();
            tx.setConn(conn);
            tx.starTransaction();

            if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacío.");
            }
            if (persona.getDni() == null || persona.getDni().trim().isEmpty()) {
                throw new IllegalArgumentException("El DNI de la persona no puede ser nulo o vacío.");
            }
            if (persona.getDomicilio() == null) {
                throw new IllegalArgumentException("La persona debe tener un domicilio.");
            }
            if (persona.getDomicilio().getCalle() == null || persona.getDomicilio().getCalle().trim().isEmpty()) {
                throw new IllegalArgumentException("La calle del domicilio no puede ser nula o vacía.");
            }

            if (persona.getDomicilio().getId() == 0){ // si el domicilio es nuevo
                domicilioDAO.insertTx(persona.getDomicilio(), conn);

            } else {
                domicilioDAO.actualizar(persona.getDomicilio());
            }
            if (persona.getDomicilio() != null) {
                if (persona.getDomicilio().getId() == 0) { // Si el domicilio es nuevo (ID 0)
                    domicilioDAO.insertTx(persona.getDomicilio(), conn); // Inserta y asigna el ID al objeto Domicilio
                } else { // Si el domicilio ya existe (tiene un ID)
                    domicilioDAO.actualizar(persona.getDomicilio());
                }
            } else {
                // Si la persona no tiene domicilio, el PersonaDAO se encargará de setear id_domicilio a NULL
                System.out.println("La persona se está insertando sin domicilio asociado.");
            }

            personaDAO.insertTx(persona,conn);
            tx.commit();
            System.out.println("Insercion de la persona con exito");

        }catch(Exception e){
            System.out.println("ERROR en la trasaccion");
            if(conn != null){
                tx.rollback();
            }
            throw e;
        }finally {
            if(conn != null){
                conn.close();
            }
        }
    }

    @Override
    public void actualizar(Persona entidad) throws Exception {
        if(entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()){
            throw new Exception("El nombre de la persona no puede ser vacio");
        }
        if(entidad.getApellido() == null || entidad.getApellido().trim().isEmpty() ){
            throw new Exception("El apellido de la persona no puede ser vacio");
        }
        if(entidad.getDni() == null || entidad.getDni().trim().isEmpty()){
            throw new Exception("El dni de la persona no puede ser vacio");
        }
        personaDAO.actualizar(entidad);

    }

    @Override
    public void eliminar(int id) throws Exception {

    }

    @Override
    public Persona getById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Persona> getAll() throws Exception {
        return List.of();
    }
}