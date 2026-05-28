package vista;

import controlador.GestorClinica;
import modelo.Consulta;
import modelo.Factura;
import modelo.Mascota;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VentanaFacturacion extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private DefaultTableModel modeloTabla;
    private JTextField txtIdConsulta, txtCostoConsulta, txtCostoExamenes,
                       txtCostoMedicamentos, txtDetalleMedicamentos;

    public VentanaFacturacion() {
        setTitle("Facturación de Servicios");
        setSize(800, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        panel.add(crearNavbar(), BorderLayout.NORTH);

        // ── FORMULARIO ──
        JPanel formulario = new JPanel(new GridLayout(7, 2, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva factura"));

        txtIdConsulta         = new JTextField();
        txtCostoConsulta      = new JTextField("15.00");
        txtCostoExamenes      = new JTextField("0.00");
        txtCostoMedicamentos  = new JTextField("0.00");
        txtDetalleMedicamentos = new JTextField();

        formulario.add(new JLabel("ID Consulta:"));               formulario.add(txtIdConsulta);
        formulario.add(new JLabel("Costo consulta base ($):"));   formulario.add(txtCostoConsulta);
        formulario.add(new JLabel("Costo exámenes lab ($):"));    formulario.add(txtCostoExamenes);
        formulario.add(new JLabel("Costo medicamentos ($):"));    formulario.add(txtCostoMedicamentos);
        formulario.add(new JLabel("Detalle medicamentos:"));      formulario.add(txtDetalleMedicamentos);

        // Etiqueta de total en tiempo real
        JLabel lblTotal = new JLabel("Total estimado: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 13));
        lblTotal.setForeground(new Color(39, 174, 96));

        // Actualizar total al cambiar cualquier campo
        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { actualizarTotal(lblTotal); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { actualizarTotal(lblTotal); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarTotal(lblTotal); }
        };
        txtCostoConsulta.getDocument().addDocumentListener(dl);
        txtCostoExamenes.getDocument().addDocumentListener(dl);
        txtCostoMedicamentos.getDocument().addDocumentListener(dl);

        formulario.add(lblTotal);

        JButton btnGuardar = new JButton("Generar Factura");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarFactura());
        formulario.add(btnGuardar);

        // ── TABLA ──
        String[] columnas = {"ID", "Fecha", "ID Consulta", "Mascota",
                             "Consulta $", "Exámenes $", "Medicamentos $", "Total $"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(39, 174, 96));
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Facturas emitidas"));

        // ── BOTÓN VER DETALLE ──
        JButton btnDetalle = new JButton("Ver detalle");
        btnDetalle.setBackground(new Color(52, 152, 219));
        btnDetalle.setForeground(Color.WHITE);
        btnDetalle.setFocusPainted(false);
        btnDetalle.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona una factura."); return; }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            for (Factura f : gestor.listarFacturas()) {
                if (f.getId() == id) { mostrarDetalle(f); break; }
            }
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) { JOptionPane.showMessageDialog(this, "Selecciona una factura."); return; }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar factura?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ArrayList<Factura> lista = gestor.listarFacturas();
                lista.removeIf(f -> f.getId() == id);
                gestor.guardarListaFacturas(lista);
                cargarTabla();
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnDetalle);
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

    private void actualizarTotal(JLabel lbl) {
        try {
            double c  = Double.parseDouble(txtCostoConsulta.getText().trim());
            double ex = Double.parseDouble(txtCostoExamenes.getText().trim());
            double me = Double.parseDouble(txtCostoMedicamentos.getText().trim());
            lbl.setText(String.format("Total estimado: $%.2f", c + ex + me));
        } catch (NumberFormatException ignored) {
            lbl.setText("Total estimado: —");
        }
    }

    private void guardarFactura() {
        if (txtIdConsulta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el ID de la consulta.");
            return;
        }
        try {
            int idConsulta = Integer.parseInt(txtIdConsulta.getText().trim());
            double cosC = Double.parseDouble(txtCostoConsulta.getText().trim());
            double cosE = Double.parseDouble(txtCostoExamenes.getText().trim());
            double cosM = Double.parseDouble(txtCostoMedicamentos.getText().trim());

            // Buscar la consulta para obtener el idMascota
            int idMascota = 0;
            for (Consulta c : gestor.listarConsultas()) {
                if (c.getId() == idConsulta) { idMascota = c.getIdMascota(); break; }
            }
            if (idMascota == 0) {
                JOptionPane.showMessageDialog(this, "No existe una consulta con ese ID.");
                return;
            }
            Factura f = new Factura(
                gestor.generarIdFactura(),
                LocalDate.now().toString(),
                idConsulta, idMascota,
                cosC, cosE, cosM,
                txtDetalleMedicamentos.getText().trim()
            );
            gestor.registrarFactura(f);
            limpiarFormulario();
            cargarTabla();
            JOptionPane.showMessageDialog(this,
                String.format("Factura generada.\nTotal: $%.2f", f.getTotal()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Los valores numéricos no son válidos.");
        }
    }

    private void mostrarDetalle(Factura f) {
        Mascota m = gestor.buscarMascota(f.getIdMascota());
        String nomMascota = (m != null) ? m.getNombre() + " (" + m.getEspecie() + ")" : "ID:" + f.getIdMascota();
        String msg = "═══════════════════════════\n" +
                     "  FACTURA #" + f.getId() + "\n" +
                     "═══════════════════════════\n" +
                     "Fecha:         " + f.getFecha() + "\n" +
                     "Paciente:      " + nomMascota + "\n" +
                     "ID Consulta:   " + f.getIdConsulta() + "\n" +
                     "───────────────────────────\n" +
                     String.format("Consulta base: $%8.2f%n", f.getCostoConsulta()) +
                     String.format("Exámenes lab:  $%8.2f%n", f.getCostoExamenes()) +
                     String.format("Medicamentos:  $%8.2f%n", f.getCostoMedicamentos()) +
                     "───────────────────────────\n" +
                     String.format("TOTAL:         $%8.2f%n", f.getTotal()) +
                     "───────────────────────────\n" +
                     "Detalle med.: " + (f.getDetalleMedicamentos().isEmpty() ? "—" : f.getDetalleMedicamentos());
        JOptionPane.showMessageDialog(this, msg, "Detalle de Factura", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarFormulario() {
        txtIdConsulta.setText("");
        txtCostoConsulta.setText("15.00");
        txtCostoExamenes.setText("0.00");
        txtCostoMedicamentos.setText("0.00");
        txtDetalleMedicamentos.setText("");
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Factura f : gestor.listarFacturas()) {
            Mascota m = gestor.buscarMascota(f.getIdMascota());
            String nomMascota = (m != null) ? m.getNombre() : "ID:" + f.getIdMascota();
            modeloTabla.addRow(new Object[]{
                f.getId(), f.getFecha(), f.getIdConsulta(), nomMascota,
                String.format("%.2f", f.getCostoConsulta()),
                String.format("%.2f", f.getCostoExamenes()),
                String.format("%.2f", f.getCostoMedicamentos()),
                String.format("%.2f", f.getTotal())
            });
        }
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
        else if (destino.contains("Citas"))        { new VentanaCitas().setVisible(true);        dispose(); }
        else if (destino.contains("Facturación"))  { /* ya estamos aquí */ }
        else if (destino.contains("Métricas"))     { new VentanaMetricas().setVisible(true);     dispose(); }
    }
}
