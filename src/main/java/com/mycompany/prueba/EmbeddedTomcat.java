package com.mycompany.Prueba;

import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class EmbeddedTomcat {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        String port = System.getenv("PORT");  // Railway asigna el puerto
        if (port == null || port.isEmpty()) {
            port = "8080"; // Si no hay puerto, usa 8080
        }
        tomcat.setPort(Integer.parseInt(port));
        tomcat.setBaseDir("temp");

        // Agrega tu aplicación web
        tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

        System.out.println("🚀 Iniciando Tomcat en el puerto " + port);
        tomcat.start();
        tomcat.getServer().await();
    }
}
