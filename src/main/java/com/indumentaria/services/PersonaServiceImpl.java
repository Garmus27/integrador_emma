package com.indumentaria.services;

import com.indumentaria.Config.DataBaseConnectionPool;
import com.indumentaria.Config.TransactionManager;
import com.indumentaria.DAO.DomicilioDAO;
import com.indumentaria.DAO.PersonaDAO;
import com.indumentaria.Models.Persona;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PersonaServiceImpl implements GenericService<Persona>{

    private final PersonaDAO personaDAO;
    private final DomicilioServiceImpl domicilioServiceImpl;

    public PersonaServiceImpl(PersonaDAO personaDAO, DomicilioServiceImpl domicilioServiceImpl) {
        this.personaDAO = personaDAO;
        this.domicilioServiceImpl = domicilioServiceImpl;
    }

    @Override
    public void insertar(Persona persona) throws Exception {
        // Validaciones mejoradas
        if(persona.getNombre() == null || persona.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacío"); // Usar IllegalArgumentException
        }
        if(persona.getApellido() == null || persona.getApellido().trim().isEmpty() ){
            throw new IllegalArgumentException("El apellido de la persona no puede ser nulo o vacío"); // Usar IllegalArgumentException
        }
        if(persona.getDni() == null || persona.getDni().trim().isEmpty()){
            throw new IllegalArgumentException("El DNI de la persona no puede ser nulo o vacío"); // Usar IllegalArgumentException
        }

        // Lógica de domicilio para insertar no transaccional:
        // Si tiene domicilio y es nuevo, lo inserta. Si tiene y ya existe, lo actualiza.
        // Si no tiene domicilio, simplemente se inserta la persona sin FK.
        if (persona.getDomicilio() != null) {
            if (persona.getDomicilio().getId() == 0) {
                domicilioServiceImpl.insertar(persona.getDomicilio());
            } else { // Asumimos que si tiene ID > 0, ya existe en la DB y puede necesitar actualización
                domicilioServiceImpl.actualizar(persona.getDomicilio()); // *** puede quitarse par ahorrar una conexion a la db
            }
        }
        personaDAO.insertar(persona);
    }

    @Override
    public void insertarTx(Persona persona) throws Exception {
        /*Connection conn = null;
        TransactionManager tx = new TransactionManager();

        try {
            conn = DataBaseConnectionPool.getConnection();
            tx.setConn(conn);
            tx.starTransaction();
            System.out.println("Trasaccion iniciada. AutoCommit deshabilitado para " + persona.getNombre() + " " + persona.getApellido()); // Log más específico

            // Validaciones
            if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacío.");
            }
            if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
                throw new IllegalArgumentException("El apellido de la persona no puede ser nulo o vacío.");
            }
            if (persona.getDni() == null || persona.getDni().trim().isEmpty()) {
                throw new IllegalArgumentException("El DNI de la persona no puede ser nulo o vacío.");
            }

            if (persona.getDomicilio() != null) {
                if (persona.getDomicilio().getCalle() == null || persona.getDomicilio().getCalle().trim().isEmpty()) {
                    throw new IllegalArgumentException("La calle del domicilio no puede ser nula o vacía.");
                }
                if (persona.getDomicilio().getNumero() == null || persona.getDomicilio().getNumero().trim().isEmpty()) {
                    throw new IllegalArgumentException("El número del domicilio no puede ser nulo o vacío.");
                }
            }
            System.out.println("--> Validaciones de Persona completadas para " + persona.getNombre());



            if (persona.getDomicilio() != null) {
                System.out.println("--> Manejando Domicilio para " + persona.getNombre() + " (ID Domicilio inicial: " + persona.getDomicilio().getId() + ")");
                if (persona.getDomicilio().getId() == 0) {
                    domicilioServiceImpl.insertTx(persona.getDomicilio(), conn);
                    System.out.println("--> Domicilio nuevo insertado con ID: " + persona.getDomicilio().getId());
                } else {
                    domicilioServiceImpl.actualizar(persona.getDomicilio());
                    System.out.println("--> Domicilio existente actualizado con ID: " + persona.getDomicilio().getId());
                }
            } else {
                System.out.println("--> La persona se está insertando sin domicilio asociado (domicilio es null).");
            }


            System.out.println("--> Preparando para insertar Persona: " + persona.getNombre() + " con Domicilio ID: " + (persona.getDomicilio() != null ? persona.getDomicilio().getId() : "null"));
            personaDAO.insertTx(persona,conn);
            System.out.println("--> Persona insertada en la transacción con ID: " + persona.getId());

            // Confirma la transacción
            tx.commit();
            System.out.println("Transaccion realizada, cambios Commitiados para " + persona.getNombre());
            System.out.println("Insercion de la persona con exito para " + persona.getNombre());

        }catch(Exception e){
            System.err.println("ERROR en la transacción de Persona para " + persona.getNombre() + ": " + e.getMessage()); // Mensaje más detallado
            e.printStackTrace();
            if(conn != null){
                try { // Añadir try-catch por si las moscas
                    tx.rollback();
                    System.out.println("Transaccion cancelada, RollBack para " + persona.getNombre());
                } catch (SQLException rollbackEx) {
                    System.err.println("Error durante el rollback para " + persona.getNombre() + ": " + rollbackEx.getMessage());
                }
            }
            throw e; // Relanza la excepción para que la maneje el main
        }finally {
            if(conn != null){
                try {
                    conn.close();
                    System.out.println("Conexión cerrada para " + persona.getNombre() + ".");
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión para " + persona.getNombre() + ": " + e.getMessage());
                }
            }
        }*/
    }

    @Override
    public void actualizar(Persona entidad) throws Exception {

        if(entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacío");
        }
        if(entidad.getApellido() == null || entidad.getApellido().trim().isEmpty() ){
            throw new IllegalArgumentException("El apellido de la persona no puede ser nulo o vacío");
        }
        if(entidad.getDni() == null || entidad.getDni().trim().isEmpty()){
            throw new IllegalArgumentException("El DNI de la persona no puede ser nulo o vacío");
        }
        personaDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {

        personaDAO.eliminar(id);
    }

    @Override
    public Persona getById(int id) throws Exception {

        return personaDAO.getById(id);
    }

    @Override
    public List<Persona> getAll() throws Exception {

        return personaDAO.getAll();
    }
}