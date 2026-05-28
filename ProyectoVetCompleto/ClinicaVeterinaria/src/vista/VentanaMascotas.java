package vista;

import controlador.GestorClinica;
import modelo.Mascota;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaMascotas extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtEspecie, txtRaza, txtEdad, txtIdDuenio;

    public VentanaMascotas() {
        setTitle("Gestión de Mascotas");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JPanel navbar = crearNavbar();

     
        JPanel formulario = new JPanel(new GridLayout(6, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar nueva mascota"));

        txtNombre   = new JTextField();
        txtEspecie  = new JTextField();
        txtRaza     = new JTextField();
        txtEdad     = new JTextField();
        txtIdDuenio = new JTextField();

        formulario.add(new JLabel("Nombre:"));       formulario.add(txtNombre);
        formulario.add(new JLabel("Especie:"));      formulario.add(txtEspecie);
        formulario.add(new JLabel("Raza:"));         formulario.add(txtRaza);
        formulario.add(new JLabel("Edad (años):"));  formulario.add(txtEdad);
        formulario.add(new JLabel("ID del Dueño:")); formulario.add(txtIdDuenio);

        JButton btnGuardar = new JButton("Guardar Mascota");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarMascota());
        formulario.add(new JLabel());
        formulario.add(btnGuardar);


        String[] columnas = {"ID", "Nombre", "Especie", "Raza", "Edad", "ID Dueño"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(46, 204, 113));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Mascotas registradas"));


        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una mascota de la tabla primero.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta mascota?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                eliminarMascota(id);
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

    private void guardarMascota() {
        if (txtNombre.getText().trim().isEmpty() || txtIdDuenio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre e ID del dueño son obligatorios.");
            return;
        }
        try {
            int idDuenio = Integer.parseInt(txtIdDuenio.getText().trim());
            int edad     = txtEdad.getText().trim().isEmpty() ? 0
                           : Integer.parseInt(txtEdad.getText().trim());
            if (gestor.buscarDuenio(idDuenio) == null) {
                JOptionPane.showMessageDialog(this, "No existe un dueño con ese ID.");
                return;
            }
            Mascota m = new Mascota(
                gestor.generarIdMascota(),
                txtNombre.getText().trim(),
                txtEspecie.getText().trim(),
                txtRaza.getText().trim(),
                edad, idDuenio
            );
            gestor.registrarMascota(m);
            txtNombre.setText(""); txtEspecie.setText("");
            txtRaza.setText("");   txtEdad.setText("");
            txtIdDuenio.setText("");
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Mascota registrada correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID del dueño y edad deben ser números.");
        }
    }

    private void eliminarMascota(int id) {
        java.util.ArrayList<Mascota> lista = gestor.listarMascotas();
        lista.removeIf(m -> m.getId() == id);
        gestor.guardarListaMascotas(lista);
        cargarTabla();
        JOptionPane.showMessageDialog(this, "Mascota eliminada correctamente.");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Mascota m : gestor.listarMascotas()) {
            modeloTabla.addRow(new Object[]{
                m.getId(), m.getNombre(), m.getEspecie(),
                m.getRaza(), m.getEdad(), m.getIdDuenio()
            });
        }
    }
}
