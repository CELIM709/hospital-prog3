package com.mycompany.hospital;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    
    public MenuPanel(WindowManager manager) {
        this.setLayout(new GridLayout(3, 1, 10, 10));
        
        JLabel titulo = new JLabel("Bienvenido al Sistema Hospitalario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnRegistrarPaciente = new JButton("Registrar Paciente");
        JButton btnSalir = new JButton("Salir");

        btnRegistrarPaciente.addActionListener(e -> manager.mostrarPantalla("REG_PACIENTE"));
        
        btnSalir.addActionListener(e -> System.exit(0));

        this.add(titulo);
        this.add(btnRegistrarPaciente);
        this.add(btnSalir);
    }
}