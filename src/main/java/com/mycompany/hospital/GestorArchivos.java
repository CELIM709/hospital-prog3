package com.mycompany.hospital;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GestorArchivos {

    private String rutaArchivo = "consultas.txt";

    public boolean guardarCita(Cita cita) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            
            String registro = "Fecha: " + cita.getFecha() + 
                              " | Medico: " + cita.getMedico().getNombre() + 
                              " | Paciente: " + cita.getPaciente().getNombre();

            writer.write(registro);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}