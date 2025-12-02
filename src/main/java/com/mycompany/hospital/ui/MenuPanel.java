package com.mycompany.hospital.ui;

import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    public MenuPanel(WindowManager manager) {
        this.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titulo = new JLabel("Bienvenido al Sistema Hospitalario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        JButton btnRegistrarMedico = new JButton("Registrar Médico");
        JButton btnSalir = new JButton("Salir");

        btnRegistrarPaciente.addActionListener(e -> manager.mostrarPantalla("REG_PACIENTE"));
        btnRegistrarMedico.addActionListener(e -> manager.mostrarPantalla("REG_MEDICO"));
        btnSalir.addActionListener(e -> System.exit(0));

        this.add(titulo);
        this.add(btnRegistrarPaciente);
        this.add(btnRegistrarMedico);
        this.add(btnSalir);
    }
}
