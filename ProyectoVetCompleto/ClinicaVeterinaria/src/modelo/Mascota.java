package modelo;

import java.io.Serializable;

public class Mascota implements Serializable {

    private int id;
    private String nombre;
    private String especie;   
    private String raza;
    private int edad;
    private int idDuenio;    

    public Mascota(int id, String nombre, String especie, String raza, int edad, int idDuenio) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.idDuenio = idDuenio;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public int getEdad() { return edad; }
    public int getIdDuenio() { return idDuenio; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + especie + ") | Dueño ID: " + idDuenio;
    }
}
