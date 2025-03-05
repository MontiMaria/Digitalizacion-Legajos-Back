package com.mycompany.prueba;

import com.mycompany.prueba.model.Legajo;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

public class PrincipalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            HttpSession session = request.getSession();
            Boolean isAdmin = (Boolean) session.getAttribute("rol");
            List<Legajo> listaLegajos = new ArrayList<>();
            String name = request.getParameter("search-input");
            String view = request.getParameter("view");

            boolean busquedaRealizada = false;

            if(name == null || name.trim().isEmpty()) {
                request.setAttribute("mensaje", "Por favor ingrese un nombre para la búsqueda.");
            } 
            else {
                String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
                String usernameDb = "root";
                String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";

                try(Connection conn = DriverManager.getConnection(url, usernameDb, passwordDb)) {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    String sql;
                    PreparedStatement stmt;

                    if(isAdmin) { 
                        sql = "SELECT * FROM legajos WHERE nombreAlumno LIKE ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, "%" + name.trim() + "%");
                    } 
                    else {
                        int idEscuela = (Integer) session.getAttribute("idEscuela");
                        sql = "SELECT * FROM legajos WHERE nombreAlumno LIKE ? AND idEscuela = ?";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, "%" + name.trim() + "%");
                        stmt.setInt(2, idEscuela);
                    }

                    try(ResultSet rs = stmt.executeQuery()) {
                        while(rs.next()) {
                            int row1 = rs.getInt("id");
                            int row2 = rs.getInt("idEscuela");
                            String row3 = rs.getString("nombre");
                            String row4 = rs.getString("nombreAlumno");
                            String row5 = rs.getString("link");

                            Legajo legajo = new Legajo(row1, row2, row3, row4, row5);
                            listaLegajos.add(legajo);
                        }
                    }

                    if(listaLegajos.isEmpty()) {
                        busquedaRealizada = true;
                    }
                } 
                catch(ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }

            request.setAttribute("alumnos", listaLegajos);
            request.setAttribute("busquedaRealizada", busquedaRealizada);
            request.setAttribute("mensaje", request.getAttribute("mensaje"));

            // Redireccionar a la vista correcta según `view`
            if("transfer".equals(view)) {
                request.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(request, response);
            } 
            else {
                request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
            }
        }

    @Override
    public String getServletInfo() {
            return "Short description";
    }
}

