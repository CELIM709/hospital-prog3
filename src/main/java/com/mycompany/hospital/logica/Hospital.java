package com.mycompany.hospital.logica;

import com.mycompany.hospital.modelos.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public boolean registrarCita(Medico medico, Paciente paciente, Date fecha, String hora, int diaIndice, int bloqueIndice) {

        if (diaIndice < 0 || diaIndice >= 5 || bloqueIndice < 0 || bloqueIndice >= 8) {
            return false;
        }

        if (medico.getAgenda()[bloqueIndice][diaIndice] != null) {
            return false;
        }

        Cita nuevaCita = new Cita(medico, paciente, fecha, hora);

        medico.getAgenda()[bloqueIndice][diaIndice] = nuevaCita;

        paciente.getHistorial().add(nuevaCita);

        gestorArchivos.guardarCita(nuevaCita);

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

    public boolean modificarCita(Cita cita, String nuevoEstado, int nuevoDia, int nuevoBloque, String nuevaHoraTexto) {
        Medico medico = cita.getMedico();
        Cita[][] agenda = medico.getAgenda();

        if (nuevoEstado.equals("CANCELADA")) {
            borrarCitaDeLaAgenda(medico, cita);
            cita.cambiarEstado("CANCELADA");
            return true;
        }

        boolean cambioHorario = !cita.getHora().equals(nuevaHoraTexto);

        if (cambioHorario) {
            if (agenda[nuevoBloque][nuevoDia] != null) {
                return false;
            }

            borrarCitaDeLaAgenda(medico, cita);

            agenda[nuevoBloque][nuevoDia] = cita;

            cita.cambiarHora(nuevaHoraTexto);
        }

        cita.cambiarEstado(nuevoEstado);

        return true;
    }

    private void borrarCitaDeLaAgenda(Medico medico, Cita citaObjetivo) {
        Cita[][] agenda = medico.getAgenda();
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 5; col++) {
                if (agenda[fila][col] == citaObjetivo) {
                    agenda[fila][col] = null;
                    return;
                }
            }
        }
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
