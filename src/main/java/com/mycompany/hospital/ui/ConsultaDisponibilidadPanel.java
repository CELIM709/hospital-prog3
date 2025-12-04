package com.mycompany.hospital.ui;

import com.mycompany.hospital.modelos.Cita;
import com.mycompany.hospital.modelos.Medico;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ConsultaDisponibilidadPanel extends JPanel {

    private WindowManager manager;

    private JComboBox<String> cmbMedicos;
    private JTable tablaHorario;
    private DefaultTableModel modeloTabla;
    private List<Medico> listaMedicos;

    public ConsultaDisponibilidadPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        JPanel norteContainer = new JPanel(new GridLayout(2, 1));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbMedicos = new JComboBox<>();
        JButton btnConsultar = new JButton("Ver Horario");

        topPanel.add(new JLabel("Seleccione Médico:"));
        topPanel.add(cmbMedicos);
        topPanel.add(btnConsultar);

        norteContainer.add(new JLabel("Consulta de Disponibilidad", SwingConstants.CENTER));
        norteContainer.add(topPanel);

        // --- TABLA ---
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaHorario = new JTable(modeloTabla);
        tablaHorario.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tablaHorario);

        JPanel bottomPanel = new JPanel();
        JButton btnVolver = new JButton("Volver");
        bottomPanel.add(btnVolver);

        // --- EVENTOS ---
        btnConsultar.addActionListener(e -> mostrarHorarioMedico());

        btnVolver.addActionListener(e -> {
            manager.mostrarPantalla("MENU");
        });

        this.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                // 1. Recargar lista (por si hay médicos nuevos)
                cargarListaMedicos();
                // Si hay médicos, mostramos el horario del primero (o del seleccionado) automáticamente
                if (cmbMedicos.getItemCount() > 0) {
                    mostrarHorarioMedico();
                }
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });

        this.add(norteContainer, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void cargarListaMedicos() {
        int seleccionPrevia = cmbMedicos.getSelectedIndex();

        cmbMedicos.removeAllItems();
        listaMedicos = manager.getHospital().getMedicos();

        for (Medico m : listaMedicos) {
            cmbMedicos.addItem(m.getNombre() + " " + m.getApellido() + " - " + m.getEspecialidad().getNombre());
        }

        if (seleccionPrevia >= 0 && seleccionPrevia < cmbMedicos.getItemCount()) {
            cmbMedicos.setSelectedIndex(seleccionPrevia);
        } else if (cmbMedicos.getItemCount() > 0) {
            cmbMedicos.setSelectedIndex(0);
        }
    }

    private void mostrarHorarioMedico() {
        // Limpiamos la foto vieja
        modeloTabla.setRowCount(0);

        int indice = cmbMedicos.getSelectedIndex();
        if (indice < 0) {
            return;
        }

        Medico medicoSeleccionado = listaMedicos.get(indice);
        Cita[][] agenda = medicoSeleccionado.getAgenda();

        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};

        for (int fila = 0; fila < 8; fila++) {
            Object[] filaDatos = new Object[6];
            filaDatos[0] = horas[fila];

            for (int dia = 0; dia < 5; dia++) {
                Cita cita = agenda[fila][dia];

                if (cita == null) {
                    filaDatos[dia + 1] = "DISPONIBLE";
                } else {
                    if ("CANCELADA".equals(cita.getEstado())) {
                        filaDatos[dia + 1] = "CANCELADA";
                    } else {
                        filaDatos[dia + 1] = "OCUPADO (" + cita.getPaciente().getNombre() + ")";
                    }
                }
            }
            modeloTabla.addRow(filaDatos);
        }
    }
}
