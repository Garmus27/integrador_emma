package com.indumentaria.Config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnectionPool {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/integrador");
        config.setUsername("root");
        config.setPassword("");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);

        try {
            dataSource = new HikariDataSource(config);
            System.out.println("Se ha establecion la conexion a la base de datos!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR al conectar con la base de datos");

        }
    }

        public static Connection getConnection() throws SQLException {
            if (dataSource == null) {
                throw new SQLException("El pool de conexiones no está inicializado.");
            }
            // HikariCP manejará el préstamo de una conexión disponible
            return dataSource.getConnection();
        }

        public static void shotDown(){
            if (dataSource == null && !dataSource.isClosed()) {
                dataSource.close();
                System.out.println("Pool de conexiones CERRADO");
        }
    }


}