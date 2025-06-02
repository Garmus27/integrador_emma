package com.indumentaria.DAO;

import com.indumentaria.Config.DataBaseConnectionPool;
import com.indumentaria.Models.Domicilio;
import com.indumentaria.Models.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements GenericDAO<Persona>{

    private final DomicilioDAO domicilioDAO;

    public PersonaDAO(DomicilioDAO domicilioDAO) {
        this.domicilioDAO = domicilioDAO;
    }

    @Override
    public void insertar(Persona persona)throws Exception {

        String sql = "INSERT INTO personas (nombre, apellido, dni,id_domicilio) VALUES (?,?,?,?)";
        try(Connection conn = DataBaseConnectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getDni());
            if(persona.getDomicilio() != null){
                stmt.setInt(4, persona.getDomicilio().getId());
            }else{
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getInt(1)); // Asigna el ID generado a la persona
                    System.out.println("Persona insertada con ID: " + persona.getId());
                } else {
                    throw new SQLException("La inserción de la persona falló, no se obtuvo ID generado.");
                }
            }



            stmt.executeUpdate();

        }

    }

    @Override
    public void insertTx(Persona persona, Connection conn) throws Exception {
        String sql = "INSERT INTO personas (nombre, apellido, dni,id_domicilio) VALUES (?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getDni());
            if(persona.getDomicilio() != null){
                stmt.setInt(4, persona.getDomicilio().getId());
            }else{
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getInt(1)); // Asigna el ID generado a la persona
                    System.out.println("Persona insertada con ID: " + persona.getId());
                } else {
                    throw new SQLException("La inserción de la persona falló, no se obtuvo ID generado.");
                }
            }



            stmt.executeUpdate();

        }

    }

    @Override
    public void actualizar(Persona persona) throws Exception {

        // Primero, actualizar el Domicilio asociado si existe y tiene un ID
        if (persona.getDomicilio() != null && persona.getDomicilio().getId() > 0) {
            domicilioDAO.actualizar(persona.getDomicilio());
        } else if (persona.getDomicilio() != null && persona.getDomicilio().getId() == 0) {
            // Si el domicilio es nuevo pero estamos actualizando una persona existente,
            // podemos insertarlo y luego actualizar la persona con el nuevo ID de domicilio.
            domicilioDAO.insertar(persona.getDomicilio());
        }
        String sql = "UPDATE personas SET nombre = ?, apellido = ? WHERE id = ?";
        try(Connection conn = DataBaseConnectionPool.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
        }

    }

    @Override
    public void eliminar(int id)throws Exception {

        String sql = "DELETE FROM personas WHERE id = ?";
        try(Connection con= DataBaseConnectionPool.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new SQLException("No se pudo eliminar la wea" + e.getMessage());
        }

    }

    @Override
    public Persona getById(int id) throws Exception {
        // Realizamos un JOIN con la tabla domicilios para obtener todos los datos
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.id_domicilio, " +
                "d.id, d.calle, d.numero " +
                "FROM personas p JOIN domicilios d ON p.id_domicilio = d.id " +
                "WHERE p.id = ?";
        try (Connection conn = DataBaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Persona persona = new Persona();
                    persona.setId(rs.getInt("id"));
                    persona.setNombre(rs.getString("nombre"));
                    persona.setApellido(rs.getString("apellido"));
                    persona.setDni(rs.getString("dni"));

                    // Reconstruir el objeto Domicilio
                    Domicilio domicilio = new Domicilio();
                    domicilio.setId(rs.getInt("d.id"));
                    domicilio.setCalle(rs.getString("calle"));
                    domicilio.setNumero(rs.getString("numero"));

                    persona.setDomicilio(domicilio);
                    return persona;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener persona por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Persona> getAll() throws Exception {
        List<Persona> personas = new ArrayList<>();
        // Realizamos un JOIN con la tabla domicilios para obtener todos los datos
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.id_domicilio, " +
                "d.id, d.calle, d.numero " +
                "FROM personas p JOIN domicilios d ON p.id_domicilio = d.id";
        try (Connection conn = DataBaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setDni(rs.getString("dni"));

                // Reconstruir el objeto Domicilio
                Domicilio domicilio = new Domicilio();
                domicilio.setId(rs.getInt("d.id"));
                domicilio.setCalle(rs.getString("calle"));
                domicilio.setNumero(rs.getString("numero"));

                persona.setDomicilio(domicilio);
                personas.add(persona);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener todas las personas: " + e.getMessage(), e);
        }
        return personas;
    }
}