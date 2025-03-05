package com.mycompany.prueba.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Usa la URL pública proporcionada por Railway
    private static final String URL = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway"; // URL pública de la base de datos
    private static final String USER = "root"; // Usuario de la base de datos
    private static final String PASSWORD = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB"; // Contraseña de la base de datos

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
