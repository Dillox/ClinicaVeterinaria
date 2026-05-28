package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Clínica Veterinaria - Sistema de Gestión");
        setSize(500, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Clínica Veterinaria", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(34, 139, 87));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 5, 0));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(9, 1, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 80, 20, 80));

        JLabel subtitulo = new JLabel("Sistema de Gestión de Consultas y Expedientes", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);
        panelBotones.add(subtitulo);

        JButton btnDuenios = crearBoton("Gestionar Dueños", new Color(52, 152, 219));
        btnDuenios.addActionListener(e -> new VentanaDuenios().setVisible(true));

        JButton btnMascotas = crearBoton("Gestionar Mascotas", new Color(46, 204, 113));
        btnMascotas.addActionListener(e -> new VentanaMascotas().setVisible(true));

        JButton btnVeterinarios = crearBoton("Gestionar Veterinarios", new Color(155, 89, 182));
        btnVeterinarios.addActionListener(e -> new VentanaVeterinarios().setVisible(true));

        JButton btnConsultas = crearBoton("Registrar Consulta", new Color(231, 76, 60));
        btnConsultas.addActionListener(e -> new VentanaConsultas().setVisible(true));

        JButton btnCitas = crearBoton("Gestionar Citas", new Color(52, 73, 94));
        btnCitas.addActionListener(e -> new VentanaCitas().setVisible(true));

        JButton btnFacturacion = crearBoton("Facturación de Servicios", new Color(39, 174, 96));
        btnFacturacion.addActionListener(e -> new VentanaFacturacion().setVisible(true));

        JButton btnMetricas = crearBoton("Métricas Clínicas", new Color(142, 68, 173));
        btnMetricas.addActionListener(e -> new VentanaMetricas().setVisible(true));

        JButton btnSalir = crearBoton("Salir del Sistema", new Color(149, 165, 166));
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                "¿Estás seguro que deseas salir?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        panelBotones.add(btnDuenios);
        panelBotones.add(btnMascotas);
        panelBotones.add(btnVeterinarios);
        panelBotones.add(btnConsultas);
        panelBotones.add(btnCitas);
        panelBotones.add(btnFacturacion);
        panelBotones.add(btnMetricas);
        panelBotones.add(btnSalir);

        panel.add(panelBotones, BorderLayout.CENTER);

        JLabel pie = new JLabel("Sistema desarrollado con Java + MVC", SwingConstants.CENTER);
        pie.setFont(new Font("Arial", Font.ITALIC, 11));
        pie.setForeground(Color.LIGHT_GRAY);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(pie, BorderLayout.SOUTH);

        add(panel);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
