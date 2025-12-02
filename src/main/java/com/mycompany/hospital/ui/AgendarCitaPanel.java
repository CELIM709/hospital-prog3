package com.mycompany.hospital.ui;

import com.mycompany.hospital.logica.Hospital;
import com.mycompany.hospital.modelos.Medico;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class AgendarCitaPanel extends JPanel {

    private WindowManager manager;

    private JComboBox<String> cmbPacientes;
    private JComboBox<String> cmbMedicos;
    private JComboBox<String> cmbDias;
    private JComboBox<String> cmbHoras;
    private JTextField txtFechaTexto;

    private List<Paciente> listaPacientes;
    private List<Medico> listaMedicos;

    public AgendarCitaPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        cmbPacientes = new JComboBox<>();
        cmbMedicos = new JComboBox<>();
        txtFechaTexto = new JTextField();

        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        cmbDias = new JComboBox<>(dias);

        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};
        cmbHoras = new JComboBox<>(horas);

        formPanel.add(new JLabel("Seleccionar Paciente:"));
        formPanel.add(cmbPacientes);
        formPanel.add(new JLabel("Seleccionar Médico:"));
        formPanel.add(cmbMedicos);
        formPanel.add(new JLabel("Fecha (dd/mm/aaaa):"));
        formPanel.add(txtFechaTexto);
        formPanel.add(new JLabel("Día de la Semana:"));
        formPanel.add(cmbDias);
        formPanel.add(new JLabel("Hora de Cita:"));
        formPanel.add(cmbHoras);

        JPanel buttonPanel = new JPanel();
        JButton btnAgendar = new JButton("Confirmar Cita");
        JButton btnVolver = new JButton("Volver");

        buttonPanel.add(btnAgendar);
        buttonPanel.add(btnVolver);

        this.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                cargarDatos();
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });

        btnAgendar.addActionListener(e -> agendarCita());

        btnVolver.addActionListener(e -> {
            limpiarCampos();
            manager.mostrarPantalla("MENU");
        });

        this.add(new JLabel("Agendar Nueva Cita", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarDatos() {

        cmbPacientes.removeAllItems();
        cmbMedicos.removeAllItems();

        Hospital hosp = manager.getHospital();
        listaPacientes = hosp.getPacientes();
        listaMedicos = hosp.getMedicos();

        for (Paciente p : listaPacientes) {
            cmbPacientes.addItem(p.getNombre() + " " + p.getApellido() + " (CI: " + p.getCedula() + ")");
        }

        for (Medico m : listaMedicos) {
            cmbMedicos.addItem(m.getNombre() + " " + m.getApellido() + " [" + m.getEspecialidad().getNombre() + "]");
        }
    }

    private void agendarCita() {
        if (listaPacientes.isEmpty() || listaMedicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan pacientes o médicos registrados.");
            return;
        }

        try {

            Paciente p = listaPacientes.get(cmbPacientes.getSelectedIndex());
            Medico m = listaMedicos.get(cmbMedicos.getSelectedIndex());

            String fechaTexto = txtFechaTexto.getText();
            if (fechaTexto.isEmpty()) {
                throw new Exception("Debe escribir una fecha.");
            }

            Date fechaObjeto = new Date();

            int diaIndice = cmbDias.getSelectedIndex();
            int horaIndice = cmbHoras.getSelectedIndex();
            String horaTexto = (String) cmbHoras.getSelectedItem();

            boolean exito = manager.getHospital().registrarCita(m, p, fechaObjeto, horaTexto, diaIndice, horaIndice);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Cita Agendada con Éxito!");
                limpiarCampos();
                manager.mostrarPantalla("MENU");
            } else {
                JOptionPane.showMessageDialog(this, "ERROR: Ese horario ya está ocupado por otra cita.", "Conflicto de Horario", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agendar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtFechaTexto.setText("");
        if (cmbDias.getItemCount() > 0) {
            cmbDias.setSelectedIndex(0);
        }
        if (cmbHoras.getItemCount() > 0) {
            cmbHoras.setSelectedIndex(0);
        }
    }
}
