package com.mycompany.hospital.ui;

import com.mycompany.hospital.modelos.Cita;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HistorialMedicoPanel extends JPanel {

    private WindowManager manager;

    // Componentes
    private JTextField txtCedula;
    private JLabel lblNombrePaciente;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    public HistorialMedicoPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: BUSCADOR ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCedula = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar Historial");
        lblNombrePaciente = new JLabel("Paciente: -");
        lblNombrePaciente.setFont(new Font("Arial", Font.BOLD, 12));
        lblNombrePaciente.setForeground(Color.BLUE);

        topPanel.add(new JLabel("Cédula del Paciente:"));
        topPanel.add(txtCedula);
        topPanel.add(btnBuscar);
        topPanel.add(Box.createHorizontalStrut(20)); // Espacio vacio
        topPanel.add(lblNombrePaciente);

        // --- PANEL CENTRAL: TABLA ---
        String[] columnas = {"Fecha", "Hora", "Médico", "Especialidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historial de Consultas"));

        // --- PANEL INFERIOR: BOTONES ---
        JPanel bottomPanel = new JPanel();
        JButton btnVolver = new JButton("Volver al Menú");
        bottomPanel.add(btnVolver);

        btnBuscar.addActionListener(e -> buscarHistorial());

        btnVolver.addActionListener(e -> {
            limpiar();
            manager.mostrarPantalla("MENU");
        });

        this.add(new JLabel("Gestión de Historial Médico", SwingConstants.CENTER), BorderLayout.NORTH);
        // Usamos un panel contenedor para el buscador y el titulo
        JPanel norteContainer = new JPanel(new BorderLayout());
        norteContainer.add(new JLabel("Gestión de Historial Médico", SwingConstants.CENTER), BorderLayout.NORTH);
        norteContainer.add(topPanel, BorderLayout.SOUTH);

        this.add(norteContainer, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void buscarHistorial() {
        try {
            int cedula = Integer.parseInt(txtCedula.getText());

            Paciente paciente = manager.getHospital().buscarPacientePorCedula(cedula);

            if (paciente == null) {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.");
                lblNombrePaciente.setText("Paciente: -");
                modeloTabla.setRowCount(0); // Limpiar tabla
                return;
            }

            lblNombrePaciente.setText("Paciente: " + paciente.getNombre() + " " + paciente.getApellido());

            List<Cita> historial = paciente.getHistorial();

            modeloTabla.setRowCount(0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Cita c : historial) {
                Object[] fila = new Object[5];

                fila[0] = sdf.format(c.getFecha());
                fila[1] = c.getHora();
                fila[2] = c.getMedico().getNombre() + " " + c.getMedico().getApellido();
                fila[3] = c.getMedico().getEspecialidad().getNombre();
                fila[4] = c.getEstado();

                modeloTabla.addRow(fila);
            }

            if (historial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Este paciente está registrado pero no tiene citas.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cédula debe ser numérica.");
        }
    }

    private void limpiar() {
        txtCedula.setText("");
        lblNombrePaciente.setText("Paciente: -");
        modeloTabla.setRowCount(0);

    }
}
