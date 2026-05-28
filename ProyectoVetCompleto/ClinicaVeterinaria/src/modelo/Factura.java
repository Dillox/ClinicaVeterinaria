package modelo;

import java.io.Serializable;

public class Factura implements Serializable {

    private int id;
    private String fecha;
    private int idConsulta;
    private int idMascota;
    private double costoConsulta;    // Consulta base
    private double costoExamenes;    // Laboratorio
    private double costoMedicamentos;
    private String detalleMedicamentos;

    public Factura(int id, String fecha, int idConsulta, int idMascota,
                   double costoConsulta, double costoExamenes,
                   double costoMedicamentos, String detalleMedicamentos) {
        this.id = id;
        this.fecha = fecha;
        this.idConsulta = idConsulta;
        this.idMascota = idMascota;
        this.costoConsulta = costoConsulta;
        this.costoExamenes = costoExamenes;
        this.costoMedicamentos = costoMedicamentos;
        this.detalleMedicamentos = detalleMedicamentos;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public int getIdConsulta() { return idConsulta; }
    public int getIdMascota() { return idMascota; }
    public double getCostoConsulta() { return costoConsulta; }
    public double getCostoExamenes() { return costoExamenes; }
    public double getCostoMedicamentos() { return costoMedicamentos; }
    public String getDetalleMedicamentos() { return detalleMedicamentos; }

    // Total = suma de los tres rubros
    public double getTotal() {
        return costoConsulta + costoExamenes + costoMedicamentos;
    }

    @Override
    public String toString() {
        return id + " | " + fecha + " | Total: $" + String.format("%.2f", getTotal());
    }
}
