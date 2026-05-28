package vista;

import controlador.GestorClinica;
import modelo.Consulta;
import modelo.Mascota;
import modelo.Veterinario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class VentanaConsultas extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtFecha, txtDiagnostico, txtTratamiento, txtIdMascota, txtIdVet;

    public VentanaConsultas() {
        setTitle("Registro de Consultas");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        // ── NAVBAR ──
        JPanel navbar = crearNavbar();

        // ── FORMULARIO ──
        JPanel formulario = new JPanel(new GridLayout(6, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva consulta"));

        txtFecha       = new JTextField(LocalDate.now().toString());
        txtDiagnostico = new JTextField();
        txtTratamiento = new JTextField();
        txtIdMascota   = new JTextField();
        txtIdVet       = new JTextField();

        formulario.add(new JLabel("Fecha:"));          formulario.add(txtFecha);
        formulario.add(new JLabel("ID Mascota:"));     formulario.add(txtIdMascota);
        formulario.add(new JLabel("ID Veterinario:")); formulario.add(txtIdVet);
        formulario.add(new JLabel("Diagnóstico:"));    formulario.add(txtDiagnostico);
        formulario.add(new JLabel("Tratamiento:"));    formulario.add(txtTratamiento);

        JButton btnGuardar = new JButton("Registrar Consulta");
        btnGuardar.setBackground(new Color(231, 76, 60));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarConsulta());
        formulario.add(new JLabel());
        formulario.add(btnGuardar);

        // ── TABLA ──
        String[] columnas = {"ID", "Fecha", "Mascota", "Veterinario", "Diagnóstico", "Tratamiento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(231, 76, 60));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial de consultas"));

        // ── BOTÓN ELIMINAR ──
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una consulta de la tabla primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta consulta?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eliminarConsulta(id);
            }
        });

        JPanel panelEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEliminar.setBackground(Color.WHITE);
        panelEliminar.add(btnEliminar);

        // ── CENTRO ──
        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(Color.WHITE);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(panelEliminar, BorderLayout.SOUTH);

        panel.add(navbar, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);

        add(panel);
        cargarTabla();
    }

    private JPanel crearNavbar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        navbar.setBackground(new Color(44, 62, 80));
        String[] botones = {"Menú", "Dueños", "Mascotas", "Veterinarios", "Consultas", "Citas", "Facturación", "Métricas"};
        for (String nombre : botones) {
            JButton btn = new JButton(nombre);
            btn.setBackground(new Color(44, 62, 80));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                String n = ((JButton) e.getSource()).getText();
                if (n.contains("Menú"))             { new VentanaPrincipal().setVisible(true);    dispose(); }
                else if (n.contains("Dueños"))       { new VentanaDuenios().setVisible(true);      dispose(); }
                else if (n.contains("Mascotas"))     { new VentanaMascotas().setVisible(true);     dispose(); }
                else if (n.contains("Veterinarios")) { new VentanaVeterinarios().setVisible(true); dispose(); }
                else if (n.contains("Consultas"))    { new VentanaConsultas().setVisible(true);    dispose(); }
                else if (n.contains("Citas"))        { new VentanaCitas().setVisible(true);        dispose(); }
                else if (n.contains("Facturación"))  { new VentanaFacturacion().setVisible(true);  dispose(); }
                else if (n.contains("Métricas"))     { new VentanaMetricas().setVisible(true);     dispose(); }
            });
            navbar.add(btn);
        }
        return navbar;
    }

    private void guardarConsulta() {
        if (txtIdMascota.getText().trim().isEmpty() ||
            txtIdVet.getText().trim().isEmpty() ||
            txtDiagnostico.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Mascota, ID Veterinario y Diagnóstico son obligatorios.");
            return;
        }
        try {
            int idMascota = Integer.parseInt(txtIdMascota.getText().trim());
            int idVet     = Integer.parseInt(txtIdVet.getText().trim());

            if (gestor.buscarMascota(idMascota) == null) {
                JOptionPane.showMessageDialog(this, "No existe una mascota con ese ID.");
                return;
            }
            Consulta c = new Consulta(
                gestor.generarIdConsulta(),
                txtFecha.getText().trim(),
                txtDiagnostico.getText().trim(),
                txtTratamiento.getText().trim(),
                idMascota, idVet
            );
            gestor.registrarConsulta(c);
            txtDiagnostico.setText(""); txtTratamiento.setText("");
            txtIdMascota.setText("");   txtIdVet.setText("");
            txtFecha.setText(LocalDate.now().toString());
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Consulta registrada correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los IDs deben ser números.");
        }
    }

    private void eliminarConsulta(int id) {
        java.util.ArrayList<Consulta> lista = gestor.listarConsultas();
        lista.removeIf(c -> c.getId() == id);
        gestor.guardarListaConsultas(lista);
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Consulta eliminada correctamente.");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Consulta c : gestor.listarConsultas()) {
            Mascota m = gestor.buscarMascota(c.getIdMascota());
            Veterinario v = null;
            for (Veterinario vet : gestor.listarVeterinarios()) {
                if (vet.getId() == c.getIdVeterinario()) { v = vet; break; }
            }
            String nomMascota = (m != null) ? m.getNombre() : "ID:" + c.getIdMascota();
            String nomVet     = (v != null) ? "Dr. " + v.getNombre() : "ID:" + c.getIdVeterinario();

            modeloTabla.addRow(new Object[]{
                c.getId(), c.getFecha(), nomMascota, nomVet,
                c.getDiagnostico(), c.getTratamiento()
            });
        }
    }
}