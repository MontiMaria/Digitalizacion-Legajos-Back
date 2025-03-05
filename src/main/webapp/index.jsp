<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.PreparedStatement"%>
<%
    Cookie[] cookies = request.getCookies();
    boolean isAuthenticated = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    if(cookies != null) {
        for(Cookie cookie : cookies) {
            System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            // Eliminar posibles espacios extra en el valor de la cookie
            if("remember_token".equals(cookie.getName()) && cookie.getValue().trim() != null && !cookie.getValue().trim().isEmpty()) {
                out.println("Cookie 'remember_token' encontrada con valor: " + cookie.getValue());
                
                try {
                    String token = cookie.getValue().trim();
                    String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
                    String usernameDb = "root";
                    String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";
                    conn = DriverManager.getConnection(url, usernameDb, passwordDb);

                    String sql = "SELECT idEscuela, isAdmin, token FROM usuarios WHERE token = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, token);
                    rs = stmt.executeQuery();

                    if(rs.next()) {
                        // Token válido, crear sesión automáticamente
                        int idEscuela = rs.getInt("idEscuela");
                        boolean rol = rs.getBoolean("isAdmin");
                        session.setAttribute("idEscuela", idEscuela);
                        session.setAttribute("rol", rol);
                        isAuthenticated = true;
                    } 
                }
                catch(SQLException e) {
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
                        if(conn != null){
                            conn.close();
                        }
                    } 
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    if(isAuthenticated) {
        System.out.println("Usuario autenticado.");
        out.println("Usuario autenticado.");
        request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
    } 
    else {
        System.out.println("Usuario no autenticado.");
        out.println("Usuario no autenticado.");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
%>