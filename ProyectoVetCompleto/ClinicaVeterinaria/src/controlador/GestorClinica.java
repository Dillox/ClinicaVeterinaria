package controlador;

import modelo.Duenio;
import modelo.Mascota;
import modelo.Veterinario;
import modelo.Consulta;
import modelo.Cita;
import modelo.Factura;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GestorClinica {

    private static final String ARCHIVO_DUENIOS      = "duenios.dat";
    private static final String ARCHIVO_MASCOTAS     = "mascotas.dat";
    private static final String ARCHIVO_VETERINARIOS = "veterinarios.dat";
    private static final String ARCHIVO_CONSULTAS    = "consultas.dat";
    private static final String ARCHIVO_CITAS        = "citas.dat";
    private static final String ARCHIVO_FACTURAS     = "facturas.dat";

 
    public void guardarLista(ArrayList<?> lista, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(archivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    private ArrayList cargarLista(String archivo) {
        File f = new File(archivo);
        if (!f.exists()) return new ArrayList();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return (ArrayList) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList();
        }
    }

  

    public ArrayList<Duenio> listarDuenios() { return cargarLista(ARCHIVO_DUENIOS); }

    public void registrarDuenio(Duenio d) {
        ArrayList<Duenio> lista = listarDuenios();
        lista.add(d);
        guardarLista(lista, ARCHIVO_DUENIOS);
    }

    public int generarIdDuenio() {
        ArrayList<Duenio> lista = listarDuenios();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public Duenio buscarDuenio(int id) {
        for (Duenio d : listarDuenios()) if (d.getId() == id) return d;
        return null;
    }

    public void guardarListaDuenios(ArrayList<Duenio> lista) { guardarLista(lista, ARCHIVO_DUENIOS); }

  

    public ArrayList<Mascota> listarMascotas() { return cargarLista(ARCHIVO_MASCOTAS); }

    public void registrarMascota(Mascota m) {
        ArrayList<Mascota> lista = listarMascotas();
        lista.add(m);
        guardarLista(lista, ARCHIVO_MASCOTAS);
    }

    public int generarIdMascota() {
        ArrayList<Mascota> lista = listarMascotas();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public ArrayList<Mascota> mascotasDeDuenio(int idDuenio) {
        ArrayList<Mascota> resultado = new ArrayList<>();
        for (Mascota m : listarMascotas()) if (m.getIdDuenio() == idDuenio) resultado.add(m);
        return resultado;
    }

    public Mascota buscarMascota(int id) {
        for (Mascota m : listarMascotas()) if (m.getId() == id) return m;
        return null;
    }

    public void guardarListaMascotas(ArrayList<Mascota> lista) { guardarLista(lista, ARCHIVO_MASCOTAS); }



    public ArrayList<Veterinario> listarVeterinarios() { return cargarLista(ARCHIVO_VETERINARIOS); }

    public void registrarVeterinario(Veterinario v) {
        ArrayList<Veterinario> lista = listarVeterinarios();
        lista.add(v);
        guardarLista(lista, ARCHIVO_VETERINARIOS);
    }

    public int generarIdVeterinario() {
        ArrayList<Veterinario> lista = listarVeterinarios();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public void guardarListaVeterinarios(ArrayList<Veterinario> lista) { guardarLista(lista, ARCHIVO_VETERINARIOS); }

   
    public ArrayList<Consulta> listarConsultas() { return cargarLista(ARCHIVO_CONSULTAS); }

    public void registrarConsulta(Consulta c) {
        ArrayList<Consulta> lista = listarConsultas();
        lista.add(c);
        guardarLista(lista, ARCHIVO_CONSULTAS);
    }

    public int generarIdConsulta() {
        ArrayList<Consulta> lista = listarConsultas();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public ArrayList<Consulta> consultasDeMascota(int idMascota) {
        ArrayList<Consulta> resultado = new ArrayList<>();
        for (Consulta c : listarConsultas()) if (c.getIdMascota() == idMascota) resultado.add(c);
        return resultado;
    }

    public void guardarListaConsultas(ArrayList<Consulta> lista) { guardarLista(lista, ARCHIVO_CONSULTAS); }



    public ArrayList<Cita> listarCitas() { return cargarLista(ARCHIVO_CITAS); }

    public void registrarCita(Cita c) {
        ArrayList<Cita> lista = listarCitas();
        lista.add(c);
        guardarLista(lista, ARCHIVO_CITAS);
    }

    public int generarIdCita() {
        ArrayList<Cita> lista = listarCitas();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public void guardarListaCitas(ArrayList<Cita> lista) { guardarLista(lista, ARCHIVO_CITAS); }


    public ArrayList<Factura> listarFacturas() { return cargarLista(ARCHIVO_FACTURAS); }

    public void registrarFactura(Factura f) {
        ArrayList<Factura> lista = listarFacturas();
        lista.add(f);
        guardarLista(lista, ARCHIVO_FACTURAS);
    }

    public int generarIdFactura() {
        ArrayList<Factura> lista = listarFacturas();
        if (lista.isEmpty()) return 1;
        return lista.get(lista.size() - 1).getId() + 1;
    }

    public void guardarListaFacturas(ArrayList<Factura> lista) { guardarLista(lista, ARCHIVO_FACTURAS); }



    public Map<String, Integer> patologiasFrecuentes(String mes) {
        Map<String, Integer> mapa = new HashMap<>();
        for (Consulta c : listarConsultas()) {
            if (mes == null || mes.isBlank() || c.getFecha().contains(mes)) {
                String diag = c.getDiagnostico().trim().toLowerCase();
                mapa.put(diag, mapa.getOrDefault(diag, 0) + 1);
            }
        }
        return mapa;
    }

    public Map<String, Integer> afluenciaPorEspecie(String mes) {
        Map<String, Integer> mapa = new HashMap<>();
        for (Consulta c : listarConsultas()) {
            if (mes == null || mes.isBlank() || c.getFecha().contains(mes)) {
                Mascota m = buscarMascota(c.getIdMascota());
                if (m != null) {
                    String especie = m.getEspecie();
                    mapa.put(especie, mapa.getOrDefault(especie, 0) + 1);
                }
            }
        }
        return mapa;
    }
}
