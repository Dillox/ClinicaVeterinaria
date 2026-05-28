package modelo;

import java.io.Serializable;

// Serializable significa que este objeto puede guardarse en un archivo .dat
public class Duenio implements Serializable {

    // Los atributos son privados: nadie los toca directamente (encapsulamiento)
    private int id;
    private String nombre;
    private String telefono;
    private String direccion;

    // Constructor: se usa para crear un nuevo Duenio con sus datos
    public Duenio(int id, String nombre, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters: permiten leer los atributos desde afuera
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }

    // Setters: permiten modificar los atributos desde afuera
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    // toString: sirve para mostrar el objeto como texto (en tablas y listas)
    @Override
    public String toString() {
        return id + " - " + nombre + " | Tel: " + telefono;
    }
}