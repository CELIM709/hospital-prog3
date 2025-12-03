package com.mycompany.hospital.ui;

import com.mycompany.hospital.logica.Hospital;
import com.mycompany.hospital.modelos.Especialidad;
import com.mycompany.hospital.modelos.Medico;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import javax.swing.*;

public class WindowManager extends JFrame {

    private Hospital hospital;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public WindowManager() {
        this.hospital = new Hospital("Hospital Central", "J-12345678-9", "Av. Principal");

        this.setTitle("Sistema de Gestión Hospitalaria");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        preloadDatosPrueba();

        this.hospital.cargarCitasAlInicio();

        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        this.mainPanel.add(new MenuPanel(this), "MENU");

        this.mainPanel.add(new RegistroPacientePanel(this), "REG_PACIENTE");
        this.mainPanel.add(new RegistroMedicoPanel(this), "REG_MEDICO");
        this.mainPanel.add(new ConsultaDisponibilidadPanel(this), "CONSULTA_DISP");
        this.mainPanel.add(new AgendarCitaPanel(this), "AGENDAR_CITA");
        this.mainPanel.add(new ModificarCitaPanel(this), "MOD_CITA");
        this.mainPanel.add(new FacturacionPanel(this), "FACTURACION");
        this.mainPanel.add(new ReportesPanel(this), "REPORTES");
        this.mainPanel.add(new HistorialMedicoPanel(this), "HISTORIAL");

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

    private void preloadDatosPrueba() {
        Especialidad cardio = hospital.getEspecialidades().get(0);
        Medico m = new Medico("Gregory", "House", 11111, "555-1234", cardio);
        hospital.registrarMedico(m);

        Paciente p = new Paciente("Juan", "Perez", 22222, "555-9876", 30, 1.80f, 80, "Masculino");
        hospital.registrarPaciente(p);
    }
}
