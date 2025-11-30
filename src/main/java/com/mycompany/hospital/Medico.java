package com.mycompany.hospital;
public class Medico extends Persona {
    // Referencia a otro objeto (Composición/Agregación)
    private Especialidad especialidad;
    
    // La matriz de agenda (Filas: Bloques Horarios, Columnas: Días)
    private Cita[][] agenda;

    // Constructor
    public Medico(String nombre, String apellido, int cedula, String telefono, 
                  Especialidad especialidad) {
        super(nombre, apellido, cedula, telefono);
        this.especialidad = especialidad;
        
        // Inicializamos la matriz (Ejemplo: 8 horas laborables x 5 días)
        // Todas las posiciones nacen en 'null' (disponibles)
        this.agenda = new Cita[8][5]; 
    }

    // Getters y Setters
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Cita[][] getAgenda() {
        return agenda;
    }
    
    // Nota: Generalmente no se hace un setAgenda() completo a menos que
    // restaures el estado del objeto, ya que la matriz nace con el médico.
}