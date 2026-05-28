package vista;

import controlador.GestorClinica;
import modelo.Cita;
import modelo.Mascota;
import modelo.Veterinario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VentanaCitas extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtFecha, txtHora, txtMotivo, txtIdMascota, txtIdVet;
    private JComboBox<String> cbEstado;
    private JTable tabla;

    public VentanaCitas() {
        setTitle("Gestión de Citas Médicas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

       
        panel.add(crearNavbar(), BorderLayout.NORTH);

        
        JPanel formulario = new JPanel(new GridLayout(7, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva cita"));

        txtFecha      = new JTextField(LocalDate.now().toString());
        txtHora       = new JTextField("08:00");
        txtMotivo     = new JTextField();
        txtIdMascota  = new JTextField();
        txtIdVet      = new JTextField();
        cbEstado      = new JComboBox<>(new String[]{"Pendiente", "Atendida", "Cancelada"});

        formulario.add(new JLabel("Fecha (yyyy-MM-dd):")); formulario.add(txtFecha);
        formulario.add(new JLabel("Hora (HH:mm):"));       formulario.add(txtHora);
        formulario.add(new JLabel("Motivo:"));             formulario.add(txtMotivo);
        formulario.add(new JLabel("ID Mascota:"));         formulario.add(txtIdMascota);
        formulario.add(new JLabel("ID Veterinario:"));     formulario.add(txtIdVet);
        formulario.add(new JLabel("Estado:"));             formulario.add(cbEstado);

        JButton btnGuardar = new JButton("Agendar Cita");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarCita());
        formulario.add(new JLabel()); formulario.add(btnGuardar);

      
        String[] columnas = {"ID", "Fecha", "Hora", "Mascota", "Veterinario", "Motivo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(52, 152, 219));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Citas registradas"));

        // ── BOTONES INFERIORES ──
        JButton btnCambiarEstado = new JButton("Cambiar estado");
        btnCambiarEstado.setBackground(new Color(39, 174, 96));
        btnCambiarEstado.setForeground(Color.WHITE);
        btnCambiarEstado.setFocusPainted(false);
        btnCambiarEstado.addActionListener(e -> cambiarEstado());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarCita());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnEliminar);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(Color.WHITE);
        centro.add(formulario, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(panelBotones, BorderLayout.SOUTH);

        panel.add(centro, BorderLayout.CENTER);
        add(panel);
        cargarTabla();
    }

    private JPanel crearNavbar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        navbar.setBackground(new Color(44, 62, 80));
        String[] botones = {"Menú", "Dueños", "Mascotas", "Veterinarios",
                            "Consultas", "Citas", "Facturación", "Métricas"};
        for (String nombre : botones) {
            JButton btn = new JButton(nombre);
            btn.setBackground(new Color(44, 62, 80));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> navegarA(((JButton) e.getSource()).getText()));
            navbar.add(btn);
        }
        return navbar;
    }

    private void navegarA(String destino) {
        if (destino.contains("Menú"))          { new VentanaPrincipal().setVisible(true);    dispose(); }
        else if (destino.contains("Dueños"))       { new VentanaDuenios().setVisible(true);      dispose(); }
        else if (destino.contains("Mascotas"))     { new VentanaMascotas().setVisible(true);     dispose(); }
        else if (destino.contains("Veterinarios")) { new VentanaVeterinarios().setVisible(true); dispose(); }
        else if (destino.contains("Consultas"))    { new VentanaConsultas().setVisible(true);    dispose(); }
        else if (destino.contains("Citas"))        { /* ya estamos aquí */ }
        else if (destino.contains("Facturación"))  { new VentanaFacturacion().setVisible(true);  dispose(); }
        else if (destino.contains("Métricas"))     { new VentanaMetricas().setVisible(true);     dispose(); }
    }

    private void guardarCita() {
        if (txtMotivo.getText().trim().isEmpty() ||
            txtIdMascota.getText().trim().isEmpty() ||
            txtIdVet.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Motivo, ID Mascota e ID Veterinario son obligatorios.");
            return;
        }
        try {
            int idMascota = Integer.parseInt(txtIdMascota.getText().trim());
            int idVet     = Integer.parseInt(txtIdVet.getText().trim());

            if (gestor.buscarMascota(idMascota) == null) {
                JOptionPane.showMessageDialog(this, "No existe una mascota con ese ID.");
                return;
            }
            Cita c = new Cita(
                gestor.generarIdCita(),
                txtFecha.getText().trim(),
                txtHora.getText().trim(),
                txtMotivo.getText().trim(),
                (String) cbEstado.getSelectedItem(),
                idMascota, idVet
            );
            gestor.registrarCita(c);
            limpiarFormulario();
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Cita agendada correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los IDs deben ser números.");
        }
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una cita primero.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String[] opciones = {"Pendiente", "Atendida", "Cancelada"};
        String nuevo = (String) JOptionPane.showInputDialog(this,
            "Nuevo estado:", "Cambiar estado",
            JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (nuevo != null) {
            ArrayList<Cita> lista = gestor.listarCitas();
            for (Cita c : lista) {
                if (c.getId() == id) { c.setEstado(nuevo); break; }
            }
            gestor.guardarListaCitas(lista);
            cargarTabla();
        }
    }

    private void eliminarCita() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una cita primero.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta cita?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ArrayList<Cita> lista = gestor.listarCitas();
            lista.removeIf(c -> c.getId() == id);
            gestor.guardarListaCitas(lista);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Cita eliminada.");
        }
    }

    private void limpiarFormulario() {
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText("08:00");
        txtMotivo.setText("");
        txtIdMascota.setText("");
        txtIdVet.setText("");
        cbEstado.setSelectedIndex(0);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cita c : gestor.listarCitas()) {
            Mascota m = gestor.buscarMascota(c.getIdMascota());
            String nomMascota = (m != null) ? m.getNombre() : "ID:" + c.getIdMascota();
            String nomVet = "ID:" + c.getIdVeterinario();
            for (Veterinario v : gestor.listarVeterinarios()) {
                if (v.getId() == c.getIdVeterinario()) { nomVet = "Dr. " + v.getNombre(); break; }
            }
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getFecha(), c.getHora(), nomMascota,
                nomVet, c.getMotivo(), c.getEstado()
            });
        }
    }
}
