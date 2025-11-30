package com.mycompany.hospital.ui;

import com.mycompany.hospital.logica.Hospital;
import java.awt.*;
import javax.swing.*;

public class WindowManager extends JFrame {
    
    private Hospital hospital;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public WindowManager() {
        this.hospital = new Hospital("Hospital Central", "J-12345678-9", "Av. Principal");

        this.setTitle("Sistema de Gestión Hospitalaria - POO Puro");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

          this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        this.mainPanel.add(new MenuPanel(this), "MENU");
        
        this.mainPanel.add(new RegistroPacientePanel(this), "REG_PACIENTE");

        this.add(mainPanel);
    }

    public void mostrarPantalla(String nombrePantalla) {
        cardLayout.show(mainPanel, nombrePantalla);
    }

    public Hospital getHospital() {
        return hospital;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowManager().setVisible(true);
        });
    }
}