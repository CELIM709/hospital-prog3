package com.mycompany.hospital.logica;

import com.mycompany.hospital.modelos.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hospital {

    private String nombre;
    private String rif;
    private String ubicacion;

    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Especialidad> especialidades;
    private List<Factura> facturas;

    private GestorArchivos gestorArchivos;

    public Hospital(String nombre, String rif, String ubicacion) {
        this.nombre = nombre;
        this.rif = rif;
        this.ubicacion = ubicacion;

        this.medicos = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.especialidades = new ArrayList<>();
        this.facturas = new ArrayList<>();

        this.gestorArchivos = new GestorArchivos();
        inicializarEspecialidades();
    }

    private void inicializarEspecialidades() {
        this.registrarEspecialidad(new Especialidad("Cardiología", 100));
        this.registrarEspecialidad(new Especialidad("Pediatría", 200));
        this.registrarEspecialidad(new Especialidad("Dermatología", 300));
        this.registrarEspecialidad(new Especialidad("Ginecología", 400));
    }

    public boolean registrarMedico(Medico medico) {
        return this.medicos.add(medico);
    }

    public boolean registrarPaciente(Paciente paciente) {
        return this.pacientes.add(paciente);
    }

    public void registrarEspecialidad(Especialidad especialidad) {
        this.especialidades.add(especialidad);
    }

    public Medico buscarMedicoPorCedula(int cedula) {
        for (Medico m : medicos) {
            if (m.getCedula() == cedula) {
                return m;
            }
        }
        return null;
    }

    public void cargarCitasAlInicio() {
        List<String> lineas = gestorArchivos.leerCitas();

        System.out.println("Cargando " + lineas.size() + " citas...");

        for (String linea : lineas) {
            try {
                String[] datos = linea.split(";");

                int cedulaPac = Integer.parseInt(datos[0]);
                int cedulaMed = Integer.parseInt(datos[1]);
                long fechaMilis = Long.parseLong(datos[2]);
                String hora = datos[3];
                int diaIdx = Integer.parseInt(datos[4]);
                int bloqueIdx = Integer.parseInt(datos[5]);
                String estado = datos[6];

                Paciente p = buscarPacientePorCedula(cedulaPac);
                Medico m = buscarMedicoPorCedula(cedulaMed);

                if (p != null && m != null) {
                    Date fecha = new Date(fechaMilis);

                    Cita citaRecuperada = new Cita(m, p, fecha, hora);
                    citaRecuperada.cambiarEstado(estado);

                    if (m.getAgenda()[bloqueIdx][diaIdx] == null) {
                        m.getAgenda()[bloqueIdx][diaIdx] = citaRecuperada;
                    }

                    p.getHistorial().add(citaRecuperada);

                }

            } catch (Exception e) {
                System.err.println("Error leyendo una línea del archivo: " + e.getMessage());
            }
        }
    }

    public boolean registrarCita(Medico medico, Paciente paciente, Date fecha, String hora, int diaIndice, int bloqueIndice) {

        // 1. VALIDACIÓN DE RANGOS (Seguridad para no romper la matriz)
        if (diaIndice < 0 || diaIndice >= 5 || bloqueIndice < 0 || bloqueIndice >= 8) {
            return false;
        }

        // 2. VALIDACIÓN DE CHOQUE DE HORARIO
        if (medico.getAgenda()[bloqueIndice][diaIndice] != null) {
            return false; //Ya existe una cita en ese hueco.
        }

        Cita nuevaCita = new Cita(medico, paciente, fecha, hora);

        medico.getAgenda()[bloqueIndice][diaIndice] = nuevaCita;

        paciente.getHistorial().add(nuevaCita);

        gestorArchivos.guardarCita(nuevaCita, diaIndice, bloqueIndice);

        return true;
    }

    public Factura generarFactura(Cita cita, String descripcion) {
        Factura nuevaFactura = new Factura(cita, descripcion);
        this.facturas.add(nuevaFactura);
        return nuevaFactura;
    }

    public Paciente buscarPacientePorCedula(int cedula) {
        for (Paciente p : pacientes) {
            if (p.getCedula() == cedula) {
                return p;
            }
        }
        return null;
    }

    public boolean modificarCita(Cita cita, String nuevoEstado, int nuevoDia, int nuevoBloque, String nuevaHoraTexto, java.util.Date nuevaFecha) {
        Medico medico = cita.getMedico();
        Cita[][] agenda = medico.getAgenda();

        if (nuevoEstado.equals("CANCELADA")) {

            borrarCitaDeLaAgenda(medico, cita);

            cita.getPaciente().getHistorial().remove(cita);

            return true;
        }

        // 2. VERIFICAR MOVIMIENTO EN LA MATRIZ
        boolean esLaMismaCasilla = (agenda[nuevoBloque][nuevoDia] == cita);

        if (!esLaMismaCasilla) {
            if (agenda[nuevoBloque][nuevoDia] != null) {
                return false; // Ocupado
            }

            borrarCitaDeLaAgenda(medico, cita);
            agenda[nuevoBloque][nuevoDia] = cita;
        }

        // 3. ACTUALIZACIÓN DE DATOS
        cita.cambiarHora(nuevaHoraTexto);
        cita.cambiarFecha(nuevaFecha);
        cita.cambiarEstado(nuevoEstado);

        return true;
    }

    
    private void borrarCitaDeLaAgenda(Medico medico, Cita citaObjetivo) {
        Cita[][] agenda = medico.getAgenda();
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 5; col++) {
                // Comparamos referencias de memoria (POO Puro)
                if (agenda[fila][col] == citaObjetivo) {
                    agenda[fila][col] = null; // Liberamos la celda
                    return; // Terminamos apenas la encontramos
                }
            }
        }
    }

    public Map<String, Integer> obtenerEstadisticasPorEspecialidad() {
        Map<String, Integer> contadores = new HashMap<>();

        for (Especialidad esp : this.especialidades) {
            contadores.put(esp.getNombre(), 0);
        }

        for (Medico m : this.medicos) {
            String nombreEsp = m.getEspecialidad().getNombre();
            int citasDelMedico = 0;

            Cita[][] agenda = m.getAgenda();
            for (int i = 0; i < 8; i++) { // Filas
                for (int j = 0; j < 5; j++) { // Columnas
                    if (agenda[i][j] != null) {
                        citasDelMedico++;
                    }
                }
            }

            int totalActual = contadores.get(nombreEsp);
            contadores.put(nombreEsp, totalActual + citasDelMedico);
        }

        return contadores;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRif() {
        return rif;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public List<Especialidad> getEspecialidades() {
        return especialidades;
    }
}
