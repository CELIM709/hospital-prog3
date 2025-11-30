package com.mycompany.hospital.modelos;

import java.util.Date;

public class Cita {
    private Medico medico;
    private Paciente paciente;
    private Date fecha;
    private String hora;
    private String estado;

    public Cita(Medico medico, Paciente paciente, Date fecha, String hora) {
        this.medico = medico;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = "PENDIENTE";
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void cambiarFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void cambiarHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void cambiarEstado(String estado) {
        this.estado = estado;
    }
}