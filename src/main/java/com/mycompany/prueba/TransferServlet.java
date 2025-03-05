package com.mycompany.prueba;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class TransferServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String newid = request.getParameter("escuela");
        int idEscuela = Integer.parseInt(id);
        int newidEscuela = Integer.parseInt(newid);
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";  
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";  
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);
            String sql = "UPDATE legajos SET idEscuela = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, newidEscuela);  
            stmt.setInt(2, idEscuela);  
            int rowsUpdated = stmt.executeUpdate(); 
            
            if(rowsUpdated > 0) {
                request.setAttribute("successMessage", "Transpaso realizado con éxito.");
                request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
            }   
            else {
                // Si no hay coincidencia, mostrar un mensaje de error
                request.setAttribute("errorRN", "Error");
                request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
            }
        }
        catch(ClassNotFoundException e) {
            System.err.println("Error: No se pudo cargar el controlador JDBC.");
            e.printStackTrace();
        } 
        catch(SQLException e) {
            System.err.println("Error de SQL: No se pudo conectar a la base de datos.");
            e.printStackTrace();
        } 
        finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
                if(conn != null) {
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
    }
}
