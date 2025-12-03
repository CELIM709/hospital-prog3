package com.mycompany.hospital.ui;

import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    public MenuPanel(WindowManager manager) {
        this.setLayout(new GridLayout(10, 1, 10, 10));

        JLabel titulo = new JLabel("Bienvenido al Sistema Hospitalario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        JButton btnRegistrarMedico = new JButton("Registrar Médico");
        JButton btnConsultar = new JButton("Consultar Disponibilidad");
        JButton btnAgendar = new JButton("Agendar Cita");
        JButton btnModificar = new JButton("Modificar/Cancelar Cita");

        JButton btnSalir = new JButton("Salir");

        btnRegistrarPaciente.addActionListener(e -> manager.mostrarPantalla("REG_PACIENTE"));
        btnRegistrarMedico.addActionListener(e -> manager.mostrarPantalla("REG_MEDICO"));
        btnConsultar.addActionListener(e -> manager.mostrarPantalla("CONSULTA_DISP"));
        btnAgendar.addActionListener(e -> manager.mostrarPantalla("AGENDAR_CITA"));
        btnModificar.addActionListener(e -> manager.mostrarPantalla("MOD_CITA"));

        btnSalir.addActionListener(e -> System.exit(0));

        this.add(titulo);
        this.add(btnRegistrarPaciente);
        this.add(btnRegistrarMedico);
        this.add(btnAgendar);
        this.add(btnConsultar);
        this.add(btnModificar);

        JButton btnFacturar = new JButton("Facturación y Pago");
        btnFacturar.addActionListener(e -> manager.mostrarPantalla("FACTURACION"));
        this.add(btnFacturar);

        JButton btnReportes = new JButton("Ver Reportes y Gráficos");
        btnReportes.addActionListener(e -> manager.mostrarPantalla("REPORTES"));
        this.add(btnReportes);

        JButton btnHistorial = new JButton("Historial Médico");
        btnHistorial.addActionListener(e -> manager.mostrarPantalla("HISTORIAL"));

        this.add(btnHistorial);

        this.add(btnSalir);
    }
}
