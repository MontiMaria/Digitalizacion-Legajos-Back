package com.mycompany.prueba;
import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class EmbeddedTomcat {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);  // Usa el puerto 8080
        tomcat.setBaseDir("temp");

        // Agrega tu aplicación WAR
        tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

        System.out.println("Starting Tomcat...");
        tomcat.start();
        tomcat.getServer().await();
    }
}
