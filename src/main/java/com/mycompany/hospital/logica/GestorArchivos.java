package com.mycompany.hospital.logica;

import com.mycompany.hospital.modelos.Cita;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    private String rutaArchivo = "citas_db.txt"; // Cambiamos nombre para no mezclar con el anterior

    // 1. GUARDAR (Formato CSV: dato;dato;dato)
    public boolean guardarCita(Cita c, int diaIdx, int bloqueIdx) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            // Guardamos IDs y datos crudos para poder reconstruir después
            String linea = c.getPaciente().getCedula() + ";"
                    + c.getMedico().getCedula() + ";"
                    + c.getFecha().getTime() + ";"
                    + // Guardamos fecha en milisegundos (long)
                    c.getHora() + ";"
                    + diaIdx + ";"
                    + bloqueIdx + ";"
                    + c.getEstado();

            writer.write(linea);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. LEER (Devuelve lista de Strings crudos)
    public List<String> leerCitas() {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            return lineas; // Si no existe, devolvemos lista vacía
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }
}
