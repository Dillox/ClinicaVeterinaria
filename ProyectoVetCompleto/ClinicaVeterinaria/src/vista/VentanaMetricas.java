package vista;

import controlador.GestorClinica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentanaMetricas extends JFrame {

    private GestorClinica gestor = new GestorClinica();
    private JTextField txtMes;
    private JTextArea areaResumen;
    private DefaultTableModel modeloPatologias;
    private DefaultTableModel modeloEspecies;

    public VentanaMetricas() {
        setTitle("Métricas Clínicas");
        setSize(750, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        panel.add(crearNavbar(), BorderLayout.NORTH);

        
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);
        filtro.setBorder(BorderFactory.createTitledBorder("Filtrar por mes"));
        filtro.add(new JLabel("Mes (ej: 2025-05 o déjalo vacío para todo):"));
        txtMes = new JTextField(10);
        txtMes.setText(LocalDate.now().toString().substring(0, 7)); // yyyy-MM
        filtro.add(txtMes);
        JButton btnGenerar = new JButton("Generar métricas");
        btnGenerar.setBackground(new Color(142, 68, 173));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.addActionListener(e -> generarMetricas());
        filtro.add(btnGenerar);

        
        modeloPatologias = new DefaultTableModel(new String[]{"Diagnóstico / Patología", "Casos"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaPatologias = new JTable(modeloPatologias);
        tablaPatologias.setRowHeight(25);
        tablaPatologias.getTableHeader().setBackground(new Color(142, 68, 173));
        tablaPatologias.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollP = new JScrollPane(tablaPatologias);
        scrollP.setBorder(BorderFactory.createTitledBorder("Patologías más frecuentes"));

        modeloEspecies = new DefaultTableModel(new String[]{"Especie", "Pacientes atendidos"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaEspecies = new JTable(modeloEspecies);
        tablaEspecies.setRowHeight(25);
        tablaEspecies.getTableHeader().setBackground(new Color(41, 128, 185));
        tablaEspecies.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollE = new JScrollPane(tablaEspecies);
        scrollE.setBorder(BorderFactory.createTitledBorder("Afluencia por especie"));

       
        areaResumen = new JTextArea(4, 40);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResumen.setBackground(new Color(245, 245, 245));
        areaResumen.setBorder(BorderFactory.createTitledBorder("Resumen"));
        JScrollPane scrollR = new JScrollPane(areaResumen);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollP, scrollE);
        split.setResizeWeight(0.5);
        split.setDividerLocation(320);
        split.setBorder(null);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(Color.WHITE);
        centro.add(filtro, BorderLayout.NORTH);
        centro.add(split, BorderLayout.CENTER);
        centro.add(scrollR, BorderLayout.SOUTH);

        panel.add(centro, BorderLayout.CENTER);
        add(panel);
        generarMetricas(); 
    }

    private void generarMetricas() {
        String mes = txtMes.getText().trim();

       
        Map<String, Integer> patologias = gestor.patologiasFrecuentes(mes);
        modeloPatologias.setRowCount(0);

       
        List<Map.Entry<String, Integer>> listaP = new ArrayList<>(patologias.entrySet());
        listaP.sort((a, b) -> b.getValue() - a.getValue());
        for (Map.Entry<String, Integer> e : listaP) {
            String diag = e.getKey().substring(0, 1).toUpperCase() + e.getKey().substring(1);
            modeloPatologias.addRow(new Object[]{diag, e.getValue()});
        }

       
        Map<String, Integer> especies = gestor.afluenciaPorEspecie(mes);
        modeloEspecies.setRowCount(0);
        List<Map.Entry<String, Integer>> listaE = new ArrayList<>(especies.entrySet());
        listaE.sort((a, b) -> b.getValue() - a.getValue());
        for (Map.Entry<String, Integer> e : listaE) {
            modeloEspecies.addRow(new Object[]{e.getKey(), e.getValue()});
        }

        
        int totalConsultas = gestor.listarConsultas().size();
        int totalCitas     = gestor.listarCitas().size();
        double totalFacturado = gestor.listarFacturas().stream()
            .mapToDouble(f -> f.getTotal()).sum();

        String labelMes = mes.isBlank() ? "todos los registros" : "mes: " + mes;
        String resumen = "Período analizado: " + labelMes + "\n" +
                         "Total consultas registradas: " + totalConsultas + "\n" +
                         "Total citas registradas:     " + totalCitas + "\n" +
                         String.format("Total facturado (global):    $%.2f", totalFacturado);
        areaResumen.setText(resumen);
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
        else if (destino.contains("Facturación"))  { new VentanaFacturacion().setVisible(true);  dispose(); }
        else if (destino.contains("Métricas"))     {  }
    }
}
