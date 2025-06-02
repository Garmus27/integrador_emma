package com.indumentaria.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/integrador";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("Driver cargado exitosamente!");

        }catch (ClassNotFoundException e){
            System.out.println("Driver no encontrado!");
            e.printStackTrace();

        }
    } // Cargacion del driver, arroja exeption si no lo cargamos bien

    public static Connection getConnection()throws SQLException{
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

    } //obtenemos la coneccion a la DB

    public static void closeConnection(Connection conn){
        if(conn != null){
            try {
                conn.close();
                System.out.println("Conexion a la base de datos CERRADA");
            }catch (SQLException e){
                System.out.println("Error al cerrar la conexion" + e.getMessage());
                e.printStackTrace();
            }
        }

    } // cerramos la conexion a la base de datos

}