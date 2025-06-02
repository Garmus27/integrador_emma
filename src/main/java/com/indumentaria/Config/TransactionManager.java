package com.indumentaria.Config;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
@Setter
@Getter
public class TransactionManager {

    private Connection conn;

    public void starTransaction() throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
            System.out.println("Trasaccion iniciada. AutoCommit deshabilitado");
        }else {
            throw new SQLException("No se puede iniciar la transaccion");
        }
    }

    public void commit() throws SQLException {
        if(conn != null) {
            conn.commit();
            System.out.println("Transaccion realizada, cambios Commitiados");
        }else{
            throw new SQLException("Error al commitear cambios, no hay conexion establecida");
        }
    }

    public void rollback() throws SQLException {
        if(conn != null) {
            conn.rollback();
            System.out.println("Transaccion cancelada, RollBack");
        }else{
            throw new SQLException("Error de Rollback, no hay conexion establecida");
        }
    }

    public void close() throws SQLException {
        if(conn != null) {
            conn.setAutoCommit(true);
            conn.close();
            System.out.println("Conexcion cerrada  ");
        }
    }

}