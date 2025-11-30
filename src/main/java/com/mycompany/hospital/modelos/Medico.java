package com.mycompany.hospital.modelos;

public class Medico extends Persona {
    private Especialidad especialidad;
    
    private Cita[][] agenda;

    public Medico(String nombre, String apellido, int cedula, String telefono, 
                  Especialidad especialidad) {
        super(nombre, apellido, cedula, telefono);
        this.especialidad = especialidad;

        this.agenda = new Cita[8][5]; 
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Cita[][] getAgenda() {
        return agenda;
    }
    
}