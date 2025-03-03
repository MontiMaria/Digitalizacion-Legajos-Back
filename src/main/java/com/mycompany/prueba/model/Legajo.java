package com.mycompany.prueba.model;

public class Legajo {
    protected int id;
    protected int idEscuela;
    protected String nombre;
    protected String nombreAlumno;       
    protected String link;

    public Legajo(int id, int idEscuela, String nombre, String nombreAlumno, String link) {
        this.id = id;
        this.idEscuela = idEscuela;
        this.nombre = nombre;
        this.nombreAlumno = nombreAlumno;
        this.link = link;
    }
    
    public void setIdEscuela(int idEscuela) {
        this.idEscuela = idEscuela;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public int getId() {
        return id;
    }

    public int getIdEscuela() {
        return idEscuela;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public String getLink() {
        return link;
    }
   
}
