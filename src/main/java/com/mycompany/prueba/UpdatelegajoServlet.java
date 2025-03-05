package com.mycompany.prueba;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UpdatelegajoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if(!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("error", "La solicitud no contiene datos de subida.");
            request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
            return;
        }

        // Configurar la subida de archivos
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        String id = null;
        String fileName = null;
        File uploadedFile = null;

        try {
            List<FileItem> items = upload.parseRequest(request);

            for(FileItem item : items) {
                if(item.isFormField()) {
                    if("id".equals(item.getFieldName())) {
                        id = item.getString("UTF-8");
                    }
                } 
                else {
                    if(id != null) {
                        String fileOName = new File(item.getName()).getName();
                        fileName = System.currentTimeMillis() + "_" + fileOName;
                        String uploadPath = "/tmp/uploads";
                        File uploadDir = new File(uploadPath);

                        if(!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        uploadedFile = new File(uploadPath + File.separator + fileName);
                        item.write(uploadedFile);
                    }
                }
            }

            if(id == null || uploadedFile == null) {
                request.setAttribute("error", "ID no encontrado o archivo vacío.");
                request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
                return;
            }

            // Actualizar la ruta del PDF en la base de datos
            updateFileLinkInDatabase(Integer.parseInt(id), fileName);

            request.setAttribute("successMessage", "Legajo actualizado correctamente.");
            request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
        } 
        catch(Exception e) {
            request.setAttribute("error", "Ocurrió un error al subir el archivo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/principal.jsp").forward(request, response);
        }
    }

    private void updateFileLinkInDatabase(int id, String fileName) {
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";

        String fileLink = "/tmp/uploads/" + fileName;

        String query = "UPDATE legajos SET nombre = ?, link = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(url, usernameDb, passwordDb);
            PreparedStatement stmt = conn.prepareStatement(query)) {
        
            Class.forName("com.mysql.cj.jdbc.Driver");

            stmt.setString(1, fileName);
            stmt.setString(2, fileLink);
            stmt.setInt(3, id);

            int rowsUpdated = stmt.executeUpdate();
            if(rowsUpdated == 0) {
                System.out.println("No se encontró un legajo con el ID: " + id);
            }
        } 
        catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
