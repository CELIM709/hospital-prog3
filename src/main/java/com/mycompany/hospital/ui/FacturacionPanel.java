package com.mycompany.hospital.ui;

import com.mycompany.hospital.logica.Hospital;
import com.mycompany.hospital.modelos.Cita;
import com.mycompany.hospital.modelos.Factura;
import com.mycompany.hospital.modelos.Paciente;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;

public class FacturacionPanel extends JPanel {

    private WindowManager manager;
    private Paciente pacienteActual;
    private Cita citaSeleccionada;

    // Componentes UI
    private JTextField txtCedula;
    private JComboBox<String> cmbCitasPendientes;
    private JTextField txtDescripcion;
    private JTextArea txtRecibo; // Aquí dibujaremos la factura
    private JButton btnFacturar;

    public FacturacionPanel(WindowManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout());

        // --- PANEL IZQUIERDO: FORMULARIO ---
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCedula = new JTextField();
        JButton btnBuscar = new JButton("1. Buscar Paciente");
        cmbCitasPendientes = new JComboBox<>();
        txtDescripcion = new JTextField("Consulta Médica General");
        btnFacturar = new JButton("2. Generar Factura y Pagar");
        JButton btnVolver = new JButton("Volver");

        btnFacturar.setEnabled(false); // Apagado hasta que haya cita

        formPanel.add(new JLabel("Cédula del Paciente:"));
        formPanel.add(txtCedula);
        formPanel.add(btnBuscar);
        formPanel.add(new JLabel("Seleccionar Cita PENDIENTE:"));
        formPanel.add(cmbCitasPendientes);
        formPanel.add(new JLabel("Descripción del Servicio:"));
        formPanel.add(txtDescripcion);
        formPanel.add(btnFacturar);
        formPanel.add(btnVolver);

        // --- PANEL DERECHO: VISTA PREVIA ---
        txtRecibo = new JTextArea();
        txtRecibo.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente tipo ticket
        txtRecibo.setEditable(false);
        JScrollPane scrollRecibo = new JScrollPane(txtRecibo);
        scrollRecibo.setBorder(BorderFactory.createTitledBorder("Vista Previa Factura"));

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> buscarCitasPendientes());

        cmbCitasPendientes.addActionListener(e -> seleccionarCita());

        btnFacturar.addActionListener(e -> procesarFactura());

        btnVolver.addActionListener(e -> {
            limpiarTodo();
            manager.mostrarPantalla("MENU");
        });

        // Layout Principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, scrollRecibo);
        splitPane.setDividerLocation(300);

        this.add(new JLabel("Facturación Electrónica", SwingConstants.CENTER), BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void buscarCitasPendientes() {
        try {
            int ced = Integer.parseInt(txtCedula.getText());
            pacienteActual = manager.getHospital().buscarPacientePorCedula(ced);

            cmbCitasPendientes.removeAllItems();
            btnFacturar.setEnabled(false);

            if (pacienteActual == null) {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.");
                return;
            }

            List<Cita> historial = pacienteActual.getHistorial();
            boolean hayPendientes = false;

            for (Cita c : historial) {
                // SOLO mostramos citas PENDIENTES. No tiene sentido facturar una cancelada o ya pagada.
                if ("PENDIENTE".equals(c.getEstado())) {
                    String item = c.getFecha().toString() + " - " + c.getMedico().getEspecialidad().getNombre();
                    cmbCitasPendientes.addItem(item);
                    hayPendientes = true;
                }
            }

            if (!hayPendientes) {
                JOptionPane.showMessageDialog(this, "El paciente no tiene citas pendientes de pago.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cédula inválida.");
        }
    }

    private void seleccionarCita() {
        if (pacienteActual == null || cmbCitasPendientes.getSelectedIndex() < 0) {
            return;
        }

        // TRUCO: Como filtramos la lista visual, no coinciden los índices con el historial completo.
        // Debemos buscar la cita PENDIENTE correcta.
        // En un sistema real usaríamos objetos en el combobox, aquí haremos una búsqueda simple.
        // Recorremos historial de nuevo para encontrar la "n-ésima" cita pendiente
        int indiceVisual = cmbCitasPendientes.getSelectedIndex();
        int contadorPendientes = 0;

        for (Cita c : pacienteActual.getHistorial()) {
            if ("PENDIENTE".equals(c.getEstado())) {
                if (contadorPendientes == indiceVisual) {
                    citaSeleccionada = c;
                    btnFacturar.setEnabled(true);
                    break;
                }
                contadorPendientes++;
            }
        }
    }

    private void procesarFactura() {
        if (citaSeleccionada == null) {
            return;
        }

        String desc = txtDescripcion.getText();

        // 1. Generar el objeto Factura en la Lógica
        Factura facturaGenerada = manager.getHospital().generarFactura(citaSeleccionada, desc);

        // 2. Mostrar el recibo visualmente
        imprimirFacturaTexto(facturaGenerada);

        JOptionPane.showMessageDialog(this, "Factura Nro " + facturaGenerada.getNumero() + " generada con éxito.\nCita marcada como REALIZADA.");

        // 3. Limpiar formulario parcial
        cmbCitasPendientes.removeAllItems();
        btnFacturar.setEnabled(false);
        txtCedula.setText("");
    }

    private void imprimirFacturaTexto(Factura f) {
        Hospital h = manager.getHospital();
        Paciente p = f.getCita().getPaciente();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       ").append(h.getNombre().toUpperCase()).append("\n");
        sb.append("========================================\n");
        sb.append("RIF: ").append(h.getRif()).append("\n");
        sb.append("Dir: ").append(h.getUbicacion()).append("\n");
        // Teléfono del Consultorio (Hardcodeado o agregado a clase Hospital)
        sb.append("Telf: 0212-555.99.00\n");
        sb.append("----------------------------------------\n");
        sb.append("FACTURA NRO: ").append(String.format("%06d", f.getNumero())).append("\n");
        sb.append("FECHA: ").append(sdf.format(f.getFecha())).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DATOS DEL CLIENTE:\n");
        sb.append("Nombre: ").append(p.getNombre()).append(" ").append(p.getApellido()).append("\n");
        sb.append("C.I.:   ").append(p.getCedula()).append("\n");
        sb.append("Telf:   ").append(p.getTelefono()).append("\n"); // Teléfono del paciente
        sb.append("----------------------------------------\n");
        sb.append("DESCRIPCION             IMPORTE\n");
        sb.append("----------------------------------------\n");

        String desc = f.getDescripcion();
        if (desc.length() > 20) {
            desc = desc.substring(0, 20); // Recortar si es largo
        }
        // Formato columnas
        sb.append(String.format("%-23s $%6.2f\n", desc, f.getMonto()));

        sb.append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL A PAGAR:          $%6.2f\n", f.getMonto()));
        sb.append("========================================\n");
        sb.append("     ¡Gracias por su preferencia!     \n");

        txtRecibo.setText(sb.toString());
    }

    private void limpiarTodo() {
        txtCedula.setText("");
        cmbCitasPendientes.removeAllItems();
        txtRecibo.setText("");
        pacienteActual = null;
        citaSeleccionada = null;
    }
}
