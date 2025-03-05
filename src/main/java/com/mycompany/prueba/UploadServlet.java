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
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificar que la solicitud es multipart
        if (!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("error", "La solicitud no contiene datos de subida.");
            request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
            return;
        }

        // Configuración para manejar archivos
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Variables para almacenar datos
        String nombre = null;
        String escuelaStr = null;
        String fileName = null;
        File uploadedFile = null;

        try {
            List<FileItem> items = upload.parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Captura de los campos del formulario
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString("UTF-8");

                    if ("nombre".equals(fieldName)) {
                        nombre = fieldValue;
                    } else if ("escuela".equals(fieldName)) {
                        escuelaStr = fieldValue;
                    }
                } else {
                    // Captura del archivo
                    String fileOName = new File(item.getName()).getName();
                    fileName = System.currentTimeMillis() + "_" + fileOName;

                    if (fileName != null && !fileName.trim().isEmpty()) {
                        // Guardar el archivo en /tmp/uploads (directorio temporal en Railway)
                        String uploadPath = "/tmp/uploads";  // Cambié la ruta a /tmp/uploads
                        File uploadDir = new File(uploadPath);

                        if (!uploadDir.exists()) {
                            boolean dirCreated = uploadDir.mkdirs();
                            if (dirCreated) {
                                System.out.println("Directorio /tmp/uploads creado.");
                            } else {
                                System.out.println("No se pudo crear el directorio /tmp/uploads.");
                            }
                        }

                        // Guardar el archivo en /tmp/uploads/
                        uploadedFile = new File(uploadPath + File.separator + fileName);
                        item.write(uploadedFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la subida del archivo.");
            request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
            return;
        }

        // Validar que el campo "nombre" no esté vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            request.setAttribute("error", "El nombre no puede estar vacío.");
            request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
            return;
        }

        // Obtener idEscuela según el rol
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("rol");
        if (isAdmin == null) {
            isAdmin = false;
        }

        int idEscuela;
        if (isAdmin) {
            idEscuela = Integer.parseInt(escuelaStr);
        } else {
            idEscuela = (Integer) session.getAttribute("idEscuela");
        }

        // Guardar la ruta del archivo en la base de datos
        if (uploadedFile != null) {
            saveFileLinkToDatabase(idEscuela, nombre, fileName);
            request.setAttribute("successMessage", "Legajo subido con éxito.");
        } else {
            request.setAttribute("error", "No se ha enviado un archivo.");
        }

        request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
    }

    private void saveFileLinkToDatabase(int idEscuela, String nombre, String fileName) {
        String url = "jdbc:mysql://shinkansen.proxy.rlwy.net:17050/railway";
        String usernameDb = "root";
        String passwordDb = "deljCHGLXyGGtEEaFiIerPubJFHnzBwB";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);
            
            // Guardar el enlace al archivo (asegúrate que este link sea accesible desde la web)
            String fileLink = "/uploads/" + fileName;  // Cambié la ruta a /uploads/
            String query = "INSERT INTO legajos (idEscuela, nombre, nombreAlumno, link) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idEscuela);
            stmt.setString(2, fileName);
            stmt.setString(3, nombre);
            stmt.setString(4, fileLink);
            stmt.executeUpdate();
        } 
        catch(ClassNotFoundException | SQLException e) {
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
}
