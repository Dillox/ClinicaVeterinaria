package vista;

import controlador.GestorClinica;
import modelo.Duenio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaDuenios extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtTelefono, txtDireccion;

    public VentanaDuenios() {
        setTitle("Gestión de Dueños");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

     
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

    
        JPanel navbar = crearNavbar();

        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar nuevo dueño"));

        txtNombre    = new JTextField();
        txtTelefono  = new JTextField();
        txtDireccion = new JTextField();

        formulario.add(new JLabel("Nombre:"));     formulario.add(txtNombre);
        formulario.add(new JLabel("Teléfono:"));   formulario.add(txtTelefono);
        formulario.add(new JLabel("Dirección:"));  formulario.add(txtDireccion);

        JButton btnGuardar = new JButton("Guardar Dueño");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarDuenio());
        formulario.add(new JLabel());
        formulario.add(btnGuardar);

     
        String[] columnas = {"ID", "Nombre", "Teléfono", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(52, 152, 219));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Dueños registrados"));

       
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un dueño de la tabla primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este dueño?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eliminarDuenio(id);
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

    private void guardarDuenio() {
        if (txtNombre.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y teléfono son obligatorios.");
            return;
        }
        Duenio d = new Duenio(
            gestor.generarIdDuenio(),
            txtNombre.getText().trim(),
            txtTelefono.getText().trim(),
            txtDireccion.getText().trim()
        );
        gestor.registrarDuenio(d);
        txtNombre.setText(""); txtTelefono.setText(""); txtDireccion.setText("");
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Dueño registrado correctamente.");
    }

    private void eliminarDuenio(int id) {
        java.util.ArrayList<Duenio> lista = gestor.listarDuenios();
        lista.removeIf(d -> d.getId() == id);
        gestor.guardarListaDuenios(lista);
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Dueño eliminado correctamente.");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Duenio d : gestor.listarDuenios()) {
            modeloTabla.addRow(new Object[]{
                d.getId(), d.getNombre(), d.getTelefono(), d.getDireccion()
            });
        }
    }
}
