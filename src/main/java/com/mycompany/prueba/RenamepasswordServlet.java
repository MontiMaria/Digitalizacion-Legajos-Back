package com.mycompany.prueba;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class RenamepasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dni = request.getParameter("dni");
        String newPassword = request.getParameter("new-password");
        String confirmPassword = request.getParameter("confirm-password");
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";  
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";  
        Connection conn = null;
        PreparedStatement stmt = null;
        
        
        if(newPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/views/forgotpassword.jsp").forward(request, response);
            return;
        }
        
        try {
            // Cargar el driver de MySQL por si las dudas)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);
            
            String sql = "UPDATE usuarios SET contrasena = ? WHERE dni = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);  
            stmt.setString(2, dni);  
            
            System.out.println("DNI recibido: " + dni);
            
            int rowsUpdated = stmt.executeUpdate(); 
            System.out.println("Filas actualizadas: " + rowsUpdated);
            
            if(rowsUpdated > 0) {
            request.setAttribute("successMessage", "Contraseña actualizada con éxito.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }   
            else {
                // Si no hay coincidencia, mostrar un mensaje de error
                request.setAttribute("errorRN", "Credencial incorrecta");
                request.getRequestDispatcher("/WEB-INF/views/forgotpassword.jsp").forward(request, response);
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
            // Asegura que la conexión y el statement se cierren correctamente
            try {
                if(stmt != null) {
                    stmt.close();
                }
                if(conn != null) {
                    conn.close();
                }
            } 
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
