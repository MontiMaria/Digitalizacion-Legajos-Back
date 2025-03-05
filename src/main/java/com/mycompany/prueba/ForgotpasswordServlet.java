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

public class ForgotpasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ForgotpasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ForgotpasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/forgotpassword.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("dni");
        boolean fail = false;
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";  
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";  
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; 
        
        try {
            // Cargar el driver de MySQL por si las dudas)
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);
            
            String sql = "SELECT * FROM usuarios WHERE dni = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);  
            
            rs = stmt.executeQuery();  
            
            if(rs.next()) {
                request.setAttribute("dni", username);
                request.setAttribute("fail", fail);
                request.getRequestDispatcher("/WEB-INF/views/renamepassword.jsp").forward(request, response);
            } 
            else {
                // Si no hay coincidencia, mostrar un mensaje de error
                fail = true;
                request.setAttribute("fail", fail);
                request.setAttribute("error", "No existe una cuenta asociada a ese DNI");
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
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
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
    }// </editor-fold>
}
