package modelo;

import java.io.Serializable;

public class Veterinario implements Serializable {

    private int id;
    private String nombre;
    private String especialidad;  // Ej: Cirugía, Dermatología, General
    private String telefono;

    public Veterinario(int id, String nombre, String especialidad, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getTelefono() { return telefono; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() {
        return id + " - Dr. " + nombre + " | " + especialidad;
    }
}