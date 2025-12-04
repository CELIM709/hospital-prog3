package com.mycompany.hospital.ui;

import com.mycompany.hospital.modelos.Cita;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import java.text.SimpleDateFormat; // Necesario para formato de fecha
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class ModificarCitaPanel extends JPanel {

    private WindowManager manager;
    private Paciente pacienteActual;
    private Cita citaSeleccionada;

    private JTextField txtCedulaBusqueda;
    private JComboBox<String> cmbCitasPaciente;

    private JComboBox<String> cmbNuevoEstado;
    private JComboBox<String> cmbNuevoDia;
    private JComboBox<String> cmbNuevaHora;

    // --- NUEVO CAMPO ---
    private JTextField txtNuevaFecha;

    private JButton btnGuardarCambios;

    public ModificarCitaPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        // Panel Búsqueda (Igual que antes)
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("1. Buscar Paciente"));
        txtCedulaBusqueda = new JTextField();
        JButton btnBuscar = new JButton("Buscar por Cédula");
        cmbCitasPaciente = new JComboBox<>();
        JButton btnSeleccionarCita = new JButton("Editar esta Cita");
        searchPanel.add(new JLabel("Cédula Paciente:"));
        searchPanel.add(txtCedulaBusqueda);
        searchPanel.add(new JLabel(""));
        searchPanel.add(btnBuscar);
        searchPanel.add(new JLabel("Seleccionar Cita:"));
        searchPanel.add(cmbCitasPaciente);

        // Panel Edición (AUMENTAMOS FILAS DE 4 A 5)
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        editPanel.setBorder(BorderFactory.createTitledBorder("2. Modificar Datos"));

        String[] estados = {"PENDIENTE", "REALIZADA", "CANCELADA"};
        cmbNuevoEstado = new JComboBox<>(estados);
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        cmbNuevoDia = new JComboBox<>(dias);
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};
        cmbNuevaHora = new JComboBox<>(horas);

        // --- INICIALIZAMOS EL CAMPO DE FECHA ---
        txtNuevaFecha = new JTextField();

        editPanel.add(new JLabel("Estado:"));
        editPanel.add(cmbNuevoEstado);

        // --- AGREGAMOS EL CAMPO AL PANEL ---
        editPanel.add(new JLabel("Nueva Fecha (dd/MM/yyyy):"));
        editPanel.add(txtNuevaFecha);

        editPanel.add(new JLabel("Nuevo Día Semana:"));
        editPanel.add(cmbNuevoDia);
        editPanel.add(new JLabel("Nueva Hora:"));
        editPanel.add(cmbNuevaHora);

        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setEnabled(false);
        editPanel.add(new JLabel(""));
        editPanel.add(btnGuardarCambios);

        // Panel Inferior
        JPanel bottomPanel = new JPanel();
        JButton btnVolver = new JButton("Volver al Menú");
        bottomPanel.add(btnVolver);

        // Eventos
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnSeleccionarCita.addActionListener(e -> cargarDatosCita());
        btnGuardarCambios.addActionListener(e -> guardarModificacion());
        btnVolver.addActionListener(e -> {
            limpiarTodo();
            manager.mostrarPantalla("MENU");
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(editPanel, BorderLayout.CENTER);
        container.add(btnSeleccionarCita, BorderLayout.SOUTH);

        this.add(new JLabel("Modificar Citas", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    // ... buscarPaciente() queda igual ...
    private void buscarPaciente() {
        try {
            int cedula = Integer.parseInt(txtCedulaBusqueda.getText());
            pacienteActual = manager.getHospital().buscarPacientePorCedula(cedula);
            cmbCitasPaciente.removeAllItems();

            if (pacienteActual != null) {
                List<Cita> historial = pacienteActual.getHistorial();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Formato para mostrar

                if (historial.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El paciente no tiene citas.");
                } else {
                    for (Cita c : historial) {
                        String info = sdf.format(c.getFecha()) + " - " + c.getHora() + " (" + c.getEstado() + ")";
                        cmbCitasPaciente.addItem(info);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cédula inválida.");
        }
    }

    private void cargarDatosCita() {
        if (pacienteActual == null || cmbCitasPaciente.getItemCount() == 0) {
            return;
        }

        int indice = cmbCitasPaciente.getSelectedIndex();
        citaSeleccionada = pacienteActual.getHistorial().get(indice);

        btnGuardarCambios.setEnabled(true);

        cmbNuevoEstado.setSelectedItem(citaSeleccionada.getEstado());
        cmbNuevaHora.setSelectedItem(citaSeleccionada.getHora());

        // --- PRECARGAMOS LA FECHA ACTUAL EN EL TEXTBOX ---
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtNuevaFecha.setText(sdf.format(citaSeleccionada.getFecha()));
    }

    private void guardarModificacion() {
        if (citaSeleccionada == null) {
            return;
        }

        try {
            String nuevoEst = (String) cmbNuevoEstado.getSelectedItem();
            int diaIdx = cmbNuevoDia.getSelectedIndex();
            int horaIdx = cmbNuevaHora.getSelectedIndex();
            String horaTxt = (String) cmbNuevaHora.getSelectedItem();

            // --- PARSEO DE LA FECHA ---
            String fechaTexto = txtNuevaFecha.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date nuevaFecha = sdf.parse(fechaTexto); // Esto convierte el texto a Objeto Date

            // --- LLAMAMOS A LA LÓGICA PASANDO LA FECHA ---
            boolean exito = manager.getHospital().modificarCita(citaSeleccionada, nuevoEst, diaIdx, horaIdx, horaTxt, nuevaFecha);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Cita modificada con éxito.");
                limpiarTodo();
                manager.mostrarPantalla("MENU");
            } else {
                JOptionPane.showMessageDialog(this, "ERROR: El nuevo horario seleccionado está ocupado.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la fecha. Use formato dd/MM/yyyy");
        }
    }

    private void limpiarTodo() {
        txtCedulaBusqueda.setText("");
        txtNuevaFecha.setText(""); // Limpiar fecha
        cmbCitasPaciente.removeAllItems();
        btnGuardarCambios.setEnabled(false);
        pacienteActual = null;
        citaSeleccionada = null;
    }
}

/*package com.mycompany.hospital.ui;

import com.mycompany.hospital.modelos.Cita;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ModificarCitaPanel extends JPanel {

    private WindowManager manager;
    private Paciente pacienteActual;
    private Cita citaSeleccionada;

    // Componentes Búsqueda
    private JTextField txtCedulaBusqueda;
    private JComboBox<String> cmbCitasPaciente;
    // Componentes Edición
    private JComboBox<String> cmbNuevoEstado;
    private JComboBox<String> cmbNuevoDia;
    private JComboBox<String> cmbNuevaHora;
    private JButton btnGuardarCambios;

    public ModificarCitaPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("1. Buscar Paciente"));

        txtCedulaBusqueda = new JTextField();
        JButton btnBuscar = new JButton("Buscar por Cédula");
        cmbCitasPaciente = new JComboBox<>();
        JButton btnSeleccionarCita = new JButton("Editar esta Cita");

        searchPanel.add(new JLabel("Cédula Paciente:"));
        searchPanel.add(txtCedulaBusqueda);
        searchPanel.add(new JLabel(""));
        searchPanel.add(btnBuscar);
        searchPanel.add(new JLabel("Seleccionar Cita:"));
        searchPanel.add(cmbCitasPaciente);

        JPanel editPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        editPanel.setBorder(BorderFactory.createTitledBorder("2. Modificar Datos"));

        String[] estados = {"PENDIENTE", "REALIZADA", "CANCELADA"};
        cmbNuevoEstado = new JComboBox<>(estados);

        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        cmbNuevoDia = new JComboBox<>(dias);

        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};
        cmbNuevaHora = new JComboBox<>(horas);

        editPanel.add(new JLabel("Estado:"));
        editPanel.add(cmbNuevoEstado);
        editPanel.add(new JLabel("Nuevo Día:"));
        editPanel.add(cmbNuevoDia);
        editPanel.add(new JLabel("Nueva Hora:"));
        editPanel.add(cmbNuevaHora);

        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setEnabled(false);
        editPanel.add(new JLabel(""));
        editPanel.add(btnGuardarCambios);

        JPanel bottomPanel = new JPanel();
        JButton btnVolver = new JButton("Volver al Menú");
        bottomPanel.add(btnVolver);

        btnBuscar.addActionListener(e -> buscarPaciente());

        btnSeleccionarCita.addActionListener(e -> cargarDatosCita());

        btnGuardarCambios.addActionListener(e -> guardarModificacion());

        btnVolver.addActionListener(e -> {
            limpiarTodo();
            manager.mostrarPantalla("MENU");
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(searchPanel, BorderLayout.NORTH);
        container.add(editPanel, BorderLayout.CENTER);
        container.add(btnSeleccionarCita, BorderLayout.SOUTH); // Botón intermedio

        this.add(new JLabel("Modificar Citas", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void buscarPaciente() {
        try {
            int cedula = Integer.parseInt(txtCedulaBusqueda.getText());
            pacienteActual = manager.getHospital().buscarPacientePorCedula(cedula);

            cmbCitasPaciente.removeAllItems();

            if (pacienteActual != null) {
                List<Cita> historial = pacienteActual.getHistorial();
                if (historial.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El paciente no tiene citas.");
                } else {
                    for (Cita c : historial) {
                        String info = c.getFecha().toString() + " - " + c.getHora() + " (" + c.getEstado() + ")";
                        cmbCitasPaciente.addItem(info);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cédula inválida.");
        }
    }

    private void cargarDatosCita() {
        if (pacienteActual == null || cmbCitasPaciente.getItemCount() == 0) {
            return;
        }

        int indice = cmbCitasPaciente.getSelectedIndex();
        citaSeleccionada = pacienteActual.getHistorial().get(indice);

        btnGuardarCambios.setEnabled(true);

        cmbNuevoEstado.setSelectedItem(citaSeleccionada.getEstado());
        cmbNuevaHora.setSelectedItem(citaSeleccionada.getHora());

    }

    private void guardarModificacion() {
        if (citaSeleccionada == null) {
            return;
        }

        String nuevoEst = (String) cmbNuevoEstado.getSelectedItem();
        int diaIdx = cmbNuevoDia.getSelectedIndex();
        int horaIdx = cmbNuevaHora.getSelectedIndex();
        String horaTxt = (String) cmbNuevaHora.getSelectedItem();

        boolean exito = manager.getHospital().modificarCita(citaSeleccionada, nuevoEst, diaIdx, horaIdx, horaTxt);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cita modificada con éxito.");
            limpiarTodo();
            manager.mostrarPantalla("MENU");
        } else {
            JOptionPane.showMessageDialog(this, "ERROR: El nuevo horario seleccionado está ocupado.");
        }
    }

    private void limpiarTodo() {
        txtCedulaBusqueda.setText("");
        cmbCitasPaciente.removeAllItems();
        btnGuardarCambios.setEnabled(false);
        pacienteActual = null;
        citaSeleccionada = null;
    }
}
*/
