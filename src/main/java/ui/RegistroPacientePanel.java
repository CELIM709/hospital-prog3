package ui;

import modelos.Paciente;
import java.awt.*;
import javax.swing.*;

public class RegistroPacientePanel extends JPanel {

    private WindowManager manager;

    // Campos de texto
    private JTextField txtNombre, txtApellido, txtCedula, txtTelefono;
    private JTextField txtEdad, txtAltura, txtPeso;
    private JComboBox<String> cmbGenero;

    public RegistroPacientePanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        // --- Formulario ---
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCedula = new JTextField();
        txtTelefono = new JTextField();
        txtEdad = new JTextField();
        txtAltura = new JTextField();
        txtPeso = new JTextField();
        
        String[] generos = {"Masculino", "Femenino", "Otro"};
        cmbGenero = new JComboBox<>(generos);

        formPanel.add(new JLabel("Nombre:")); formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido:")); formPanel.add(txtApellido);
        formPanel.add(new JLabel("Cédula (Números):")); formPanel.add(txtCedula);
        formPanel.add(new JLabel("Teléfono:")); formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Edad:")); formPanel.add(txtEdad);
        formPanel.add(new JLabel("Altura (m):")); formPanel.add(txtAltura);
        formPanel.add(new JLabel("Peso (kg):")); formPanel.add(txtPeso);
        formPanel.add(new JLabel("Género:")); formPanel.add(cmbGenero);

        // --- Botones ---
        JPanel buttonPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnVolver = new JButton("Volver");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnVolver);

        // Lógica del botón Guardar
        btnGuardar.addActionListener(e -> guardarPaciente());

        // Lógica del botón Volver
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            manager.mostrarPantalla("MENU");
        });

        this.add(new JLabel("Registro de Paciente", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void guardarPaciente() {
        try {
            // 1. Obtener datos crudos de la vista
            String nom = txtNombre.getText();
            String ape = txtApellido.getText();
            int ced = Integer.parseInt(txtCedula.getText()); // Puede fallar si meten letras
            String tel = txtTelefono.getText();
            int edad = Integer.parseInt(txtEdad.getText());
            float alt = Float.parseFloat(txtAltura.getText());
            float peso = Float.parseFloat(txtPeso.getText());
            String gen = (String) cmbGenero.getSelectedItem();

            // 2. Crear el Objeto (POO Puro)
            Paciente nuevoPaciente = new Paciente(nom, ape, ced, tel, edad, alt, peso, gen);

            // 3. Inyectarlo al sistema a través del Manager
            manager.getHospital().registrarPaciente(nuevoPaciente);

            // Feedback al usuario
            JOptionPane.showMessageDialog(this, "Paciente " + nom + " registrado con éxito.");
            
            // Opcional: Limpiar y volver
            limpiarCampos();
            manager.mostrarPantalla("MENU");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Verifique que Cédula, Edad, Altura y Peso sean números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtEdad.setText("");
        txtAltura.setText("");
        txtPeso.setText("");
    }
}
