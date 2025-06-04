package com.indumentaria.DAO;
import com.indumentaria.Config.DataBaseConnectionPool;
import com.indumentaria.Models.Domicilio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioDAO implements GenericDAO<Domicilio>{


    @Override
    public void insertar(Domicilio domicilio) throws SQLException {
        String sql = "INSERT INTO domicilios (calle, numero) VALUES (?, ?)";

        try (Connection conn = DataBaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumero());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    domicilio.setId(generatedKeys.getInt(1)); // asignamos el id generado al objeto Domicilio
                    System.out.println("Domicilio insertado con ID: " + domicilio.getId());
                } else {
                    throw new SQLException("La inserción del domicilio falló, no se obtuvo ID generado.");
                }
            }
        }

    }

    @Override
    public void insertTx(Domicilio domicilio, Connection conn) throws Exception {
        String sql="INSERT INTO domicilios(calle, numero) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumero());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    domicilio.setId(generatedKeys.getInt(1));
                    System.out.println("Domicilio insertado con ID: " + domicilio.getId());
                } else {
                    throw new SQLException("La inserción del domicilio falló, no se obtuvo ID generado.");
                }
            }
        }

    }

    @Override
    public void actualizar(Domicilio domicilio) throws SQLException {
        String sql="UPDATE domicilios SET calle=?, numero=? WHERE id=?";
        try(Connection conn = DataBaseConnectionPool.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumero());
            stmt.setInt(3, domicilio.getId());
            stmt.executeUpdate();

        }

    }

    @Override
    public void eliminar(int id)throws SQLException {
        String sql = "DELETE FROM domicilios WHERE id=?";
        try(Connection conn = DataBaseConnectionPool.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

    @Override
    public Domicilio getById(int id)throws SQLException {
        String sql = "SELECT * FROM domicilios WHERE id=?";
        try(Connection conn = DataBaseConnectionPool.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Domicilio(
                        rs.getInt("id"),
                        rs.getString("calle"),
                        rs.getString("numero"));
            }
        }
        return null;
    }



    @Override
    public List<Domicilio> getAll() throws SQLException {
        String sql = "SELECT * FROM domicilios";
        try(Connection conn = DataBaseConnectionPool.getConnection(); Statement stmt = conn.createStatement(); ){
            ResultSet rs = stmt.executeQuery(sql);
            List<Domicilio> domicilios = new ArrayList<Domicilio>();
            while(rs.next()){
                domicilios.add(new Domicilio(
                        rs.getInt("id"),
                        rs.getString("calle"),
                        rs.getString("numero")
                ));
            }
            return domicilios;
        }

    }
}