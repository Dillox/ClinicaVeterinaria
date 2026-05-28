package modelo;

import java.io.Serializable;

public class Consulta implements Serializable {

    private int id;
    private String fecha;         // Ej: "21/05/2025"
    private String diagnostico;
    private String tratamiento;
    private int idMascota;        // A qué mascota se atendió
    private int idVeterinario;    // Quién la atendió

    public Consulta(int id, String fecha, String diagnostico, String tratamiento, int idMascota, int idVeterinario) {
        this.id = id;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public int getIdMascota() { return idMascota; }
    public int getIdVeterinario() { return idVeterinario; }

    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    @Override
    public String toString() {
        return id + " | " + fecha + " | " + diagnostico;
    }
}