package com.mycompany.hospital.ui;

import com.mycompany.hospital.modelos.Especialidad;
import com.mycompany.hospital.modelos.Medico;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class RegistroMedicoPanel extends JPanel {

    private WindowManager manager;

    private JTextField txtNombre, txtApellido, txtCedula, txtTelefono;
    private JComboBox<String> cmbEspecialidad;
    private List<Especialidad> listaEspecialidadesDisponibles;

    public RegistroMedicoPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCedula = new JTextField();
        txtTelefono = new JTextField();
        cmbEspecialidad = new JComboBox<>();

        cargarEspecialidades();

        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtApellido);
        formPanel.add(new JLabel("Cédula:"));
        formPanel.add(txtCedula);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Especialidad:"));
        formPanel.add(cmbEspecialidad);

        JPanel buttonPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar Médico");
        JButton btnVolver = new JButton("Volver");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnVolver);

        btnGuardar.addActionListener(e -> guardarMedico());

        btnVolver.addActionListener(e -> {
            limpiarCampos();
            manager.mostrarPantalla("MENU");
        });

        this.add(new JLabel("Registro de Médico", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarEspecialidades() {
        listaEspecialidadesDisponibles = manager.getHospital().getEspecialidades();

        for (Especialidad esp : listaEspecialidadesDisponibles) {
            cmbEspecialidad.addItem(esp.getNombre() + " ($" + esp.getPrecio() + ")");
        }
    }

    private void guardarMedico() {
        try {
            String nom = txtNombre.getText().trim();
            String ape = txtApellido.getText().trim();
            String tel = txtTelefono.getText().trim();

            if (nom.matches(".*\\d.*") || ape.matches(".*\\d.*") || nom.isEmpty() || ape.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Apellido no pueden contener números ni estar vacíos.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!tel.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int ced = Integer.parseInt(txtCedula.getText().trim());

            int indiceSeleccionado = cmbEspecialidad.getSelectedIndex();
            Especialidad especialidadSeleccionada = listaEspecialidadesDisponibles.get(indiceSeleccionado);

            Medico nuevoMedico = new Medico(nom, ape, ced, tel, especialidadSeleccionada);

            manager.getHospital().registrarMedico(nuevoMedico);

            JOptionPane.showMessageDialog(this, "Dr. " + nom + " registrado en " + especialidadSeleccionada.getNombre());

            limpiarCampos();
            manager.mostrarPantalla("MENU");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: La cédula debe ser numérica.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        if (cmbEspecialidad.getItemCount() > 0) {
            cmbEspecialidad.setSelectedIndex(0);
        }
    }
}
