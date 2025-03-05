package com.mycompany.prueba;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class AdduserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/adduser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("nombre");
        String surename = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String password = dni;
        String id = request.getParameter("escuela");
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";  
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";  
        Connection conn = null;
        PreparedStatement stmt = null;

        int idEscuela = Integer.parseInt(id);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);

            // Verificar si el DNI ya existe
            String checkDniSql = "SELECT COUNT(*) FROM usuarios WHERE dni = ?";
            try(PreparedStatement checkStmt = conn.prepareStatement(checkDniSql)) {
                checkStmt.setString(1, dni);
                ResultSet rs = checkStmt.executeQuery();
                if(rs.next() && rs.getInt(1) > 0) {
                    request.setAttribute("error", "El DNI ya está registrado.");
                    request.getRequestDispatcher("/WEB-INF/views/adduser.jsp").forward(request, response);
                    return;
                }
            }
            String sql = "INSERT INTO usuarios (nombre, apellido, dni, contrasena, idEscuela) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
    
            stmt.setString(1, name);         
            stmt.setString(2, surename);     
            stmt.setString(3, dni);          
            stmt.setString(4, password);     
            stmt.setInt(5, idEscuela);          
    
            int rowsInserted = stmt.executeUpdate();
            
            if(rowsInserted > 0) {
                request.setAttribute("successMessage", "Usuario registrado con éxito.");
                request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
            } 
            else {
                // Si la inserción no fue exitosa
                request.setAttribute("error", "Error al registrar el usuario.");
                request.getRequestDispatcher("/WEB-INF/views/adduser.jsp").forward(request, response);
            }
        } 
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        } 
        catch(SQLException e) {
            e.printStackTrace();
        } 
        finally {
        // Cerrar recursos
            try {
                if(stmt != null){ 
                    stmt.close();
                }
                if(conn != null){ 
                    conn.close();
                }
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
