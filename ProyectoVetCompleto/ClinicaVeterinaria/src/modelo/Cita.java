package modelo;

import java.io.Serializable;

public class Cita implements Serializable {

    private int id;
    private String fecha;       // Ej: "28/05/2025"
    private String hora;        // Ej: "10:30"
    private String motivo;
    private String estado;      // "Pendiente", "Atendida", "Cancelada"
    private int idMascota;
    private int idVeterinario;

    public Cita(int id, String fecha, String hora, String motivo, String estado,
                int idMascota, int idVeterinario) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.idMascota = idMascota;
        this.idVeterinario = idVeterinario;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
    public int getIdMascota() { return idMascota; }
    public int getIdVeterinario() { return idVeterinario; }

    public void setEstado(String estado) { this.estado = estado; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }

    @Override
    public String toString() {
        return id + " | " + fecha + " " + hora + " | " + motivo + " [" + estado + "]";
    }
}
