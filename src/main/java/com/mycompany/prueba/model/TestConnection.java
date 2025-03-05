package com.mycompany.prueba.model;

import java.sql.Connection;
import java.sql.SQLException;


public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a la base de datos!");
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}
