package com.mycompany.hospital.ui;

import com.mycompany.hospital.logica.Hospital;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class ReportesPanel extends JPanel {

    private WindowManager manager;
    private PanelGrafico panelGrafico;

    public ReportesPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Estadísticas: Citas por Especialidad", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panelGrafico = new PanelGrafico();
        panelGrafico.setBackground(Color.WHITE);

        JPanel botonera = new JPanel();
        JButton btnVolver = new JButton("Volver al Menú");
        JButton btnActualizar = new JButton("Actualizar Gráfico");

        botonera.add(btnActualizar);
        botonera.add(btnVolver);

        btnActualizar.addActionListener(e -> cargarDatos());

        btnVolver.addActionListener(e -> manager.mostrarPantalla("MENU"));

        this.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                cargarDatos();
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent event) {
            }
        });

        this.add(titulo, BorderLayout.NORTH);
        this.add(panelGrafico, BorderLayout.CENTER); // El gráfico ocupa todo el centro
        this.add(botonera, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        Hospital h = manager.getHospital();

        Map<String, Integer> datos = h.obtenerEstadisticasPorEspecialidad();

        panelGrafico.setDatos(datos);
    }
}
