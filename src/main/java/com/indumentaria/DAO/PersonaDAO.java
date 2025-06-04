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

        String sql = "INSERT INTO personas (nombre, apellido, dni, domicilio_id) VALUES (?,?,?,?)";
        try(Connection conn = DataBaseConnectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getDni());
            if(persona.getDomicilio() != null && persona.getDomicilio().getId() > 0){
                stmt.setInt(4, persona.getDomicilio().getId());
            }else{
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getInt(1));
                    System.out.println("Persona insertada con ID: " + persona.getId());
                } else {
                    throw new SQLException("La inserción de la persona falló, no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public void insertTx(Persona persona, Connection conn) throws Exception {
        // Anotacion que me sirve para no olvidarme nunca de anotar bien el nombre
        // de la columna, le puse 'id_domicilio' y era 'domicilio_id'

        String sql = "INSERT INTO personas (nombre, apellido, dni, domicilio_id) VALUES (?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getDni());
            if(persona.getDomicilio() != null && persona.getDomicilio().getId() > 0){
                stmt.setInt(4, persona.getDomicilio().getId());
            }else{
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getInt(1));
                    System.out.println("Persona insertada con ID (Tx): " + persona.getId());
                } else {
                    throw new SQLException("La inserción de la persona falló (Tx), no se obtuvo ID generado.");
                }
            }
        }
    }

    @Override
    public void actualizar(Persona persona) throws Exception {
        if (persona.getDomicilio() != null && persona.getDomicilio().getId() > 0) {
            domicilioDAO.actualizar(persona.getDomicilio());
        } else if (persona.getDomicilio() != null && persona.getDomicilio().getId() == 0) {
            domicilioDAO.insertar(persona.getDomicilio());
        }

        String sql = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, domicilio_id = ? WHERE id = ?";
        try(Connection conn = DataBaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getDni());
            if(persona.getDomicilio() != null && persona.getDomicilio().getId() > 0){
                ps.setInt(4, persona.getDomicilio().getId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setInt(5, persona.getId());
            ps.executeUpdate();
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
            throw new SQLException("No se pudo eliminar la persona con ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public Persona getById(int id) throws Exception {
        // aca se armo bardo porque habia que usar SELECT y JOIN
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.domicilio_id, " +
                "d.id AS dom_id, d.calle, d.numero " +
                "FROM personas p LEFT JOIN domicilios d ON p.domicilio_id = d.id " +
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

                    int domicilioId = rs.getInt("domicilio_id"); // CORREGIDO: 'id_domicilio' a 'domicilio_id'
                    if (domicilioId > 0) {
                        Domicilio domicilio = new Domicilio();
                        domicilio.setId(rs.getInt("dom_id"));
                        domicilio.setCalle(rs.getString("calle"));
                        domicilio.setNumero(rs.getString("numero"));
                        persona.setDomicilio(domicilio);
                    } else {
                        persona.setDomicilio(null);
                    }
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
        String sql = "SELECT p.id, p.nombre, p.apellido, p.dni, p.domicilio_id, " +
                "d.id AS dom_id, d.calle, d.numero " +
                "FROM personas p LEFT JOIN domicilios d ON p.domicilio_id = d.id";
        try (Connection conn = DataBaseConnectionPool.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setDni(rs.getString("dni"));

                int domicilioId = rs.getInt("domicilio_id"); //
                if (domicilioId > 0) {
                    Domicilio domicilio = new Domicilio();
                    domicilio.setId(rs.getInt("dom_id"));
                    domicilio.setCalle(rs.getString("calle"));
                    domicilio.setNumero(rs.getString("numero"));
                    persona.setDomicilio(domicilio);
                } else {
                    persona.setDomicilio(null);
                }
                personas.add(persona);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener todas las personas: " + e.getMessage(), e);
        }
        return personas;
    }
}