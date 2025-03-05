# Usar la imagen de Tomcat
FROM tomcat:9-jdk11-openjdk-slim

# Copiar el archivo .war al contenedor
COPY target/Prueba-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar Tomcat
CMD ["catalina.sh", "run"]
