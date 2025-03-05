package com.mycompany.prueba;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    // Parámetros de la base de datos Railway
    private static final String URL = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Verificar si se requiere cerrar sesión
        String action = request.getParameter("action");
        if("logout".equals(action)) {
            logout(request, response); // Llamar al método logout
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("usuario");
        String password = request.getParameter("password");
        boolean fail = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT idEscuela, dni, contrasena, isAdmin FROM usuarios WHERE dni = ? AND contrasena = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if(rs.next()) {
                int idEscuela = rs.getInt("idEscuela");
                boolean rol = rs.getBoolean("isAdmin");

                // Guardar datos en sesión
                HttpSession session = request.getSession();
                session.setAttribute("idEscuela", idEscuela);
                session.setAttribute("rol", rol);
                request.setAttribute("fail", fail);

                // Implementación de Recordarme
                String remember = request.getParameter("remember_me");
                if(remember != null && remember.equals("on")) {
                    // Generar un token único seguro
                    SecureRandom random = new SecureRandom();
                    String token = new BigInteger(130, random).toString(32);

                    // Guardar el token en la base de datos
                    String updateSQL = "UPDATE usuarios SET token = ? WHERE dni = ?";
                    stmt2 = conn.prepareStatement(updateSQL);
                    stmt2.setString(1, token);
                    stmt2.setString(2, username);
                    stmt2.executeUpdate();

                    // Crear una cookie persistente con el token (expira en 30 días)
                    Cookie cookie = new Cookie("remember_token", token);
                    cookie.setMaxAge(30 * 24 * 60 * 60); // 30 días
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }

                // Redirigir al usuario a principal.jsp
                request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
            } 
            else {
                // Credenciales incorrectas
                fail = true;
                request.setAttribute("fail", fail);
                request.setAttribute("error", "Credenciales incorrectas");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
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
            // Cerrar recursos
            try {
                if(rs != null) {
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(stmt2 != null){
                    stmt2.close();
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

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidar la sesión si existe
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate(); // Eliminar los atributos de sesión
        }

        // Eliminar la cookie "remember_token" si existe
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("remember_token")) {
                    cookie.setMaxAge(0); 
                    cookie.setPath("/");
                    response.addCookie(cookie); 
                }
            }
        }

        // Redirigir al login
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Arriba las manos esto es un asalto";
    }
}
