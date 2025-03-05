package com.mycompany.prueba.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Usa la URL pública proporcionada por Railway
    // Usando las variables de entorno para conectar con la base de datos:
    private static final String URL = "jdbc:mysql://" + System.getenv("MYSQL_HOST") + ":" + System.getenv("MYSQL_PORT") + "/" + System.getenv("MYSQL_DATABASE");
    private static final String USER = System.getenv("MYSQL_USER");
    private static final String PASSWORD = System.getenv("MYSQL_PASSWORD");


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a la base de datos!");
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
