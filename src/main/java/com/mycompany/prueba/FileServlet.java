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

        // Obtener el nombre del archivo desde el parámetro 'file'
        String fileName = request.getParameter("file");

        // Verificar que el nombre del archivo no esté vacío
        if (fileName == null || fileName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se proporcionó el archivo.");
            return;
        }

        // Construir la ruta completa del archivo
        String uploadPath = "/tmp/uploads" + File.separator + fileName;
        File file = new File(uploadPath);

        // Verificar si el archivo existe
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El archivo no fue encontrado.");
            return;
        }

        // Configurar la respuesta para que se pueda descargar el archivo
        response.setContentType(getServletContext().getMimeType(file.getName()));
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Enviar el archivo al cliente
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
        return "Short description";
    }// </editor-fold>
}
