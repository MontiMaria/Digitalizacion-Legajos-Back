package com.mycompany.prueba;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener el nombre del archivo desde la ruta (path info)
        String fileName = request.getPathInfo(); // Ejemplo: "/1741179952296_CULPABLES-convertido.pdf"
        if (fileName == null || fileName.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se proporcionó el archivo.");
            return;
        }
        // Quitar la barra inicial
        fileName = fileName.substring(1);

        // Construir la ruta completa del archivo usando la misma ubicación que en UploadServlet
        String uploadPath = getServletContext().getRealPath("/uploads");
        File file = new File(uploadPath, fileName);

        // Verificar si el archivo existe
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El archivo no fue encontrado.");
            return;
        }

        // Configurar la respuesta para la descarga
        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "FileServlet";
    }
}
