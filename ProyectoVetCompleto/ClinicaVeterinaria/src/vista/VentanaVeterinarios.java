package vista;

import controlador.GestorClinica;
import modelo.Veterinario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaVeterinarios extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtEspecialidad, txtTelefono;

    public VentanaVeterinarios() {
        setTitle("Gestión de Veterinarios");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JPanel navbar = crearNavbar();

        
        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar veterinario"));

        txtNombre       = new JTextField();
        txtEspecialidad = new JTextField();
        txtTelefono     = new JTextField();

        formulario.add(new JLabel("Nombre:"));       formulario.add(txtNombre);
        formulario.add(new JLabel("Especialidad:")); formulario.add(txtEspecialidad);
        formulario.add(new JLabel("Teléfono:"));     formulario.add(txtTelefono);

        JButton btnGuardar = new JButton("Guardar Veterinario");
        btnGuardar.setBackground(new Color(155, 89, 182));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarVeterinario());
        formulario.add(new JLabel());
        formulario.add(btnGuardar);

        String[] columnas = {"ID", "Nombre", "Especialidad", "Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(155, 89, 182));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Veterinarios registrados"));

        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un veterinario de la tabla primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este veterinario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eliminarVeterinario(id);
            }
        });

        JPanel panelEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEliminar.setBackground(Color.WHITE);
        panelEliminar.add(btnEliminar);

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

    private void guardarVeterinario() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }
        Veterinario v = new Veterinario(
            gestor.generarIdVeterinario(),
            txtNombre.getText().trim(),
            txtEspecialidad.getText().trim(),
            txtTelefono.getText().trim()
        );
        gestor.registrarVeterinario(v);
        txtNombre.setText(""); txtEspecialidad.setText(""); txtTelefono.setText("");
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Veterinario registrado correctamente.");
    }

    private void eliminarVeterinario(int id) {
        java.util.ArrayList<Veterinario> lista = gestor.listarVeterinarios();
        lista.removeIf(v -> v.getId() == id);
        gestor.guardarListaVeterinarios(lista);
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Veterinario eliminado correctamente.");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Veterinario v : gestor.listarVeterinarios()) {
            modeloTabla.addRow(new Object[]{
                v.getId(), v.getNombre(), v.getEspecialidad(), v.getTelefono()
            });
        }
    }
}
